package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    private final DriveSubsystem drive;
    
    final SwerveDriveOdometry swerveOdometry;

    DoubleProperty leftStartPosX;
    DoubleProperty leftStartPosY;

    DoubleProperty midStartPosX;
    DoubleProperty midStartPosY;

    DoubleProperty rightStartPosX;
    DoubleProperty rightStartPosY;

    @Inject
    public PoseSubsystem(CommonLibFactory clf, PropertyFactory propManager, DriveSubsystem drive) {
        super(clf, propManager);
        this.drive = drive;

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

    @Inject
    public void setCurrentPositiontoLeft () {
        this.leftStartPosX.get();
        this.leftStartPosY.get();

        setCurrentPosition(leftStartPosX.get(), leftStartPosY.get());
    }

    @Inject
    public void setCurrentPositiontoMid () {
        this.midStartPosX.get();
        this.midStartPosY.get();

        setCurrentPosition(midStartPosX.get(), midStartPosY.get());
    }

    @Inject
    public void setCurrentPositiontoRight () {
        this.rightStartPosX.get();
        this.rightStartPosY.get();

        setCurrentPosition(rightStartPosX.get(), rightStartPosY.get());
    }

    @Override
    public void setCurrentPosition(double newXPosition, double newYPosition) {
        super.setCurrentPosition(newXPosition, newYPosition);
        swerveOdometry.resetPosition(
            new Pose2d(
                newXPosition / PoseSubsystem.INCHES_IN_A_METER, 
                newYPosition / PoseSubsystem.INCHES_IN_A_METER, 
                this.getCurrentHeading()),
            this.getCurrentHeading());
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