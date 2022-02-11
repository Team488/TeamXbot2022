package competition.electrical_contract;

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

    public abstract DeviceInfo getLeftClimberNeo();

    public abstract DeviceInfo getRightClimberNeo();

    public abstract boolean isShooterReady();

    public abstract DeviceInfo getPrimaryShooterNeo();

    public abstract DeviceInfo getSecondaryShooterNeo();

    public abstract DeviceInfo getArduinoDio1();

    public abstract DeviceInfo getArduinoDio2();
}
