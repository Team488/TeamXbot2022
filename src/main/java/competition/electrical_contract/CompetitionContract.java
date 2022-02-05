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
                return new DeviceInfo(1, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(2, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(3, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(4, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringNeo(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(11, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(12, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(13, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(14, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(21, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(22, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(23, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(24, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public boolean isLauncherReady() {
        return true;
    }

    //TODO: temporary channel numbers
    @Override
    public DeviceInfo getShooterMotorLeader() {
        return new DeviceInfo(10, true);
    }

    @Override
    public DeviceInfo getShooterMotorFollower() {
        return new DeviceInfo(15, false);
    }

}
