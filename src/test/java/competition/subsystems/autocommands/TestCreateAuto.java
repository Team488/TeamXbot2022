package competition.subsystems.autocommands;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.auto_programs.CollectThenScoreTwiceCommand;
import competition.auto_programs.DoNothingCommand;
import competition.auto_programs.DriveForwardOutOfTarmac;
import competition.auto_programs.GoCollectComebackCommand;
import competition.auto_programs.MoonshotCommand;
import competition.auto_programs.SCSFromOneRobotAwayCommand;
import competition.auto_programs.ShootCollectShootCommand;
import competition.auto_programs.ShootRecklesslyThenEscapeCommand;
import competition.auto_programs.ShootThenEscapeCommand;
import edu.wpi.first.wpilibj2.command.Command;

public class TestCreateAuto extends BaseCompetitionTest {
    
    @Override
    public void setUp() {
        super.setUp();

        shakedown(injector.getInstance(CollectThenScoreTwiceCommand.class));
        shakedown(injector.getInstance(DoNothingCommand.class));
        shakedown(injector.getInstance(DriveForwardOutOfTarmac.class));
        shakedown(injector.getInstance(GoCollectComebackCommand.class));
        shakedown(injector.getInstance(MoonshotCommand.class));
        shakedown(injector.getInstance(SCSFromOneRobotAwayCommand.class));
        shakedown(injector.getInstance(ShootCollectShootCommand.class));
        shakedown(injector.getInstance(ShootRecklesslyThenEscapeCommand.class));
        shakedown(injector.getInstance(ShootThenEscapeCommand.class));
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
