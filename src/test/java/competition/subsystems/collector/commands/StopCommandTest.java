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
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        stopCommand.initialize();
        stopCommand.execute();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void stopWhenStopped() {
        StopCommand stopCommand = injector.getInstance(StopCommand.class);
        CollectorSubsystem collectorSubsystem = injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(0);
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        stopCommand.initialize();
        stopCommand.execute();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
    }
}
