package competition.subsystems.vision.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class ShooterRPMWithVisionCommandTest extends BaseCompetitionTest {

    private ShooterRPMWithVisionCommand command;

    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(ShooterRPMWithVisionCommand.class);
    }

    @Test
    public void testSpeedFromPitchLowWhenInBounds() {
        assertEquals(2005.24, this.command.speedFromPitchLow(-1.4), 0.001);
        assertEquals(1645.0, this.command.speedFromPitchLow(10), 0.001);
        assertEquals(1265.8, this.command.speedFromPitchLow(22), 0.001);
    }

    @Test
    public void testSpeedFromPitchHighWhenInBounds() {
        assertEquals(3272.8, this.command.speedFromPitchHigh(-1), 0.001);
        assertEquals(3028.8, this.command.speedFromPitchHigh(4), 0.001);
        assertEquals(2784.8, this.command.speedFromPitchHigh(9), 0.001);
    }

}
