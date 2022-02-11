package competition.subsystems.launcher.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.launcher.LauncherSubsystem;
import xbot.common.command.BaseCommand;

public class StopLauncherCommand extends BaseCommand{
    
    final LauncherSubsystem launch;
    final OperatorInterface oi;

    @Inject
    public StopLauncherCommand(OperatorInterface oi, LauncherSubsystem launch){
        this.oi = oi;
        this.launch = launch;
        this.addRequirements(this.launch);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
    }

    @Override
    public void execute() {
        launch.stop();
    }
    
}
