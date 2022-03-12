package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MotorArmSetZeroCommandTest extends BaseClimberArmTest {
    
    MotorArmSetZeroCommand command;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(MotorArmSetZeroCommand.class);
    }

    @Test
    public void testSetZero() {
        leftArm.armMotor.setPosition(10);

        assertTrue("Subsystem should be calibrated", leftArm.isCalibrated());
        assertEquals("Subsystem position should match motor position", 10, leftArm.getPosition(), 0.001);

        command.initialize();
        command.execute();

        assertTrue("Subsystem should be calidbrated", leftArm.isCalibrated());
        assertEquals("Subsystem position should reset to zero", 0, leftArm.getPosition(), 0.001);
    }

}
