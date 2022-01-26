package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class DriveSubsystemTest extends BaseCompetitionTest {
    
    @Test
    public void testSwerveModuleInjection(){
        DriveSubsystem driveSubsystem = this.injector.getInstance(DriveSubsystem.class);

        assertNotSame(
            driveSubsystem.getFrontLeftSwerveModuleSubsystem(),
            driveSubsystem.getFrontRightSwerveModuleSubsystem());
        assertEquals("SwerveModuleSubsystem/FrontLeftDrive/", driveSubsystem.getFrontLeftSwerveModuleSubsystem().getPrefix());
        assertEquals("SwerveModuleSubsystem/FrontRightDrive/", driveSubsystem.getFrontRightSwerveModuleSubsystem().getPrefix());
        assertEquals("SwerveModuleSubsystem/RearLeftDrive/", driveSubsystem.getRearLeftSwerveModuleSubsystem().getPrefix());
        assertEquals("SwerveModuleSubsystem/RearRightDrive/", driveSubsystem.getRearRightSwerveModuleSubsystem().getPrefix());

        assertNotSame(
            driveSubsystem.getFrontLeftSwerveModuleSubsystem().getRotationSubsystem(),
            driveSubsystem.getFrontRightSwerveModuleSubsystem().getRotationSubsystem());
        assertEquals("SwerveRotationSubsystem/FrontLeftDrive/", driveSubsystem.getFrontLeftSwerveModuleSubsystem().getRotationSubsystem().getPrefix());
        assertEquals("SwerveRotationSubsystem/FrontRightDrive/", driveSubsystem.getFrontRightSwerveModuleSubsystem().getRotationSubsystem().getPrefix());
        assertEquals("SwerveRotationSubsystem/RearLeftDrive/", driveSubsystem.getRearLeftSwerveModuleSubsystem().getRotationSubsystem().getPrefix());
        assertEquals("SwerveRotationSubsystem/RearRightDrive/", driveSubsystem.getRearRightSwerveModuleSubsystem().getRotationSubsystem().getPrefix());

        assertNotSame(
            driveSubsystem.getFrontLeftSwerveModuleSubsystem().getDriveSubsystem(),
            driveSubsystem.getFrontRightSwerveModuleSubsystem().getDriveSubsystem());
        assertEquals("SwerveDriveSubsystem/FrontLeftDrive/", driveSubsystem.getFrontLeftSwerveModuleSubsystem().getDriveSubsystem().getPrefix());
        assertEquals("SwerveDriveSubsystem/FrontRightDrive/", driveSubsystem.getFrontRightSwerveModuleSubsystem().getDriveSubsystem().getPrefix());
        assertEquals("SwerveDriveSubsystem/RearLeftDrive/", driveSubsystem.getRearLeftSwerveModuleSubsystem().getDriveSubsystem().getPrefix());
        assertEquals("SwerveDriveSubsystem/RearRightDrive/", driveSubsystem.getRearRightSwerveModuleSubsystem().getDriveSubsystem().getPrefix());

        DriveSubsystem anotheDriveSubsystem = this.injector.getInstance(DriveSubsystem.class);
        assertSame(
            driveSubsystem,
            anotheDriveSubsystem);
        assertSame(
            driveSubsystem.getFrontLeftSwerveModuleSubsystem(),
            anotheDriveSubsystem.getFrontLeftSwerveModuleSubsystem());
        assertSame(
            driveSubsystem.getFrontLeftSwerveModuleSubsystem().getRotationSubsystem(),
            anotheDriveSubsystem.getFrontLeftSwerveModuleSubsystem().getRotationSubsystem());
            assertSame(
                driveSubsystem.getFrontLeftSwerveModuleSubsystem().getDriveSubsystem(),
                anotheDriveSubsystem.getFrontLeftSwerveModuleSubsystem().getDriveSubsystem());
    }

}
