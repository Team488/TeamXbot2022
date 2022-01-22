package competition.electrical_contract;

import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.DeviceInfo;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return false;
    }

    @Override
    public DeviceInfo getFrontLeftDriveNeo() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRightDriveNeo() {
        return null;
    }

    @Override
    public DeviceInfo getRearLeftDriveNeo() {
        return null;
    }

    @Override
    public DeviceInfo getRearRightDriveNeo() {
        return null;
    }

    @Override
    public DeviceInfo getFrontLeftSteeringNeo() {
        return null;
    }

    @Override
    public DeviceInfo getFrontRightSteeringNeo() {
        return null;
    }

    @Override
    public DeviceInfo getRearLeftSteeringNeo() {
        return null;
    }

    @Override
    public DeviceInfo getRearRightSteeringNeo() {
        return null;
    }
}
