package competition.subsystems.drive.swerve.commands;

import static org.junit.Assert.assertTrue;

import com.google.inject.Key;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.injection.swerve.FrontLeftDrive;
import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;

public class SwerveDriveMaintainerCommandTest extends BaseCompetitionTest {
    
    SwerveDriveMaintainerCommand command;
    SwerveDriveSubsystem subsystem;

    @Override
    public void setUp() {
        super.setUp();
        subsystem = injector.getInstance(Key.get(SwerveDriveSubsystem.class, FrontLeftDrive.class));
        command = injector.getInstance(Key.get(SwerveDriveMaintainerCommand.class, FrontLeftDrive.class));
    }

    @Test
    public void testMaintain() {
        subsystem.setTargetValue(1);
        
        command.initialize();
        command.execute();

        assertTrue("Wheel should be rotating clockwise", subsystem.getSparkMax().get() < 0.1);
    }

}
