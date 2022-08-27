package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmStopCommandTest extends BaseCompetitionTest {
        
    ClimberArmSubsystem leftArm;
    MotorArmStopCommand command;

    @Override
    public void setUp() {
        super.setUp();

        leftArm = getInjectorComponent().armComponents().leftArm.subsystem();
        command = getInjectorComponent().armComponents().leftArm.stopCommand();
    }

    @Test
    public void testArmStop(){
        leftArm.setPower(0.5);
        assertEquals("Arm should start moving", 0.5, leftArm.armMotor.get(), 0.0001);
        
        command.initialize();
        command.execute();

        assertEquals("Arm stops", 0, leftArm.armMotor.get(), 0.0001);
    }
   
}
