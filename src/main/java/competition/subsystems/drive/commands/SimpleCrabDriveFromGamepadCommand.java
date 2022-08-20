package competition.subsystems.drive.commands;

import javax.inject.Inject;
import javax.inject.Named;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

/**
 * A really basic crab drive command that sets raw powers to the swerve modules. As a result,
 * the wheels go out of alignment very quickly. Only used for extremely basic benchtop testing,
 * and the {@link DebuggingSwerveWithJoysticksCommand} is probably a better choice in most scenarios.
 */
public class SimpleCrabDriveFromGamepadCommand extends BaseCommand {

    final DriveSubsystem drive;
    final OperatorInterface oi;

    @Inject
    public SimpleCrabDriveFromGamepadCommand(
        DriveSubsystem drive, 
        OperatorInterface oi,
        @Named("FrontLeftDrive") SwerveSteeringSubsystem frontLeftSteering,
        @Named("FrontLeftDrive") SwerveDriveSubsystem frontLeftDrive,
        @Named("FrontRightDrive") SwerveSteeringSubsystem frontRightSteering,
        @Named("FrontRightDrive") SwerveDriveSubsystem frontRightDrive,
        @Named("RearLeftDrive") SwerveSteeringSubsystem rearLeftSteering,
        @Named("RearLeftDrive") SwerveDriveSubsystem rearLeftDrive,
        @Named("RearRightDrive") SwerveSteeringSubsystem rearRightSteering,
        @Named("RearRightDrive") SwerveDriveSubsystem rearRightDrive) {
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
        double drivePower = MathUtils.deadband(oi.driverGamepad.getLeftStickY(), oi.getDriverGamepadTypicalDeadband(), (a) -> a);
        double steeringPower = MathUtils.deadband(oi.driverGamepad.getRightStickX(), oi.getDriverGamepadTypicalDeadband(), (a) -> a);

        drive.crabDrive(drivePower, steeringPower);
    } 
    
}
