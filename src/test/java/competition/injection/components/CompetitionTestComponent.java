package competition.injection.components;

import javax.inject.Singleton;

import competition.auto_programs.CollectThenHighScoreCommand;
import competition.auto_programs.CollectThenScoreTwiceCommand;
import competition.auto_programs.DoNothingCommand;
import competition.auto_programs.DriveForwardOutOfTarmac;
import competition.auto_programs.GoCollectComebackCommand;
import competition.auto_programs.MoonshotCommand;
import competition.auto_programs.SCSFromOneRobotAwayCommand;
import competition.auto_programs.ShootCollectShootCommand;
import competition.auto_programs.ShootRecklesslyThenEscapeCommand;
import competition.auto_programs.ShootThenEscapeCommand;
import competition.electrical_contract.ElectricalContract;
import competition.injection.modules.CommonModule;
import competition.injection.modules.CompetitionTestModule;
import competition.subsystems.climber_arm.commands.DualArmControllerCommandWithJoysticks;
import competition.subsystems.climber_arm.commands.MotorArmSetZeroCommand;
import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;
import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.commands.EjectCommand;
import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.collector_deployment.commands.RetractCollectorCommand;
import competition.subsystems.deploy_hood.HoodSubsystem;
import competition.subsystems.deploy_hood.commands.HoodDeployCommand;
import competition.subsystems.deploy_hood.commands.HoodRetractCommand;
import competition.subsystems.drive.commands.SetSwerveMotorControllerPidParametersCommand;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.drive.swerve.SwerveSteeringMotorPidSubsystem;
import competition.subsystems.latch.LatchSubsystem;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.latch.commands.LatchReleaseCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.commands.BangBangCommand;
import competition.subsystems.shooterwheel.commands.ShooterWheelSetPassPowerCommand;
import competition.subsystems.shooterwheel.commands.ShooterWheelWaitForGoalCommand;
import competition.subsystems.vision.VisionShooterGoalsSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import dagger.Component;
import xbot.common.injection.modules.MockControlsModule;
import xbot.common.injection.modules.MockDevicesModule;
import xbot.common.injection.modules.UnitTestModule;

@Singleton
@Component(modules = { UnitTestModule.class, MockDevicesModule.class, MockControlsModule.class, CompetitionTestModule.class, CommonModule.class })
public abstract class CompetitionTestComponent extends BaseRobotComponent {

    public abstract ElectricalContract electricalContract();

    public abstract CollectThenScoreTwiceCommand collectThenScoreTwiceCommand();

    public abstract DoNothingCommand doNothingCommand();

    public abstract DriveForwardOutOfTarmac driveForwardOutOfTarmac();

    public abstract GoCollectComebackCommand goCollectComebackCommand();

    public abstract MoonshotCommand moonshotCommand();

    public abstract SCSFromOneRobotAwayCommand scsFromOneRobotAwayCommand();

    public abstract ShootCollectShootCommand shootCollectShootCommand();

    public abstract ShootRecklesslyThenEscapeCommand shootRecklesslyThenEscapeCommand();

    public abstract ShootThenEscapeCommand shootThenEscapeCommand();

    public abstract CollectThenHighScoreCommand collectThenHighScoreCommand();

    public abstract DualArmControllerCommandWithJoysticks dualArmControllerCommandWithJoysticks();

    public abstract MotorArmSetZeroCommand motorArmSetZeroCommand();

    public abstract ClimberPivotSubsystem climberPivotSubsystem();

    public abstract PivotInCommand pivotInCommand();

    public abstract PivotOutCommand pivotOutCommand();

    public abstract CollectorSubsystem collectorSubsystem();

    public abstract VisionSubsystem visionSubsystem();

    public abstract VisionShooterGoalsSubsystem visionShooterGoalsSubsystem();

    public abstract LatchSubsystem latchSubsystem();

    public abstract LatchArmCommand latchArmCommand();

    public abstract LatchReleaseCommand latchReleaseCommand();

    public abstract SwerveDriveWithJoysticksCommand swerveDriveWithJoysticksCommand();

    public abstract SwerveToPointCommand swerveToPointCommand();

    public abstract SetSwerveMotorControllerPidParametersCommand setSwerveMotorControllerPidParametersCommand();

    public abstract SwerveSteeringMotorPidSubsystem swerveSteeringMotorPidSubsystem();

    public abstract EjectCommand ejectCommand();

    public abstract IntakeCommand intakeCommand();

    public abstract StopCollectorCommand stopCollectorCommand();

    public abstract CollectorDeploymentSubsystem collectorDeploymentSubsystem();

    public abstract DeployCollectorCommand deployCollectorCommand();

    public abstract RetractCollectorCommand retractCollectorCommand();

    public abstract HoodSubsystem hoodSubsystem();

    public abstract HoodDeployCommand hoodDeployCommand();

    public abstract HoodRetractCommand hoodRetractCommand();

    public abstract BangBangCommand bangBangCommand();

    public abstract ShooterWheelSubsystem shooterWheelSubsystem();

    public abstract ShooterWheelWaitForGoalCommand shooterWheelWaitForGoalCommand();

    public abstract ShooterWheelSetPassPowerCommand shooterWheelSetPassPowerCommand();
}