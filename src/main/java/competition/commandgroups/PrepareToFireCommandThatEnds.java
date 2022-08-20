package competition.commandgroups;

import java.util.function.Supplier;

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
import xbot.common.command.NamedInstantCommand;
import xbot.common.command.SimpleWaitForMaintainerCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

/**
 * Prepares the robot to launch cargo. Clears the barrel,
 * spins up the wheel to a target RPM, and waits for either the
 * wheel to reach the target speed or a few seconds, whichever comes first.
 * Requires:
 * - ShooterWheel.SetpointLock
 * - Conveyor.
 */
public class PrepareToFireCommandThatEnds extends SequentialCommandGroup {

    private final DoubleProperty conveyorReverseTimeProp;
    private final DoubleProperty waitTimeProp;
    private final ShooterWheelSubsystem wheel;
    private Supplier<Double> externalWaitSupplier;
    private Supplier<TargetRPM> externalTargetSupplier;

    private static Logger log = Logger.getLogger(PrepareToFireCommandThatEnds.class);

    @Inject
    PrepareToFireCommandThatEnds(ShooterWheelSubsystem wheel,
            ConveyorSubsystem conveyor, PropertyFactory pf) {
        pf.setPrefix(this.getName());
        conveyorReverseTimeProp = pf.createPersistentProperty("Conveyor Reverse Time", 0.2);
        waitTimeProp = pf.createPersistentProperty("Max Wait Time", 5);
        this.wheel = wheel;
     
        // We will need to reverse the conveyor for a litle bit before spinning up the shooter to make sure
        // nothing is touching the wheel
        var setHotDog = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        var reverseConveyor = new ParallelRaceGroup(
            setHotDog,
            conveyor.getReverseCommand(),
            new DelayViaSupplierCommand(() -> conveyor.getHasRetracted() ? 0 : conveyorReverseTimeProp.get())
        );

        var setTargetRPM = new NamedInstantCommand("ApplyShooterTargetRPM", () -> applyTargetRPM(), wheel.getSetpointLock());
        var waitForReadiness = new SimpleWaitForMaintainerCommand(wheel, getWaitTime());
        var stopConveyor = new InstantCommand(() -> conveyor.stop(), conveyor);
        var markConveyorRetracted = new InstantCommand(() -> conveyor.setHasRetracted(true));

        addCommands(reverseConveyor, stopConveyor, setTargetRPM, waitForReadiness, markConveyorRetracted);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        super.initialize();
    }

    public void setTargetRPM(TargetRPM target) {
        this.externalTargetSupplier = () -> target;
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
}