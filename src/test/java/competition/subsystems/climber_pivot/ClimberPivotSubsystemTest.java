package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;


public class ClimberPivotSubsystemTest extends BaseCompetitionTest {
    @Test
    public void testingPivotIn(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        
        climberPivotSubsystem.pivot.setForward();
        assertTrue("Latch starts pivoted out", climberPivotSubsystem.pivot.getIsForward());
        climberPivotSubsystem.pivotIn();
        assertTrue("Latch pivots in", climberPivotSubsystem.pivot.getIsReverse());
    }

    @Test
    public void testingPivotOut(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);

        assertTrue("Latch is off", climberPivotSubsystem.pivot.getIsOff());

        climberPivotSubsystem.pivotOut();

        assertTrue("Latch is pivoted out", climberPivotSubsystem.pivot.getIsReverse());
    }

    @Test
    public void testingPivotInandOut(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        
        climberPivotSubsystem.pivot.setForward();
        assertTrue("Latch starts pivoted out", climberPivotSubsystem.pivot.getIsForward());
        climberPivotSubsystem.pivotIn();
        assertTrue("Latch starts pivoted in", !climberPivotSubsystem.pivot.getIsReverse());
        climberPivotSubsystem.pivotOut();
        assertTrue("Latch is pivoted out again", climberPivotSubsystem.pivot.getIsForward());

    }
}
