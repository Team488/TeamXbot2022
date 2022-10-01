package competition.subsystems.collector_deployment.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;

public class DeployCommandTest extends BaseCompetitionTest{
    @Test  
    public void testingDeployCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = getInjectorComponent().collectorDeploymentSubsystem();
        DeployCollectorCommand deployCommand = getInjectorComponent().deployCollectorCommand();

        collectorDeploymentSubsystem.deploy.setReverse();;
        assertTrue("Collector retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector deploys", collectorDeploymentSubsystem.deploy.getIsForward());
    }

    @Test  
    public void testingDoubleDeployCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = getInjectorComponent().collectorDeploymentSubsystem();
        DeployCollectorCommand deployCommand = getInjectorComponent().deployCollectorCommand();

        collectorDeploymentSubsystem.deploy.setForward();
        assertTrue("Collector Starts Deployed", collectorDeploymentSubsystem.deploy.getIsForward());
        deployCommand.initialize();
        deployCommand.execute();
        assertTrue("Collector Stays Deployed", collectorDeploymentSubsystem.deploy.getIsForward());
    }
}
