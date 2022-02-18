package competition.subsystems.climber_deployment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import competition.subsystems.collector_deployment.commands.DeployCommand;

public class DeployCommandTest extends BaseCompetitionTest{
    @Test  
    public void testingDeployCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        DeployCommand deployCommand = this.injector.getInstance(DeployCommand.class);

        collectorDeploymentSubsystem.deploy.setOn(false);
        assertFalse("Collector retracts", collectorDeploymentSubsystem.deploy.getAdjusted());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector deploys", collectorDeploymentSubsystem.deploy.getAdjusted());
    }

    @Test  
    public void testingDoubleDeployCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        DeployCommand deployCommand = this.injector.getInstance(DeployCommand.class);

        collectorDeploymentSubsystem.deploy.setOn(true);
        assertTrue("Collector Starts Deployed", collectorDeploymentSubsystem.deploy.getAdjusted());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector Stays Deployed", collectorDeploymentSubsystem.deploy.getAdjusted());
    }
}
