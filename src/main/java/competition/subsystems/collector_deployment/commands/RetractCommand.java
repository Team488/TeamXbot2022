package competition.subsystems.collector_deployment.commands;

import com.google.inject.Inject;

import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import xbot.common.command.BaseCommand;

public class RetractCommand extends BaseCommand{
    final CollectorDeploymentSubsystem deploy;

    @Inject
    public RetractCommand(CollectorDeploymentSubsystem deploy){
        this.deploy = deploy;
        addRequirements(deploy);
    }
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        deploy.retract();        
    }
    
}
