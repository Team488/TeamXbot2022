package competition.subsystems.climber_pivot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class ClimberPivotSubsystem extends BaseSubsystem {
    public XDoubleSolenoid pivot;

    private boolean automaticPivotingEnabled = false;

    @Inject
    public ClimberPivotSubsystem(CommonLibFactory factory, ElectricalContract contract) {
        if (contract.arePneumaticsReady()) {
            pivot = factory.createDoubleSolenoid(
                factory.createSolenoid(contract.getPivotSolenoid().channel),
                factory.createSolenoid(contract.getPivotSolenoid2().channel)
            );

            // Eventually need a better solution for inverting double solenoids
            // via the contract.
            pivot.setInverted(contract.getPivotSolenoid().inverted);
        }
    }

    public void pivotIn() {
        pivot.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
    }

    public void pivotOut() {
        pivot.setDoubleSolenoid(DoubleSolenoidMode.FORWARD);
    }

    public boolean isAllowedtoAutomaticallyPivot() {
        return automaticPivotingEnabled;
    }

    public void setAutomaticPivotingEnabled(boolean enabled) {
        automaticPivotingEnabled = enabled;
    }

    
}