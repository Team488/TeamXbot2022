package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;


public class ClimberPivotSubsystemTest extends BaseCompetitionTest {
    @Test
    public void testingPivotIn(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        
        climberPivotSubsystem.pivot.setOn(true);
        assertTrue("Latch starts pivoted out", !climberPivotSubsystem.pivot.getAdjusted());
        climberPivotSubsystem.pivotIn();
        assertTrue("Latch starts pivoted out", !climberPivotSubsystem.pivot.getAdjusted());
    }

    @Test
    public void testingPivotOut(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);

        assertTrue("Latch is pivoted in", !climberPivotSubsystem.pivot.getAdjusted());

        climberPivotSubsystem.pivotOut();

        assertTrue("Latch is pivoted out", climberPivotSubsystem.pivot.getAdjusted());
    }

    @Test
    public void testingPivotInandOut(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        
        climberPivotSubsystem.pivot.setOn(true);
        assertTrue("Latch starts pivoted out", climberPivotSubsystem.pivot.getAdjusted());
        climberPivotSubsystem.pivotIn();
        assertTrue("Latch starts pivoted in", !climberPivotSubsystem.pivot.getAdjusted());
        climberPivotSubsystem.pivotOut();
        assertTrue("Latch is pivoted out again", climberPivotSubsystem.pivot.getAdjusted());

    }
}
