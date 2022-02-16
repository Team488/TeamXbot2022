package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector.CollectorSubsystem;

public class IntakeCommandTest extends BaseCompetitionTest {

    @Test
    public void testingIntakeCommand (){
    IntakeCommand intakeCommand = this.injector.getInstance(IntakeCommand.class);
    CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

    collectorSubsystem.collectorMotor.set(-1);
    assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    intakeCommand.initialize();
    intakeCommand.execute();
    assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingDoubleIntakeCommand (){
    IntakeCommand intakeCommand = this.injector.getInstance(IntakeCommand.class);
    CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

    intakeCommand.initialize();
    intakeCommand.execute();
    assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
    intakeCommand.initialize();
    intakeCommand.execute();
    assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

}