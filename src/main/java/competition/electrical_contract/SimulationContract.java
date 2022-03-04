package competition.electrical_contract;

import competition.injection.swerve.SwerveInstance;
import xbot.common.injection.electrical_contract.DeviceInfo;
import xbot.common.math.XYPair;

public class SimulationContract extends CompetitionContract {

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public boolean areCanCodersReady() {
        return true;
    }

    @Override
    public boolean isClimberReady() {
        return false;
    }

    @Override
    public boolean isShooterReady() {
        return false;
    }

    public boolean isIntakeReady() {
        return false;
    }

    @Override
    public boolean arePneumaticsReady() {
        return false;
    }
    
    @Override
    public boolean isConveyerReady() {
        return false;
    }

    @Override
    public boolean isCollectorStage2Ready() {
        return false;
    }
    
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
        // The steering encoders report in radians in the simulator; e.g. a half turn is 3.14 (pi). We'd like this to return degrees.
        // So we'll multiply by 180/pi, but we need to undo the scaling factor applied in SwerveSteeringSubsystem.
        double simulationScalingValue = 180 / Math.PI / 28.1502912;

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
    
    @Override
    public XYPair getSwerveModuleOffsets(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new XYPair(-14.7, 13.8);
            case "FrontRightDrive":
                return new XYPair(14.7, 13.8);
            case "RearLeftDrive":
                return new XYPair(-14.7, -13.8);
            case "RearRightDrive":
                return new XYPair(14.7, -13.8);
            default:
                return new XYPair(0, 0);
        }
    }
}
