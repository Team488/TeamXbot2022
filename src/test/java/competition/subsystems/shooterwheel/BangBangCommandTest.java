package competition.subsystems.shooterwheel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shooterwheel.commands.BangBangCommand;

public class BangBangCommandTest extends BaseCompetitionTest {

    ShooterWheelSubsystem shooterWheelSubsystem;
    BangBangCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        shooterWheelSubsystem = this.injector.getInstance(ShooterWheelSubsystem.class);
        command = this.injector.getInstance(BangBangCommand.class);    
    }
    
    @Test
    public void testFullPower(){
        shooterWheelSubsystem.setTargetRPM(400);
        command.initialize();
        command.execute();

        assertEquals(0.5, shooterWheelSubsystem.getPower(), 0.001);
    }
    @Test
    public void testZeroPower(){
        shooterWheelSubsystem.setTargetRPM(400);
        command.initialize();
        command.execute();
        assertEquals(0.5, shooterWheelSubsystem.getPower(),0.001);
        shooterWheelSubsystem.setTargetRPM(0);
        command.execute();
        assertEquals(0, shooterWheelSubsystem.getPower(), 0.001);
    }
}