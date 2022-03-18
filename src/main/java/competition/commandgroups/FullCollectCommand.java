package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

/**
 * Deploys the collector, runs the collector and conveyor in "intake" mode,
 * and sets the shooter wheel to "HotDog" mode to keep the cargo from escaping.
 * Will not end on its own.
 * Requires:
 * - Collector.
 * - CollectorDeployment.
 * - Conveyor.
 * - ShooterWheel.SetpointLock.
 */
public class FullCollectCommand extends ParallelCommandGroup {
    
    @Inject
    public FullCollectCommand(
        IntakeCommand intake,
        DeployCollectorCommand deploy,
        ShooterWheelSubsystem wheel,
        ConveyorSubsystem conveyor
    ) {
        var setHotDogIntake = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        this.addCommands(setHotDogIntake, intake,  deploy, conveyor.getForwardCommand());
    }
}
