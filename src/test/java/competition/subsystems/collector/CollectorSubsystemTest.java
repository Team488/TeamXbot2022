package competition.subsystems.collector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class CollectorSubsystemTest extends BaseCompetitionTest{
    @Test
    public void testingBasicCollectorSubsystem (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(1);
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.intake();
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingEject (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(1);
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingIntake (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(-1);
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.intake();
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.intake();
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingCollectorStop (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);


        collectorSubsystem.intake();
        assertEquals(1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.stop();
        assertEquals(0, collectorSubsystem.collectorMotor.get(), 0.001);
           
    }
}
