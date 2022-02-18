package competition.electrical_contract;

import competition.injection.arm.ArmInstance;
import competition.injection.swerve.SwerveInstance;
import xbot.common.injection.electrical_contract.DeviceInfo;

public abstract class ElectricalContract {
    public abstract boolean isDriveReady();

    public abstract boolean areCanCodersReady();

    public abstract DeviceInfo getDriveNeo(SwerveInstance swerveInstance);
    
    public abstract DeviceInfo getSteeringNeo(SwerveInstance swerveInstance);

    public abstract DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance);

    public abstract boolean isIntakeReady();

    public abstract DeviceInfo getIntakeNeo();

    public abstract boolean isClimberReady();

    public abstract DeviceInfo getClimberNeo(ArmInstance armInstance);

    public abstract DeviceInfo getCollectorMotor();

    public abstract DeviceInfo getCollectorStage2Motor();

    public abstract DeviceInfo getClimberPawl(ArmInstance armInstance);

    public abstract boolean isShooterReady();

    public abstract DeviceInfo getPrimaryShooterNeo();

    public abstract DeviceInfo getSecondaryShooterNeo();

    public abstract DeviceInfo getArduinoDio1();

    public abstract DeviceInfo getArduinoDio2();

    public abstract boolean arePneumaticsReady();

    public abstract DeviceInfo getLatchSolenoid();

    public abstract DeviceInfo getPivotSolenoid();

    public abstract boolean isLauncherReady();

    public abstract DeviceInfo getShooterMotorLeader();

    public abstract DeviceInfo getShooterMotorFollower();
}
