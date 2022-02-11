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

        
        latchSubsystem.latch.setOn(false);
        assertTrue("Latch starts released", !latchSubsystem.latch.getAdjusted());
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch is now armed", latchSubsystem.latch.getAdjusted());


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchArmCommand latchArmCommand = this.injector.getInstance(LatchArmCommand.class);
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        latchSubsystem.latch.setOn(true);
        assertTrue("Latch starts released", latchSubsystem.latch.getAdjusted());
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch stays released", latchSubsystem.latch.getAdjusted());

    }
}
