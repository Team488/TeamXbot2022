package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmRetractCommandTest extends BaseCompetitionTest{
    @Test
    public void testArmRetract (){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);

        climberArmSubsystem.motorRetract(-1);
        assertEquals("Arm retracts", -1, climberArmSubsystem.armMotor.get(), 0.0001);
    }
    @Test
    public void testArmStop (){
        ClimberArmSubsystem climberArmSubsystem = this.injector.getInstance(ClimberArmSubsystem.class);

        climberArmSubsystem.motorRetract(-1);
        assertEquals("Arm retracts", -1, climberArmSubsystem.armMotor.get(), 0.0001);
        climberArmSubsystem.motorStop(0);
        assertEquals("Arm stops", 0, climberArmSubsystem.armMotor.get(), 0.0001);
    }
}
