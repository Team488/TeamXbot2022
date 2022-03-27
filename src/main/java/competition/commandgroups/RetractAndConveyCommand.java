package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.collector_deployment.commands.RetractCollectorCommand;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

public class RetractAndConveyCommand extends ParallelCommandGroup{
    
    @Inject
    public RetractAndConveyCommand(ShooterWheelSubsystem wheel, RetractCollectorCommand retractCollector,
    ConveyorSubsystem conveyor) {
        var hotDog = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        this.addCommands(retractCollector, hotDog, conveyor.getForwardCommand());
    }
}
