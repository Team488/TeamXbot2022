package competition.subsystems.deploy_hood;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.XDoubleSolenoidFactory;
import xbot.common.controls.actuators.XSolenoid.XSolenoidFactory;

@Singleton
public class HoodSubsystem extends BaseSubsystem{
    public XDoubleSolenoid hoodDeploy;

    @Inject
    public HoodSubsystem(XDoubleSolenoidFactory doubleSolenoidFactory, XSolenoidFactory solenoidFactory, ElectricalContract eContract){
        if (eContract.arePneumaticsReady()) {
            hoodDeploy = doubleSolenoidFactory.create(
                solenoidFactory.create(eContract.getDeployHoodSoleniod1().channel),
                solenoidFactory.create(eContract.getDeployHoodSoleniod2().channel)
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
