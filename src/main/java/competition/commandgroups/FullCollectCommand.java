package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

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
