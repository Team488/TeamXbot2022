package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.ArmInstance;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.CalibrationDecider;
import xbot.common.logic.CalibrationDecider.CalibrationMode;
import xbot.common.logic.TimeStableValidator;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;

public class ClimberArmMaintainerCommand extends BaseMaintainerCommand {

    private final ClimberArmSubsystem arm;
    private final String armLabel;
    private final OperatorInterface oi;
    private final CalibrationDecider calibrationDecider;
    private final DoubleProperty calibrationPower;
    private final StringProperty calibrationAttemptState;

    private final TimeStableValidator calibrationValidator;
    private final DoubleProperty waitForMotorStallCalibrationTime;
    private final BooleanProperty attemptAutomaticCalibration;
    private final PIDManager positionPid;

    @Inject
    public ClimberArmMaintainerCommand(ArmInstance armInstance, ClimberArmSubsystem arm, PropertyFactory pf,
            CommonLibFactory clf, OperatorInterface oi) {
        super(arm, pf, clf, 1, 0.001);
        this.arm = arm;
        this.oi = oi;
        this.armLabel = armInstance.getLabel();

        // Properties that are shared between the two arms
        pf.setPrefix(this);
        calibrationPower = pf.createPersistentProperty("CalibrationPower", 0.0); // Change from 0 once we know safer
                                                                                 // values
        waitForMotorStallCalibrationTime = pf.createPersistentProperty("Motor Stall Calibration Time", 0.25);
        calibrationValidator = new TimeStableValidator(() -> waitForMotorStallCalibrationTime.get());
        attemptAutomaticCalibration = pf.createPersistentProperty("Attempt Automatic Calibration", false);
        positionPid = clf.createPIDManager(super.getPrefix() + "PositionPID", 0.33, 0, 0);

        // Properties that are unique to each arm
        pf.setPrefix(this + armLabel);
        calibrationAttemptState = pf.createEphemeralProperty("CalibrationAttemptState", "Uninitialized");

        calibrationDecider = clf.createCalibrationDecider(this.getPrefix() + armLabel);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        calibrationDecider.reset();
        arm.setTargetValue(arm.getCurrentValue());
    }

    @Override
    protected void calibratedMachineControlAction() {
        double power = positionPid.calculate(arm.getTargetValue(), arm.getCurrentValue());
        arm.setPower(power);
    }

    @Override
    protected void uncalibratedMachineControlAction() {
        // A basic calibration routine would be a good idea here.
        // If we have some kind of distance or bump sensor, that would let build a
        // reliable zeroing routine... but if that's not available, then we can just
        // drive the mechanism downwards slowly for a few seconds and see if it settles.

        if (attemptAutomaticCalibration.get()) {
            CalibrationMode mode = calibrationDecider.decideMode(arm.isCalibrated());
            calibrationAttemptState.set(mode.toString());

            switch (mode) {
                case Attempting:
                    // Move the arm down slowly - just enough power to retract the arm,
                    // but not so much that would damage the mechanism if it stalls for a few
                    // seconds
                    arm.setPower(calibrationPower.get());

                    // Check to see if the arm isn't moving very much, and hasn't been moving for at
                    // least a few moments.
                    boolean isStablyStopped = calibrationValidator.checkStable(Math.abs(arm.getVelocity()) < 0.1);

                    // If the arm does seem to not be moving after running it for a while, we can
                    // assume that has reached the bottom
                    // and is fully retracted. Let's set this to position zero.
                    if (isStablyStopped) {
                        arm.setCurrentPositionToZero();
                    }
                    break;
                case Calibrated:
                    // Nothing to do - we are in great shape. This is the last time the parent
                    // uncalibratedMachineControlAction
                    // should be called.
                    arm.setPower(0);
                    break;
                case GaveUp:
                    // It took too long for calibration to complete, so we're letting humans take
                    // control.
                    humanControlAction();
                    break;
                default:
                    // Not sure how you got here, but when in doubt, let the humans take over.
                    humanControlAction();
                    break;
            }
        } else {
            // We're not trying to calibrate, so let the humans take over and handle
            // calibration themselves.
            humanControlAction();
        }
    }

    @Override
    protected double getHumanInput() {
        switch (armLabel) {
            case "LeftArm":
                return MathUtils.deadband(
                    oi.operatorGamepad.getLeftStickY(),
                    oi.getDriverGamepadTypicalDeadband());
            case "RightArm":
                return MathUtils.deadband(
                    oi.operatorGamepad.getRightStickY(),
                    oi.getDriverGamepadTypicalDeadband());
            default:
                return 0;
        }
    }
}