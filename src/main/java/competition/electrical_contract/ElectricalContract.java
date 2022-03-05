package competition.electrical_contract;

import competition.injection.arm.ArmInstance;
import competition.injection.swerve.SwerveInstance;
import xbot.common.injection.electrical_contract.CANTalonInfo;
import xbot.common.injection.electrical_contract.DeviceInfo;
import xbot.common.math.XYPair;

public abstract class ElectricalContract {
    public abstract boolean isDriveReady();

    public abstract boolean areCanCodersReady();

    public abstract DeviceInfo getDriveNeo(SwerveInstance swerveInstance);
    
    public abstract DeviceInfo getSteeringNeo(SwerveInstance swerveInstance);

    public abstract DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance);

    public abstract XYPair getSwerveModuleOffsets(SwerveInstance swerveInstance);

    public abstract boolean isIntakeReady();

    public abstract DeviceInfo getIntakeNeo();

    public abstract boolean isClimberReady();

    public abstract DeviceInfo getClimberNeo(ArmInstance armInstance);

    public abstract CANTalonInfo getLeftCollectorMotor();

    public abstract CANTalonInfo getRightCollectorMotor();

    public abstract CANTalonInfo getCollectorStage2Motor();

    public abstract boolean isCollectorStage2Ready();

    public abstract DeviceInfo getClimberPawl(ArmInstance armInstance);

    public abstract boolean isShooterReady();

    public abstract DeviceInfo getArduinoDio0();
    public abstract DeviceInfo getArduinoDio1();
    public abstract DeviceInfo getArduinoDio2();
    public abstract DeviceInfo getArduinoDio3();
    public abstract DeviceInfo getArduinoAllianceDio();
    public abstract DeviceInfo getArduinoAnalogOutput();

    public abstract boolean arePneumaticsReady();

    public abstract DeviceInfo getLatchSolenoid();
    public abstract DeviceInfo getLatchSolenoid2();

    public abstract DeviceInfo getPivotSolenoid();
    public abstract DeviceInfo getPivotSolenoid2();

    public abstract DeviceInfo getShooterMotorLeader();
    public abstract DeviceInfo getShooterMotorFollower();

    public abstract DeviceInfo getCollectorDeviceSoleniod1();
    public abstract DeviceInfo getCollectorDeviceSoleniod2();

    public abstract DeviceInfo getDeployHoodSoleniod1();
    public abstract DeviceInfo getDeployHoodSoleniod2();

    public abstract CANTalonInfo getConveyerMotor();

    public abstract boolean isConveyerReady();
}
