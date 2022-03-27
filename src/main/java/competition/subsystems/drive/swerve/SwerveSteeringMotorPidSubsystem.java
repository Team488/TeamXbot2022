package competition.subsystems.drive.swerve;

import com.google.inject.Inject;

import competition.subsystems.BaseMotorPidSubsystem;
import xbot.common.properties.PropertyFactory;

/**
 * Container for steering motor controller PIDs.
 */
public class SwerveSteeringMotorPidSubsystem extends BaseMotorPidSubsystem {

    @Inject
    public SwerveSteeringMotorPidSubsystem(PropertyFactory pf) {
        super(pf, 0.5, 0.0, 0.0, 0.0, -1.0, 1.0, 0.02, 0.05);
    }
    
}