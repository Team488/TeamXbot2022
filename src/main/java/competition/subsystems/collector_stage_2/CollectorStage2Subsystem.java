package competition.subsystems.collector_stage_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class CollectorStage2Subsystem extends SimpleMotorSubsystem {

    public final XCANSparkMax motor;

    @Inject
    public CollectorStage2Subsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("CollectorStage2", pf);
        if(eContract.isCollectorStage2Ready()) {
            motor = clf.createCANSparkMax(eContract.getCollectorStage2Motor(), this.getPrefix(), "Collector Stage2 Motor");
        }
    }

    @Override
    public void setPower(double power) {
        motor.set(power);
    }
    
}
