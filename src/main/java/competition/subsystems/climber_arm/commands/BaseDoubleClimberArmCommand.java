package competition.subsystems.climber_arm.commands;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.ClimberSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseDoubleClimberArmCommand extends BaseCommand {
    
    protected final ClimberArmSubsystem leftArm;
    protected final ClimberArmSubsystem rightArm;

    public BaseDoubleClimberArmCommand(ClimberSubsystem climber){
        this.leftArm = climber.getLeftArm();
        this.rightArm = climber.getRightArm();
        this.addRequirements(climber, leftArm, rightArm);
    }
    
}
