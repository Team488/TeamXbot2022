package competition.subsystems.drive.swerve.commands;

import org.junit.Test;

import competition.subsystems.drive.commands.SwerveToPointCommand;
import xbot.common.math.XYPair;

public class SwerveToPointCommandTest extends BaseFullSwerveTest {
    
    SwerveToPointCommand command;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(SwerveToPointCommand.class);
    }

    @Test
    public void multiplePositionsTest() {

        pose.setCurrentPosition(0, 0);
        pose.setCurrentHeading(90);
        command.setTargetPosition(new XYPair(100, 100), 90);
        command.initialize();
        command.execute();

        checkAllModuleAngle(45);
        checkAllModulesGoingForward(true);

        pose.setCurrentPosition(100, 0);
        command.execute();

        checkAllModuleAngle(90);
        checkAllModulesGoingForward(true);

        pose.setCurrentPosition(100, 150);
        command.execute();

        checkAllModuleAngle(-90);
        checkAllModulesGoingForward(true);
    }

    @Test
    public void robotSimpleRelativeMotionTest() {
        command.setRobotRelativeMotion();
        command.setTargetPosition(new XYPair(0, 60), 90);

        command.initialize();
        command.execute();

        checkAllModulesGoingForward(true);

        command.setTargetPosition(new XYPair(0,0), 0);

        command.initialize();
        command.execute();

        checkRobotTurning(false);
    }

    @Test
    public void robotOffsetRelativeMotionTest() {

        pose.setCurrentHeading(-45); 
        setAllSteeringModuleAngles(-45);

        command.setRobotRelativeMotion();
        command.setTargetPosition(new XYPair(0, 60), 90);

        command.initialize();
        command.execute();

        checkAllModulesGoingForward(true);

        command.setTargetPosition(new XYPair(0,0), 0);
        setAllSteeringModuleAngles(90);

        command.initialize();
        command.execute();

        checkRobotTurning(false);
    }

}
