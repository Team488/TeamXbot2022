package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.commands.ClimberArmMaintainerCommand;
import competition.subsystems.climber_arm.commands.DualArmControllerCommandWithJoysticks;
import competition.subsystems.climber_arm.commands.MotorArmExtendCommand;
import competition.subsystems.climber_arm.commands.MotorArmRetractCommand;
import competition.subsystems.climber_arm.commands.MotorArmSetZeroCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;
import competition.subsystems.climber_arm.commands.SetArmsToPositionCommand;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;
import competition.subsystems.collector_stage_2.CollectorStage2Subsystem;
import competition.subsystems.drive.commands.CalibrateSteeringCommand;
import competition.subsystems.drive.commands.DebuggingSwerveWithJoysticksCommand;
import competition.subsystems.drive.commands.GoToNextActiveSwerveModuleCommand;
import competition.subsystems.drive.commands.SetSwerveMotorControllerPidParametersCommand;
import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.drive.commands.TurnLeft90DegreesCommand;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.latch.commands.LatchReleaseCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.commands.ShooterWheelWaitForGoalCommand;
import competition.subsystems.shooterwheel.commands.StopShooterWheelCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.BaseCommand;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.command.NamedInstantCommand;
import xbot.common.controls.sensors.ChordButton;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

/**
 * Maps operator interface buttons to commands
 */
@Singleton
public class OperatorCommandMap {

    final OperatorInterface operatorInterface;

    @Inject
    public OperatorCommandMap(OperatorInterface operatorInterface) {
        this.operatorInterface = operatorInterface;
    }

    // Example for setting up a command to fire when a button is pressed:
    @Inject
    public void setupMyCommands(
            SetRobotHeadingCommand resetHeading,
            PoseSubsystem pose) {
        resetHeading.setHeadingToApply(90);

        NamedInstantCommand resetPosition = new NamedInstantCommand("Reset Position",
                () -> pose.setCurrentPosition(0, 0));
        ParallelCommandGroup resetPose = new ParallelCommandGroup(resetPosition, resetHeading);
        operatorInterface.driverGamepad.getifAvailable(1).whenPressed(resetPose);
    }

    @Inject
    public void setupDriveCommands(
            DebuggingSwerveWithJoysticksCommand swerveDebugging,
            GoToNextActiveSwerveModuleCommand goToNextActiveSwerveModule) {
        operatorInterface.driverGamepad.getifAvailable(2).whenPressed(swerveDebugging);
        operatorInterface.driverGamepad.getifAvailable(3).whenPressed(goToNextActiveSwerveModule);

    }

    @Inject
    public void setupClimbingCommands(
            MotorArmExtendCommand extendArmCommand,
            MotorArmRetractCommand retractArmCommand,
            LatchArmCommand latchArm,
            LatchReleaseCommand latchRelease,
            PivotInCommand pivotIn,
            PivotOutCommand pivotOut,
            DualArmControllerCommandWithJoysticks dualArmWithJoysticksSafe,
            DualArmControllerCommandWithJoysticks dualArmWithJoysticksUnsafe,
            MotorArmSetZeroCommand calibrateBothArms,
            @LeftArm ClimberArmMaintainerCommand leftArmMaintainer,
            @RightArm ClimberArmMaintainerCommand rightArmMaintainer,
            @LeftArm MotorArmStopCommand stopLeftArm,
            @RightArm MotorArmStopCommand stopRightArm,
            Provider<SetArmsToPositionCommand> setArmPositionCommandProvider,
            @LeftArm ClimberArmSubsystem leftArm,
            @RightArm ClimberArmSubsystem rightArm,
            CommonLibFactory clf,
            PropertyFactory pf) {
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.FullyRetracted).includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.ClearCurrentBar).includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.FullyExtended).includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.EngageNextBar).includeOnSmartDashboard();

        ParallelCommandGroup stopBothArms = new ParallelCommandGroup(stopLeftArm, stopRightArm);

        NamedInstantCommand freePawl = new NamedInstantCommand("FreePawlCommand", () -> {
            leftArm.freePawl();
            rightArm.freePawl();
        });

        NamedInstantCommand lockPawl = new NamedInstantCommand("LockPawlCommand", () -> {
            leftArm.lockPawl();
            rightArm.lockPawl();

        });
        ParallelCommandGroup maintainArms = new ParallelCommandGroup(leftArmMaintainer, rightArmMaintainer);

        pf.setPrefix("OperatorCommandMap/");
        DoubleProperty pivotDelayTime = pf.createPersistentProperty("Pivot Delay Time", 0.15);

        // When releasing the latch, we want to wait just a moment (to start falling) before actually pushing the pivot arm out
        // to avoid slamming the climbing arms into the uprights.
        ParallelRaceGroup latchReleaseAndSmallWait = new ParallelRaceGroup(latchRelease, new DelayViaSupplierCommand(() -> pivotDelayTime.get()));
        SequentialCommandGroup latchReleaseThenPivotOut = new SequentialCommandGroup(latchReleaseAndSmallWait, pivotOut);

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.A).whenPressed(stopBothArms);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.B).whenPressed(maintainArms);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.X).whenPressed(dualArmWithJoysticksUnsafe);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.Y).whenPressed(calibrateBothArms);
        
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(pivotIn);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(pivotOut);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftTrigger).whenPressed(freePawl);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightTrigger).whenPressed(lockPawl);    
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.Start).whenPressed(latchArm);

        ChordButton driverNuclearLaunch = clf.createChordButton(
            operatorInterface.driverGamepad.getifAvailable(XboxButton.LeftTrigger),
            operatorInterface.driverGamepad.getifAvailable(XboxButton.RightTrigger)
        );

        ChordButton totalNuclearLaunch = clf.createChordButton(
            driverNuclearLaunch,
            operatorInterface.operatorGamepad.getifAvailable(XboxButton.Back)
        );

        totalNuclearLaunch.whenPressed(latchReleaseThenPivotOut);
        latchRelease.includeOnSmartDashboard();
    }

    @Inject
    public void setupGeneralSwerveCommands(
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
            SetSwerveMotorControllerPidParametersCommand setSteeringPidValues) {
        ParallelCommandGroup swerveCommands = new ParallelCommandGroup(
                maintainSteeringFrontLeft,
                maintainDriveFrontLeft,
                maintainSteeringFrontRight,
                maintainDriveFrontRight,
                maintainSteeringRearLeft,
                maintainDriveRearLeft,
                maintainSteeringRearRight,
                maintainDriveRearRight,
                swerveDriveWithJoysticks);

        operatorInterface.driverGamepad.getifAvailable(5).whenPressed(calibrateSteering);
        operatorInterface.driverGamepad.getifAvailable(6).whenPressed(swerveCommands);
        operatorInterface.driverGamepad.getifAvailable(7).whenPressed(setSteeringPidValues);

        setSteeringPidValues.includeOnSmartDashboard("Commit steering pid values");
    }

    @Inject
    public void setShooterCommand ( OperatorInterface oi,
        ShooterWheelSubsystem shooter,
        StopShooterWheelCommand stopCommand
    ){
        InstantCommand increaseTrim = new NamedInstantCommand("ShooterIncreaseTrim100RPMInstantCommand", () -> shooter.changeTrimRPM(100));
        InstantCommand decreaseTrim = new NamedInstantCommand("ShooterDecreaseTrim100RPMInstantCommand", () -> shooter.changeTrimRPM(-100));
        SmartDashboard.putData("Trim Up", increaseTrim);
        SmartDashboard.putData("Trim down", decreaseTrim);
        stopCommand.includeOnSmartDashboard();
    }

    @Inject
    public void setupMobilityCommands(OperatorInterface oi,
            TurnLeft90DegreesCommand turnleft90,
            SwerveToPointCommand swerveToPoint,
            PropertyFactory pf) {

        pf.setPrefix("OperatorCommandMap/");
        DoubleProperty xTarget = pf.createEphemeralProperty("OI/SwerveToPointTargetX", 0);
        DoubleProperty yTarget = pf.createEphemeralProperty("OI/SwerveToPointTargetY", 0);
        DoubleProperty angleTarget = pf.createEphemeralProperty("OI/SwerveToPointTargetAngle", 0);

        swerveToPoint.setTargetSupplier(
            () -> {
                return new XYPair(xTarget.get(), yTarget.get());
            },
            () -> {
                return angleTarget.get();
            }
        );
        oi.driverGamepad.getifAvailable(8).whenPressed(turnleft90);
        oi.driverGamepad.getifAvailable(4).whenPressed(swerveToPoint);
    }

    @Inject
    public void setupCollectorStage2Commands(CollectorStage2Subsystem subsystem) {
        // TODO: Change these mappings, this is just an example to prove the concept of
        // the SimpleMotorSubsystem
        // operatorInterface.driverGamepad.getifAvailable(1).whenHeld(subsystem.getForwardCommand());
        // operatorInterface.driverGamepad.getifAvailable(2).whenHeld(subsystem.getReverseCommand());
    }
}
