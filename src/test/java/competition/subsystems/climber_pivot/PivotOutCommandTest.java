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

        climberPivotSubsystem.pivot.setOn(false);
        assertTrue("Pivoted in", !climberPivotSubsystem.pivot.getAdjusted());
        pivotOutCommand.initialize();
        pivotOutCommand.execute();
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());
    }

    @Test
    public void testingPivotOutDouble(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotOutCommand pivotOutCommand = this.injector.getInstance(PivotOutCommand.class);

        climberPivotSubsystem.pivot.setOn(true);
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());
        pivotOutCommand.initialize();
        pivotOutCommand.execute();
        assertTrue("Stays pivoted out", climberPivotSubsystem.pivot.getAdjusted());

    }
    
}
