package competition.commandgroups;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.conveyer.ConveyorSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class ReverseConveyorForShootingCommand extends SequentialCommandGroup {

    private static Logger log = Logger.getLogger(ReverseConveyorForShootingCommand.class);

    private final DoubleProperty conveyorReverseTimeProp;

    @Inject
    public ReverseConveyorForShootingCommand(PropertyFactory pf, ConveyorSubsystem conveyor) {
        pf.setPrefix(getName());
        conveyorReverseTimeProp = pf.createPersistentProperty("Conveyor Reverse Time", 0.2);

        var reverseConveyor = new ParallelRaceGroup(
            conveyor.getReverseCommand(),
            new DelayViaSupplierCommand(() -> {
                if (conveyor.getHasRetracted()) {
                    log.warn("Conveyor already retracted, not retracting again!");
                    return 0.0;
                } else {
                    return conveyorReverseTimeProp.get();
                }
            })
        );
        var stopConveyor = new InstantCommand(() -> conveyor.stop(), conveyor);
        var markConveyorRetracted = new InstantCommand(() -> conveyor.setHasRetracted(true));

        addCommands(reverseConveyor, stopConveyor, markConveyorRetracted);
    }
    
}
