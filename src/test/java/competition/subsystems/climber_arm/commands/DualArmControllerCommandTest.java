package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.first.wpilibj.MockXboxControllerAdapter;

public class DualArmControllerCommandTest extends BaseClimberArmTest {
     
    DualArmControllerCommandWithJoysticks command;

    @Override
    public void setUp() {
        super.setUp();
        command = getInjectorComponent().dualArmControllerCommandWithJoysticks();
    }

    @Ignore
    @Test
    public void testSafeOperation() {
        command.setIsSafe(true);
        command.initialize();
        // Start not moving
        checkArmPowers(0, 0);

        // Move left joystick/arm
        ((MockXboxControllerAdapter)oi.operatorGamepad).setLeftStick(0, 0.5);
        command.execute();
        checkArmPowers(0.5, 0, 0.2);

        // Move the right joystick/arm
        ((MockXboxControllerAdapter)oi.operatorGamepad).setRightStick(0, 0.5);
        command.execute();
        checkArmPowers(0.5, 0.5, 0.2);

        // Move the arm way out of safe boundaries
        leftArm.armMotor.setPosition(1000);
        rightArm.armMotor.setPosition(1000);

        command.execute();
        checkArmPowers(0,0);
    }

    @Test
    public void testUnsafeOperation() {
        command.setIsSafe(false);
        command.initialize();
        // Start not moving
        checkArmPowers(0, 0);

        // Move left joystick/arm
        ((MockXboxControllerAdapter)oi.operatorGamepad).setLeftStick(0, 0.5);
        command.execute();
        checkArmPowers(0.5, 0, 0.2);

        // Move the right joystick/arm
        ((MockXboxControllerAdapter)oi.operatorGamepad).setRightStick(0, 0.5);
        command.execute();
        checkArmPowers(0.5, 0.5, 0.2);

        // Move the arm way out of safe boundaries
        leftArm.armMotor.setPosition(1000);
        rightArm.armMotor.setPosition(1000);

        command.execute();
        checkArmPowers(0.5, 0.5, 0.2);
    }

    protected void checkArmPowers(double leftPower, double rightPower, double tolerance) {
        assertEquals(leftPower, leftArm.armMotor.get(), tolerance);
        assertEquals(rightPower, rightArm.armMotor.get(), tolerance);
    }

    protected void checkArmPowers(double leftPower, double rightPower) {
        checkArmPowers(leftPower, rightPower, 0.001);
    }

}
