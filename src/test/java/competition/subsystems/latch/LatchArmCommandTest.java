package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.latch.commands.LatchArmCommand;

public class LatchArmCommandTest extends BaseCompetitionTest{
    @Test
    public void testingReleaseLatch(){
        LatchArmCommand latchArmCommand = this.injector.getInstance(LatchArmCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        
        latchSubsystem.latch.setReverse();
        assertTrue("Latch starts released", latchSubsystem.latch.getIsReverse());
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch is now armed", latchSubsystem.latch.getIsForward());


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchArmCommand latchArmCommand = this.injector.getInstance(LatchArmCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        latchSubsystem.latch.setForward();
        assertTrue("Latch starts released", latchSubsystem.latch.getIsForward());
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch stays released", latchSubsystem.latch.getIsForward());

    }
}
