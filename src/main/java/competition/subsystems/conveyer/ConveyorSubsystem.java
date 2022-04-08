package competition.subsystems.conveyer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.simplemotor.SimpleMotorSubsystem;

@Singleton
public class ConveyorSubsystem extends SimpleMotorSubsystem {
    public final XCANTalon motor;
    public final XDigitalInput topSensor;
    public final XDigitalInput bottomSensor;
    public final XDigitalInput collectorSensor;
    public final boolean isReady;

    private final BooleanProperty topSensorProp;
    private final BooleanProperty bottomSensorProp;
    private final BooleanProperty collectorSensorProp;

    private boolean hasRetracted = true;

    @Inject
    public ConveyorSubsystem(PropertyFactory pf, CommonLibFactory clf, ElectricalContract eContract) {
        super("ConveyerSubsystem", pf);
        this.isReady = eContract.isConveyerReady();
        if(isReady) {
            motor = clf.createCANTalon(eContract.getConveyerMotor());
            motor.setNeutralMode(NeutralMode.Brake);
            motor.configOpenloopRamp(0.1, 100);
        } else {
            motor = null;
        }

        topSensor = clf.createDigitalInput(eContract.getConveyorTopSensor().channel);
        topSensor.setInverted(eContract.getConveyorTopSensor().inverted);
        bottomSensor = clf.createDigitalInput(eContract.getConveyorBottomSensor().channel);
        bottomSensor.setInverted(eContract.getConveyorBottomSensor().inverted);
        collectorSensor = clf.createDigitalInput(eContract.getCollectorSensor().channel);
        collectorSensor.setInverted(eContract.getCollectorSensor().inverted);

        topSensorProp = pf.createEphemeralProperty("Top sensor value", topSensor.get());
        bottomSensorProp = pf.createEphemeralProperty("Bottom sensor value", bottomSensor.get());
        collectorSensorProp = pf.createEphemeralProperty("Collector sensor value", collectorSensor.get());
    }

    @Override
    public void setPower(double power) {
        if(isReady) {
            // if the conveyor goes forward, mark not retracted so we know there
            // might be a ball in contact with the shooter wheel now
            if(power > 0) {
                hasRetracted = false;
            }
            motor.set(ControlMode.PercentOutput, power);
        }
    }

    public boolean getHasRetracted() {
        return hasRetracted;
    }

    public void setHasRetracted(boolean hasRetracted) {
        this.hasRetracted = hasRetracted;
    }
    
    @Override
    public void periodic() {
        super.periodic();

        this.topSensorProp.set(this.topSensor.get());
        this.bottomSensorProp.set(this.bottomSensor.get());
        this.collectorSensorProp.set(this.collectorSensor.get());
    }
}
