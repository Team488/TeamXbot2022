package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.collector.CollectorSubsystem;

public class StopCommandTest extends BaseCollectorTest {

    @Test
    public void testingStopWhileIntaking() {
        StopCommand stopCommand = injector.getInstance(StopCommand.class);
        CollectorSubsystem collectorSubsystem = injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(1);
        checkCollectorPower(1);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    @Test
    public void stopWhenStopped() {
        StopCommand stopCommand = injector.getInstance(StopCommand.class);
        CollectorSubsystem collectorSubsystem = injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(0);
        checkCollectorPower(0);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        assertEquals(power, collectorSubsystem.collectorMotor.get(), 0.001);
    }
}
