package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    private final DriveSubsystem drive;
    
    public final SwerveModuleState frontLeftSwerveModuleState;
    public final SwerveModuleState frontRightSwerveModuleState;
    public final SwerveModuleState rearLeftSwerveModuleState;
    public final SwerveModuleState rearRightSwerveModuleState;

    @Inject
    public PoseSubsystem(CommonLibFactory clf, PropertyFactory propManager, DriveSubsystem drive) {
        super(clf, propManager);
        this.drive = drive;
        
        frontLeftSwerveModuleState = new SwerveModuleState();
        frontRightSwerveModuleState = new SwerveModuleState();
        rearLeftSwerveModuleState = new SwerveModuleState();
        rearRightSwerveModuleState = new SwerveModuleState();
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