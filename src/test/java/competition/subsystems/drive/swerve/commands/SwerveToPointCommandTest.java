package competition.subsystems.drive.swerve.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.XYPair;

public class SwerveToPointCommandTest extends BaseCompetitionTest {
    
    SwerveToPointCommand command;
    DriveSubsystem drive;
    PoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(SwerveToPointCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(PoseSubsystem.class);
    }

    @Test
    public void forwardTest() {

        pose.setCurrentPosition(0, 0);
        pose.setCurrentHeading(90);
        command.setTargetPosition(new XYPair(100, 100));
        command.initialize();
        command.execute();

        testAllModuleAngle(45);
        testAllModulesGoingForward(true);

        pose.setCurrentPosition(100, 0);
        command.execute();

        testAllModuleAngle(90);
        testAllModulesGoingForward(true);

        pose.setCurrentPosition(100, 150);
        command.execute();

        testAllModuleAngle(90);
        testAllModulesGoingForward(false);
    }

    private void testAllModuleAngle(double angle) {
        assertEquals(angle, drive.getFrontLeftSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getFrontRightSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getRearLeftSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getRearRightSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
    }

    private void testAllModulesGoingForward(boolean forward) {
        assertEquals(forward, drive.getFrontLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
        assertEquals(forward, drive.getFrontRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
        assertEquals(forward, drive.getRearLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
        assertEquals(forward, drive.getRearRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
    }
}
