package competition.commandgroups;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class ClearBarrelCommand extends SequentialCommandGroup {

    private final DoubleProperty conveyorReverseTimeProp;

    private static Logger log = Logger.getLogger(PrepareToFireCommand.class);

    @Inject
    ClearBarrelCommand(ShooterWheelSubsystem wheel,
            ConveyorSubsystem conveyor, PropertyFactory pf) {
        pf.setPrefix(this.getName());
        conveyorReverseTimeProp = pf.createPersistentProperty("Conveyor Reverse Time", 0.2);
     
        // We will need to reverse the conveyor for a litle bit before spinning up the shooter to make sure
        // nothing is touching the wheel
        var setHotDog = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        var reverseConveyor = new ParallelRaceGroup(
            setHotDog,
            conveyor.getReverseCommand(),
            new DelayViaSupplierCommand(() -> conveyorReverseTimeProp.get())
        );

        var stopConveyor = new InstantCommand(() -> conveyor.stop(), conveyor);

        addCommands(reverseConveyor, stopConveyor);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        super.initialize();
    }
}