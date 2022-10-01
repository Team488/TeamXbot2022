package competition.subsystems.collector_deployment.commands;

import javax.inject.Inject;

import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import xbot.common.command.BaseCommand;

public class DeployCollectorCommand extends BaseCommand{
    final CollectorDeploymentSubsystem deploy;

    @Inject
    public DeployCollectorCommand(CollectorDeploymentSubsystem deploy){
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
