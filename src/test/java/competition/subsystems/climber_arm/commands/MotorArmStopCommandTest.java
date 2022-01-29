package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmStopCommandTest extends BaseCompetitionTest{
    @Test
    public void testArmStop(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        climberArmSubsystem.setPower(5);
        assertEquals("Arm should start moving", 5, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm stops", 0, climberArmSubsystem.armMotor.get(), 0.0001);
    }
    @Test
    public void testDoubleArmStop(){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);
        assertEquals("Arm should start stopped", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm stops", 0, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm stops", 0, climberArmSubsystem.armMotor.get(), 0.0001);
    }
}
