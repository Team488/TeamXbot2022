package competition.subsystems.collector_deployment;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class CollectorDeploymentSubsystemTest extends BaseCompetitionTest{
    
    @Test
    public void testingDeploymentSubsystem (){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = getInjectorComponent().collectorDeploymentSubsystem();

        collectorDeploymentSubsystem.deploy.setForward();;
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getIsForward());
        collectorDeploymentSubsystem.deploy.setReverse();;
        assertTrue("Collector Retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        
    }
}
