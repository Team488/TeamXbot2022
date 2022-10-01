package competition.subsystems.climber_arm.commands;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberSubsystem;
import xbot.common.math.MathUtils;

public class DualArmControllerCommandWithJoysticks extends BaseDoubleClimberArmCommand {

    protected final OperatorInterface oi;

    boolean isSafe;

    @Inject
    public DualArmControllerCommandWithJoysticks(ClimberSubsystem climber, OperatorInterface oi) {
        super(climber);
        this.oi = oi;
    }

    public void setIsSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double leftArmPower = oi.operatorGamepad.getLeftStickY();
        double rightArmPower = oi.operatorGamepad.getRightStickY();

        // Use a simple deadband
        leftArmPower = MathUtils.deadband(leftArmPower, oi.getOperatorGamepadTypicalDeadband(), (a) -> a);
        rightArmPower = MathUtils.deadband(rightArmPower, oi.getOperatorGamepadTypicalDeadband(), (a) -> a);

        leftArm.setPower(leftArmPower, isSafe);
        rightArm.setPower(rightArmPower, isSafe);
    }
}