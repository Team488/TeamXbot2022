package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import competition.subsystems.latch.LatchSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDManager;

public class DualArmBalancerCommand extends BaseDoubleClimberArmCommand {

    final PIDManager balancePid;
    final OperatorInterface oi;
    boolean isSafe = true;
    final ClimberPivotSubsystem pivot;
    final LatchSubsystem latch;

    @Inject
    public DualArmBalancerCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm,
            ClimberPivotSubsystem pivot, LatchSubsystem latch, OperatorInterface oi, CommonLibFactory clf) {
        super(leftArm, rightArm);
        this.oi = oi;
        this.pivot = pivot;
        this.latch = latch;
        balancePid = clf.createPIDManager(this.getPrefix() + "BalancePID", 0.1, 0, 0);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
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

        // Let's consider the left arm to be the "primary" arm, and see if we can get
        // the right one as close as possible to it.
        double balancePower = 0;
        if (leftArm.isCalibrated() && rightArm.isCalibrated()) {
            balancePower = balancePid.calculate(leftArm.getPosition(), rightArm.getPosition());
        }


        leftArm.setPower(climbPower - balancePower, isSafe);
        rightArm.setPower(climbPower + balancePower, isSafe);
    }
}
