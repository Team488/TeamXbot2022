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
        double targetXmetersPerSecond = translate.x * maxTargetSpeed.get() / BasePoseSubsystem.INCHES_IN_A_METER;
        double targetYmetersPerSecond = translate.y * maxTargetSpeed.get() / BasePoseSubsystem.INCHES_IN_A_METER;
        double targetRotationRadiansPerSecond = rotate * maxTargetTurnRate.get();

        translationXTargetMPS.set(targetXmetersPerSecond);
        translationYTargetMPS.set(targetYmetersPerSecond);
        rotationTargetRadians.set(targetRotationRadiansPerSecond);

        // Now that we are in metric units, we can use the kinematics to convert the target speeds to wheel speeds.
        ChassisSpeeds targetMotion = new ChassisSpeeds(targetXmetersPerSecond, targetYmetersPerSecond, targetRotationRadiansPerSecond);

        Translation2d centerOfRotationTranslation = new Translation2d(
            centerOfRotation.x / BasePoseSubsystem.INCHES_IN_A_METER,
            centerOfRotation.y / BasePoseSubsystem.INCHES_IN_A_METER);
        SwerveModuleState[] moduleStates = swerveDriveKinematics.toSwerveModuleStates(targetMotion, centerOfRotationTranslation);

        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, maxTargetSpeed.get() / BasePoseSubsystem.INCHES_IN_A_METER);

        this.getFrontLeftSwerveModuleSubsystem().setTargetState(moduleStates[0]);
        this.getFrontRightSwerveModuleSubsystem().setTargetState(moduleStates[1]);
        this.getRearLeftSwerveModuleSubsystem().setTargetState(moduleStates[2]);
        this.getRearRightSwerveModuleSubsystem().setTargetState(moduleStates[3]);
    }

    /***
     * Give the same power to all steering modules, and the another power to all the drive wheels.
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
