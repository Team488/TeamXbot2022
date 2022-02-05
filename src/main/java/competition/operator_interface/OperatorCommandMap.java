package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.drive.commands.DebuggingSwerveWithJoysticksCommand;
import competition.subsystems.drive.commands.GoToNextActiveSwerveModuleCommand;
import competition.subsystems.drive.commands.SimpleCrabDriveFromGamepadCommand;
import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

/**
 * Maps operator interface buttons to commands
 */
@Singleton
public class OperatorCommandMap {
    
    // Example for setting up a command to fire when a button is pressed:
    @Inject
    public void setupMyCommands(
            OperatorInterface operatorInterface,
            SetRobotHeadingCommand resetHeading)
    {
        resetHeading.setHeadingToApply(90);
        operatorInterface.gamepad.getifAvailable(1).whenPressed(resetHeading);
    }

    @Inject
    public void setupDriveCommands(
        OperatorInterface operatorInterface, 
        SimpleCrabDriveFromGamepadCommand crabDrive,
        DebuggingSwerveWithJoysticksCommand swerveDebugging,
        GoToNextActiveSwerveModuleCommand goToNextActiveSwerveModule) 
    {
        operatorInterface.gamepad.getifAvailable(4).whenPressed(crabDrive);
        operatorInterface.gamepad.getifAvailable(2).whenPressed(swerveDebugging);
        operatorInterface.gamepad.getifAvailable(3).whenPressed(goToNextActiveSwerveModule);
    }

    @Inject
    public void setupGeneralSwerveCommands(
        OperatorInterface operatorInterface,
        @FrontLeftDrive SwerveSteeringMaintainerCommand maintainSteeringFrontLeft,
        @FrontLeftDrive SwerveDriveMaintainerCommand maintainDriveFrontLeft,
        @FrontRightDrive SwerveSteeringMaintainerCommand maintainSteeringFrontRight,
        @FrontRightDrive SwerveDriveMaintainerCommand maintainDriveFrontRight,
        @RearLeftDrive SwerveSteeringMaintainerCommand maintainSteeringRearLeft,
        @RearLeftDrive SwerveDriveMaintainerCommand maintainDriveRearLeft,
        @RearRightDrive SwerveSteeringMaintainerCommand maintainSteeringRearRight,
        @RearRightDrive SwerveDriveMaintainerCommand maintainDriveRearRight) 
    {
        ParallelCommandGroup swerveCommands = new ParallelCommandGroup(
            maintainSteeringFrontLeft,
            maintainDriveFrontLeft,
            maintainSteeringFrontRight,
            maintainDriveFrontRight,
            maintainSteeringRearLeft,
            maintainDriveRearLeft,
            maintainSteeringRearRight,
            maintainDriveRearRight);
        operatorInterface.gamepad.getifAvailable(5).whenPressed(swerveCommands);
    }
}
