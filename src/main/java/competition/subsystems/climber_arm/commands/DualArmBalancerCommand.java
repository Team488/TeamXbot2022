package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDManager;

public class DualArmBalancerCommand extends BaseDoubleClimberArmCommand {

    final PIDManager balancePid;
    final OperatorInterface oi;

    @Inject
    public DualArmBalancerCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm, OperatorInterface oi, CommonLibFactory clf) {
        super(leftArm, rightArm);
        this.oi = oi;
        balancePid = clf.createPIDManager(this.getPrefix() + "BalancePID", 0.1, 0, 0);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double climbPower = oi.operatorGamepad.getLeftStickY();
        // Let's consider the left arm to be the "primary" arm, and see if we can get the right one as close as possible to it.
        double balancePower = 0;
        if (leftArm.isCalibrated() && rightArm.isCalibrated()) {
            balancePower = balancePid.calculate(leftArm.getPosition(), rightArm.getPosition());    
        }

        leftArm.setPower(climbPower - balancePower);
        rightArm.setPower(climbPower + balancePower);
    }
}
