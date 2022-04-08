package competition.subsystems.vision;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class VisionShooterGoalsSubsystemTest extends BaseCompetitionTest {

    private VisionShooterGoalsSubsystem command;

    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(VisionShooterGoalsSubsystem.class);
        
        this.command.setLowGoalBelowMinPitchSlope(VisionShooterGoalsSubsystem.LOW_GOAL_SLOPE);
        this.command.setLowGoalAboveMaxPitchSlope(VisionShooterGoalsSubsystem.LOW_GOAL_SLOPE);
        this.command.setLowMinPitch(VisionShooterGoalsSubsystem.LOW_MIN_PITCH);
        this.command.setLowMaxPitch(VisionShooterGoalsSubsystem.LOW_MAX_PITCH);
        
        this.command.setHighGoalBelowMinPitchSlope(VisionShooterGoalsSubsystem.HIGH_GOAL_SLOPE);
        this.command.setHighGoalAboveMaxPitchSlope(VisionShooterGoalsSubsystem.HIGH_GOAL_SLOPE);
        this.command.setHighMinPitch(VisionShooterGoalsSubsystem.HIGH_MIN_PITCH);
        this.command.setHighMaxPitch(VisionShooterGoalsSubsystem.HIGH_MAX_PITCH);
        this.command.setHighGoalBelowMinPitchSlope(VisionShooterGoalsSubsystem.HIGH_GOAL_SLOPE);
        this.command.setHighGoalAboveMaxPitchSlope(VisionShooterGoalsSubsystem.HIGH_GOAL_SLOPE);
        this.command.setHighMinPitch(VisionShooterGoalsSubsystem.HIGH_MIN_PITCH);
        this.command.setHighMaxPitch(VisionShooterGoalsSubsystem.HIGH_MAX_PITCH);
    }

    @Test
    public void testSpeedFromPitchLowWithDefaultProps() {
        assertEquals(2005.24, this.command.speedFromPitchLow(-1.4), 0.001);
        assertEquals(1645.0, this.command.speedFromPitchLow(10), 0.001);
        assertEquals(1265.8, this.command.speedFromPitchLow(22), 0.001);

        // known to be unreliable speeds
        assertEquals(2119.0, this.command.speedFromPitchLow(-5.0), 0.001);
        assertEquals(1171.0, this.command.speedFromPitchLow(25), 0.001);
    }

    @Test
    public void testSpeedFromPitchHighWithDefaultProps() {
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
    public void testSpeedFromPitchLowWithNonDefaultOutOfBoundsSlopeAndRange() {
        this.command.setLowGoalBelowMinPitchSlope(10);
        this.command.setLowGoalAboveMaxPitchSlope(50);
        this.command.setLowMinPitch(0);
        this.command.setLowMaxPitch(20);

        assertEquals(1937.0, this.command.speedFromPitchLow(-2.4), 0.001);
        assertEquals(1479.0, this.command.speedFromPitchLow(23), 0.001);
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

    @Test
    public void testSpeedFromPitchHighWithNonDefaultOutOfBoundsSlopeAndRange() {
        this.command.setHighGoalBelowMinPitchSlope(50);
        this.command.setHighGoalAboveMaxPitchSlope(10);
        this.command.setHighMinPitch(0);
        this.command.setHighMaxPitch(8);

        assertEquals(3124.0, this.command.speedFromPitchHigh(-2), 0.001);
        assertEquals(2853.6, this.command.speedFromPitchHigh(10), 0.001);
    }
}
