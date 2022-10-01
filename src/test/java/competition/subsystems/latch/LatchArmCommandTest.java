package competition.subsystems.latch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.latch.commands.LatchArmCommand;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;

public class LatchArmCommandTest extends BaseCompetitionTest{
    @Test
    public void testingReleaseLatch(){
        LatchArmCommand latchArmCommand = getInjectorComponent().latchArmCommand();
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();
        
        latchSubsystem.latch.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
        assertTrue("Latch starts released", latchSubsystem.latch.getDoubleSolenoidMode() == DoubleSolenoidMode.REVERSE);
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch is now armed", latchSubsystem.latch.getDoubleSolenoidMode() == DoubleSolenoidMode.FORWARD);


    }

    @Test
    public void testingStartReleaseLatch(){
        LatchArmCommand latchArmCommand = getInjectorComponent().latchArmCommand();
        LatchSubsystem latchSubsystem = getInjectorComponent().latchSubsystem();

        latchSubsystem.latch.setForward();
        assertTrue("Latch starts released", latchSubsystem.latch.getIsForward());
        latchArmCommand.initialize();
        latchArmCommand.execute();
        assertTrue("Latch stays released", latchSubsystem.latch.getIsForward());

    }
}
