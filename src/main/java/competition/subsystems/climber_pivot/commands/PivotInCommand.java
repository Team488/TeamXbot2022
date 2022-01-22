package competition.subsystems.climber_pivot.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import xbot.common.command.BaseCommand;


public class PivotInCommand extends BaseCommand{
    final ClimberPivotSubsystem pivot;
    
    @Inject
    public PivotInCommand(ClimberPivotSubsystem pivot){
        this.pivot = pivot;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        pivot.pivotIn();
    }
    
}
