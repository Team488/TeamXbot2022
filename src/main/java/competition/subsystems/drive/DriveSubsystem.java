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
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
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

    private final SwerveDriveKinematics swerveDriveKinematics;

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

        this.maxTargetSpeed = pf.createPersistentProperty("MaxTargetSpeedInchesPerSecond", 1.0);
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

    @Override
    public void move(XYPair translate, double rotate) {
        double targetX = translate.x * maxTargetSpeed.get() * BasePoseSubsystem.INCHES_IN_A_METER;
        double targetY = translate.y * maxTargetSpeed.get() * BasePoseSubsystem.INCHES_IN_A_METER;
        double targetRotation = Math.toRadians(rotate);

        ChassisSpeeds targetMotion = new ChassisSpeeds(targetX, targetY, targetRotation);

        SwerveModuleState[] moduleStates = swerveDriveKinematics.toSwerveModuleStates(targetMotion);
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, maxTargetSpeed.get() * BasePoseSubsystem.INCHES_IN_A_METER);

        this.getFrontLeftSwerveModuleSubsystem().setTargetState(moduleStates[0]);
        this.getFrontRightSwerveModuleSubsystem().setTargetState(moduleStates[1]);
        this.getRearLeftSwerveModuleSubsystem().setTargetState(moduleStates[2]);
        this.getRearRightSwerveModuleSubsystem().setTargetState(moduleStates[3]);
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
}
