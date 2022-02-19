package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.latch.commands.LatchReleaseCommand;

public class LatchReleaseCommandTest extends BaseCompetitionTest{
    @Test
    public void testingReleaseLatch(){
        LatchReleaseCommand latchReleaseCommand = this.injector.getInstance(LatchReleaseCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        
        latchSubsystem.latch.setForward();
        assertTrue("Latch starts armed", latchSubsystem.latch.getIsForward());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch is now released", latchSubsystem.latch.getIsReverse());


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchReleaseCommand latchReleaseCommand = this.injector.getInstance(LatchReleaseCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        latchSubsystem.latch.setReverse();
        assertTrue("Latch starts released", latchSubsystem.latch.getIsReverse());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch stays released", latchSubsystem.latch.getIsReverse());

    }
}
