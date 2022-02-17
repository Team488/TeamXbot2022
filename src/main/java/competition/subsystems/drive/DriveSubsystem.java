package competition.subsystems.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.drive.swerve.SwerveModuleSubsystem;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);
    
    private final SwerveModuleSubsystem frontLeftSwerveModuleSubsystem;
    private final SwerveModuleSubsystem frontRightSwerveModuleSubsystem;
    private final SwerveModuleSubsystem rearLeftSwerveModuleSubsystem;
    private final SwerveModuleSubsystem rearRightSwerveModuleSubsystem;

    private final DoubleProperty maxTargetSpeed;
    private final DoubleProperty maxTargetTurnRate;

    private final SwerveDriveKinematics swerveDriveKinematics;
    private final StringProperty activeModuleProp;
    
    private final DoubleProperty translationXTargetMPS;
    private final DoubleProperty translationYTargetMPS;
    private final DoubleProperty rotationTargetRadians;

    private final PIDManager positionalPidManager;
    private final PIDManager headingPidManager;

    private XYPair lastCommandedDirection;

    public enum SwerveModuleLocation {
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT;

        private static SwerveModuleLocation[] values = values();
        public SwerveModuleLocation next() {
            return values[(this.ordinal() + 1) % values.length];
        }
    }

    private SwerveModuleLocation activeModule = SwerveModuleLocation.FRONT_LEFT;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract contract, PropertyFactory pf,
            @FrontLeftDrive SwerveModuleSubsystem frontLeftSwerve, @FrontRightDrive SwerveModuleSubsystem frontRightSwerve,
            @RearLeftDrive SwerveModuleSubsystem rearLeftSwerve, @RearRightDrive SwerveModuleSubsystem rearRightSwerve) {
        log.info("Creating DriveSubsystem");
        pf.setPrefix(this);

        this.frontLeftSwerveModuleSubsystem = frontLeftSwerve;
        this.frontRightSwerveModuleSubsystem = frontRightSwerve;
        this.rearLeftSwerveModuleSubsystem = rearLeftSwerve;
        this.rearRightSwerveModuleSubsystem = rearRightSwerve;

        this.swerveDriveKinematics = new SwerveDriveKinematics(
            this.frontLeftSwerveModuleSubsystem.getModuleTranslation(),
            this.frontRightSwerveModuleSubsystem.getModuleTranslation(),
            this.rearLeftSwerveModuleSubsystem.getModuleTranslation(),
            this.rearRightSwerveModuleSubsystem.getModuleTranslation()
        );

        this.maxTargetSpeed = pf.createPersistentProperty("MaxTargetSpeedInchesPerSecond", 120.0);
        this.maxTargetTurnRate = pf.createPersistentProperty("MaxTargetTurnRate", MathUtils.Tau);
        this.activeModuleProp = pf.createEphemeralProperty("ActiveModule", activeModule.toString());
        this.translationXTargetMPS = pf.createEphemeralProperty("TranslationXMetersPerSecond", 0.0);
        this.translationYTargetMPS = pf.createEphemeralProperty("TranslationYMetersPerSecond", 0.0);
        this.rotationTargetRadians = pf.createEphemeralProperty("RotationTargetRadians", 0.0);

        // TODO: eventually, this should retrieved from auto or the pose subsystem as a field like 
        // "Desired initial wheel direction" so there's no thrash right at the start of a match.
        // Probably not a huge priority, Since as soon as we move once the robot remembers the last commanded direction.
        lastCommandedDirection = new XYPair(0, 90);

        positionalPidManager = factory.createPIDManager(this.getPrefix() + "PositionPID", 1.0/90, 0, 0);
        headingPidManager = factory.createPIDManager(this.getPrefix() + "HeadingPID", 1.0/36, 0, 0);
    }

    public SwerveDriveKinematics getSwerveDriveKinematics() {
        return swerveDriveKinematics;
    }

    public double getMaxTargetSpeedInchesPerSecond() {
        return maxTargetSpeed.get();
    }

    @Override
    public PIDManager getPositionalPid() {
        return null;
    }

    @Override
    public PIDManager getRotateToHeadingPid() {
        return null;
    }

    @Override
    public PIDManager getRotateDecayPid() {
        return null;
    }

    /**
     * Set the target movement speed and rotation, rotating around the center of the robot.
     * @param translate The translation velocity.
     * @param rotate The rotation velocity.
     */
    @Override
    public void move(XYPair translate, double rotate) {
        move(translate, rotate, new XYPair());
    }

    /**
     * Set the target movement speed and rotation, with an arbitrary center of rotation.
     * @param translate The translation velocity.
     * @param rotate The rotation velocity.
     * @param centerOfRotation The center of rotation.
     */
    public void move(XYPair translate, double rotate, XYPair centerOfRotation) {

        // First, we need to check if we've been asked to move at all. If not, we should look at the last time we were given a commanded direction
        // and keep the wheels pointed that way. That prevents the wheels from returning to "0" degrees when the driver has gone back to 
        // neutral joystick position.
        boolean isNotMoving = translate.getMagnitude() < 0.01 && Math.abs(rotate) < 0.01;

        if (isNotMoving)
        {
            translate = lastCommandedDirection;
        }

        // Then we translate the translation and rotation "intents" into velocities. Basically,
        // going from the -1 to 1 power scale to -maxTargetSpeed to +maxTargetSpeed. We also need to convert them
        // into metric units, since the next library we call expects metric units.
        double targetXmetersPerSecond = translate.x * maxTargetSpeed.get() / BasePoseSubsystem.INCHES_IN_A_METER;
        double targetYmetersPerSecond = translate.y * maxTargetSpeed.get() / BasePoseSubsystem.INCHES_IN_A_METER;
        double targetRotationRadiansPerSecond = rotate * maxTargetTurnRate.get();

        translationXTargetMPS.set(targetXmetersPerSecond);
        translationYTargetMPS.set(targetYmetersPerSecond);
        rotationTargetRadians.set(targetRotationRadiansPerSecond);

        // This handy library from WPILib will take our robot's overall desired translation & rotation and figure out
        // what each swerve module should be doing in order to achieve that.
        ChassisSpeeds targetMotion = new ChassisSpeeds(targetXmetersPerSecond, targetYmetersPerSecond, targetRotationRadiansPerSecond);

        // One optional step - we can choose to rotate around a specific point, rather than the center of the robot.
        Translation2d centerOfRotationTranslation = new Translation2d(
            centerOfRotation.x / BasePoseSubsystem.INCHES_IN_A_METER,
            centerOfRotation.y / BasePoseSubsystem.INCHES_IN_A_METER);
        SwerveModuleState[] moduleStates = swerveDriveKinematics.toSwerveModuleStates(targetMotion, centerOfRotationTranslation);

        // Another potentially optional step - it's possible that in the calculations above, one or more swerve modules could be asked to
        // move at higer than its maximum speed. At this point, we have a choice. Either:
        // - "Prioritize speed/power" - don't change any module powers, and anything going above 100% will, due to reality, be capped at 100%.
        //   This means that the robot's motion might be a little odd, but this could be useful if we want to push as hard as possible.
        // - "Prioritize motion" - reduce all module powers proportionately so that the "fastest" module is moving at 100%. For example, if you had modules
        //   initially asked to move at 200%, 100%, 100%, and 50%, this would reduce them all to 100%, 50%, 50%, and 25%.
        //   This means the overall motion will be more correct, but we will lose some speed/power.
        // For now, we're choosing to prioritize motion. We're somewhat new to swerve, so debugging any strange motion will be easier if we know the system is
        // always trying to prioritize motion.

        // Also, one more special check - if there was no commanded motion, set the maximum speed to 0.
        double topSpeedMetersPerSecond = isNotMoving ? 0 : maxTargetSpeed.get();
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, topSpeedMetersPerSecond);

        this.getFrontLeftSwerveModuleSubsystem().setTargetState(moduleStates[0]);
        this.getFrontRightSwerveModuleSubsystem().setTargetState(moduleStates[1]);
        this.getRearLeftSwerveModuleSubsystem().setTargetState(moduleStates[2]);
        this.getRearRightSwerveModuleSubsystem().setTargetState(moduleStates[3]);

        // If we were asked to move in a direction, remember that direction.
        if (translate.getMagnitude() > 0.05) {
            lastCommandedDirection = translate;
        }        
    }

    /***
     * Give the same power to all steering modules, and the another power to all the drive wheels.
     * Does not currently use PID! As a result, wheel positions will vary wildly!
     * @param drivePower -1 to 1 power to apply to the drive wheels.
     * @param steeringPower -1 to 1 power to apply to the steering modules.
     */
    public void crabDrive(double drivePower, double steeringPower) {
        this.getFrontLeftSwerveModuleSubsystem().setPowers(drivePower, steeringPower);
        this.getFrontRightSwerveModuleSubsystem().setPowers(drivePower, steeringPower);
        this.getRearLeftSwerveModuleSubsystem().setPowers(drivePower, steeringPower);
        this.getRearRightSwerveModuleSubsystem().setPowers(drivePower, steeringPower);
    }

    @Override
    public double getLeftTotalDistance() {
        return 0;
    }

    @Override
    public double getRightTotalDistance() {
        return 0;
    }

    @Override
    public double getTransverseDistance() {
        return 0;
    }

    public SwerveModuleSubsystem getFrontLeftSwerveModuleSubsystem() {
        return this.frontLeftSwerveModuleSubsystem;
    }

    public SwerveModuleSubsystem getFrontRightSwerveModuleSubsystem() {
        return this.frontRightSwerveModuleSubsystem;
    }

    public SwerveModuleSubsystem getRearLeftSwerveModuleSubsystem() {
        return this.rearLeftSwerveModuleSubsystem;
    }

    public SwerveModuleSubsystem getRearRightSwerveModuleSubsystem() {
        return this.rearRightSwerveModuleSubsystem;
    }

    /**
     * Meant to be used alongside methods such as {@link #controlOnlyActiveSwerveModuleSubsystem(SwerveModuleLocation)}. 
     * Has no effect when the robot is in normal, "Maintainer" operation.
     * @param activeModule Which module to set as the active module.
     */
    public void setActiveModule(SwerveModuleLocation activeModule) {
        this.activeModule = activeModule;
        activeModuleProp.set(activeModule.toString());
    }

    /**
     * Meant to be used alongside methods such as {@link #controlOnlyActiveSwerveModuleSubsystem(SwerveModuleLocation)}. 
     * Has no effect when the robot is in normal, "Maintainer" operation.
     * Moves the active module to the next module, according to the pattern FrontLeft, FrontRight, RearLeft, RearRight.
     */
    public void setNextModuleAsActiveModule() {
        setActiveModule(this.activeModule.next());
    }

    private SwerveModuleSubsystem getSwerveModuleSubsystem(SwerveModuleLocation location) {
        switch (location) {
            case FRONT_LEFT:
                return this.getFrontLeftSwerveModuleSubsystem();
            case FRONT_RIGHT:
                return this.getFrontRightSwerveModuleSubsystem();
            case REAR_LEFT:
                return this.getRearLeftSwerveModuleSubsystem();
            case REAR_RIGHT:
                return this.getRearRightSwerveModuleSubsystem();
            default:
                log.warn("Attempted to get a SwerveModuleSubsystem for an invalid SwerveModuleLocation. Returning front left so that something is returned.");
                return this.getFrontLeftSwerveModuleSubsystem();
        }
    }

    private SwerveModuleSubsystem getActiveSwerveModuleSubsystem() {
        return this.getSwerveModuleSubsystem(this.activeModule);
    }
    
    private void stopInactiveModules() {
        SwerveModuleLocation[] values = SwerveModuleLocation.values();
        for (SwerveModuleLocation value : values) {
            if (value != this.activeModule) {
                this.getSwerveModuleSubsystem(value).setPowers(0, 0);
            }
        }
    }

    /**
     * Controls the drive power and steering power of the active module. Stops all other modules.
     * Intended for use when you want to investigate a single module without moving all the others.
     * @param drivePower -1 to 1 power to apply to the drive component.
     * @param steeringPower -1 to 1 power to apply to the steering component.
     */
    public void controlOnlyActiveSwerveModuleSubsystem(double drivePower, double steeringPower) {
        this.getActiveSwerveModuleSubsystem().setPowers(drivePower, steeringPower);
        stopInactiveModules();
    }
}
