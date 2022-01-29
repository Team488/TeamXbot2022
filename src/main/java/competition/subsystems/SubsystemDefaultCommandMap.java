package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, TankDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupFrontLeftSwerveSteeringSubsystem(
            @FrontLeftDrive SwerveSteeringSubsystem subsystem,
            @FrontLeftDrive SwerveSteeringMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupFrontRightSwerveSteeringSubsystem(
            @FrontRightDrive SwerveSteeringSubsystem subsystem,
            @FrontRightDrive SwerveSteeringMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRearLeftSwerveSteeringSubsystem(
            @RearLeftDrive SwerveSteeringSubsystem subsystem,
            @RearLeftDrive SwerveSteeringMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRearRightSwerveSteeringSubsystem(
            @RearRightDrive SwerveSteeringSubsystem subsystem,
            @RearRightDrive SwerveSteeringMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }
}
