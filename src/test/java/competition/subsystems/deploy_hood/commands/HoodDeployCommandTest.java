package competition.subsystems.deploy_hood.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.deploy_hood.HoodSubsystem;

public class HoodDeployCommandTest extends BaseCompetitionTest{
    @Test
    public void testingHoodDeployCommand(){
        HoodSubsystem hoodSubsystem = getInjectorComponent().hoodSubsystem();
        HoodDeployCommand hoodDeployCommand = getInjectorComponent().hoodDeployCommand();

        hoodSubsystem.retract();
        assertTrue("The hood is retracted", hoodSubsystem.hoodDeploy.getIsReverse());
        hoodDeployCommand.initialize();
        hoodDeployCommand.execute();
        assertTrue("Hood is deployed", hoodSubsystem.hoodDeploy.getIsForward());
    }

    @Test
    public void testingDoubleHoodDeployCommand(){
        HoodSubsystem hoodSubsystem = getInjectorComponent().hoodSubsystem();
        HoodDeployCommand hoodDeployCommand = getInjectorComponent().hoodDeployCommand();

        hoodSubsystem.deploy();
        assertTrue("The hood is deployed", hoodSubsystem.hoodDeploy.getIsForward());
        hoodDeployCommand.initialize();
        hoodDeployCommand.execute();
        assertTrue("Hood stays deployed", hoodSubsystem.hoodDeploy.getIsForward());
    }
}
