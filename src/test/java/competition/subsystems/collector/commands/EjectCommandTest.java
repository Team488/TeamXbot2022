package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class EjectCommandTest extends BaseCompetitionTest{
    @Test
    public void testingEjectCommand (){
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();
        EjectCommand ejectCommand = getInjectorComponent().ejectCommand();

        collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, 1);
        checkCollectorPower(1);
        ejectCommand.initialize();
        ejectCommand.execute();
        checkCollectorPower(-1);
    }

    @Test
    public void testingDoubleEjectCommand (){
        EjectCommand ejectCommand = getInjectorComponent().ejectCommand();

        ejectCommand.initialize();
        ejectCommand.execute();
        checkCollectorPower(-1);
        ejectCommand.initialize();
        ejectCommand.execute();
        checkCollectorPower(-1);
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();
        MockCANTalon mockMotor = (MockCANTalon) collectorSubsystem.collectorMotor;

        assertEquals(power, mockMotor.getMotorOutputPercent(), 0.001);
    }
}
