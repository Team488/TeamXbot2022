package competition.subsystems.deploy_hood;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class HoodSubsystem extends BaseSubsystem{
    public XDoubleSolenoid hoodDeploy;

    @Inject
    public HoodSubsystem(CommonLibFactory factory, ElectricalContract eContract){
        if (eContract.arePneumaticsReady()) {
            hoodDeploy = factory.createDoubleSolenoid(
                factory.createSolenoid(eContract.getDeployHoodSoleniod1().channel),
                factory.createSolenoid(eContract.getDeployHoodSoleniod2().channel)
            );
        }
    }

    public void deploy(){
        hoodDeploy.setForward();
    }

    public void retract(){
        hoodDeploy.setReverse();
    }
    
}
