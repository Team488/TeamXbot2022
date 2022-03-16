package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import competition.auto_programs.DoNothingCommand;
import competition.auto_programs.DriveFiveFeetCommand;
import competition.auto_programs.GoCollectComebackCommand;
import competition.auto_programs.ShootCollectShootCommand;
import competition.auto_programs.ShootThenEscapeCommand;
import competition.commandgroups.FireCommand;
import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
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
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.collector_deployment.commands.RetractCollectorCommand;
import competition.subsystems.collector_stage_2.CollectorStage2Subsystem;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.deploy_hood.commands.HoodDeployCommand;
import competition.subsystems.deploy_hood.commands.HoodRetractCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.DebuggingSwerveWithJoysticksCommand;
import competition.subsystems.drive.commands.GoToNextActiveSwerveModuleCommand;
import competition.subsystems.drive.commands.SetSwerveMotorControllerPidParametersCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.drive.commands.TurnLeft90DegreesCommand;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.latch.commands.LatchReleaseCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.StartingPosition;
import competition.subsystems.pose.SetPoseCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.shooterwheel.commands.StopShooterWheelCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.command.NamedInstantCommand;
import xbot.common.command.SmartDashboardCommandPutter;
import xbot.common.controls.sensors.ChordButton;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.SmartDashboardTableWrapper;
import xbot.common.subsystems.autonomous.SetAutonomousCommand;
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
            ConveyorSubsystem conveyer,
            @LeftArm ClimberArmMaintainerCommand leftArmMaintainer,
            @RightArm ClimberArmMaintainerCommand rightArmMaintainer,
            @LeftArm MotorArmStopCommand stopLeftArm,
            @RightArm MotorArmStopCommand stopRightArm,
            Provider<SetArmsToPositionCommand> setArmPositionCommandProvider,
            @LeftArm ClimberArmSubsystem leftArm,
            @RightArm ClimberArmSubsystem rightArm,
            CommonLibFactory clf,
            PropertyFactory pf) {
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.FullyRetracted)
                .includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.ClearCurrentBar)
                .includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.FullyExtended)
                .includeOnSmartDashboard();
        setArmPositionCommandProvider.get().setTargetPosition(SetArmsToPositionCommand.TargetPosition.EngageNextBar)
                .includeOnSmartDashboard();

        dualArmBalancer.setSafe(true);

        pf.setPrefix("OperatorCommandMap/");
        DoubleProperty latchOpenTime = pf.createPersistentProperty("Latch Open Time", 2);

        // For normal operation, we want the latch to only be unlatched for a few
        // seconds
        ParallelRaceGroup latchReleaseAndSmallWait = new ParallelRaceGroup(latchRelease,
                new DelayViaSupplierCommand(() -> latchOpenTime.get()));

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.A).whenPressed(dualArmBalancer);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.X).whenPressed(dualArmWithJoysticksUnsafe);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.Back).whenPressed(calibrateBothArms);

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(pivotIn);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(pivotOut);

        ChordButton driverNuclearLaunch = clf.createChordButton(
                operatorInterface.driverGamepad.getifAvailable(XboxButton.Start),
                operatorInterface.driverGamepad.getifAvailable(XboxButton.Back));

        ChordButton totalNuclearLaunch = clf.createChordButton(
                driverNuclearLaunch,
                operatorInterface.operatorGamepad.getifAvailable(XboxButton.Start));

        totalNuclearLaunch.whenPressed(latchReleaseAndSmallWait);
        latchReleaseDashboardOnly.includeOnSmartDashboard();
        latchArm.includeOnSmartDashboard();
    }

    @Inject
    public void setupGeneralSwerveCommands(SetSwerveMotorControllerPidParametersCommand setSteeringPidValues) {
        setSteeringPidValues.includeOnSmartDashboard("Commit steering pid values");
    }

    @Inject
    public void setShooterCommand ( OperatorInterface oi,
        ShooterWheelSubsystem shooter,
        StopShooterWheelCommand stopCommand,
        FireCommand fireCloseCommand,
        FireCommand fireFarCommand
    ){
        fireCloseCommand.setTargetRPM(TargetRPM.NearShot);
        fireFarCommand.setTargetRPM(TargetRPM.DistanceShot);

        InstantCommand increaseTrim = new NamedInstantCommand("ShooterIncreaseTrim100RPMInstantCommand", () -> shooter.changeTrimRPM(100));
        InstantCommand decreaseTrim = new NamedInstantCommand("ShooterDecreaseTrim100RPMInstantCommand", () -> shooter.changeTrimRPM(-100));
        SmartDashboard.putData("Trim Up", increaseTrim);
        SmartDashboard.putData("Trim down", decreaseTrim);
        stopCommand.includeOnSmartDashboard();

        oi.operatorGamepad.getifAvailable(XboxButton.Y).whenHeld(fireFarCommand);
        oi.operatorGamepad.getifAvailable(XboxButton.B).whenHeld(fireCloseCommand);
    }

    @Inject
    public void setupMobilityCommands(OperatorInterface oi,
            TurnLeft90DegreesCommand turnleft90,
            SwerveToPointCommand swerveToPoint,
            DriveSubsystem drive,
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
                });

        // Precision Commands
        StartEndCommand activatePrecisionDrive = new StartEndCommand(
            () -> drive.setPrecisionTranslationActive(true),
            () -> drive.setPrecisionTranslationActive(false));

        StartEndCommand activatePrecisionRotation = new StartEndCommand(
            () -> drive.setPrecisionRotationActive(true),
            () -> drive.setPrecisionRotationActive(false));

        StartEndCommand activateCollectorOrientedTurning = new StartEndCommand(
            () -> drive.setCollectorOrientedTurningActive(true),
            () -> drive.setCollectorOrientedTurningActive(false));

        oi.driverGamepad.getifAvailable(XboxButton.LeftBumper).whileHeld(activatePrecisionDrive);
        oi.driverGamepad.getifAvailable(XboxButton.RightBumper).whileHeld(activatePrecisionRotation);
    }

    @Inject
    public void setupCollectionCommands(IntakeCommand intake, EjectCommand eject, ConveyorSubsystem conveyer,
            CollectorStage2Subsystem stageTwo, Provider<DeployCollectorCommand> deployCollector, RetractCollectorCommand retractCollector,
            ShooterWheelSubsystem wheel) {

        var setHotDogIntake = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        var setHotDogEject = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());

        ParallelCommandGroup groupIntake = new ParallelCommandGroup(
            setHotDogIntake, intake, stageTwo.getForwardCommand(), deployCollector.get(), conveyer.getForwardCommand());
        ParallelCommandGroup groupEject = new ParallelCommandGroup(
            setHotDogEject, eject, stageTwo.getReverseCommand(), conveyer.getReverseCommand(), deployCollector.get());

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightTrigger).whenHeld(groupIntake);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftTrigger).whenHeld(groupEject);
        
        operatorInterface.operatorGamepad.getPovIfAvailable(0).whenPressed(retractCollector);
        operatorInterface.operatorGamepad.getPovIfAvailable(90).whenPressed(retractCollector);
        operatorInterface.operatorGamepad.getPovIfAvailable(180).whenPressed(retractCollector);
        operatorInterface.operatorGamepad.getPovIfAvailable(270).whenPressed(retractCollector);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.B).whenPressed(retractCollector);


    }

    @Inject
    public void setupLaunchingCommands(HoodDeployCommand deployHood, HoodRetractCommand retractHood) {
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftStick).whenPressed(deployHood);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightStick).whenPressed(retractHood);
    }


    @Inject
    public void setupDebuggingCommands(SmartDashboardTableWrapper dashboard,
    SmartDashboardCommandPutter commandPutter) {
        NamedInstantCommand setFastMode = 
        new NamedInstantCommand("SetFastMode", () -> {dashboard.setFastMode(true);});
        NamedInstantCommand setSlowMode =
        new NamedInstantCommand("SetSlowMode", () -> {dashboard.setFastMode(false);});

        SmartDashboard.putData(setFastMode);
        SmartDashboard.putData(setSlowMode);
    }

    @Inject
    public void setupAutonomousCommands(
        PoseSubsystem pose,
        DoNothingCommand doNothing,
        DriveFiveFeetCommand driveFiveFeet,
        GoCollectComebackCommand goCollectComeback,
        ShootCollectShootCommand shootCollectShoot,
        ShootThenEscapeCommand shootThenEscape,
        Provider<SetAutonomousCommand> setAutoCommandProvider,
        Provider<SetRobotHeadingCommand> setHeadingCommandProvider,
        Provider<SetPoseCommand> setPoseCommandProvider)
    {
        SetAutonomousCommand setDoNothing = setAutoCommandProvider.get();
        setDoNothing.setAutoCommand(doNothing);
        SetAutonomousCommand setDriveFiveFeet = setAutoCommandProvider.get();
        setDriveFiveFeet.setAutoCommand(driveFiveFeet);
        SetAutonomousCommand setGoCollectComeback = setAutoCommandProvider.get();
        setGoCollectComeback.setAutoCommand(goCollectComeback);
        SetAutonomousCommand setShootCollectShoot = setAutoCommandProvider.get();
        setShootCollectShoot.setAutoCommand(shootCollectShoot);
        SetAutonomousCommand setShootThenEscape = setAutoCommandProvider.get();
        setShootThenEscape.setAutoCommand(shootThenEscape);


        setDoNothing.includeOnSmartDashboard("AutoPrograms/DoNothing");
        setDriveFiveFeet.includeOnSmartDashboard("AutoPrograms/DriveFiveFeet");
        setGoCollectComeback.includeOnSmartDashboard("AutoPrograms/GoCollectComeback");
        setShootCollectShoot.includeOnSmartDashboard("AutoPrograms/ShootCollectShoot");
        setShootThenEscape.includeOnSmartDashboard("AutoPrograms/ShootThenEscape");

        operatorInterface.autoGamepad.getPovIfAvailable(0).whenPressed(setDoNothing);
        operatorInterface.autoGamepad.getPovIfAvailable(90).whenPressed(setDriveFiveFeet);
        operatorInterface.autoGamepad.getPovIfAvailable(180).whenPressed(setShootCollectShoot);
        operatorInterface.autoGamepad.getPovIfAvailable(270).whenPressed(setShootThenEscape);

        SetPoseCommand setPoseForLeftStart = setPoseCommandProvider.get();
        setPoseForLeftStart.setPose(pose.getStartingPose(StartingPosition.Left));
        SetPoseCommand setPoseForMiddleStart = setPoseCommandProvider.get();
        setPoseForMiddleStart.setPose(pose.getStartingPose(StartingPosition.Middle));
        SetPoseCommand setPoseForRightStart = setPoseCommandProvider.get();
        setPoseForRightStart.setPose(pose.getStartingPose(StartingPosition.Right));
        SetPoseCommand setPoseForLeftHub = setPoseCommandProvider.get();
        setPoseForLeftHub.setPose(pose.getStartingPose(StartingPosition.LeftHub));
        SetPoseCommand setPoseForRightHub = setPoseCommandProvider.get();
        setPoseForRightHub.setPose(pose.getStartingPose(StartingPosition.RightHub));
        SetPoseCommand setPoseforNeutral = setPoseCommandProvider.get();
        setPoseforNeutral.setPose(new FieldPose(0,0,90));

        operatorInterface.autoGamepad.getifAvailable(XboxButton.X).whenPressed(setPoseForLeftStart);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.Y).whenPressed(setPoseForMiddleStart);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.B).whenPressed(setPoseForRightStart);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.A).whenPressed(setPoseforNeutral);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(setPoseForLeftHub);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(setPoseForRightHub);

        SmartDashboard.putData("Start/Left", setPoseForLeftStart);
        SmartDashboard.putData("Start/Middle", setPoseForMiddleStart);
        SmartDashboard.putData("Start/Right", setPoseForRightStart);
        SmartDashboard.putData("Start/LeftHub", setPoseForLeftHub);
        SmartDashboard.putData("Start/RightHub", setPoseForRightHub);
    }
}
