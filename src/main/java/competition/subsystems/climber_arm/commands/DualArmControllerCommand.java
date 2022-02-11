package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;

public class DualArmControllerCommand extends BaseCommand {

    protected final ClimberArmSubsystem leftArm;
    protected final ClimberArmSubsystem rightArm;
    protected final OperatorInterface oi;

    boolean isSafe;

    @Inject
    public DualArmControllerCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm, OperatorInterface oi) {
        this.addRequirements(leftArm, rightArm);
        this.leftArm = leftArm;
        this.rightArm = rightArm;
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

        leftArm.setPower(leftArmPower, isSafe);
        rightArm.setPower(rightArmPower, isSafe);
    }
}