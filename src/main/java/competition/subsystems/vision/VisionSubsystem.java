package competition.subsystems.vision;

import com.google.inject.Inject;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import xbot.common.command.BaseSubsystem;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.logic.TimeStableValidator;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class VisionSubsystem extends BaseSubsystem {

    public static final String VISION_TABLE = "photonvision";
    public static final String FIX_ACQUIRED_PROPERTY = "gloworm/hasTarget";
    public static final String TARGET_YAW_PROPERTY = "gloworm/targetYaw";
    public static final String TARGET_PITCH_PROPERTY = "gloworm/targetPitch";

    final RobotAssertionManager assertionManager;
    final BooleanProperty isInverted;
    final DoubleProperty yawOffset;
    final DoubleProperty waitForStableFixTime;
    final TimeStableValidator fixIsStable;
    NetworkTable visionTable;

    @Inject
    public VisionSubsystem(PropertyFactory pf, RobotAssertionManager assertionManager) {
        this.assertionManager = assertionManager;
        visionTable = NetworkTableInstance.getDefault().getTable(VISION_TABLE);

        pf.setPrefix(this);
        isInverted = pf.createPersistentProperty("Yaw inverted", true);
        yawOffset = pf.createPersistentProperty("Yaw offset", 0);

        waitForStableFixTime = pf.createPersistentProperty("Fix stable time", 0.1);
        fixIsStable = new TimeStableValidator(() -> waitForStableFixTime.get());
    }

    public double getBearingToHub() {
        if (getFixAcquired()) {
            return getYawToTarget();
        } else {
            return 0;
        }
    }

    public double getPitchToHub() {
        if (getFixAcquired()) {
            return getPitchToTarget();
        } else {
            return 0;
        }
    }

    public boolean getFixAcquired() {
        boolean fixAcquired = visionTable.getEntry(FIX_ACQUIRED_PROPERTY).getBoolean(false);
        boolean isStable = fixIsStable.checkStable(fixAcquired);

        return fixAcquired && isStable;
    }

    private double getYawToTarget() {
        double yawToTarget = (visionTable.getEntry(TARGET_YAW_PROPERTY).getDouble(0) + this.yawOffset.get()) * getInversionFactor();

        return yawToTarget;
    }

    private double getPitchToTarget() {
        return (visionTable.getEntry(TARGET_PITCH_PROPERTY).getDouble(0));
    }

    private double getInversionFactor() {
        if (this.isInverted.get()) {
            return -1;
        }
        return 1;
    }
}
