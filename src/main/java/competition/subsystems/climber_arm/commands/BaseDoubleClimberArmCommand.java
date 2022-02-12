package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseDoubleClimberArmCommand extends BaseCommand {
    
    protected final ClimberArmSubsystem leftArm;
    protected final ClimberArmSubsystem rightArm;

    @Inject
    public BaseDoubleClimberArmCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm){
        this.leftArm = leftArm;
        this.rightArm = rightArm;
        this.addRequirements(leftArm, rightArm);
    }
    
}
