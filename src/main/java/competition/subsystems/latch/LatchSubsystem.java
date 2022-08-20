package competition.subsystems.latch;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;
import xbot.common.controls.actuators.XDoubleSolenoid.XDoubleSolenoidFactory;
import xbot.common.controls.actuators.XSolenoid.XSolenoidFactory;

@Singleton
public class LatchSubsystem extends BaseSubsystem {
    public XDoubleSolenoid latch;
    private int numberOfUnlatches = 0;
    final ElectricalContract contract;

    @Inject
    public LatchSubsystem(XDoubleSolenoidFactory doubleSolenoidFactory, XSolenoidFactory solenoidFactory, ElectricalContract contract) {
        this.contract = contract;

        if (contract.arePneumaticsReady()) {
            latch = doubleSolenoidFactory.create(
                solenoidFactory.create(contract.getLatchSolenoid().channel), 
                solenoidFactory.create(contract.getLatchSolenoid2().channel)
            );
        }
    }

    public void arm(){
        if (contract.arePneumaticsReady()) {
            latch.setDoubleSolenoid(DoubleSolenoidMode.FORWARD);
        }
    }
    public void release(){
        if (contract.arePneumaticsReady()) {
            latch.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
        }
        
        numberOfUnlatches++;
    }

    public int getUnlatchCount() {
        return numberOfUnlatches;
    }

}