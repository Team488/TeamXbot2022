package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.climber_arm.commands.DualArmControllerCommandWithJoysticks;
import competition.subsystems.climber_arm.commands.MotorArmExtendCommand;
import competition.subsystems.climber_arm.commands.MotorArmRetractCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;
import competition.subsystems.drive.commands.CalibrateSteeringCommand;
import competition.subsystems.drive.commands.DebuggingSwerveWithJoysticksCommand;
import competition.subsystems.drive.commands.GoToNextActiveSwerveModuleCommand;
import competition.subsystems.drive.commands.SetSteeringMotorControllerPidParametersCommand;
import competition.subsystems.drive.commands.SimpleCrabDriveFromGamepadCommand;
import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.latch.commands.LatchReleaseCommand;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import xbot.common.command.NamedInstantCommand;
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
            SetRobotHeadingCommand resetHeading,
            PoseSubsystem pose)
    {
        resetHeading.setHeadingToApply(90);

        NamedInstantCommand resetPosition = new NamedInstantCommand("Reset Position", () -> pose.setCurrentPosition(0,0));
        ParallelCommandGroup resetPose = new ParallelCommandGroup(resetPosition, resetHeading);
        operatorInterface.driverGamepad.getifAvailable(1).whenPressed(resetPose);
    }

    @Inject
    public void setupDriveCommands(
        OperatorInterface operatorInterface, 
        SimpleCrabDriveFromGamepadCommand crabDrive,
        DebuggingSwerveWithJoysticksCommand swerveDebugging,
        GoToNextActiveSwerveModuleCommand goToNextActiveSwerveModule) 
    {
        operatorInterface.driverGamepad.getifAvailable(4).whenPressed(crabDrive);
        operatorInterface.driverGamepad.getifAvailable(2).whenPressed(swerveDebugging);
        operatorInterface.driverGamepad.getifAvailable(3).whenPressed(goToNextActiveSwerveModule);

    }
    
    @Inject
    public void setupClimbingCommands(
        OperatorInterface operatorInterface,
        MotorArmExtendCommand extendArmCommand,
        MotorArmRetractCommand retractArmCommand,
        LatchArmCommand latchArm,
        LatchReleaseCommand latchRelease,
        PivotInCommand pivotIn,
        PivotOutCommand pivotOut,
        DualArmControllerCommandWithJoysticks dualArmWithJoysticks,
        @LeftArm MotorArmStopCommand stopLeftArm,
        @RightArm MotorArmStopCommand stopRightArm)
    {
        operatorInterface.operatorGamepad.getifAvailable(8).whenPressed(latchArm);
        operatorInterface.operatorGamepad.getifAvailable(7).whenPressed(latchRelease);
        operatorInterface.operatorGamepad.getifAvailable(5).whenPressed(pivotIn);
        operatorInterface.operatorGamepad.getifAvailable(6).whenPressed(pivotOut);

        ParallelCommandGroup stopBothArms = new ParallelCommandGroup(stopLeftArm, stopRightArm);

        operatorInterface.operatorGamepad.getifAvailable(1).whenPressed(dualArmWithJoysticks);
        operatorInterface.operatorGamepad.getifAvailable(2).whenPressed(stopBothArms);
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
        @RearRightDrive SwerveDriveMaintainerCommand maintainDriveRearRight,
        SwerveDriveWithJoysticksCommand swerveDriveWithJoysticks,
        CalibrateSteeringCommand calibrateSteering,
        SetSteeringMotorControllerPidParametersCommand setSteeringPidValues) 
    {
        ParallelCommandGroup swerveCommands = new ParallelCommandGroup(
            maintainSteeringFrontLeft,
            maintainDriveFrontLeft,
            maintainSteeringFrontRight,
            maintainDriveFrontRight,
            maintainSteeringRearLeft,
            maintainDriveRearLeft,
            maintainSteeringRearRight,
            maintainDriveRearRight,
            swerveDriveWithJoysticks
            );

        operatorInterface.driverGamepad.getifAvailable(5).whenPressed(calibrateSteering);
        operatorInterface.driverGamepad.getifAvailable(6).whenPressed(swerveCommands);
        operatorInterface.driverGamepad.getifAvailable(7).whenPressed(setSteeringPidValues);
    }
}
