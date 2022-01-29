package competition.subsystems.climber_arm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj2.command.Subsystem;

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
    }
}
