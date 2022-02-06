package competition.electrical_contract;

import competition.injection.swerve.SwerveInstance;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.DeviceInfo;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public DeviceInfo getDriveNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(31, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(29, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(39, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(21, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringNeo(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(30, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(28, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(38, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(20, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public boolean areCanCodersReady() {
        return false;
    }

    @Override
    public DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(3, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(1, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(4, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(2, false, simulationScalingValue);

            default:
                return null;
        }
    }
    
    public boolean isIntakeReady() {
        return false;
    }

    @Override
    public DeviceInfo getIntakeNeo() {
        return new DeviceInfo(26, false);
    }

    @Override
    public boolean isClimberReady() {
        return false;
    }

    @Override
    public DeviceInfo getLeftClimberNeo() {
        return new DeviceInfo(37, false);
    }

    @Override
    public DeviceInfo getRightClimberNeo() {
        return new DeviceInfo(22, false);
    }

    @Override
    public boolean isShooterReady() {
        return false;
    }

    @Override
    public DeviceInfo getPrimaryShooterNeo() {
        return new DeviceInfo(32, false);
    }

    @Override
    public DeviceInfo getSecondaryShooterNeo() {
        return new DeviceInfo(34, false);
    }
}
