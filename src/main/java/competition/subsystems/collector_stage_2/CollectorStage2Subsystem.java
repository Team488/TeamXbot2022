package competition.subsystems.collector_stage_2;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ctre.phoenix.motorcontrol.ControlMode;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XCANTalon.XCANTalonFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class CollectorStage2Subsystem extends SimpleMotorSubsystem {

    public final XCANTalon motor;
    public final boolean isReady;

    @Inject
    public CollectorStage2Subsystem(PropertyFactory pf, XCANTalonFactory talonFactory, ElectricalContract eContract) {
        super("CollectorStage2", pf);
        this.isReady = eContract.isCollectorStage2Ready();
        if(isReady) {
            motor = talonFactory.create(eContract.getCollectorStage2Motor());
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
