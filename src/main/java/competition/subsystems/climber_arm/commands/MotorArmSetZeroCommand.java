package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class MotorArmSetZeroCommand extends BaseDoubleClimberArmCommand {

    @Inject
    public MotorArmSetZeroCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm) {
        super(leftArm, rightArm);
    }

    @Override
    public void initialize() {
        leftArm.setCurrentPositionToZero();
        rightArm.setCurrentPositionToZero();
    }

    @Override
    public void execute() {
        // Nothing to do here
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
}