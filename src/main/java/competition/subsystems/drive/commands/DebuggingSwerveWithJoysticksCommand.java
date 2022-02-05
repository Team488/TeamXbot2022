package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

public class DebuggingSwerveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem drive;
    final OperatorInterface oi;

    @Inject
    public DebuggingSwerveWithJoysticksCommand(
        DriveSubsystem drive, 
        OperatorInterface oi,
        @FrontLeftDrive SwerveSteeringSubsystem frontLeftSteering,
        @FrontLeftDrive SwerveDriveSubsystem frontLeftDrive,
        @FrontRightDrive SwerveSteeringSubsystem frontRightSteering,
        @FrontRightDrive SwerveDriveSubsystem frontRightDrive,
        @RearLeftDrive SwerveSteeringSubsystem rearLeftSteering,
        @RearLeftDrive SwerveDriveSubsystem rearLeftDrive,
        @RearRightDrive SwerveSteeringSubsystem rearRightSteering,
        @RearRightDrive SwerveDriveSubsystem rearRightDrive) {
        this.drive = drive;
        this.oi = oi;
        
        this.addRequirements(drive, 
        frontLeftDrive, frontRightDrive, rearLeftDrive, rearRightDrive, 
        frontLeftSteering, frontRightSteering, rearLeftSteering, rearRightSteering);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double drivePower = MathUtils.deadband(oi.gamepad.getLeftStickY(), 0.15, (a) -> a);
        double steeringPower = MathUtils.deadband(oi.gamepad.getRightStickX(), 0.15, (a) -> a);

        drive.controlOnlyActiveSwerveModuleSubsystem(drivePower, steeringPower);
    }
    
}
