package competition.subsystems.conveyer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XCANTalon.XCANTalonFactory;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.controls.sensors.XDigitalInput.XDigitalInputFactory;
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
    public ConveyorSubsystem(PropertyFactory pf, XCANTalonFactory talonFactory, XDigitalInputFactory digitalInputFactory, ElectricalContract eContract) {
        super("ConveyerSubsystem", pf);
        this.isReady = eContract.isConveyerReady();
        if(isReady) {
            motor = talonFactory.create(eContract.getConveyerMotor());
            motor.setNeutralMode(NeutralMode.Brake);
            motor.configOpenloopRamp(0.1, 100);
        } else {
            motor = null;
        }

        topSensor = digitalInputFactory.create(eContract.getConveyorTopSensor().channel);
        topSensor.setInverted(eContract.getConveyorTopSensor().inverted);
        bottomSensor = digitalInputFactory.create(eContract.getConveyorBottomSensor().channel);
        bottomSensor.setInverted(eContract.getConveyorBottomSensor().inverted);
        collectorSensor = digitalInputFactory.create(eContract.getCollectorSensor().channel);
        collectorSensor.setInverted(eContract.getCollectorSensor().inverted);

        // Make top-level so it shows up on SmartDashboard
        pf.setTopLevelPrefix();
        topSensorProp = pf.createEphemeralProperty("Conveyor top sensor value", topSensor.get());
        bottomSensorProp = pf.createEphemeralProperty("Conveyor bottom sensor value", bottomSensor.get());
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
