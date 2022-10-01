package competition.subsystems.climber_arm.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;

import competition.BaseCompetitionTest;
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

        leftArm = getInjectorComponent().armComponents().leftArm.subsystem();
        rightArm = getInjectorComponent().armComponents().rightArm.subsystem();

        oi = getInjectorComponent().operatorInterface();
    }

    
    protected void checkArmPowers(double leftPower, double rightPower) {
        assertEquals(leftPower, leftArm.armMotor.get(), 0.01);
        assertEquals(rightPower, rightArm.armMotor.get(), 0.01);
    }

    protected boolean isLeftArmPowerLargerThanRightArmPower() {
        return leftArm.armMotor.get() > rightArm.armMotor.get();
    }
}
