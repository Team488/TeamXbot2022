package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_pivot.commands.PivotOutCommand;


public class PivotOutCommandTest extends BaseCompetitionTest{

    @Test
    public void testingPivotOut(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotOutCommand pivotOutCommand = this.injector.getInstance(PivotOutCommand.class);

        climberPivotSubsystem.pivot.setReverse();
        assertTrue("Pivoted in", climberPivotSubsystem.pivot.getIsReverse());
        pivotOutCommand.initialize();
        pivotOutCommand.execute();
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getIsForward());
    }

    @Test
    public void testingPivotOutDouble(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotOutCommand pivotOutCommand = this.injector.getInstance(PivotOutCommand.class);

        climberPivotSubsystem.pivot.setForward();
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getIsForward());
        pivotOutCommand.initialize();
        pivotOutCommand.execute();
        assertTrue("Stays pivoted out", climberPivotSubsystem.pivot.getIsForward());

    }
    
}
