package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.junit.Test;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class StopCommandTest extends BaseCollectorTest {

    @Test
    public void testingStopWhileIntaking() {
        StopCollectorCommand stopCommand = getInjectorComponent().stopCollectorCommand();
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();

        collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, 1);
        checkCollectorPower(1);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    @Test
    public void stopWhenStopped() {
        StopCollectorCommand stopCommand = getInjectorComponent().stopCollectorCommand();
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();
        collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, 0);
        checkCollectorPower(0);
        stopCommand.initialize();
        stopCommand.execute();
        checkCollectorPower(0);
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();
        MockCANTalon mockMotor = (MockCANTalon) collectorSubsystem.collectorMotor;

        assertEquals(power, mockMotor.getMotorOutputPercent(), 0.001);
    }
}
