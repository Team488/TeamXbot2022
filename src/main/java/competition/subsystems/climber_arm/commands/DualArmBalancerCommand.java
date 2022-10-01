package competition.subsystems.climber_arm.commands;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberSubsystem;
import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import competition.subsystems.latch.LatchSubsystem;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDManager.PIDManagerFactory;

public class DualArmBalancerCommand extends BaseDoubleClimberArmCommand {

    final PIDManager balancePid;
    final OperatorInterface oi;
    final ClimberPivotSubsystem pivot;
    final LatchSubsystem latch;
    final Latch calibrationLatch;

    @Inject
    public DualArmBalancerCommand(ClimberSubsystem climber,
            ClimberPivotSubsystem pivot, LatchSubsystem latch, OperatorInterface oi, PIDManagerFactory pidf) {
        super(climber);
        this.oi = oi;
        this.pivot = pivot;
        this.latch = latch;
        balancePid = pidf.create(this.getPrefix() + "BalancePID", 0.1, 0, 0);

        calibrationLatch = new Latch(true, EdgeType.RisingEdge, edge -> {
            if(edge == EdgeType.RisingEdge) {
                // Calibration involves a set call over CAN, which is expensive.
                // Let's only do it the first time both arms are lowered.
                calibrateBothArms();
            }
        });
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    private void calibrateBothArms() {
        leftArm.setCurrentPositionToZero();
        rightArm.setCurrentPositionToZero();
    }

    @Override
    public void execute() {
        double climbPower = MathUtils.deadband(
                oi.operatorGamepad.getLeftStickY(),
                oi.getOperatorGamepadTypicalDeadband());

        if (climbPower > 0) {
            // If we are raising the arm, allow automatic pivoting
            pivot.setAutomaticPivotingEnabled(true);
        } else if (climbPower  < 0) {
            // When we are climbing up, we don't want the pivot actuating.
            pivot.setAutomaticPivotingEnabled(false);
        }

        calibrationLatch.setValue(leftArm.isAtLowerLimitSwitch() && rightArm.isAtLowerLimitSwitch());

        // Let's consider the left arm to be the "primary" arm, and see if we can get
        // the right one as close as possible to it.
        double balancePower = 0;
        if (leftArm.isCalibrated() && rightArm.isCalibrated()) {
            balancePower = balancePid.calculate(leftArm.getPosition(), rightArm.getPosition());
        }

        // If either arm is in "unlocked" mode, move them independently.
        if (leftArm.getArmsUnlocked() || rightArm.getArmsUnlocked()) {
            leftArm.setPower(climbPower);
            
            double rightPower = MathUtils.deadband(
                oi.operatorGamepad.getRightStickY(),
                oi.getOperatorGamepadTypicalDeadband());

            rightArm.setPower(rightPower);
        } else {
            leftArm.setPower(climbPower - balancePower);
            rightArm.setPower(climbPower + balancePower);
        }
    }
}
