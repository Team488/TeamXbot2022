package competition.subsystems.climber_deployment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;

public class CollectorDeploymentSubsystemTest extends BaseCompetitionTest{
    
    @Test
    public void testingDeploymentSubsystem (){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);

        collectorDeploymentSubsystem.deploy.setForward();;
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getIsForward());
        collectorDeploymentSubsystem.deploy.setReverse();;
        assertTrue("Collector Retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        
    }
}
