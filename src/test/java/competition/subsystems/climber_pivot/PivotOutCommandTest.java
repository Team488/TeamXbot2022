package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;


public class PivotOutCommandTest extends BaseCompetitionTest{

    @Test
    public void testingPivotIn(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotOutCommand PivotOutCommand = this.injector.getInstance(PivotOutCommand.class);

        climberPivotSubsystem.pivot.setOn(true);
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());
        PivotOutCommand.initialize();
        PivotOutCommand.execute();
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());

    }

    @Test
    public void testingPivotInDouble(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotOutCommand pivotOutCommand = this.injector.getInstance(PivotOutCommand.class);

        climberPivotSubsystem.pivot.setOn(true);
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());
        pivotOutCommand.initialize();
        pivotOutCommand.execute();
        assertTrue("Stays pivoted out", climberPivotSubsystem.pivot.getAdjusted());

    }
    
}
