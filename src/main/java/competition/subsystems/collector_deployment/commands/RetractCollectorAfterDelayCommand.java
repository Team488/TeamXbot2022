package competition.subsystems.collector_deployment.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XTimer;

public class RetractCollectorAfterDelayCommand extends BaseCommand {

    CollectorDeploymentSubsystem collectorDeployment;
    double initializationTime = 100;

    @Inject
    public RetractCollectorAfterDelayCommand(CollectorDeploymentSubsystem collectorDeployment) {
        this.collectorDeployment = collectorDeployment;
        addRequirements(collectorDeployment);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        initializationTime = XTimer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (XTimer.getFPGATimestamp() - initializationTime > 2.0) {
            collectorDeployment.retract();
        }
    }
}