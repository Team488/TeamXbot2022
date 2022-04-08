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
    public void testSpeedFromPitchLowWithDefaultProps() {
        this.command.setLowGoalBelowMinPitchSlope(ShooterRPMWithVisionCommand.LOW_GOAL_SLOPE);
        this.command.setLowGoalAboveMaxPitchSlope(ShooterRPMWithVisionCommand.LOW_GOAL_SLOPE);

        assertEquals(2005.24, this.command.speedFromPitchLow(-1.4), 0.001);
        assertEquals(1645.0, this.command.speedFromPitchLow(10), 0.001);
        assertEquals(1265.8, this.command.speedFromPitchLow(22), 0.001);

        // known to be unreliable speeds
        assertEquals(2119.0, this.command.speedFromPitchLow(-5.0), 0.001);
        assertEquals(1171.0, this.command.speedFromPitchLow(25), 0.001);
    }

    @Test
    public void testSpeedFromPitchHighWithDefaultProps() {
        this.command.setHighGoalBelowMinPitchSlope(ShooterRPMWithVisionCommand.HIGH_GOAL_SLOPE);
        this.command.setHighGoalAboveMaxPitchSlope(ShooterRPMWithVisionCommand.HIGH_GOAL_SLOPE);

        assertEquals(3272.8, this.command.speedFromPitchHigh(-1), 0.001);
        assertEquals(3028.8, this.command.speedFromPitchHigh(4), 0.001);
        assertEquals(2784.8, this.command.speedFromPitchHigh(9), 0.001);

        // known to be unreliable speeds
        assertEquals(3370.4, this.command.speedFromPitchHigh(-3), 0.001);
        assertEquals(2638.4, this.command.speedFromPitchHigh(12), 0.001);
    }

    @Test
    public void testSpeedFromPitchLowWithZeroOutOfBoundsSlope() {
        this.command.setLowGoalBelowMinPitchSlope(0);
        this.command.setLowGoalAboveMaxPitchSlope(0);

        assertEquals(2005.24, this.command.speedFromPitchLow(-5.0), 0.001);
        assertEquals(1265.8, this.command.speedFromPitchLow(25), 0.001);
    }

    @Test
    public void testSpeedFromPitchLowWithNonDefaultOutOfBoundsSlope() {
        this.command.setLowGoalBelowMinPitchSlope(10);
        this.command.setLowGoalAboveMaxPitchSlope(50);

        assertEquals(1995.24, this.command.speedFromPitchLow(-2.4), 0.001);
        assertEquals(1315.8, this.command.speedFromPitchLow(23), 0.001);
    }
    
    @Test
    public void testSpeedFromPitchHighWithZeroOutOfBoundsSlope() {
        this.command.setHighGoalBelowMinPitchSlope(0);
        this.command.setHighGoalAboveMaxPitchSlope(0);

        assertEquals(3272.8, this.command.speedFromPitchHigh(-3), 0.001);
        assertEquals(2784.8, this.command.speedFromPitchHigh(12), 0.001);
    }
    
    @Test
    public void testSpeedFromPitchHighWithNonDefaultOutOfBoundsSlope() {
        this.command.setHighGoalBelowMinPitchSlope(50);
        this.command.setHighGoalAboveMaxPitchSlope(10);

        assertEquals(3222.8, this.command.speedFromPitchHigh(-2), 0.001);
        assertEquals(2794.8, this.command.speedFromPitchHigh(10), 0.001);
    }
}
