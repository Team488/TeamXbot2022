package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;

public class SetSteeringMotorControllerPidParametersCommand extends BaseCommand {

    private final SwerveSteeringSubsystem frontLeft;
    private final SwerveSteeringSubsystem frontRight;
    private final SwerveSteeringSubsystem rearLeft;
    private final SwerveSteeringSubsystem rearRight;

    @Inject
    public SetSteeringMotorControllerPidParametersCommand(
        @FrontLeftDrive SwerveSteeringSubsystem frontLeftSteering,
        @FrontRightDrive SwerveSteeringSubsystem frontRightSteering,
        @RearLeftDrive SwerveSteeringSubsystem rearLeftSteering,
        @RearRightDrive SwerveSteeringSubsystem rearRightSteering)
    {
        this.frontLeft = frontLeftSteering;
        this.frontRight = frontRightSteering;
        this.rearLeft = rearLeftSteering;
        this.rearRight = rearRightSteering;
    }

    @Override
    public void initialize() {
        setParameters(this.frontLeft);
        setParameters(this.frontRight);
        setParameters(this.rearLeft);
        setParameters(this.rearRight);
    }

    private void setParameters(SwerveSteeringSubsystem subsystem) {
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
