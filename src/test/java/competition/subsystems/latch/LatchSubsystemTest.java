package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class LatchSubsystemTest extends BaseCompetitionTest {

    @Test
    public void testingArmLatch() {
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        assertTrue("Latch starts off", latchSubsystem.latch.getIsOff());
        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getIsForward());

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
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        latchSubsystem.latch.setForward();
        assertTrue("Latch starts armed", latchSubsystem.latch.getIsForward());
        latchSubsystem.release();
        assertTrue("Latch is now released", latchSubsystem.latch.getIsReverse());
    }

    @Test
    public void testingArmAndReleaseLatch() {
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getIsForward());
        latchSubsystem.release();
        assertTrue("Latch is disarmed", latchSubsystem.latch.getIsReverse());
    }
    
    @Test
    public void testingDoublesLatch() {
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        latchSubsystem.arm();
        assertTrue("Latch is now armed", latchSubsystem.latch.getIsForward());
        latchSubsystem.arm();
        assertTrue("Latch is disarmed", latchSubsystem.latch.getIsForward());
    }
}