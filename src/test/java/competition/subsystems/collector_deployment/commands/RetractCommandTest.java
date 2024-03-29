package competition.subsystems.collector_deployment.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;

public class RetractCommandTest extends BaseCompetitionTest{
    @Test  
    public void testingRetractCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = getInjectorComponent().collectorDeploymentSubsystem();
        RetractCollectorCommand retractCommand = getInjectorComponent().retractCollectorCommand();

        collectorDeploymentSubsystem.deploy.setForward();
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getIsForward());
        retractCommand.initialize();
        retractCommand.execute();
        assertTrue("Collector retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
    }
    
    @Test  
    public void testingDoubleRetractCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = getInjectorComponent().collectorDeploymentSubsystem();
        RetractCollectorCommand retractCommand = getInjectorComponent().retractCollectorCommand();

        collectorDeploymentSubsystem.deploy.setReverse();
        assertTrue("Collector Retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        retractCommand.initialize();
        retractCommand.execute();
        assertTrue("Collector Stays Retracted", collectorDeploymentSubsystem.deploy.getIsReverse());
    }
    
}

