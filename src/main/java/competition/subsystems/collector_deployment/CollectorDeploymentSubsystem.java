package competition.subsystems.collector_deployment;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class CollectorDeploymentSubsystem extends BaseSubsystem{
    public XSolenoid deploy;

    @Inject
    public CollectorDeploymentSubsystem(CommonLibFactory factory, ElectricalContract eContract){
        if (eContract.arePneumaticsReady()) {
        deploy = factory.createSolenoid(eContract.getDeploymentSoleniod().channel);
        }
    }

    public void deploy (){
        deploy.setOn(true);
    }

    public void retract (){
        deploy.setOn(false);
    }
    
}
