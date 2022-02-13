package competition.subsystems.drive.swerve.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SetSteeringMotorControllerPidParametersCommand;
import competition.subsystems.drive.swerve.SwerveSteeringMotorPidSubsystem;

public class SetSteeringMotorControllerPidParametersCommandTest extends BaseCompetitionTest {
    
    private SetSteeringMotorControllerPidParametersCommand command;
    private SwerveSteeringMotorPidSubsystem configSubsystem;
    private DriveSubsystem driveSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(SetSteeringMotorControllerPidParametersCommand.class);
        this.configSubsystem = injector.getInstance(SwerveSteeringMotorPidSubsystem.class);
        this.driveSubsystem = injector.getInstance(DriveSubsystem.class);
    }

    @Test
    public void testCommand() {

        this.configSubsystem.setAllProperties(1, 2, 3, 4, 5, 6);

        this.command.initialize();
        this.command.execute();

        assertTrue("Command should be finished", this.command.isFinished());

        // Front left
        assertEquals("P should be set correctly", 1, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getP(), 0.001);
        assertEquals("I should be set correctly", 2, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getI(), 0.001);
        assertEquals("D should be set correctly", 3, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getD(), 0.001);
        assertEquals("FF should be set correctly", 4, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getFF(), 0.001);
        assertEquals("MinOutput should be set correctly", 5, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMin(), 0.001);
        assertEquals("MaxOutput should be set correctly", 6, this.driveSubsystem.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMax(), 0.001);

        // Front right
        assertEquals("P should be set correctly", 1, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getP(), 0.001);
        assertEquals("I should be set correctly", 2, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getI(), 0.001);
        assertEquals("D should be set correctly", 3, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getD(), 0.001);
        assertEquals("FF should be set correctly", 4, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getFF(), 0.001);
        assertEquals("MinOutput should be set correctly", 5, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMin(), 0.001);
        assertEquals("MaxOutput should be set correctly", 6, this.driveSubsystem.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMax(), 0.001);

        // Rear left
        assertEquals("P should be set correctly", 1, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getP(), 0.001);
        assertEquals("I should be set correctly", 2, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getI(), 0.001);
        assertEquals("D should be set correctly", 3, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getD(), 0.001);
        assertEquals("FF should be set correctly", 4, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getFF(), 0.001);
        assertEquals("MinOutput should be set correctly", 5, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMin(), 0.001);
        assertEquals("MaxOutput should be set correctly", 6, this.driveSubsystem.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMax(), 0.001);

        // Rear right
        assertEquals("P should be set correctly", 1, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getP(), 0.001);
        assertEquals("I should be set correctly", 2, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getI(), 0.001);
        assertEquals("D should be set correctly", 3, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getD(), 0.001);
        assertEquals("FF should be set correctly", 4, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getFF(), 0.001);
        assertEquals("MinOutput should be set correctly", 5, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMin(), 0.001);
        assertEquals("MaxOutput should be set correctly", 6, this.driveSubsystem.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getSparkMax().getOutputMax(), 0.001);

    }

}
