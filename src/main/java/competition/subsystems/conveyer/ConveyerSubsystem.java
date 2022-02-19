package competition.subsystems.conveyer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class ConveyerSubsystem extends SimpleMotorSubsystem {
    public final XCANSparkMax motor;
    public final boolean isReady;

    @Inject
    public ConveyerSubsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("ConveyerSubsystem", pf);
        this.isReady = eContract.isConveyerReady();
        if(isReady) {
            motor = clf.createCANSparkMax(eContract.getConveyerMotor(), this.getPrefix(), "Conveyer Motor");
        } else {
            motor = null;
        }
    }

    @Override
    public void setPower(double power) {
        if(isReady) {
            motor.set(power);
        }
    }
    
}
