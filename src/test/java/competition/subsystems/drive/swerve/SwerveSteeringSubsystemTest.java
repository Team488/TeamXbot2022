package competition.subsystems.drive.swerve;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;

public class SwerveSteeringSubsystemTest extends BaseCompetitionTest {
    
    @Test
    public void testGetSetTargetValue() {
        SwerveSteeringSubsystem swerveSteeringSubsystem = this.injector.getInstance(DriveSubsystem.class)
            .getFrontLeftSwerveModuleSubsystem()
            .getSteeringSubsystem();
            
        assertEquals(swerveSteeringSubsystem.getTargetValue(), 0.0, 0.001);

        swerveSteeringSubsystem.setTargetValue(1.0);

        assertEquals(swerveSteeringSubsystem.getTargetValue(), 1.0, 0.001);
    }
}
