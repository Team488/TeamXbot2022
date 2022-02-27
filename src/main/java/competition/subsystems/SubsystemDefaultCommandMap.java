package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import competition.subsystems.latch.LatchSubsystem;
import competition.subsystems.latch.commands.LatchArmCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, SwerveDriveWithJoysticksCommand command) {
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
    
    @Inject
    public void setupFrontLeftSwerveDriveSubsystem(
            @FrontLeftDrive SwerveDriveSubsystem subsystem,
            @FrontLeftDrive SwerveDriveMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupFrontRightSwerveDriveSubsystem(
            @FrontRightDrive SwerveDriveSubsystem subsystem,
            @FrontRightDrive SwerveDriveMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRearLeftSwerveDriveSubsystem(
            @RearLeftDrive SwerveDriveSubsystem subsystem,
            @RearLeftDrive SwerveDriveMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupRearRightSwerveDriveSubsystem(
            @RearRightDrive SwerveDriveSubsystem subsystem,
            @RearRightDrive SwerveDriveMaintainerCommand command) {
        subsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupLatchSubsystem(LatchSubsystem subsystem, LatchArmCommand latch) {
        subsystem.setDefaultCommand(latch);
    }

    // For now, have the default state of the arms be stopped - that's pretty safe. Later,
    // once we have the PIDs tuned, we can move to using the maintainers.
    @Inject
    public void setupArms(
        @LeftArm ClimberArmSubsystem leftArm,
        @RightArm ClimberArmSubsystem rightArm,
        @LeftArm MotorArmStopCommand leftArmStopCommand,
        @RightArm MotorArmStopCommand rightArmStopCommand) {
        leftArm.setDefaultCommand(leftArmStopCommand);
        rightArm.setDefaultCommand(rightArmStopCommand);
    }
}
