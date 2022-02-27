package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import competition.electrical_contract.ElectricalContract;
import competition.injection.arm.ArmInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.controls.actuators.XSolenoid;
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
    public XSolenoid armPawl;
    public double armMotorPosition;
    public final DoubleProperty safeArmExtendedNumber;
    public final DoubleProperty safeArmRetractedNumber;
    private final DoubleProperty armMotorPositionProp;
    private final BooleanProperty isCalibratedProp;
    private final DoubleProperty armPositionTarget;
    private final DoubleProperty armInchesPerRotation;
    private final DoubleProperty pawlDeadband;
    private final DoubleProperty armPowerFactor;
    private final Latch safetyLatch;
    final String label;
    final ElectricalContract contract;

    public final DoubleProperty positionFullyRetractedProperty;
    public final DoubleProperty positionClearCurrentBarProperty;
    public final DoubleProperty positionFullyExtendedProperty;
    public final DoubleProperty positionEngageNextBarProperty;
    public final DoubleProperty positionAutomaticPivotIn;

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
        if (eContract.isClimberReady()) {
            armMotor = factory.createCANSparkMax(eContract.getClimberNeo(armInstance) , this.getPrefix(), "ArmMotor");
            armMotor.enableVoltageCompensation(12);
            armMotor.setIdleMode(IdleMode.kBrake);
            armPawl = factory.createSolenoid(eContract.getClimberPawl(armInstance).channel);
        }
        
        label = armInstance.getLabel();
        this.contract = eContract;
        
        // Shared properties
        pf.setPrefix(super.getPrefix());
        safeArmExtendedNumber = pf.createPersistentProperty("safelyExtendable", 10);
        safeArmRetractedNumber = pf.createPersistentProperty("safelyRetractable", -10);
        // Assume this is shared - if not, we'll split it out.
        armInchesPerRotation = pf.createPersistentProperty("ArmInchesPerRotation", 1.0);
        pawlDeadband = pf.createPersistentProperty("PawlDeadband", 0.02);
        armPowerFactor = pf.createPersistentProperty("PowerFactor", 1);

        positionFullyRetractedProperty = pf.createPersistentProperty("FullyRetractedPositionInches", 0.0);
        positionClearCurrentBarProperty = pf.createPersistentProperty("ClearCurrentBarPositionInches", 6.0);
        positionFullyExtendedProperty = pf.createPersistentProperty("FullyExtendedPositionInches", 24.5);
        positionEngageNextBarProperty = pf.createPersistentProperty("EngageNextBarPositionInches", 22.5);
        positionAutomaticPivotIn = pf.createPersistentProperty("AutomaticPivotInInches", 23.0);

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

    public double getFullyRectractedPosition() {
        return positionFullyRetractedProperty.get();
    }

    public double getClearCurrentBarPosition() {
        return positionClearCurrentBarProperty.get();
    }

    public double getFullyExtendedPosition() {
        return positionFullyExtendedProperty.get();
    }

    public double getEngageNextBarPosition() {
        return positionEngageNextBarProperty.get();
    }

    public double getAutomaticPivotInPosition() {
        return positionAutomaticPivotIn.get();
    }

    private void setSoftLimitsEnabled(boolean enabled) {
        if (contract.isClimberReady()) {

            if (enabled) {
                armMotor.setSoftLimit(SoftLimitDirection.kForward, (float)(safeArmExtendedNumber.get() / armInchesPerRotation.get()));
                //armMotor.setSoftLimit(SoftLimitDirection.kReverse, (float)(safeArmRetractedNumber.get() / armInchesPerRotation.get()));
            }

            armMotor.enableSoftLimit(SoftLimitDirection.kForward, enabled);
            // Always allow to pull in for climbing
            armMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        }
    }

    private void setPawl(boolean disengaged) {
        if (contract.isClimberReady()) {
            armPawl.setOn(disengaged);
        }
    }

    public void lockPawl() {
        setPawl(false);
    }

    public void freePawl() {
        setPawl(true);
    }


    private void setMotorPower(double power, boolean isSafe) {

        safetyLatch.setValue(isSafe);

        power *= MathUtils.constrainDoubleToRobotScale(armPowerFactor.get());

        // To a first approximation, whenever the device is moving, the pawl should be disengaged.
        // If there is any hint of power, the pawl should be disengaged.

        // We will not optimistically re-engage the pawl - that will only be done via manual action.
        if (Math.abs(power) > pawlDeadband.get()) {
            freePawl();
        }

        if (isSafe) {
            if (isArmOverExtended()) {
                power = MathUtils.constrainDouble(power, -1, 0);

            } 
            if (isArmOverRetracted()) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
        }
        
        if (contract.isClimberReady()) {
            armMotor.set(power);
        }
    }

    public void stop(){
        setMotorPower(0, true);
        lockPawl();
    }

    @Override
    public double getCurrentValue() {
        return getPosition();
    }

    public double getVelocity() {
        if (contract.isClimberReady()) {
            return armMotor.getVelocity() * armInchesPerRotation.get();
        }
        return 0;
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
        // Since we can no longer be sure what the motor is doing, release the pawl just in case
        freePawl();
        
        // Convert from inches to rotations (the native unit of the controller)
        if (contract.isClimberReady()) {
            armMotor.setReference(positionInInches / armInchesPerRotation.get(), ControlType.kPosition, PidSlot.Position.getSlot());
        }
        
    }

    public void setVelocityReference(double velocityInInchesPerSecond) {
        // Since we can no longer be sure what the motor is doing, release the pawl just in case
        freePawl();

        // Convert from inches to rotations/sec (the native unit of the controller)
        if (contract.isClimberReady()) {
            armMotor.setReference(velocityInInchesPerSecond / armInchesPerRotation.get(), ControlType.kVelocity, PidSlot.Velocity.getSlot());
        }
    }

    @Override
    public boolean isCalibrated() {
        return isCalibratedProp.get();
    }

    public void setCurrentPositionToZero() {
        if (contract.isClimberReady()) {
            armMotor.setPosition(0);
        }
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
        if (contract.isClimberReady()) {
            return armMotor.getPosition() * armInchesPerRotation.get();
        }
        return 0;
    }


    @Override
    public void periodic() {
        this.armMotorPositionProp.set(getPosition());
    }
}