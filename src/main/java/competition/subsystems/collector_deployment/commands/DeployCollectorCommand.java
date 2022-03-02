package competition.subsystems.collector_deployment.commands;

import com.google.inject.Inject;

import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import xbot.common.command.BaseCommand;

public class DeployCommand extends BaseCommand{
    final CollectorDeploymentSubsystem deploy;

    @Inject
    public DeployCommand(CollectorDeploymentSubsystem deploy){
        this.deploy = deploy;
        addRequirements(deploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        deploy.deploy();
    }
    
}
