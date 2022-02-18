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

        collectorDeploymentSubsystem.deploy.setOn(true);
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getAdjusted());
        collectorDeploymentSubsystem.deploy.setOn(false);
        assertFalse("Collector Retracts", collectorDeploymentSubsystem.deploy.getAdjusted());
        
    }
}
