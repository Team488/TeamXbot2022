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
public class ConveyerSubsystem extends SimpleMotorSubsystem {
    public final XCANTalon motor1;
    public final XCANTalon motor2;
    public final boolean isReady;

    @Inject
    public ConveyerSubsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("ConveyerSubsystem", pf);
        this.isReady = eContract.isConveyerReady();
        if(isReady) {
            motor1 = clf.createCANTalon(eContract.getConveyerMotor1());
            motor2 = clf.createCANTalon(eContract.getConveyerMotor2());
        } else {
            motor1 = null;
            motor2 = null;
        }
    }

    @Override
    public void setPower(double power) {
        if(isReady) {
            motor1.set(ControlMode.PercentOutput, power);
            motor2.set(ControlMode.PercentOutput, power);
        }
    }
    
}
