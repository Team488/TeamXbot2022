package competition.commandgroups;

import java.util.function.Supplier;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.SimpleWaitForMaintainerCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class PrepareToFireCommand extends SequentialCommandGroup {

    private final DoubleProperty waitTimeProp;
    Supplier<Double> externalWaitSupplier;

    private static Logger log = Logger.getLogger(PrepareToFireCommand.class);

    @Inject
    PrepareToFireCommand(ShooterWheelSubsystem wheel, PropertyFactory pf) {
        pf.setPrefix(this.getName());
        waitTimeProp = pf.createPersistentProperty("Max Wait Time", 5);

        // We will need to reverse the conveyor for a litle bit before spinning up the shooter.

        var waitForReadiness = new ParallelCommandGroup(
            new SimpleWaitForMaintainerCommand(wheel, getWaitTime())
        );

        addCommands(waitForReadiness);
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

    @Override
    public void initialize() {
        super.initialize();
        log.info("Initializing");
    }
}