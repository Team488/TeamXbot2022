package competition.subsystems.drive.swerve.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.MockXboxControllerAdapter;
import xbot.common.controls.sensors.mock_adapters.MockAbsoluteEncoder;
import xbot.common.math.XYPair;

public class SwerveDriveWithJoysticksCommandTest extends BaseCompetitionTest {

    SwerveDriveWithJoysticksCommand command;
    DriveSubsystem drive;
    OperatorInterface oi;
    PoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(SwerveDriveWithJoysticksCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
        oi = injector.getInstance(OperatorInterface.class);
        pose = injector.getInstance(PoseSubsystem.class);

        pose.setCurrentHeading(90);

        ((MockAbsoluteEncoder)(drive.getFrontLeftSwerveModuleSubsystem().getSteeringSubsystem().getEncoder())).setAbsolutePosition(90);
        ((MockAbsoluteEncoder)(drive.getFrontRightSwerveModuleSubsystem().getSteeringSubsystem().getEncoder())).setAbsolutePosition(90);
        ((MockAbsoluteEncoder)(drive.getRearLeftSwerveModuleSubsystem().getSteeringSubsystem().getEncoder())).setAbsolutePosition(90);
        ((MockAbsoluteEncoder)(drive.getRearRightSwerveModuleSubsystem().getSteeringSubsystem().getEncoder())).setAbsolutePosition(90);
    }

    @Test
    public void simpleTest() {
        ((MockXboxControllerAdapter)oi.driverGamepad).setLeftStick(new XYPair(0, 1));

        command.initialize();
        command.execute();

        checkAllModuleAngle(90);
        checkAllModulePower(1);

        ((MockXboxControllerAdapter)oi.driverGamepad).setLeftStick(new XYPair(1, 1));

        command.execute();

        checkAllModuleAngle(45);
        checkAllModulePower(1);
    }

    @Test
    public void testAbsoluteHeading() {
        ((MockXboxControllerAdapter)oi.driverGamepad).setLeftStick(new XYPair(0, 0));
        ((MockXboxControllerAdapter)oi.driverGamepad).setRightStick(new XYPair(0, 1));
        command.setAbsoluteHeadingMode(true);

        // at this point, a command of 0,1 should be interpreted as a goal of 90 degrees - meaning no motion,
        // since the robot is already at 90 degrees.
        command.initialize();
        //command.execute();

        //checkAllModulePower(0);

        // Point the joystick to 0 degrees
        ((MockXboxControllerAdapter)oi.driverGamepad).setRawRightStick(new XYPair(1, 0));
        command.execute();

        // Check for right turn (since we started at 90)
        checkModulesTurning(false);

        // Point the joystick to 180 degrees
        ((MockXboxControllerAdapter)oi.driverGamepad).setRawRightStick(new XYPair(-1, 0));
        command.execute();

        checkModulesTurning(true);

        // Check that we preserve the last high-magnitude value (in this case, the left turn).
        ((MockXboxControllerAdapter)oi.driverGamepad).setRawRightStick(new XYPair(0, 0));
        command.execute();
        checkModulesTurning(true);
    }

    private void checkAllModuleAngle(double angle) {
        assertEquals(angle, drive.getFrontLeftSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getFrontRightSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getRearLeftSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
        assertEquals(angle, drive.getRearRightSwerveModuleSubsystem().getTargetState().angle.getDegrees(), 0.001);
    }

    private void checkAllModulePower(double power) {
        assertEquals(power, drive.getFrontLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond
                * PoseSubsystem.INCHES_IN_A_METER / drive.getMaxTargetSpeedInchesPerSecond(), 0.001);
        assertEquals(power, drive.getFrontRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond
                * PoseSubsystem.INCHES_IN_A_METER / drive.getMaxTargetSpeedInchesPerSecond(), 0.001);
        assertEquals(power, drive.getRearLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond
                * PoseSubsystem.INCHES_IN_A_METER / drive.getMaxTargetSpeedInchesPerSecond(), 0.001);
        assertEquals(power, drive.getRearRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond
                * PoseSubsystem.INCHES_IN_A_METER / drive.getMaxTargetSpeedInchesPerSecond(), 0.001);
    }

    private void checkModulesTurning(boolean turningLeft) {
        assertTrue(turningLeft == drive.getFrontLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond < 0);
        assertTrue(turningLeft == drive.getFrontRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
        assertTrue(turningLeft == drive.getRearLeftSwerveModuleSubsystem().getTargetState().speedMetersPerSecond < 0);
        assertTrue(turningLeft == drive.getRearRightSwerveModuleSubsystem().getTargetState().speedMetersPerSecond > 0);
    }
}