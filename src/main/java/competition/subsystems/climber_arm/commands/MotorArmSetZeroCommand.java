package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberSubsystem;

public class MotorArmSetZeroCommand extends BaseDoubleClimberArmCommand {

    @Inject
    public MotorArmSetZeroCommand(ClimberSubsystem climber) {
        super(climber);
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
