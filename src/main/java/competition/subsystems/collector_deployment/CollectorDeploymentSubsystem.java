package competition.subsystems.collector_deployment;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class CollectorDeploymentSubsystem extends BaseSubsystem{
    public XDoubleSolenoid deploy;

    @Inject
    public CollectorDeploymentSubsystem(CommonLibFactory factory, ElectricalContract eContract){
        if (eContract.arePneumaticsReady()) {
        deploy = factory.createDoubleSolenoid(
            factory.createSolenoid(eContract.getCollectorDeviceSoleniod1().channel),
            factory.createSolenoid(eContract.getCollectorDeviceSoleniod2().channel)
        );
    }
    }

    public void deploy (){
        deploy.setForward();
    }

    public void retract (){
        deploy.setReverse();
    }
    
}
