package competition.subsystems.conveyer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class ConveyorSubsystem extends SimpleMotorSubsystem {
    public final XCANTalon motor;
    public final boolean isReady;

    @Inject
    public ConveyorSubsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("ConveyerSubsystem", pf);
        this.isReady = eContract.isConveyerReady();
        if(isReady) {
            motor = clf.createCANTalon(eContract.getConveyerMotor());
        } else {
            motor = null;
        }
    }

    @Override
    public void setPower(double power) {
        if(isReady) {
            motor.set(ControlMode.PercentOutput, power);
        }
    }
    
}
