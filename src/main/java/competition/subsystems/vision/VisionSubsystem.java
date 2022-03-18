package competition.subsystems.vision;

import java.sql.Driver;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.command.BaseSubsystem;
import xbot.common.logging.LoggingLatch;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;

public class VisionSubsystem extends BaseSubsystem {

    final StringProperty dataFromOffboardVision;
    final RobotAssertionManager assertionManager;

    private boolean isGettingData;
    private Alliance cargoAlliance;
    private double cargoBearing;

    NetworkTable visionTable;

    @Inject
    public VisionSubsystem(PropertyFactory pf, RobotAssertionManager assertionManager) {
        this.assertionManager = assertionManager;
        pf.setPrefix(this);
        dataFromOffboardVision = pf.createEphemeralProperty("DataFromOffboardVision", "");
        this.register();

        NetworkTableInstance.getDefault().getTable("Vision");
    }

    public double getBearingtoCargo() {
        Alliance currentAlliance = DriverStation.getAlliance();

        if (getFixAcquired(currentAlliance)) {
            return getYawToTarget(currentAlliance);
        } else {
            return 0;
        }
    }

    public boolean getFixAcquired(Alliance color) {
        boolean fixAcquired = false;
        if (color == Alliance.Red) {
            fixAcquired = visionTable.getEntry("fixAcquiredRed").getBoolean(false);
        } else if (color == Alliance.Blue) {
            fixAcquired = visionTable.getEntry("fixAcquiredBlue").getBoolean(false);
        }

        return fixAcquired;
    }

    private double getYawToTarget(Alliance color) {
        double yawToTarget = 0;
        if (color == Alliance.Red) {
            yawToTarget = visionTable.getEntry("yawToTargetRed").getDouble(0);
        } else if (color == Alliance.Blue) {
            yawToTarget = visionTable.getEntry("yawToTargetBlue").getDouble(0);
        }

        return yawToTarget;
    }
}
