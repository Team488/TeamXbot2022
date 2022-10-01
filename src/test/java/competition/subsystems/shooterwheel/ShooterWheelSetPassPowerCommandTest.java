package competition.subsystems.shooterwheel;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import competition.BaseCompetitionTest;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shooterwheel.commands.ShooterWheelSetPassPowerCommand;

@RunWith(Parameterized.class)
public class ShooterWheelSetPassPowerCommandTest extends BaseCompetitionTest {
    
    @Parameter(0)
    public double currentFieldY;

    @Parameter(1)
    public double expectedTargetRpm;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
            { 0, 2000 },
            { 200, 2000 },
            { 250, 1750 },
            { 300, 1500 },
            { 350, 1250 },
            { 400, 1000 },
            { 500, 1000 }
        });
    }

    @Test
    public void testCorrectTargetRpm() {
        PoseSubsystem pose = (PoseSubsystem)getInjectorComponent().poseSubsystem();
        ShooterWheelSubsystem shooter = getInjectorComponent().shooterWheelSubsystem();
        ShooterWheelSetPassPowerCommand command = getInjectorComponent().shooterWheelSetPassPowerCommand();

        pose.setCurrentPosition(0, this.currentFieldY);

        command.execute();
        double actualTargetRpm = shooter.getTargetRPM();

        assertEquals(this.expectedTargetRpm, actualTargetRpm, 0.01);
    }
}