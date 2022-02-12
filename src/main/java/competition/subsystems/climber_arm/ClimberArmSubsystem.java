package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import competition.electrical_contract.ElectricalContract;
import competition.injection.arm.ArmInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
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
    private final DoubleProperty armPositionTarget;
    private final DoubleProperty armInchesPerRotation;
    private final Latch safetyLatch;
    final String label;

    private enum PidSlot {
        Position(0),
        Velocity(1);

        private final int slot;
        private PidSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }
    

    @Inject
    public ClimberArmSubsystem(ArmInstance armInstance, CommonLibFactory factory, PropertyFactory pf, ElectricalContract eContract){
        armMotor = factory.createCANSparkMax(eContract.getClimberNeo(armInstance) , this.getPrefix(), "ArmMotor");
        armMotor.enableVoltageCompensation(12);
        label = armInstance.getLabel();
        
        // Shared properties
        pf.setPrefix(super.getPrefix());
        safeArmExtendedNumber = pf.createPersistentProperty("safelyExtendable", 10);
        safeArmRetractedNumber = pf.createPersistentProperty("safelyRetractable", -10);
        // Assume this is shared - if not, we'll split it out.
        armInchesPerRotation = pf.createPersistentProperty("ArmInchesPerRotation", 1.0);

        // Unique properties
        pf.setPrefix(this);
        isCalibratedProp = pf.createEphemeralProperty("IsArmCalibrated", false);
        armMotorPositionProp = pf.createEphemeralProperty("ArmMotorPosition", 0.0);
        armPositionTarget = pf.createEphemeralProperty("TargetPosition", 0);

        safetyLatch = new Latch(true, EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                // re-enable internal safety features
                setSoftLimitsEnabled(true);
            }
            else if(edge == EdgeType.FallingEdge) {
                // disable internal safety features
                setSoftLimitsEnabled(false);
            }
        });

        this.register();
    }

    @Override
    public String getPrefix() {
        return super.getPrefix() + this.label + "/";
    }

    private void setSoftLimitsEnabled(boolean enabled) {
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, enabled);
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, enabled);
    }


    private void setMotorPower(double power, boolean isSafe) {

        safetyLatch.setValue(isSafe);

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

    public void stop(){
        setMotorPower(0, true);
    }

    @Override
    public double getCurrentValue() {
        return armMotor.getPosition() * armInchesPerRotation.get();
    }

    public double getVelocity() {
        return armMotor.getVelocity();
    }

    @Override
    public double getTargetValue() {
        return armPositionTarget.get();
    }

    @Override
    public void setTargetValue(double value) {
        armPositionTarget.set(value);
    }

    @Override
    public void setPower(double power) {
        setMotorPower(power, true);
    }

    public void setPower(double power, boolean isSafe) {
        setMotorPower(power, isSafe);
    }

    public void setPositionReference(double positionInInches) {
        // Convert from inches to rotations (the native unit of the controller)
        armMotor.setReference(positionInInches / armInchesPerRotation.get(), ControlType.kPosition, PidSlot.Position.getSlot());
    }

    public void setVelocityReference(double velocityInInchesPerSecond) {
        // Convert from inches to rotations/sec (the native unit of the controller)
        armMotor.setReference(velocityInInchesPerSecond / armInchesPerRotation.get(), ControlType.kVelocity, PidSlot.Velocity.getSlot());
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