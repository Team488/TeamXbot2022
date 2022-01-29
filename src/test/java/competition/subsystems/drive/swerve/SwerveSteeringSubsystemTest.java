package competition.subsystems.drive.swerve;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.controls.sensors.mock_adapters.MockAbsoluteEncoder;

public class SwerveSteeringSubsystemTest extends BaseCompetitionTest {
    
    @Test
    public void testGetSetTargetValue() {
        SwerveSteeringSubsystem swerveSteeringSubsystem = this.injector.getInstance(DriveSubsystem.class)
            .getFrontLeftSwerveModuleSubsystem()
            .getSteeringSubsystem();
            
        assertEquals(0.0, swerveSteeringSubsystem.getTargetValue(), 0.001);

        swerveSteeringSubsystem.setTargetValue(1.0);

        assertEquals(1.0, swerveSteeringSubsystem.getTargetValue(), 0.001);
    }

    @Test
    public void testReachTargetValue() {
        SwerveSteeringSubsystem swerveSteeringSubsystem = this.injector.getInstance(DriveSubsystem.class)
            .getFrontLeftSwerveModuleSubsystem()
            .getSteeringSubsystem();
        MockAbsoluteEncoder mockEncoder = (MockAbsoluteEncoder) swerveSteeringSubsystem.getEncoder();
            
        double targetValue = 45.0;

        swerveSteeringSubsystem.periodic();

        assertEquals(0.0, swerveSteeringSubsystem.getTargetValue(), 0.001);
        assertEquals(0.0, swerveSteeringSubsystem.getSparkMax().get(), 0.001);

        swerveSteeringSubsystem.setTargetValue(targetValue);
        swerveSteeringSubsystem.periodic();

        assertEquals(targetValue, swerveSteeringSubsystem.getTargetValue(), 0.001);
        assertEquals(-0.1, swerveSteeringSubsystem.getSparkMax().get(), 0.001);

        mockEncoder.setAbsolutePosition(targetValue);
        swerveSteeringSubsystem.periodic();

        assertEquals(targetValue, swerveSteeringSubsystem.getCurrentValue(), 0.001);
        assertEquals(0.0, swerveSteeringSubsystem.getSparkMax().get(), 0.001);
    }
}
