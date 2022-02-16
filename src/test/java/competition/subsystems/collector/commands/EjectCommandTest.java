package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector.CollectorSubsystem;

public class EjectCommandTest extends BaseCompetitionTest{
    @Test
    public void testingEjectCommand (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);
        EjectCommand ejectCommand = this.injector.getInstance(EjectCommand.class);

        collectorSubsystem.collectorMotor.set(1);
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        ejectCommand.initialize();
        ejectCommand.execute();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingDoubleEjectCommand (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);
        EjectCommand ejectCommand = this.injector.getInstance(EjectCommand.class);

        ejectCommand.initialize();
        ejectCommand.execute();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        ejectCommand.initialize();
        ejectCommand.execute();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    }
}
