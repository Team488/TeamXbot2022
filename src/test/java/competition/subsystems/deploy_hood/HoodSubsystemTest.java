package competition.subsystems.deploy_hood;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class HoodSubsystemTest extends BaseCompetitionTest{
    @Test
    public void testingHoodSubsystem(){
        HoodSubsystem hoodSubsystem = this.injector.getInstance(HoodSubsystem.class);

        hoodSubsystem.retract();
        assertTrue("Starts retracted", hoodSubsystem.hoodDeploy.getIsReverse());
    }
    
}
