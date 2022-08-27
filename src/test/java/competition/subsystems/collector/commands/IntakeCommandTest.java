package competition.subsystems.collector.commands;

import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class IntakeCommandTest extends BaseCompetitionTest {

    @Test
    public void testingIntakeCommand (){
    IntakeCommand intakeCommand = getInjectorComponent().intakeCommand();
    CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();

    collectorSubsystem.collectorMotor.set(ControlMode.PercentOutput, -1);
    checkCollectorPower(-1);
    intakeCommand.initialize();
    intakeCommand.execute();
    checkCollectorPower(1);
    }

    @Test
    public void testingDoubleIntakeCommand (){
    IntakeCommand intakeCommand = getInjectorComponent().intakeCommand();

    intakeCommand.initialize();
    intakeCommand.execute();
    checkCollectorPower(1);
    intakeCommand.initialize();
    intakeCommand.execute();
    checkCollectorPower(1);
    }

    public void checkCollectorPower(double power) {
        CollectorSubsystem collectorSubsystem = getInjectorComponent().collectorSubsystem();
        MockCANTalon mockMotor = (MockCANTalon) collectorSubsystem.collectorMotor;

        assertEquals(power, mockMotor.getMotorOutputPercent(), 0.001);
    }

}