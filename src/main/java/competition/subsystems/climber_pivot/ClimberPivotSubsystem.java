package competition.subsystems.climber_pivot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class ClimberPivotSubsystem extends BaseSubsystem {
    public XDoubleSolenoid pivot;

    @Inject
    public ClimberPivotSubsystem(CommonLibFactory factory, ElectricalContract contract) {
        if (contract.arePneumaticsReady()) {
            pivot = factory.createDoubleSolenoid(
                factory.createSolenoid(contract.getPivotSolenoid().channel),
                factory.createSolenoid(contract.getPivotSolenoid2().channel)
            );
        }
    }

    public void pivotIn() {
        pivot.setReverse();
    }

    public void pivotOut() {
        pivot.setForward();
    }

    
}