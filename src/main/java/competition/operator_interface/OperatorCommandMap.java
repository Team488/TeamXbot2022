package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import competition.auto_programs.CollectThenHighScoreCommand;
import competition.auto_programs.CollectThenScoreTwiceCommand;
import competition.auto_programs.DoNothingCommand;
import competition.auto_programs.DriveForwardOutOfTarmac;
import competition.auto_programs.GoCollectComebackCommand;
import competition.auto_programs.MoonshotCommand;
import competition.auto_programs.SCSFromOneRobotAwayCommand;
import competition.auto_programs.ShootCollectShootCommand;
import competition.auto_programs.ShootFarThenEscapeCommand;
import competition.auto_programs.ShootRecklesslyThenEscapeCommand;
import competition.auto_programs.ShootThenEscapeCommand;
import competition.commandgroups.DriverFireCommand;
import competition.commandgroups.DriverRecklessFireCommand;
import competition.commandgroups.HumanShootWithVision;
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
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.DebuggingSwerveWithJoysticksCommand;
import competition.subsystems.drive.commands.GoToNextActiveSwerveModuleCommand;
import competition.subsystems.drive.commands.RotateToVisionTargetCommand;
import competition.subsystems.drive.commands.SetSwerveMotorControllerPidParametersCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.drive.commands.TurnLeft90DegreesCommand;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.latch.commands.LatchReleaseCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.KeyPosition;
import competition.subsystems.pose.SetPoseCommand;
import competition.subsystems.pose.SetRobotAngleViaJoysticksCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.shooterwheel.commands.StopShooterWheelCommand;
import competition.subsystems.vision.VisionSubsystem;
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
            DriveSubsystem drive,
            PoseSubsystem pose,
            VisionSubsystem vision,
            SetRobotAngleViaJoysticksCommand setAngleViaJoysticks) {
        resetHeading.setHeadingToApply(90);

        NamedInstantCommand resetPosition = new NamedInstantCommand("Reset Position",
                () -> pose.setCurrentPosition(0, 0));
        ParallelCommandGroup resetPose = new ParallelCommandGroup(resetPosition, resetHeading);

        StartEndCommand enableVisionRotation = new StartEndCommand(
                () -> drive.setRotateToHubActive(true),
                () -> drive.setRotateToHubActive(false));

        operatorInterface.driverGamepad.getifAvailable(XboxButton.A).whenPressed(resetPose);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.RightStick).whenHeld(setAngleViaJoysticks);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.Y).whenHeld(setAngleViaJoysticks);
        operatorInterface.driverGamepad.getifAvailable(XboxButton.RightBumper).whileHeld(enableVisionRotation);

        operatorInterface.driverGamepad.getPovIfAvailable(0).whenHeld(setAngleViaJoysticks);
        operatorInterface.driverGamepad.getPovIfAvailable(90).whenHeld(setAngleViaJoysticks);
        operatorInterface.driverGamepad.getPovIfAvailable(180).whenHeld(setAngleViaJoysticks);
        operatorInterface.driverGamepad.getPovIfAvailable(270).whenHeld(setAngleViaJoysticks);
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

        pf.setPrefix("OperatorCommandMap/");
        DoubleProperty latchOpenTime = pf.createPersistentProperty("Latch Open Time", 2);

        // For normal operation, we want the latch to only be unlatched for a few
        // seconds
        ParallelRaceGroup latchReleaseAndSmallWait = new ParallelRaceGroup(latchRelease,
                new DelayViaSupplierCommand(() -> latchOpenTime.get()));

        var setArmsSafe = new InstantCommand(() -> {
                leftArm.setIgnoreLimits(false);
                rightArm.setIgnoreLimits(false);
        });

        var setArmsLocked = new InstantCommand(() -> {
                leftArm.setArmsUnlocked(false);
                rightArm.setArmsUnlocked(false);
                // also need to calibrate at the current position to keep
                // arms in sync.
                leftArm.setCurrentPositionToZero();
                rightArm.setCurrentPositionToZero();
        });

        var safeLockedArms = new ParallelCommandGroup(setArmsSafe, setArmsLocked);

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightStick).whenPressed(safeLockedArms);

        var setArmsUnsafe = new InstantCommand(() -> {
                leftArm.setIgnoreLimits(true);
                rightArm.setIgnoreLimits(true);
        });
        
        var unlockArms = new InstantCommand(() -> {
                leftArm.setArmsUnlocked(true);
                rightArm.setArmsUnlocked(true);
        });

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftStick).whenPressed(setArmsUnsafe);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightJoystickYAxis).whenPressed(unlockArms);


        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightBumper).whenPressed(pivotIn);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftBumper).whenPressed(pivotOut);
        
        ChordButton totalNuclearLaunch = clf.createChordButton(
                operatorInterface.driverGamepad.getifAvailable(XboxButton.Start),
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
    public void setShooterCommand(OperatorInterface oi,
            ShooterWheelSubsystem shooter,
            StopShooterWheelCommand stopCommand,
            DriverRecklessFireCommand recklessFireCommand,
            DriverFireCommand fireLowCommand,
            DriverFireCommand fireHighCommand,
            HumanShootWithVision fireLowVisionCommand,
            HumanShootWithVision fireHighVisionCommand) {
        fireLowCommand.setTargetRPM(TargetRPM.NearShot);
        fireHighCommand.setTargetRPM(TargetRPM.DistanceShot);


        fireLowVisionCommand.setTarget(Target.Low);
        fireHighVisionCommand.setTarget(Target.High);

        InstantCommand increaseTrim = new NamedInstantCommand("ShooterIncreaseTrim100RPMInstantCommand",
                () -> shooter.changeTrimRPM(100));
        InstantCommand decreaseTrim = new NamedInstantCommand("ShooterDecreaseTrim100RPMInstantCommand",
                () -> shooter.changeTrimRPM(-100));
        SmartDashboard.putData("Trim Up", increaseTrim);
        SmartDashboard.putData("Trim down", decreaseTrim);
        stopCommand.includeOnSmartDashboard();
        

        oi.operatorGamepad.getifAvailable(XboxButton.B).whenHeld(fireLowVisionCommand);
        oi.operatorGamepad.getifAvailable(XboxButton.Y).whenHeld(fireHighVisionCommand);
        oi.operatorGamepad.getifAvailable(XboxButton.A).whenHeld(fireLowCommand);
        oi.operatorGamepad.getifAvailable(XboxButton.X).whenHeld(fireHighCommand);
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

        StartEndCommand activateRobotOrientedDrive = new StartEndCommand(
                () -> drive.setIsRobotOrientedDrive(true),
                () -> drive.setIsRobotOrientedDrive(false));

        oi.driverGamepad.getifAvailable(XboxButton.LeftBumper).whileHeld(activateRobotOrientedDrive);
        //oi.driverGamepad.getifAvailable(XboxButton.RightBumper).whileHeld(activatePrecisionRotation);
    }

    @Inject
    public void setupCollectionCommands(IntakeCommand intake, EjectCommand eject, ConveyorSubsystem conveyer,
            CollectorStage2Subsystem stageTwo, Provider<DeployCollectorCommand> deployCollector,
            RetractCollectorCommand retractCollector,
            RetractCollectorCommand retractCollectorTwo,
            ShooterWheelSubsystem wheel) {

        var setHotDogIntake = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        var setHotDogEject = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());

        ParallelCommandGroup groupIntake = new ParallelCommandGroup(
                setHotDogIntake, intake, stageTwo.getForwardCommand(), deployCollector.get(),
                conveyer.getForwardCommand());
        ParallelCommandGroup groupEject = new ParallelCommandGroup(
                setHotDogEject, eject, stageTwo.getReverseCommand(), conveyer.getReverseCommand(),
                deployCollector.get());

        operatorInterface.operatorGamepad.getifAvailable(XboxButton.RightTrigger).whenHeld(groupIntake);
        operatorInterface.operatorGamepad.getifAvailable(XboxButton.LeftTrigger).whenHeld(groupEject);

        var hotDog = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        
        var retractAndConvey = new ParallelCommandGroup(
                retractCollectorTwo,
                hotDog,
                conveyer.getForwardCommand()
        );

        

        operatorInterface.operatorGamepad.getPovIfAvailable(90).whenHeld(retractAndConvey);
        operatorInterface.operatorGamepad.getPovIfAvailable(180).whenHeld(retractAndConvey);
        operatorInterface.operatorGamepad.getPovIfAvailable(270).whenHeld(retractAndConvey);

        operatorInterface.driverGamepad.getifAvailable(XboxButton.B).whenPressed(retractCollector);

    }
    
    @Inject
    public void setupDebuggingCommands(SmartDashboardTableWrapper dashboard,
            SmartDashboardCommandPutter commandPutter) {
        NamedInstantCommand setFastMode = new NamedInstantCommand("SetFastMode", () -> {
            dashboard.setFastMode(true);
        });
        NamedInstantCommand setSlowMode = new NamedInstantCommand("SetSlowMode", () -> {
            dashboard.setFastMode(false);
        });

        SmartDashboard.putData(setFastMode);
        SmartDashboard.putData(setSlowMode);
    }

    @Inject
    public void setupAutonomousCommands(
            PoseSubsystem pose,
            DoNothingCommand doNothing,
            DriveForwardOutOfTarmac driveFiveFeet,
            GoCollectComebackCommand goCollectComeback,
            ShootCollectShootCommand shootCollectShoot,
            ShootThenEscapeCommand shootThenEscape,
            CollectThenScoreTwiceCommand collectThenScoreTwice,
            ShootRecklesslyThenEscapeCommand shootRecklesslyThenEscape,
            SCSFromOneRobotAwayCommand scsFromOneRobotAwayCommand,
            ShootFarThenEscapeCommand shootFarThenEscape,
            CollectThenHighScoreCommand highScore,
            MoonshotCommand moonshot,
            Provider<SetAutonomousCommand> setAutoCommandProvider,
            Provider<SetRobotHeadingCommand> setHeadingCommandProvider,
            Provider<SetPoseCommand> setPoseCommandProvider,
            RotateToVisionTargetCommand rotate) {
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
        SetAutonomousCommand setCollectThenScoreTwice = setAutoCommandProvider.get();
        setCollectThenScoreTwice.setAutoCommand(collectThenScoreTwice);
        SetAutonomousCommand setShootRecklesslyThenEscape = setAutoCommandProvider.get();
        setShootRecklesslyThenEscape.setAutoCommand(shootRecklesslyThenEscape);
        SetAutonomousCommand setScsFromOneRobotAwayCommand = setAutoCommandProvider.get();
        setScsFromOneRobotAwayCommand.setAutoCommand(scsFromOneRobotAwayCommand);
        SetAutonomousCommand setShootFarThenEscapeCommand = setAutoCommandProvider.get();
        setShootFarThenEscapeCommand.setAutoCommand(shootFarThenEscape);
        SetAutonomousCommand setHighScore = setAutoCommandProvider.get();
        setHighScore.setAutoCommand(highScore);
        SetAutonomousCommand setMoonshot = setAutoCommandProvider.get();
        setMoonshot.setAutoCommand(moonshot);

        setDoNothing.includeOnSmartDashboard("AutoPrograms/DoNothing");
        setDriveFiveFeet.includeOnSmartDashboard("AutoPrograms/DriveFiveFeet");
        setGoCollectComeback.includeOnSmartDashboard("AutoPrograms/GoCollectComeback");
        setShootCollectShoot.includeOnSmartDashboard("AutoPrograms/ShootCollectShoot");
        setShootThenEscape.includeOnSmartDashboard("AutoPrograms/ShootThenEscape");
        setCollectThenScoreTwice.includeOnSmartDashboard("AutoPrograms/CollectThenScoreTwice");
        setShootRecklesslyThenEscape.includeOnSmartDashboard("AutoPrograms/ShootRecklesslyThenEscape");
        setScsFromOneRobotAwayCommand.includeOnSmartDashboard("AutoPrograms/SCSFromOneRobotAway");
        setShootFarThenEscapeCommand.includeOnSmartDashboard("AutoPrograms/ShootFarThenEscape");
        setHighScore.includeOnSmartDashboard("AutoPrograms/HighScore");
        setMoonshot.includeOnSmartDashboard("AutoPrograms/Moonshot");

        operatorInterface.autoGamepad.getPovIfAvailable(0).whenPressed(setDoNothing);
        operatorInterface.autoGamepad.getPovIfAvailable(90).whenPressed(setDriveFiveFeet);
        operatorInterface.autoGamepad.getPovIfAvailable(180).whenPressed(setShootThenEscape);
        operatorInterface.autoGamepad.getPovIfAvailable(270).whenPressed(setHighScore);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.LeftStick).whenPressed(moonshot);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.RightStick).whenPressed(setShootCollectShoot);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.Back).whenPressed(setScsFromOneRobotAwayCommand);
        operatorInterface.autoGamepad.getifAvailable(XboxButton.Start).whenPressed(setShootFarThenEscapeCommand);

        SmartDashboard.putData(rotate.getName(), rotate);

        SetPoseCommand setPoseForLeftStart = setPoseCommandProvider.get();
        setPoseForLeftStart.setPose(pose.getStartingPose(KeyPosition.LeftFacingOut));
        SetPoseCommand setPoseForMiddleStart = setPoseCommandProvider.get();
        setPoseForMiddleStart.setPose(pose.getStartingPose(KeyPosition.MiddleFacingOut));
        SetPoseCommand setPoseForRightStart = setPoseCommandProvider.get();
        setPoseForRightStart.setPose(pose.getStartingPose(KeyPosition.RightFacingOut));
        SetPoseCommand setPoseForLeftHub = setPoseCommandProvider.get();
        setPoseForLeftHub.setPose(pose.getStartingPose(KeyPosition.LeftHubFacingHub));
        SetPoseCommand setPoseForRightHub = setPoseCommandProvider.get();
        setPoseForRightHub.setPose(pose.getStartingPose(KeyPosition.RightHubFacingHub));
        SetPoseCommand setPoseforNeutral = setPoseCommandProvider.get();
        setPoseforNeutral.setPose(new FieldPose(0, 0, 90));

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
