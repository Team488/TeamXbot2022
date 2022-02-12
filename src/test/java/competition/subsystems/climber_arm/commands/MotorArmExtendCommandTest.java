package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmExtendCommandTest extends BaseClimberArmTest {
    @Test
    public void testingArmExtend (){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm extended", 1, leftArm.armMotor.get(), 0.0001);
    }

    @Test
    public void testingExtendStop(){
        assertEquals("Arm should start stopped", 0, leftArm.armMotor.get(), 0.0001);
        leftArm.setPower(1);
        assertEquals("Arm extended", 1, leftArm.armMotor.get(), 0.0001);
        leftArm.stop();
        assertEquals("Arm stops", 0, leftArm.armMotor.get(), 0.0001);
    }
    
}
