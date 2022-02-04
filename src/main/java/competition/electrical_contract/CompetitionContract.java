package competition.electrical_contract;

import competition.injection.swerve.SwerveInstance;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.DeviceInfo;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return false;
    }

    @Override
    public DeviceInfo getDriveNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(26, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(27, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(28, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(29, false, simulationScalingValue);

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
                return new DeviceInfo(31, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(32, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(33, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(34, false);

            case "FrontRightDrive":
                return new DeviceInfo(35, false);

            case "RearLeftDrive":
                return new DeviceInfo(36, false);

            case "RearRightDrive":
                return new DeviceInfo(37, false);

            default:
                return null;
        }
    }
}
