package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmExtendCommandTest extends BaseCompetitionTest{
    @Test
    public void testingArmExtend (){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class); 
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm extended", 1, climberArmSubsystem.armMotor.get(), 0.0001);
    }

    @Test
    public void testingExtendStop(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.setPower(1);
        assertEquals("Arm extended", 1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm stops", 0, climberArmSubsystem.armMotor.get(), 0.0001);
    }
    
}
