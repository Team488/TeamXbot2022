package competition.subsystems.climber_deployment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import competition.subsystems.collector_deployment.commands.RetractCommand;

public class RetractCommandTest extends BaseCompetitionTest{
    @Test  
    public void testingRetractCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        RetractCommand retractCommand = this.injector.getInstance(RetractCommand.class);

        collectorDeploymentSubsystem.deploy.setForward();
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getIsForward());
        retractCommand.initialize();
        retractCommand.execute();
        assertTrue("Collector retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
    }
    
    @Test  
    public void testingDoubleRetractCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        RetractCommand retractCommand = this.injector.getInstance(RetractCommand.class);

        collectorDeploymentSubsystem.deploy.setReverse();
        assertTrue("Collector Retracts", collectorDeploymentSubsystem.deploy.getIsReverse());
        retractCommand.initialize();
        retractCommand.execute();
        assertTrue("Collector Stays Retracted", collectorDeploymentSubsystem.deploy.getIsReverse());
    }
    
}

