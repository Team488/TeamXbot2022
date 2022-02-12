package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import com.google.inject.Key;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.injection.arm.LeftArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmStopCommandTest extends BaseCompetitionTest {
        
    ClimberArmSubsystem leftArm;
    MotorArmStopCommand command;

    @Override
    public void setUp() {
        super.setUp();

        leftArm = injector.getInstance(Key.get(ClimberArmSubsystem.class, LeftArm.class));
        command = injector.getInstance(Key.get(MotorArmStopCommand.class, LeftArm.class));
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
