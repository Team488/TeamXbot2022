package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmSetZeroCommandTest extends BaseCompetitionTest {
    
    MotorArmSetZeroCommand command;
    ClimberArmSubsystem subsystem;

    @Override
    public void setUp() {
        super.setUp();
        subsystem = injector.getInstance(ClimberArmSubsystem.class);
        command = injector.getInstance(MotorArmSetZeroCommand.class);
    }

    @Test
    public void testSetZero() {
        subsystem.armMotor.setPosition(10);

        assertFalse("Subsystem should be uncalibrated", subsystem.isCalibrated());
        assertEquals("Subsystem position should match motor position", 10, subsystem.getPosition(), 0.001);

        command.initialize();
        command.execute();

        assertTrue("Subsystem should be calidbrated", subsystem.isCalibrated());
        assertEquals("Subsystem position should reset to zero", 0, subsystem.getPosition(), 0.001);
    }

}
