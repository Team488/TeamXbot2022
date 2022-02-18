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

        collectorDeploymentSubsystem.deploy.setOn(true);
        assertTrue("Collector Deploys", collectorDeploymentSubsystem.deploy.getAdjusted());
        retractCommand.initialize();
        retractCommand.execute();
        assertFalse("Collector retracts", collectorDeploymentSubsystem.deploy.getAdjusted());
    }
    @Test  
    public void testingDoubleRetractCommand(){
        CollectorDeploymentSubsystem collectorDeploymentSubsystem = this.injector.getInstance(CollectorDeploymentSubsystem.class);
        RetractCommand retractCommand = this.injector.getInstance(RetractCommand.class);

        collectorDeploymentSubsystem.deploy.setOn(false);
        assertFalse("Collector Retracts", collectorDeploymentSubsystem.deploy.getAdjusted());
        retractCommand.initialize();
        retractCommand.execute();
        assertFalse("Collector Stays Retracted", collectorDeploymentSubsystem.deploy.getAdjusted());
    }
    
}

