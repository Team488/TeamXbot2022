package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MotorArmRetractCommandTest extends BaseClimberArmTest {
    @Test
    public void testArmRetract (){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm retracts", -1, leftArm.armMotor.get(), 0.0001);
    }
    @Test
    public void testArmStop (){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm retracts", -1, leftArm.armMotor.get(), 0.0001);
        leftArm.stop();
        assertEquals("Arm stops", 0, leftArm.armMotor.get(), 0.0001);
    }
}
