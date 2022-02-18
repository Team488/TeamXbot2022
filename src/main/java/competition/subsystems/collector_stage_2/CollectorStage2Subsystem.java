package competition.subsystems.collector_stage_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.controls.actuators.mock_adapters.MockCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class CollectorStage2Subsystem extends SimpleMotorSubsystem {

    public final XCANSparkMax motor;
    public final boolean isReady;

    @Inject
    public CollectorStage2Subsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("CollectorStage2", pf);
        this.isReady = eContract.isCollectorStage2Ready();
        if(isReady) {
            motor = clf.createCANSparkMax(eContract.getCollectorStage2Motor(), this.getPrefix(), "Collector Stage2 Motor");
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
