package competition.subsystems.deploy_hood.commands;

import com.google.inject.Inject;

import competition.subsystems.deploy_hood.HoodSubsystem;
import xbot.common.command.BaseCommand;

public class HoodDeployCommand extends BaseCommand{
    final HoodSubsystem hoodDeploy;
    
    @Inject
    public HoodDeployCommand (HoodSubsystem hoodDeploy) {
        this.hoodDeploy = hoodDeploy;
        addRequirements(hoodDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        hoodDeploy.deploy();
        
    }
    
}
