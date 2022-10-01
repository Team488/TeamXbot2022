package competition.subsystems.climber_pivot;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDoubleSolenoid;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;
import xbot.common.controls.actuators.XDoubleSolenoid.XDoubleSolenoidFactory;
import xbot.common.controls.actuators.XSolenoid.XSolenoidFactory;

@Singleton
public class ClimberPivotSubsystem extends BaseSubsystem {
    public XDoubleSolenoid pivot;

    private boolean automaticPivotingEnabled = false;

    @Inject
    public ClimberPivotSubsystem(XDoubleSolenoidFactory doubleSolenoidFactory, XSolenoidFactory solenoidFactory, ElectricalContract contract) {
        if (contract.arePneumaticsReady()) {
            pivot = doubleSolenoidFactory.create(
                solenoidFactory.create(contract.getPivotSolenoid().channel),
                solenoidFactory.create(contract.getPivotSolenoid2().channel)
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