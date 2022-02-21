package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
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
        @FrontLeftDrive SwerveDriveSubsystem frontLeftDrive,
        @FrontRightDrive SwerveDriveSubsystem frontRightDrive,
        @RearLeftDrive SwerveDriveSubsystem rearLeftDrive,
        @RearRightDrive SwerveDriveSubsystem rearRightDrive,
        @FrontLeftDrive SwerveSteeringSubsystem frontLeftSteering,
        @FrontRightDrive SwerveSteeringSubsystem frontRightSteering,
        @RearLeftDrive SwerveSteeringSubsystem rearLeftSteering,
        @RearRightDrive SwerveSteeringSubsystem rearRightSteering)
    {
        this.frontLeftDrive = frontLeftDrive;
        this.frontRightDrive = frontRightDrive;
        this.rearLeftDrive = rearLeftDrive;
        this.rearRightDrive = rearRightDrive;
        this.frontLeftSteering = frontLeftSteering;
        this.frontRightSteering = frontRightSteering;
        this.rearLeftSteering = rearLeftSteering;
        this.rearRightSteering = rearRightSteering;
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
