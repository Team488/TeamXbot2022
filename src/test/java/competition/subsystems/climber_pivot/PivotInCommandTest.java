package competition.subsystems.climber_pivot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climber_pivot.commands.PivotInCommand;
import xbot.common.controls.actuators.XDoubleSolenoid.DoubleSolenoidMode;

public class PivotInCommandTest extends BaseCompetitionTest{
    @Test
    public void testingPivotIn(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotInCommand pivotInCommand = this.injector.getInstance(PivotInCommand.class);

        climberPivotSubsystem.pivot.setDoubleSolenoid(DoubleSolenoidMode.FORWARD);
        assertTrue("Pivoted out", climberPivotSubsystem.pivot.getIsForward());
        pivotInCommand.initialize();
        pivotInCommand.execute();
        assertTrue("Pivoted in", climberPivotSubsystem.pivot.getIsReverse());

    }

    @Test
    public void testingPivotInDouble(){
        ClimberPivotSubsystem climberPivotSubsystem = this.injector.getInstance(ClimberPivotSubsystem.class);
        PivotInCommand pivotInCommand = this.injector.getInstance(PivotInCommand.class);

        climberPivotSubsystem.pivot.setDoubleSolenoid(DoubleSolenoidMode.REVERSE);
        assertTrue("Pivoted in", climberPivotSubsystem.pivot.getIsReverse());
        pivotInCommand.initialize();
        pivotInCommand.execute();
        assertTrue("Stays pivoted in", climberPivotSubsystem.pivot.getIsReverse());

    }
    
}
