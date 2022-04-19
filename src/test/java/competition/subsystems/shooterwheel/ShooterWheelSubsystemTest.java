package competition.subsystems.shooterwheel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class ShooterWheelSubsystemTest extends BaseCompetitionTest {
    
    @Test
    public void testSpin() {
        ShooterWheelSubsystem shooterWheel = this.injector.getInstance(ShooterWheelSubsystem.class);
        shooterWheel.setPower(0.5);
      
        assertEquals(0.5, shooterWheel.leader.get(), 0.01);
    }

    @Test
    public void testStop() {
        ShooterWheelSubsystem shooterWheel = this.injector.getInstance(ShooterWheelSubsystem.class);
        shooterWheel.changeTargetRPM(100);
        shooterWheel.stop();

        assertEquals(0, shooterWheel.leader.get(), 0.001);
    }
    
    @Test
    public void testSpeedFromPitchWithCustomFF() {
        ShooterWheelSubsystem shooterWheel = this.injector.getInstance(ShooterWheelSubsystem.class);

        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(0), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3000), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3100), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3200), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3300), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3400), 0.000001);
        assertEquals(1.85E-4, shooterWheel.feedForwardFromTargetRPM(3500), 0.000001);
    }
}