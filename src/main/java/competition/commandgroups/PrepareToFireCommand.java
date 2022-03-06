package competition.commandgroups;

import java.util.function.Supplier;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.command.NamedInstantCommand;
import xbot.common.command.SimpleWaitForMaintainerCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class PrepareToFireCommand extends SequentialCommandGroup {

    private final DoubleProperty conveyorReverseTimeProp;
    private final DoubleProperty waitTimeProp;
    private final ShooterWheelSubsystem wheel;
    private final ConveyorSubsystem conveyor;
    private Supplier<Double> externalWaitSupplier;
    private Supplier<Double> externalTargetSupplier;

    private static Logger log = Logger.getLogger(PrepareToFireCommand.class);

    @Inject
    PrepareToFireCommand(ShooterWheelSubsystem wheel, ConveyorSubsystem conveyor, PropertyFactory pf) {
        pf.setPrefix(this.getName());
        conveyorReverseTimeProp = pf.createPersistentProperty("Conveyor Reverse Time", 0.2);
        waitTimeProp = pf.createPersistentProperty("Max Wait Time", 5);
        this.wheel = wheel;
        this.conveyor = conveyor;

        // We don't know which commands to run in sequence until initialize
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        prepareCommands();
        super.initialize();
    }

    public void setTargetRPM(Supplier<Double> externalTargetSupplier) {
        this.externalTargetSupplier = externalTargetSupplier;
    }

    public void applyTargetRPM() {
        if (externalTargetSupplier == null) {
            wheel.setTargetRPM(TargetRPM.Safe);
        } else {
            wheel.setTargetRPM(externalTargetSupplier.get());
        }
    }

    public void setWaitTime(Supplier<Double> externalWaitSupplier) {
        this.externalWaitSupplier = externalWaitSupplier;
    }

    private Supplier<Double> getWaitTime() {
        if (externalWaitSupplier == null) {
            return () -> waitTimeProp.get();
        }
        return externalWaitSupplier;
    }

    private void prepareCommands() {
        // We will need to reverse the conveyor for a litle bit before spinning up the shooter to make sure
        // nothing is touching the wheel
        if (wheel.getTargetRPM() == 0) {
            var reverseConveyor = new ParallelRaceGroup(
                conveyor.getReverseCommand(),
                new DelayViaSupplierCommand(() -> conveyorReverseTimeProp.get())
            );
            addCommands(reverseConveyor);
        }

        var setTargetRPM = new NamedInstantCommand("ApplyShooterTargetRPM", () -> applyTargetRPM(), wheel.getSetpointLock());
        var waitForReadiness = new SimpleWaitForMaintainerCommand(wheel, getWaitTime());

        addCommands(setTargetRPM, waitForReadiness);
    }
}