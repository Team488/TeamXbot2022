package competition.electrical_contract;

import xbot.common.injection.electrical_contract.DeviceInfo;

public abstract class ElectricalContract {
    public abstract boolean isDriveReady();

    public abstract DeviceInfo getFrontLeftDriveNeo();
    public abstract DeviceInfo getFrontRightDriveNeo();
    public abstract DeviceInfo getRearLeftDriveNeo();
    public abstract DeviceInfo getRearRightDriveNeo();
    
    public abstract DeviceInfo getFrontLeftSteeringNeo();
    public abstract DeviceInfo getFrontRightSteeringNeo();
    public abstract DeviceInfo getRearLeftSteeringNeo();
    public abstract DeviceInfo getRearRightSteeringNeo();

    public boolean isLauncherReady(){
        return true;
    }
}
