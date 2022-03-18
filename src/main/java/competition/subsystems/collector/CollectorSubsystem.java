package competition.subsystems.collector;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class CollectorSubsystem extends BaseSubsystem{
    public final XCANTalon collectorMotor;
    public DoubleProperty intakePower;
    public DoubleProperty ejectPower;

    final ElectricalContract contract;

    @Inject
    public CollectorSubsystem(CommonLibFactory factory, PropertyFactory pf, ElectricalContract eContract){
        this.contract = eContract;
        if (eContract.isIntakeReady()) {
            collectorMotor = factory.createCANTalon(eContract.getLeftCollectorMotor());
            collectorMotor.configOpenloopRamp(0.1, 100);
        } else {
            collectorMotor = null;
        }
        pf.setPrefix(this);
        intakePower = pf.createPersistentProperty("intakePower", 1);
        ejectPower = pf.createPersistentProperty("ejectPower", -1);
    }

    private void setMotorPower(double power){
        if (contract.isIntakeReady()){
            collectorMotor.set(ControlMode.PercentOutput, power);
        }
    }

    public void intake(){
        setMotorPower(intakePower.get());
    }

    public void eject(){
        setMotorPower(ejectPower.get());
    }

    public void stop(){
        setMotorPower(0);
    }

}