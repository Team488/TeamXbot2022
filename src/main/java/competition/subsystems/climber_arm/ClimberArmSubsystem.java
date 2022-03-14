package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.FaultID;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import competition.electrical_contract.ElectricalContract;
import competition.injection.arm.ArmInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.logic.StallDetector;
import xbot.common.logic.StallDetector.StallState;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;

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
    private final DoubleProperty armPowerFactor;
    private final Latch safetyLatch;
    final String label;
    final ElectricalContract contract;

    public final DoubleProperty positionFullyRetractedProperty;
    public final DoubleProperty positionClearCurrentBarProperty;
    public final DoubleProperty positionFullyExtendedProperty;
    public final DoubleProperty positionEngageNextBarProperty;
    public final DoubleProperty positionAutomaticPivotIn;

    final DoubleProperty armInstantVelocity;
    final StringProperty armStallState;

    StallDetector armStallDetector;
    double lastArmPosition;
    double directVelocity;

    public XDigitalInput lowerLimitSwitch;
    public XDigitalInput upperLimitSwitch;

    public final BooleanProperty upperLimitSwitchState;
    public final BooleanProperty lowerLimitSwitchState;

    ArmInstance armInstance;

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
        label = armInstance.getLabel();
        this.armInstance = armInstance;
        if (eContract.isClimberReady()) {
            armMotor = factory.createCANSparkMax(eContract.getClimberNeo(armInstance) , this.getPrefix(), "ArmMotor");
            armMotor.enableVoltageCompensation(12);
            armMotor.setIdleMode(IdleMode.kBrake);
        }

        if (eContract.areClimberLimitSensorsReady(armInstance)) {
            lowerLimitSwitch = factory.createDigitalInput(eContract.getClimberLowerLimitSensor(armInstance).channel);
            upperLimitSwitch = factory.createDigitalInput(eContract.getClimberUpperLimitSensor(armInstance).channel);
        }

        this.contract = eContract;
        
        // Shared properties
        pf.setPrefix(super.getPrefix());
        safeArmExtendedNumber = pf.createPersistentProperty("safelyExtendable", 10);
        safeArmRetractedNumber = pf.createPersistentProperty("safelyRetractable", -10);
        // Assume this is shared - if not, we'll split it out.
        armInchesPerRotation = pf.createPersistentProperty("ArmInchesPerRotation", 1.0);
        armPowerFactor = pf.createPersistentProperty("PowerFactor", 1);

        positionFullyRetractedProperty = pf.createPersistentProperty("FullyRetractedPositionInches", 0.0);
        positionClearCurrentBarProperty = pf.createPersistentProperty("ClearCurrentBarPositionInches", 6.0);
        positionFullyExtendedProperty = pf.createPersistentProperty("FullyExtendedPositionInches", 24.5);
        positionEngageNextBarProperty = pf.createPersistentProperty("EngageNextBarPositionInches", 22.5);
        positionAutomaticPivotIn = pf.createPersistentProperty("AutomaticPivotInInches", 23.0);

        armStallDetector = factory.createStallDetector(super.getPrefix());

        // Unique properties
        pf.setPrefix(this);
        isCalibratedProp = pf.createEphemeralProperty("IsArmCalibrated", true);
        armMotorPositionProp = pf.createEphemeralProperty("ArmMotorPosition", 0.0);
        armPositionTarget = pf.createEphemeralProperty("TargetPosition", 0);
        armInstantVelocity = pf.createEphemeralProperty("ArmInstantVelocity", 0);
        armStallState = pf.createEphemeralProperty("ArmStallState", "NotYetRun");
        upperLimitSwitchState = pf.createEphemeralProperty("UpperLimitSwitchState", false);
        lowerLimitSwitchState = pf.createEphemeralProperty("LowerLimitSwitchState", false);


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

        setupStatusFrames();

        this.register();
    }
    
    /**
     * Set up status frame intervals to reduce unnecessary CAN activity.
     */
    private void setupStatusFrames() {
        if (this.contract.isClimberReady()) {
            // We need to re-set frame intervals after a device reset.
            if (this.armMotor.getStickyFault(FaultID.kHasReset)) {
                log.info("Setting status frame periods.");

                // See https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
                // for description of the different status frames. kStatus2 is the only frame with data needed for software PID.

                this.armMotor.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus0, 500 /* default 10 */);
                this.armMotor.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20 /* default 20 */);
                this.armMotor.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20 /* default 20 */);
                this.armMotor.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus3, 500 /* default 50 */);
                
                this.armMotor.clearFaults();
            }
        }
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

    private void setMotorPower(double power, boolean isSafe) {

        safetyLatch.setValue(isSafe);

        power *= MathUtils.constrainDoubleToRobotScale(armPowerFactor.get());

        if (isSafe) {
            if (isArmOverExtended()/* Add upper limit here when needed */) {
                power = MathUtils.constrainDouble(power, -1, 0);

            } 
            if (isArmOverRetracted() || isAtLowerLimitSwitch()) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
        }

        // Prevent ourself from stalling!
        if (power < 0) {
            // If we are in stall cooldown, no point doing anything.
            if (armStallDetector.wasStalledRecently()) {
                power = 0;
            }

            // Feed the stall detector and check behavior
            StallState stallState = armStallDetector.getIsStalled(armMotor.getOutputCurrent(), power, directVelocity);
            armStallState.set(stallState.toString());
            if (stallState == StallState.STALLED || stallState == StallState.WAS_STALLED_RECENTLY) {
                power = 0;
            }
        } else {
            // No need to stall protect while extending
            armStallState.set("GoingUp"); 
        }
        
        if (contract.isClimberReady()) {
            armMotor.set(power);
        }
    }

    public void stop(){
        setMotorPower(0, true);
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
        // Convert from inches to rotations (the native unit of the controller)
        if (contract.isClimberReady()) {
            armMotor.setReference(positionInInches / armInchesPerRotation.get(), ControlType.kPosition, PidSlot.Position.getSlot());
        }
        
    }

    public void setVelocityReference(double velocityInInchesPerSecond) {
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

    public boolean isAtLowerLimitSwitch() {
        // For now, assume that the swtich is hooked into RoboRIO dio
        if (contract.areClimberLimitSensorsReady(armInstance)) {
            return lowerLimitSwitch.get();
        }
        return false;
    }

    public boolean isAtUpperLimitSwitch() {
        // For now, assume that the swtich is hooked into RoboRIO dio
        if (contract.areClimberLimitSensorsReady(armInstance)) {
            return upperLimitSwitch.get();
        }
        return false;
    }

    @Override
    public void periodic() {
        this.armMotorPositionProp.set(getPosition());
        armMotor.periodic();
        setupStatusFrames();

        double currentPosition = getPosition();
        directVelocity = currentPosition - lastArmPosition;
        lastArmPosition = currentPosition;

        armInstantVelocity.set(directVelocity);;

        if (contract.areClimberLimitSensorsReady(armInstance)) {
            upperLimitSwitchState.set(upperLimitSwitch.get());
            lowerLimitSwitchState.set(lowerLimitSwitch.get());
        }
    }
}