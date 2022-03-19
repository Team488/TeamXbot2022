package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.FieldPose;
import xbot.common.math.WrappedRotation2d;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    private final DriveSubsystem drive;
    private final DoubleProperty levelThresholdDegrees;
    
    final SwerveDriveOdometry swerveOdometry;

    final DoubleProperty leftStartPosX;
    final DoubleProperty leftStartPosY;
    final DoubleProperty leftHeading;

    final DoubleProperty midStartPosX;
    final DoubleProperty midStartPosY;
    final DoubleProperty midHeading;

    final DoubleProperty rightStartPosX;
    final DoubleProperty rightStartPosY;
    final DoubleProperty rightHeading;

    final DoubleProperty leftHubStartPosX;
    final DoubleProperty leftHubStartPosY;
    final DoubleProperty leftHubHeading;

    final DoubleProperty rightHubStartPosX;
    final DoubleProperty rightHubStartPosY;
    final DoubleProperty rightHubHeading;

    public enum KeyPosition {
        LeftFacingOut,
        MiddleFacingOut,
        RightFacingOut,
        LeftHubFacingHub,
        RightHubFacingHub,
        CollectSecondCargo,
        CollectHumanStationCargo
    }

    @Inject
    public PoseSubsystem(
        CommonLibFactory clf, 
        PropertyFactory pf, 
        DriveSubsystem drive
        ) {
        super(clf, pf);
        this.drive = drive;
        this.levelThresholdDegrees = pf.createPersistentProperty("Levelling Threshold", 3);

        this.leftStartPosX = pf.createPersistentProperty("Starting Left Position X Value", 118);
        this.leftStartPosY = pf.createPersistentProperty("Starting Left Position Y Value", 242);
        this.leftHeading = pf.createPersistentProperty("Starting Left Heading", 226);

        this.midStartPosX = pf.createPersistentProperty("Starting Mid Position X Value", 220);
        this.midStartPosY = pf.createPersistentProperty("Starting Mid Position Y Value", 261);
        this.midHeading = pf.createPersistentProperty("Starting Mid Heading", -67);

        this.rightStartPosX = pf.createPersistentProperty("Starting Right Position X Value", 252);
        this.rightStartPosY = pf.createPersistentProperty("Starting Right Position Y Value", 300);
        this.rightHeading = pf.createPersistentProperty("Starting Right Heading", 2);

        this.leftHubStartPosX = pf.createPersistentProperty("Starting LeftHub X Value", 142);
        this.leftHubStartPosY = pf.createPersistentProperty("Starting LeftHub Y Value", 273);
        this.leftHubHeading = pf.createPersistentProperty("Starting LeftHub Heading", 68);
        
        this.rightHubStartPosX = pf.createPersistentProperty("Starting RightHub X Value", 212);
        this.rightHubStartPosY = pf.createPersistentProperty("Starting RightHub Y Value", 304);
        this.rightHubHeading = pf.createPersistentProperty("Starting RightHub Heading", 159);

    /* Remember: WPILib uses a different coordinate convention than our legacy code. Theirs:
          //   0,+y. 90 degrees
        //       ----------------------------
        //       |                          |
        // Driver|                          |
        //       |                          |
        //       |                          |
        //       ----------------------------
        //     0,0                        +x,0. 0 degrees
        //
        // Our  code:
        //     0,0                        +y,90 degrees
        //       ----------------------------
        //       |                          |
        // Driver|                          |
        //       |                          |
        //       |                          |
        //       ----------------------------
        //     x,0. 0 degrees
        //
        // However, this isn't really a big deal - since both systems respect the "Right hand rule" (e.g. the Y axis is 90 degrees CCW to the X axis)
        // we can just ignore the difference. Any tool that looks at our position will be confused, since we will appear to be driving outside the field,
        // but it will work just fine for our own code.
        // That being said, at some point we should probably switch to the WPILib convention, since there are a number of path-planning tools and other
         utilities that expect the same conventions. */

        swerveOdometry = new SwerveDriveOdometry(drive.getSwerveDriveKinematics(), new Rotation2d(), new Pose2d(0, 0, new Rotation2d()));
    }

    public boolean isRobotLevel() {
        return Math.abs(getRobotRoll()) < levelThresholdDegrees.get();
    }

    @Override
    protected void updateOdometry() {
        // The swerve modules return units in meters, which is what the swerve odometry expects.
        // In principle the input/output here is unitless, but we're using meters for consistency.
        Pose2d updatedPosition = swerveOdometry.update(this.getCurrentHeading(),
            drive.getFrontLeftSwerveModuleSubsystem().getCurrentState(),
            drive.getFrontRightSwerveModuleSubsystem().getCurrentState(),
            drive.getRearLeftSwerveModuleSubsystem().getCurrentState(),
            drive.getRearRightSwerveModuleSubsystem().getCurrentState());

            // Convert back to inches
            totalDistanceX.set(updatedPosition.getY() * PoseSubsystem.INCHES_IN_A_METER);
            totalDistanceY.set(-updatedPosition.getX() * PoseSubsystem.INCHES_IN_A_METER);
    }

    public XYPair getKeyPosition(KeyPosition position) {
        switch (position) {
            case LeftFacingOut:
                return new XYPair(leftStartPosX.get(), leftStartPosY.get());
            case MiddleFacingOut:
                return new XYPair(midStartPosX.get(), midStartPosY.get());
            case RightFacingOut:
                return new XYPair(rightStartPosX.get(), rightStartPosY.get());
            case LeftHubFacingHub:
                return new XYPair(leftHubStartPosX.get(), leftHubStartPosY.get());
            case RightHubFacingHub:
                return new XYPair(rightHubStartPosX.get(), rightHubStartPosY.get());
            default:
                log.warn("Invalid starting position, returning zero position.");
                return new XYPair(0, 0);
        }
    }

    public Rotation2d getKeyHeading(KeyPosition position) {
        switch (position) {
            case LeftFacingOut:
                return Rotation2d.fromDegrees(leftHeading.get());
            case MiddleFacingOut:
                return Rotation2d.fromDegrees(midHeading.get());
            case RightFacingOut:
                return Rotation2d.fromDegrees(rightHeading.get());
            case LeftHubFacingHub:
                return Rotation2d.fromDegrees(leftHubHeading.get());
            case RightHubFacingHub:
                return Rotation2d.fromDegrees(rightHubHeading.get());
            default:
                log.warn("Invalid starting position, returning 90 degrees.");
                return Rotation2d.fromDegrees(90);
        }
    }

    public FieldPose getStartingPose(KeyPosition position) {
        return new FieldPose(getKeyPosition(position), getKeyHeading(position));
    }

    public void setCurrentPosition(double newXPosition, double newYPosition, WrappedRotation2d heading) {
        super.setCurrentPosition(newXPosition, newYPosition);
        swerveOdometry.resetPosition(
            new Pose2d(
                newXPosition / PoseSubsystem.INCHES_IN_A_METER, 
                newYPosition / PoseSubsystem.INCHES_IN_A_METER, 
                this.getCurrentHeading()),
            this.getCurrentHeading());
    }

    @Override
    public void setCurrentPosition(double newXPosition, double newYPosition) {
        setCurrentPosition(newXPosition, newYPosition, this.getCurrentHeading());
    }

    public void setCurrentPose(FieldPose newPose) {
        setCurrentPosition(newPose.getPoint().x, newPose.getPoint().y, newPose.getHeading());
        this.setCurrentHeading(newPose.getHeading().getDegrees());
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftTotalDistance();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightTotalDistance();
    }

}