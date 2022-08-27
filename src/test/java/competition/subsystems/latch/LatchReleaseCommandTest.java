package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.latch.commands.LatchReleaseCommand;

public class LatchReleaseCommandTest extends BaseCompetitionTest{
    @Test
    public void testingReleaseLatch(){
        LatchReleaseCommand latchReleaseCommand = getInjectorComponent().latchReleaseCommand();
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        
        latchSubsystem.latch.setForward();
        assertTrue("Latch starts armed", latchSubsystem.latch.getIsForward());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch is now released", latchSubsystem.latch.getIsReverse());


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchReleaseCommand latchReleaseCommand = getInjectorComponent().latchReleaseCommand();
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        latchSubsystem.latch.setReverse();
        assertTrue("Latch starts released", latchSubsystem.latch.getIsReverse());
        latchReleaseCommand.initialize();
        latchReleaseCommand.execute();
        assertTrue("Latch stays released", latchSubsystem.latch.getIsReverse());

    }
}
