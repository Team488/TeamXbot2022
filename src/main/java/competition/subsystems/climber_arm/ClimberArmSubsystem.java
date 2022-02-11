package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class ClimberArmSubsystem extends BaseSetpointSubsystem {
    public XCANSparkMax armMotor;
    public double armMotorPosition;
    public final DoubleProperty safeArmExtendedNumber;
    public final DoubleProperty safeArmRetractedNumber;
    private final DoubleProperty armMotorPositionProp;
    private final BooleanProperty isCalibratedProp;
    

    @Inject
    public ClimberArmSubsystem(CommonLibFactory factory, PropertyFactory pf, ElectricalContract eContract){
        armMotor = factory.createCANSparkMax(eContract.getLeftClimberNeo(), this.getPrefix(), "ArmMotor");
        pf.setPrefix(this);
        safeArmExtendedNumber = pf.createPersistentProperty("safelyExtendable", 10);
        safeArmRetractedNumber = pf.createPersistentProperty("safelyRetractable", -10);
        armMotorPositionProp = pf.createEphemeralProperty("ArmMotorPosition", 0.0);
        isCalibratedProp = pf.createEphemeralProperty("IsArmCalibrated", false);

        this.register();
    }


    private void setMotorPower(double power, boolean isSafe) {
        if (isSafe) {
            if (isArmOverExtended()) {
                power = MathUtils.constrainDouble(power, -1, 0);

            } 
            if (isArmOverRetracted()) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
        }
        
        armMotor.set(power);
    }

    public void motorStop(double power){
        setMotorPower(0, true);
    }

    // Won't use right now

    @Override
    public double getCurrentValue() {
        return 0;
    }

    @Override
    public double getTargetValue() {
        return 0;
    }

    @Override
    public void setTargetValue(double value) {
    }

    @Override
    public void setPower(double power) {
        setMotorPower(power, true);
    }

    @Override
    public boolean isCalibrated() {
        return isCalibratedProp.get();
    }

    public void setCurrentPositionToZero() {
        armMotor.setPosition(0);
        isCalibratedProp.set(true);
    }

    public boolean isArmOverExtended() {
        armMotorPosition = getPosition();
        return armMotorPosition > safeArmExtendedNumber.get();
    }

    public boolean isArmOverRetracted() {
        armMotorPosition = getPosition();
        return armMotorPosition < safeArmRetractedNumber.get();
    }

    public double getPosition() {
        return armMotor.getPosition();
    }

    @Override
    public void periodic() {
        this.armMotorPositionProp.set(armMotor.getPosition());
    }
}