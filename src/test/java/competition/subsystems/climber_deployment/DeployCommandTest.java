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

        collectorDeploymentSubsystem.deploy.setReverse();;
        assertTrue("Collector retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector deploys", collectorDeploymentSubsystem.deploy.getIsForward());
    }

    @Test  
    public void testingDoubleDeployCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        DeployCommand deployCommand = this.injector.getInstance(DeployCommand.class);

        collectorDeploymentSubsystem.deploy.setForward();
        assertTrue("Collector Starts Deployed", collectorDeploymentSubsystem.deploy.getIsForward());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector Stays Deployed", collectorDeploymentSubsystem.deploy.getIsForward());
    }
}
