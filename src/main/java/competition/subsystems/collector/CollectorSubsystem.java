package competition.subsystems.collector;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ctre.phoenix.motorcontrol.ControlMode;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XCANTalon.XCANTalonFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class CollectorSubsystem extends BaseSubsystem{
    public final XCANTalon collectorMotor;
    public DoubleProperty intakePower;
    public DoubleProperty ejectPower;

    final ElectricalContract contract;

    @Inject
    public CollectorSubsystem(XCANTalonFactory talonFactory, PropertyFactory pf, ElectricalContract eContract){
        this.contract = eContract;
        if (eContract.isIntakeReady()) {
            collectorMotor = talonFactory.create(eContract.getLeftCollectorMotor());
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