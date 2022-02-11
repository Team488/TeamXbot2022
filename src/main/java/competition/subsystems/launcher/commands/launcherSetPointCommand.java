package competition.subsystems.launcher.commands;

import xbot.common.command.BaseSetpointCommand;
import xbot.common.command.SupportsSetpointLock;

public class LauncherSetPointCommandF extends BaseSetpointCommand{

    public LauncherSetPointCommand(SupportsSetpointLock system) {
        super(system);
    }

    @Override
    public void initialize() {
        log.info("Initializing");        
    }

    @Override
    public void execute() {
        
    }
    
}
