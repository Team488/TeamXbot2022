package competition.subsystems.latch;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class LatchSubsystem extends BaseSubsystem {
    public XDoubleSolenoid latch;

    private int numberOfUnlatches = 0;

    @Inject
    public LatchSubsystem(CommonLibFactory factory, ElectricalContract contract) {
        
        if (contract.arePneumaticsReady()) {
            latch = factory.createDoubleSolenoid(
                factory.createSolenoid(contract.getLatchSolenoid().channel), 
                factory.createSolenoid(contract.getLatchSolenoid2().channel)
            );
        }
    }

    public void arm(){
        latch.setDoubleSolenoid(DoubleSolenoidMode.FORWARD);
    }
    public void release(){
        latch.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
        numberOfUnlatches++;
    }

    public int getUnlatchCount() {
        return numberOfUnlatches;
    }

}