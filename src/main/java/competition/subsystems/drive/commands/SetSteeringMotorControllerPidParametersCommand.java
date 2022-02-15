package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.drive.swerve.SwerveSteeringMotorPidSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;

public class SetSteeringMotorControllerPidParametersCommand extends BaseCommand {

    private final SwerveSteeringMotorPidSubsystem config;
    private final SwerveSteeringSubsystem frontLeft;
    private final SwerveSteeringSubsystem frontRight;
    private final SwerveSteeringSubsystem rearLeft;
    private final SwerveSteeringSubsystem rearRight;

    @Inject
    public SetSteeringMotorControllerPidParametersCommand(
        SwerveSteeringMotorPidSubsystem configSubsystem,
        @FrontLeftDrive SwerveSteeringSubsystem frontLeftSteering,
        @FrontRightDrive SwerveSteeringSubsystem frontRightSteering,
        @RearLeftDrive SwerveSteeringSubsystem rearLeftSteering,
        @RearRightDrive SwerveSteeringSubsystem rearRightSteering)
    {
        this.config = configSubsystem;
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
        subsystem.setMotorControllerPositionPidParameters(
            this.config.getP(),
            this.config.getI(),
            this.config.getD(),
            this.config.getFF(),
            this.config.getMinOutput(),
            this.config.getMaxOutput());
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
}
