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
                return null;

            case "FrontRightDrive":
                return null;

            case "RearLeftDrive":
                return null;

            case "RearRightDrive":
                return null;

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return null;

            case "FrontRightDrive":
                return null;

            case "RearLeftDrive":
                return null;

            case "RearRightDrive":
                return null;

            default:
                return null;
        }
    }
}
