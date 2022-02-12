package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;

public class MotorArmStopCommand extends BaseCommand {

    private final ClimberArmSubsystem arm;

    @Inject
    public MotorArmStopCommand(ClimberArmSubsystem arm) {
        this.arm = arm;
        this.addRequirements(arm);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        arm.stop();
    }
}