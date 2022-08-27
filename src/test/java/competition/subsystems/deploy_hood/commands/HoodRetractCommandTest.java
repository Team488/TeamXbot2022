package competition.subsystems.deploy_hood.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.deploy_hood.HoodSubsystem;

public class HoodRetractCommandTest extends BaseCompetitionTest{
    @Test
    public void testingHoodRetractCommand(){
        HoodSubsystem hoodSubsystem = getInjectorComponent().hoodSubsystem();
        HoodRetractCommand hoodRetractCommand = getInjectorComponent().hoodRetractCommand();

        hoodSubsystem.deploy();
        assertTrue("Starts deployed", hoodSubsystem.hoodDeploy.getIsForward());
        hoodRetractCommand.initialize();
        hoodRetractCommand.execute();
        assertTrue("Retracts", hoodSubsystem.hoodDeploy.getIsReverse());

    }

    @Test
    public void testingDoubleHoodRetractCommand(){
        HoodSubsystem hoodSubsystem = getInjectorComponent().hoodSubsystem();
        HoodRetractCommand hoodRetractCommand = getInjectorComponent().hoodRetractCommand();

        hoodSubsystem.retract();
        assertTrue("Starts deployed", hoodSubsystem.hoodDeploy.getIsReverse());
        hoodRetractCommand.initialize();
        hoodRetractCommand.execute();
        assertTrue("Retracts", hoodSubsystem.hoodDeploy.getIsReverse());

    }
}
