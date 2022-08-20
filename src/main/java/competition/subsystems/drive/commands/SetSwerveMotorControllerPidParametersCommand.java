package competition.subsystems.drive.commands;

import javax.inject.Inject;

import competition.injection.swerve.FrontLeftSwerveComponent;
import competition.injection.swerve.FrontRightSwerveComponent;
import competition.injection.swerve.RearLeftSwerveComponent;
import competition.injection.swerve.RearRightSwerveComponent;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;

public class SetSwerveMotorControllerPidParametersCommand extends BaseCommand {

    private final SwerveDriveSubsystem frontLeftDrive;
    private final SwerveDriveSubsystem frontRightDrive;
    private final SwerveDriveSubsystem rearLeftDrive;
    private final SwerveDriveSubsystem rearRightDrive;
    private final SwerveSteeringSubsystem frontLeftSteering;
    private final SwerveSteeringSubsystem frontRightSteering;
    private final SwerveSteeringSubsystem rearLeftSteering;
    private final SwerveSteeringSubsystem rearRightSteering;

    @Inject
    public SetSwerveMotorControllerPidParametersCommand(
        FrontLeftSwerveComponent frontLeftSwerveComponent,
        FrontRightSwerveComponent frontRightSwerveComponent,
        RearLeftSwerveComponent rearLeftSwerveComponent,
        RearRightSwerveComponent rearRightSwerveComponent)
    {
        this.frontLeftDrive = frontLeftSwerveComponent.swerveDriveSubsystem();
        this.frontRightDrive = frontLeftSwerveComponent.swerveDriveSubsystem();
        this.rearLeftDrive = rearLeftSwerveComponent.swerveDriveSubsystem();
        this.rearRightDrive = rearRightSwerveComponent.swerveDriveSubsystem();
        this.frontLeftSteering = frontLeftSwerveComponent.swerveSteeringSubsystem();
        this.frontRightSteering = frontRightSwerveComponent.swerveSteeringSubsystem();
        this.rearLeftSteering = rearLeftSwerveComponent.swerveSteeringSubsystem();
        this.rearRightSteering = rearRightSwerveComponent.swerveSteeringSubsystem();
    }

    @Override
    public void initialize() {
        setDriveParameters(this.frontLeftDrive);
        setDriveParameters(this.frontRightDrive);
        setDriveParameters(this.rearLeftDrive);
        setDriveParameters(this.rearRightDrive);
        setSteeringParameters(this.frontLeftSteering);
        setSteeringParameters(this.frontRightSteering);
        setSteeringParameters(this.rearLeftSteering);
        setSteeringParameters(this.rearRightSteering);
    }

    private void setDriveParameters(SwerveDriveSubsystem subsystem) {
        subsystem.setMotorControllerPositionPidParameters();
    }

    private void setSteeringParameters(SwerveSteeringSubsystem subsystem) {
        subsystem.setMotorControllerPositionPidParameters();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
}
