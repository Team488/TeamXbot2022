package competition.auto_programs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;

public class CollectThenScoreHighCommandTests extends BaseCompetitionTest {

    CollectThenHighScoreCommand command;
    CollectorSubsystem collector;
    ConveyorSubsystem conveyor;
    ShooterWheelSubsystem shooter;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(CollectThenHighScoreCommand.class);
        collector = injector.getInstance(CollectorSubsystem.class);
        conveyor = injector.getInstance(ConveyorSubsystem.class);
        shooter = injector.getInstance(ShooterWheelSubsystem.class);
    }
    
    @Test
    public void testExecution() {
        command.initialize();

        // Collector starts
        command.execute();
        assertEquals(collector.intakePower.get(), collector.collectorMotor.getMotorOutputPercent(), 0.001);

        // Wait for collector to stop
        while (collector.collectorMotor.getMotorOutputPercent() != 0) {
            command.execute();
            timer.advanceTimeInSecondsBy(0.02);
        }

        assertFalse(conveyor.getHasRetracted());

        // Wait for shooter wheel to have target
        while (shooter.getTargetRPM() <= 0) {
            command.execute();
            timer.advanceTimeInSecondsBy(0.02);
        }

        assertTrue(shooter.getTargetRPM() > 1000);
        assertTrue(conveyor.getHasRetracted());
        assertEquals(0, conveyor.motor.getMotorOutputPercent(), 0.01);

        while (!command.isFinished()) {
            command.execute();
            timer.advanceTimeInSecondsBy(0.02);
        }

        command.end(false);
    }
}
