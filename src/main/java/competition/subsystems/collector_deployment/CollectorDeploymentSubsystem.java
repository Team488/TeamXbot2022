package competition.subsystems.collector_deployment;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;
import xbot.common.controls.actuators.XDoubleSolenoid.XDoubleSolenoidFactory;
import xbot.common.controls.actuators.XSolenoid.XSolenoidFactory;

@Singleton
public class CollectorDeploymentSubsystem extends BaseSubsystem{
    public XDoubleSolenoid deploy;

    @Inject
    public CollectorDeploymentSubsystem(XDoubleSolenoidFactory doubleSolenoidFactory, XSolenoidFactory solenoidFactory, ElectricalContract eContract){
        if (eContract.arePneumaticsReady()) {
        deploy = doubleSolenoidFactory.create(
            solenoidFactory.create(eContract.getCollectorDeviceSoleniod1().channel),
            solenoidFactory.create(eContract.getCollectorDeviceSoleniod2().channel)
        );
    }
    }

    public void deploy (){
        deploy.setDoubleSolenoid(DoubleSolenoidMode.FORWARD);
    }

    public void retract (){
        deploy.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
    }
    
}
