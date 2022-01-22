package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class LatchSubsystemTest extends BaseCompetitionTest {

    @Test
    public void testingArmLatch() {
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        assertTrue("Latch starts disarmed", !latchSubsystem.latch.getAdjusted());
        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getAdjusted());

        // Prepare
        // Get all the objects and classes we need
        // Confirm that they are in the state we want them in

        // Act
        // Take an action

        // Verify
        // Confirm the results of the action
    }

    @Test
    public void testingReleaseLatch() {
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        latchSubsystem.latch.setOn(true);
        assertTrue("Latch starts armed", latchSubsystem.latch.getAdjusted());
        latchSubsystem.release();
        assertTrue("Latch is now released", !latchSubsystem.latch.getAdjusted());
    }

    @Test
    public void testingArmAndReleaseLatch() {
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        assertTrue("Latch starts disarmed", !latchSubsystem.latch.getAdjusted());
        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getAdjusted());
        latchSubsystem.release();
        assertTrue("Latch is disarmed", !latchSubsystem.latch.getAdjusted());
    }
    
    @Test
    public void testingDoublesLatch() {
        LatchSubsystem latchSubsystem = this.injector.getInstance(LatchSubsystem.class);

        assertTrue("Latch starts disarmed", !latchSubsystem.latch.getAdjusted());
        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getAdjusted());
        latchSubsystem.arm();
        assertTrue("Latch is disarmed", latchSubsystem.latch.getAdjusted());
    }
}