package competition.commandgroups;

import javax.inject.Inject;

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

/**
 * "Clears the barrel" by making sure a cargo is not pressed up against the shooter wheel.
 * The robot accomplishes by briefly running the conveyor in reverse while additionally running
 * the shooter wheel in "HotDog" mode.
 * Will end after a few moments.
 * Requires:
 * - Conveyor.
 * - ShooterWheel.SetpointLock.
 */
public class ClearBarrelCommandThatEnds extends SequentialCommandGroup {

    private final DoubleProperty conveyorReverseTimeProp;

    private static Logger log = Logger.getLogger(PrepareToFireCommandThatEnds.class);

    @Inject
    ClearBarrelCommandThatEnds(ShooterWheelSubsystem wheel,
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