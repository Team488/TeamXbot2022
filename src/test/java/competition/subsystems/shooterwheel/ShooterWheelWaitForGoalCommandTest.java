package competition.subsystems.shooterwheel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shooterwheel.commands.ShooterWheelWaitForGoalCommand;

public class ShooterWheelWaitForGoalCommandTest extends BaseCompetitionTest {

    @Test
    @Ignore // need to write speed modification support for the Neos
    public void testAtGoal() {
        ShooterWheelSubsystem shooter = getInjectorComponent().shooterWheelSubsystem();

        ShooterWheelWaitForGoalCommand command = getInjectorComponent().shooterWheelWaitForGoalCommand();

        shooter.setTargetValue(9001);

        command.initialize();
        command.execute();

        assertFalse(command.isFinished());
        
        shooter.setMaintainerIsAtGoal(true);

        command.execute();

        assertTrue(command.isFinished());
    }

    @Test
    public void testTimeout() {
        ShooterWheelSubsystem shooter = getInjectorComponent().shooterWheelSubsystem();

        ShooterWheelWaitForGoalCommand command = getInjectorComponent().shooterWheelWaitForGoalCommand();

        shooter.setTargetRPM(9001);

        command.initialize();
        command.execute();

        assertFalse(command.isFinished());
 
        this.timer.advanceTimeInSecondsBy(10);

        assertTrue(command.isFinished());
    }

}