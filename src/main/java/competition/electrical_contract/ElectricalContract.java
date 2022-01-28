package competition.electrical_contract;

import competition.injection.swerve.SwerveInstance;
import xbot.common.injection.electrical_contract.CANTalonInfo;

public abstract class ElectricalContract {
    public abstract boolean isDriveReady();

    public abstract CANTalonInfo getDriveNeo(SwerveInstance swerveInstance);
    
    public abstract CANTalonInfo getSteeringNeo(SwerveInstance swerveInstance);
}
