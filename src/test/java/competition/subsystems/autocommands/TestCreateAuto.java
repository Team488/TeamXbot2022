package competition.subsystems.autocommands;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj2.command.Command;

public class TestCreateAuto extends BaseCompetitionTest {
    
    @Override
    public void setUp() {
        super.setUp();

        shakedown(getInjectorComponent().collectThenScoreTwiceCommand());
        shakedown(getInjectorComponent().doNothingCommand());
        shakedown(getInjectorComponent().driveForwardOutOfTarmac());
        shakedown(getInjectorComponent().goCollectComebackCommand());
        shakedown(getInjectorComponent().moonshotCommand());
        shakedown(getInjectorComponent().scsFromOneRobotAwayCommand());
        shakedown(getInjectorComponent().shootCollectShootCommand());
        shakedown(getInjectorComponent().shootRecklesslyThenEscapeCommand());
        shakedown(getInjectorComponent().shootThenEscapeCommand());
        shakedown(getInjectorComponent().collectThenHighScoreCommand());
    }

    @Test
    public void testCreation() {
        // Do nothing.
    }

    private void shakedown(Command c) {
        c.initialize();
        c.execute();
    }
}
