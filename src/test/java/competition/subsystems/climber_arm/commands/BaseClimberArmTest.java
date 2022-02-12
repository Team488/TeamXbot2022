package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import com.google.inject.Key;

import org.junit.Ignore;

import competition.BaseCompetitionTest;
import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

@Ignore
public class BaseClimberArmTest extends BaseCompetitionTest {
    
    protected ClimberArmSubsystem leftArm;
    protected ClimberArmSubsystem rightArm;
    protected OperatorInterface oi;

    @Override
    public void setUp() {
        super.setUp();

        leftArm = injector.getInstance(Key.get(ClimberArmSubsystem.class, LeftArm.class));
        rightArm = injector.getInstance(Key.get(ClimberArmSubsystem.class, RightArm.class));

        oi = injector.getInstance(OperatorInterface.class);
    }

    
    protected void checkArmPowers(double leftPower, double rightPower) {
        assertEquals(leftPower, leftArm.armMotor.get(), 0.01);
        assertEquals(rightPower, rightArm.armMotor.get(), 0.01);
    }

    protected boolean isLeftArmPowerLargerThanRightArmPower() {
        return leftArm.armMotor.get() > rightArm.armMotor.get();
    }
}
