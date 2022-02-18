package competition.subsystems.collector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class CollectorSubsystemTest extends BaseCompetitionTest{
    @Test
    public void testingBasicCollectorSubsystem (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(1);
        checkCollectorPower(1);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
        collectorSubsystem.intake();
        checkCollectorPower(1);
        collectorSubsystem.stop();
        checkCollectorPower(0);
        collectorSubsystem.eject();
        assertEquals(-1, collectorSubsystem.collectorMotor.get(), 0.001);
    }

    @Test
    public void testingEject (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(1);
        checkCollectorPower(1);
        collectorSubsystem.eject();
        checkCollectorPower(-1);
        collectorSubsystem.stop();
        checkCollectorPower(0);
        collectorSubsystem.eject();
        checkCollectorPower(-1);
    }

    @Test
    public void testingIntake (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(-1);
        checkCollectorPower(-1);
        collectorSubsystem.intake();
        checkCollectorPower(1);
        collectorSubsystem.stop();
        checkCollectorPower(0);
        collectorSubsystem.intake();
        checkCollectorPower(1);
    }

    @Test
    public void testingCollectorStop (){
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);


        collectorSubsystem.intake();
        checkCollectorPower(1);
        collectorSubsystem.stop();
        checkCollectorPower(0);
        collectorSubsystem.eject();
        checkCollectorPower(-1);
        collectorSubsystem.stop();
        checkCollectorPower(0);
        collectorSubsystem.stop();
        checkCollectorPower(0);
           
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);

        assertEquals(power, collectorSubsystem.collectorMotor.get(), 0.001);
    }
}
