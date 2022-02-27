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
import competition.subsystems.climber_arm.commands.DualArmBalancerCommand;
import competition.subsystems.climber_arm.commands.DualArmControllerCommandWithJoysticks;
import competition.subsystems.climber_arm.commands.MotorArmExtendCommand;
import competition.subsystems.climber_arm.commands.MotorArmRetractCommand;
import competition.subsystems.climber_arm.commands.MotorArmSetZeroCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;
import competition.subsystems.climber_arm.commands.SetArmsToPositionCommand;
import competition.subsystems.climber_pivot.commands.PivotAccordingToArm;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;
import competition.subsystems.collector.commands.EjectCommand;
import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector_stage_2.CollectorStage2Subsystem;
import competition.subsystems.conveyer.ConveyerSubsystem;
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
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
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
        operatorInterface.driverGamepad.getifAvailable(XboxButton.A).whenPressed(resetPose);
    }

    @Inject
    public void setupDriveCommands(
            DebuggingSwerveWithJoysticksCommand swerveDebugging,
            GoToNextActiveSwerveModuleCommand goToNextActiveSwerveModule) {
        operatorInterface.driverGamepad.getifAvailable(XboxButton.B).whenPressed(swerveDebugging);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.X).whenPressed(goToNextActiveSwerveModule);

    }

    @Inject
    public void setupClimbingCommands(
            MotorArmExtendCommand extendArmCommand,
            MotorArmRetractCommand retractArmCommand,
            LatchArmCommand latchArm,
            LatchReleaseCommand latchRelease,
            LatchReleaseCommand latchReleaseDashboardOnly,
            PivotInCommand pivotIn,
            PivotOutCommand pivotOut,
            PivotOutCommand pivotOutAsPartOfLatchRelease,
            DualArmControllerCommandWithJoysticks dualArmWithJoysticksSafe,
            DualArmControllerCommandWithJoysticks dualArmWithJoysticksUnsafe,
            MotorArmSetZeroCommand calibrateBothArms,
            DualArmBalancerCommand dualArmBalancer,
            PivotAccordingToArm pivotAccordingToArm,
            ConveyerSubsystem conveyer,
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

        //ParallelCommandGroup stopBothArms = new ParallelCommandGroup(stopLeftArm, stopRightArm);
        ParallelCommandGroup maintainArms = new ParallelCommandGroup(leftArmMaintainer, rightArmMaintainer);

        dualArmBalancer.setSafe(true);

        pf.setPrefix("OperatorCommandMap/");
        DoubleProperty latchOpenTime = pf.createPersistentProperty("Latch Open Time", 1);

        // For normal operation, we want the latch to only be unlatched for a few seconds
        ParallelRaceGroup latchReleaseAndSmallWait = new ParallelRaceGroup(latchRelease, new DelayViaSupplierCommand(() -> latchOpenTime.get()));

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.A).whenPressed(dualArmBalancer);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.B).whenPressed(maintainArms);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.X).whenPressed(dualArmWithJoysticksUnsafe);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.Y).whenPressed(calibrateBothArms);
        
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(pivotIn);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(pivotOut);

        ChordButton driverNuclearLaunch = clf.createChordButton(
            operatorInterface.driverGamepad.getifAvailable(XboxButton.LeftTrigger),
            operatorInterface.driverGamepad.getifAvailable(XboxButton.RightTrigger)
        );

        ChordButton totalNuclearLaunch = clf.createChordButton(
            driverNuclearLaunch,
            operatorInterface.operatorGamepad.getifAvailable(XboxButton.Back)
        );

        totalNuclearLaunch.whileHeld(latchReleaseAndSmallWait, false);

        latchReleaseDashboardOnly.includeOnSmartDashboard();
        latchArm.includeOnSmartDashboard();

        operatorInterface.operatorGamepad.getPovIfAvailable(0).whenPressed(pivotAccordingToArm);
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

        operatorInterface.driverGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(calibrateSteering);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(swerveCommands);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.Back).whenPressed(setSteeringPidValues);

        setSteeringPidValues.includeOnSmartDashboard("Commit steering pid values");
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
        oi.driverGamepad.getifAvailable(XboxButton.Start).whenPressed(turnleft90);
        oi.driverGamepad.getifAvailable(XboxButton.Y).whenPressed(swerveToPoint);
    }

    @Inject
    public void setupCollectorCommands(IntakeCommand intake, EjectCommand eject, ConveyerSubsystem conveyer) {

        ParallelCommandGroup groupIntake = new ParallelCommandGroup(intake, conveyer.getForwardCommand());
        ParallelCommandGroup groupEject = new ParallelCommandGroup(eject, conveyer.getReverseCommand());

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftTrigger).whenHeld(groupIntake);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightTrigger).whenHeld(groupEject);  
    }

    @Inject
    public void setupCollectorStage2Commands(CollectorStage2Subsystem subsystem) {
        // TODO: Change these mappings, this is just an example to prove the concept of
        // the SimpleMotorSubsystem
        // operatorInterface.driverGamepad.getifAvailable(1).whenHeld(subsystem.getForwardCommand());
        // operatorInterface.driverGamepad.getifAvailable(2).whenHeld(subsystem.getReverseCommand());
    }
}
