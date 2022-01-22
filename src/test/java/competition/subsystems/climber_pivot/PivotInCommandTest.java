package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import competition.subsystems.latch.LatchSubsystem;
import competition.BaseCompetitionTest;
public class PivotInCommandTest extends BaseCompetitionTest{
    @Test
    public void testingPivotIn(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotInCommand pivotInCommand = this.injector.getInstance(PivotInCommand.class);

        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getAdjusted());
        pivotInCommand.initialize();
        pivotInCommand.execute();
        assertTrue("Pivoted in", !climberPivotSubsystem.pivot.getAdjusted());

    }

    @Test
    public void testingPivotInDouble(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotInCommand pivotInCommand = this.injector.getInstance(PivotInCommand.class);

        climberPivotSubsystem.pivot.setOn(false);
        assertTrue("Pivoted in", !climberPivotSubsystem.pivot.getAdjusted());
        pivotInCommand.initialize();
        pivotInCommand.execute();
        assertTrue("Stays pivoted in", !climberPivotSubsystem.pivot.getAdjusted());

    }
    
}
