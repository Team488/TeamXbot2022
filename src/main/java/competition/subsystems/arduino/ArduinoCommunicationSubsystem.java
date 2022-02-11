package competition.subsystems.arduino;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDigitalOutput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class ArduinoCommunicationSubsystem extends BaseSubsystem {
    
    XDigitalOutput dio1;
    XDigitalOutput dio2;

    private final BooleanProperty dio1State;
    private final BooleanProperty dio2State;
    
    @Inject
    public ArduinoCommunicationSubsystem(CommonLibFactory clf, ElectricalContract contract, PropertyFactory pf) {
        dio1 = clf.createDigitalOutput(contract.getArduinoDio1().channel);
        dio2 = clf.createDigitalOutput(contract.getArduinoDio2().channel);
        this.register();

        pf.setPrefix(this);
        dio1State = pf.createEphemeralProperty("dio1State", false);
        dio2State = pf.createEphemeralProperty("dio2State", false);
    }

    @Override
    public void periodic() {
        boolean dsEnabled = DriverStation.isEnabled();
        boolean isRedAlliance = DriverStation.getAlliance() == Alliance.Red;

        dio1State.set(dsEnabled);
        dio2State.set(isRedAlliance);

        dio1.set(dsEnabled);
        dio2.set(isRedAlliance);
    }
}
