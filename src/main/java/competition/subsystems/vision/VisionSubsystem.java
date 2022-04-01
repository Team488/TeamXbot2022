package competition.subsystems.vision;

import com.google.inject.Inject;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import xbot.common.command.BaseSubsystem;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class VisionSubsystem extends BaseSubsystem {

    final RobotAssertionManager assertionManager;
    final BooleanProperty isInverted;
    final DoubleProperty yawOffset;
    NetworkTable visionTable;

    @Inject
    public VisionSubsystem(PropertyFactory pf, RobotAssertionManager assertionManager) {
        this.assertionManager = assertionManager;
        visionTable = NetworkTableInstance.getDefault().getTable("photonvision");

        pf.setPrefix(this);
        isInverted = pf.createPersistentProperty("Yaw inverted", true);
        yawOffset = pf.createPersistentProperty("Yaw offset", 0);
    }

    public double getBearingToHub() {
        if (getFixAcquired()) {
            return getYawToTarget();
        } else {
            return 0;
        }
    }

    public boolean getFixAcquired() {
        boolean fixAcquired = visionTable.getEntry("gloworm/hasTarget").getBoolean(false);

        return fixAcquired;
    }

    private double getYawToTarget() {
        double yawToTarget = (visionTable.getEntry("gloworm/targetYaw").getDouble(0) + this.yawOffset.get()) * getInversionFactor();

        return yawToTarget;
    }

    private double getInversionFactor() {
        if (this.isInverted.get()) {
            return -1;
        }
        return 1;
    }
}
