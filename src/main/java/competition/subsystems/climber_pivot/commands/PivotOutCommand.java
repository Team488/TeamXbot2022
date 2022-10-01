package competition.subsystems.climber_pivot.commands;

import javax.inject.Inject;

import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import xbot.common.command.BaseCommand;

public class PivotOutCommand extends BaseCommand{
    final ClimberPivotSubsystem pivot;
    
    @Inject
    public PivotOutCommand(ClimberPivotSubsystem pivot){
        this.pivot = pivot;
        addRequirements(pivot);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        pivot.pivotOut();
    }
    
}
