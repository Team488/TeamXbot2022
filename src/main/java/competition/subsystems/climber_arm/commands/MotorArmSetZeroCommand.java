package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;

public class MotorArmSetZeroCommand extends BaseCommand {

    private final ClimberArmSubsystem subsystem;

    @Inject
    public MotorArmSetZeroCommand(ClimberArmSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initialize() {
        // nothing to do
    }

    @Override
    public void execute() {
        this.subsystem.setCurrentPositionToZero();
    }
    
}
