package competition.subsystems.collector;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class CollectorSubsystem extends BaseSubsystem{
    public final XCANSparkMax collectorMotor;
    public DoubleProperty intakePower;
    public DoubleProperty ejectPower;

    @Inject
    public CollectorSubsystem(CommonLibFactory factory, PropertyFactory pf, ElectricalContract eContract){
        collectorMotor = factory.createCANSparkMax(eContract.getCollectorMotor(), this.getPrefix(), "Collector Motor");
        pf.setPrefix(this);
        intakePower = pf.createPersistentProperty("intakePower", 1);
        ejectPower = pf.createPersistentProperty("ejectPower", -1);
    }

    private void setMotorPower(double power){
        collectorMotor.set(power);
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