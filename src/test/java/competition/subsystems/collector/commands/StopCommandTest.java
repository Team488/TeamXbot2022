package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.junit.Test;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class StopCommandTest extends BaseCollectorTest {

    @Test
    public void testingStopWhileIntaking() {
        StopCollectorCommand stopCommand = injector.getInstance(StopCollectorCommand.class);
        CollectorSubsystem collectorSubsystem = injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, 1);
        checkCollectorPower(1);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    @Test
    public void stopWhenStopped() {
        StopCollectorCommand stopCommand = injector.getInstance(StopCollectorCommand.class);
        CollectorSubsystem collectorSubsystem = injector.getInstance(CollectorSubsystem.class);

        collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, 0);
        checkCollectorPower(0);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = this.injector.getInstance(CollectorSubsystem.class);
        MockCANTalon mockMotor = (MockCANTalon) collectorSubsystem.collectorMotor;

        assertEquals(power, mockMotor.getMotorOutputPercent(), 0.001);
    }
}
