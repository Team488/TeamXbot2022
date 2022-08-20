package competition.subsystems.deploy_hood.commands;

import javax.inject.Inject;

import competition.subsystems.deploy_hood.HoodSubsystem;
import xbot.common.command.BaseCommand;

public class HoodRetractCommand extends BaseCommand{
    final HoodSubsystem hoodDeploy;

    @Inject
    public HoodRetractCommand (HoodSubsystem hoodDeploy) {
        this.hoodDeploy = hoodDeploy;
        addRequirements(hoodDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        hoodDeploy.retract();
    }
    
}
