package competition.subsystems.drive.swerve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.revrobotics.CANSparkMax.ControlType;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.electrical_conract.TestElectricalContract;
import competition.electrical_contract.ElectricalContract;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANSparkMax;
import xbot.common.controls.sensors.mock_adapters.MockAbsoluteEncoder;

public class SwerveSteeringSubsystemTest extends BaseCompetitionTest {
    
    private TestElectricalContract contract;
    private SwerveSteeringSubsystem subsystem;
    private MockCANSparkMax motorController;

    @Override
    public void setUp() {
        super.setUp();
        this.contract = (TestElectricalContract)this.injector.getInstance(ElectricalContract.class);
        this.subsystem = this.injector.getInstance(DriveSubsystem.class)
            .getFrontLeftSwerveModuleSubsystem()
            .getSteeringSubsystem();
        this.motorController = (MockCANSparkMax)subsystem.getSparkMax();
    }

    @Test
    public void testGetSetTargetValue() {
        assertEquals(0.0, subsystem.getTargetValue(), 0.001);

        subsystem.setTargetValue(1.0);

        assertEquals(1.0, subsystem.getTargetValue(), 0.001);
    }

    @Test
    public void testGetBestMotorPositionInDegrees() {
        this.contract.setCanCodersReady(true);
        double position = subsystem.getBestEncoderPositionInDegrees();

        assertEquals("Position should start at zero", 0, position, 0.001);

        subsystem.getSparkMax().setPosition(999);
        position = subsystem.getBestEncoderPositionInDegrees();

        assertEquals("The SparkMAX position should not impact our reported position since XAbsoluteEncoder is available", 0, position, 0.001);

        ((MockAbsoluteEncoder)subsystem.getEncoder()).setAbsolutePosition(10);
        position = subsystem.getBestEncoderPositionInDegrees();

        assertEquals("The new encoder value should be reflected", 10, position, 0.001);

        contract.setCanCodersReady(false);
        position = subsystem.getBestEncoderPositionInDegrees();

        assertEquals("The SparkMAX position value should be reflected", 42.141, position, 0.001);
    }

    @Test
    public void testSetMotorControllerTarget() {
        assertNull("Motor controller PID should start unset", motorController.getControlType());

        subsystem.setMotorControllerPidTarget();

        assertEquals("Motor controller PID should be in position mode", ControlType.kPosition, motorController.getControlType());
        assertEquals("Motor controller target should be zero", 0, motorController.getReference(), 0.001);

        subsystem.setTargetValue(360);
        subsystem.setMotorControllerPidTarget();
        
        assertEquals("Motor controller target should still be zero", 0, motorController.getReference(), 0.001);

        subsystem.setTargetValue(90);
        subsystem.setMotorControllerPidTarget();
        
        assertEquals("Motor controller target should still be positive", 3.197, motorController.getReference(), 0.001);

        subsystem.setTargetValue(-90);
        subsystem.setMotorControllerPidTarget();
        
        assertEquals("Motor controller target should still be negative", -3.197, motorController.getReference(), 0.001);

        motorController.setPosition(10);
        subsystem.setMotorControllerPidTarget();

        assertEquals("Motor controller target should handle an offset", 6.803, motorController.getReference(), 0.001);
    }

    @Test
    public void testIsMotorControllerDriftTooHigh() {
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(0, 0, 1));
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(-180, 180, 1));
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(180, -180, 1));
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(360, 0, 1));
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(0, 360, 1));
        assertFalse(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(720, 0, 1));

        assertTrue(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(0, 90, 1));
        assertTrue(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(15, 25, 10));
        assertTrue(SwerveSteeringSubsystem.isMotorControllerDriftTooHigh(25, 15, 10));
    }

}
