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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getFrontRightDriveNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getRearLeftDriveNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getRearRightDriveNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getFrontLeftSteeringNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getFrontRightSteeringNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getRearLeftSteeringNeo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceInfo getRearRightSteeringNeo() {
        // TODO Auto-generated method stub
        return null;
    }
}
