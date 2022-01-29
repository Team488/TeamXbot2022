package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;

public class MotorArmStopCommand extends BaseCommand{
    public ClimberArmSubsystem armStop;

    @Inject
    public MotorArmStopCommand(ClimberArmSubsystem armStop){
        armStop = this.armStop;
        
    }
    @Override
    public void initialize() {
        // 
        log.info("Initializing");
    }

    @Override
    public void execute() {
        // 
        armStop.motorExtend(0);
    }

}