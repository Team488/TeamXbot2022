package competition.subsystems.collector.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.command.BaseCommand;

public class EjectCommand extends BaseCommand{
    final CollectorSubsystem collector;

    @Inject
    public EjectCommand(CollectorSubsystem collector){
        this.collector = collector;
        addRequirements(collector);
    }

    @Override
    public void initialize() {
        log.info("Initializing");        
    }

    @Override
    public void execute() {
        collector.eject();        
    }

}
