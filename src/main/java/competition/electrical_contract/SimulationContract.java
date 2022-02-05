package competition.electrical_contract;

import competition.injection.swerve.SwerveInstance;
import xbot.common.injection.electrical_contract.DeviceInfo;

public class SimulationContract extends CompetitionContract {
    
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
        // The steering encoders report in radians in the simulator; e.g. a half turn is 3.14 (pi). We'd like this to return degrees.
        // So we'll multiply by 180/pi.
        double simulationScalingValue = 180.0 / Math.PI;

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
}
