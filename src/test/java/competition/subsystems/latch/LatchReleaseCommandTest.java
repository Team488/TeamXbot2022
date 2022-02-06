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

        
        latchSubsystem.latch.setOn(true);
        assertTrue("Latch starts armed", latchSubsystem.latch.getAdjusted());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch is now released", !latchSubsystem.latch.getAdjusted());


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchReleaseCommand latchReleaseCommand = this.injector.getInstance(LatchReleaseCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        latchSubsystem.latch.setOn(false);
        assertTrue("Latch starts released", !latchSubsystem.latch.getAdjusted());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch stays released", !latchSubsystem.latch.getAdjusted());

    }
}
