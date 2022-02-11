package competition.subsystems.climber_arm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class ClimberArmsubsystemTest extends BaseCompetitionTest {
    
    @Test
    public void testMotorArmExtend(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);

        climberArmSubsystem.motorStop(0);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm should be extending", 1, climberArmSubsystem.armMotor.get(), 0.0001);

    }

    @Test
    public void testArmRetract(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(-1);
        assertEquals("Arm should be retracting", -1, climberArmSubsystem.armMotor.get(), 0.0001);
    }

    @Test
    public void testArmStop(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm should stay still", 0, climberArmSubsystem.armMotor.get(), 0.0001);
    }

    @Test
    public void testExtrastStopRetractStop(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm should be extending", 1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm should be stopping", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(-1);
        assertEquals("Arm should be retracting", -1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm should be stopping", 0, climberArmSubsystem.armMotor.get(), 0.0001);

    }
    @Test
    public void testSafetyExtend(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start not moving", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm should be extending", 1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.armMotor.setPosition(20);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm should start not moving", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(-1);
        assertEquals("Arm should be retracting", -1, climberArmSubsystem.armMotor.get(), 0.0001);
    }

    @Test
    public void testSafetyRetract(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start not moving", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(-1);
        assertEquals("Arm should be retracting", -1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.armMotor.setPosition(-25);
        climberArmSubsystem.setPower(-1);
        assertEquals("Arm should start not moving", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm should be extending", 1, climberArmSubsystem.armMotor.get(), 0.0001);
    }

    @Test
    public void testSetCurrentPositionToZero() {
        ClimberArmSubsystem subsystem = this.injector.getInstance(ClimberArmSubsystem.class);

        assertEquals("Arm position defaults to zero", 0, subsystem.getPosition(), 0.001);
        assertFalse("Arm should start uncalibrated", subsystem.isCalibrated());
        subsystem.armMotor.setPosition(10);
        assertFalse("Arm should still be uncalibrated after motor position changes", subsystem.isCalibrated());
        assertEquals("Arm position should be the motor position", 10, subsystem.getPosition(), 0.001);
        subsystem.setCurrentPositionToZero();
        assertTrue("Arm should be calibrated after zeroing", subsystem.isCalibrated());
        assertEquals("Arm position should be zero again", 0, subsystem.getPosition(), 0.001);
        subsystem.armMotor.setPosition(10);
        assertTrue("Arm should still be calibrated after motor position changes", subsystem.isCalibrated());
        assertEquals("Arm position should be the motor position", 10, subsystem.getPosition(), 0.001);
    }
}
