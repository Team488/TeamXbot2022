package competition.subsystems.climber_arm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climber_arm.commands.BaseClimberArmTest;

public class ClimberArmsubsystemTest extends BaseClimberArmTest {
    
    @Test
    public void testMotorArmExtend(){

        leftArm.stop();
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm should be extending", 1, leftArm.armMotor.get(), 0.0001);

    }

    @Test
    public void testArmRetract(){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm should be retracting", -1, leftArm.armMotor.get(), 0.0001);
    }

    @Test
    public void testArmStop(){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.stop();
        assertEquals("Arm should stay still", 0, leftArm.armMotor.get(), 0.0001);
    }

    @Test
    public void testExtrastStopRetractStop(){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm should be extending", 1, leftArm.armMotor.get(), 0.0001);
        leftArm.stop();
        assertEquals("Arm should be stopping", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm should be retracting", -1, leftArm.armMotor.get(), 0.0001);
        leftArm.stop();
        assertEquals("Arm should be stopping", 0, leftArm.armMotor.get(), 0.0001);

    }
    @Test
    public void testSafetyExtend(){
        assertEquals("Arm should start not moving", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm should be extending", 1, leftArm.armMotor.get(), 0.0001);
        leftArm.armMotor.setPosition(20);
        leftArm.setPower(1);
        assertEquals("Arm should start not moving", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm should be retracting", -1, leftArm.armMotor.get(), 0.0001);
    }

    @Test
    public void testSafetyRetract(){
        assertEquals("Arm should start not moving", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(-1);
        assertEquals("Arm should be retracting", -1, leftArm.armMotor.get(), 0.0001);
        leftArm.armMotor.setPosition(-25);
        leftArm.setPower(-1);
        assertEquals("Arm should start not moving", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm should be extending", 1, leftArm.armMotor.get(), 0.0001);
    }

    @Test
    public void testSetCurrentPositionToZero() {
        assertEquals("Arm position defaults to zero", 0, leftArm.getPosition(), 0.001);
        assertTrue("Arm should start calibrated", leftArm.isCalibrated());
        leftArm.armMotor.setPosition(10);
        assertTrue("Arm should still be calibrated after motor position changes", leftArm.isCalibrated());
        assertEquals("Arm position should be the motor position", 10, leftArm.getPosition(), 0.001);
        leftArm.setCurrentPositionToZero();
        assertTrue("Arm should be calibrated after zeroing", leftArm.isCalibrated());
        assertEquals("Arm position should be zero again", 0, leftArm.getPosition(), 0.001);
        leftArm.armMotor.setPosition(10);
        assertTrue("Arm should still be calibrated after motor position changes", leftArm.isCalibrated());
        assertEquals("Arm position should be the motor position", 10, leftArm.getPosition(), 0.001);
    }
}
