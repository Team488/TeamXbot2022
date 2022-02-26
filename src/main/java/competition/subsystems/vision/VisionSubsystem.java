package competition.subsystems.vision;

import java.sql.Driver;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

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
    final LoggingLatch parsingErrorLatch;
    final RobotAssertionManager assertionManager;

    private boolean isGettingData;
    private Alliance cargoAlliance;
    private double cargoBearing;

    @Inject
    public VisionSubsystem(PropertyFactory pf, RobotAssertionManager assertionManager) {

        this.assertionManager = assertionManager;
        pf.setPrefix(this);
        dataFromOffboardVision = pf.createEphemeralProperty("DataFromOffboardVision", "");
        parsingErrorLatch = new LoggingLatch(
            this.getName(), 
            "Couldn't parse JSON from offboard vision",
            EdgeType.RisingEdge);
        this.register();
    }

    /*
     * Vision Data Format:
     * An array of "Cargo" objects, each of which has a color and a bearing
     */

    private void parseVisionData(String data) {

        if (data == null || data.isBlank()) {
            // We're not getting any data - skip all processing
            return;
        }

        try {
            JSONObject formattedData = new JSONObject(data);

            JSONArray cargoArray = formattedData.getJSONArray("Cargo");
            // For now, take the first element.
            JSONObject firstCargo = cargoArray.getJSONObject(0);
            cargoAlliance = firstCargo.getString("Alliance").equals("Red") ? Alliance.Red : Alliance.Blue;
            cargoBearing = firstCargo.getDouble("Bearing");
            parsingErrorLatch.checkValue(false);
            isGettingData = true;
        } catch (Exception e) {
            // Only throw exceptions when testing
            assertionManager.throwException("Problems parsing vision data", e);
            // Only log once instead of filling the log with errors
            parsingErrorLatch.checkValue(true);
            return;
        }
    }

    public double getBearingToBestTarget() {
        
        if (cargoAlliance == DriverStation.getAlliance()) {
            return cargoBearing;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void periodic() {
        parseVisionData(dataFromOffboardVision.get());
    }
}
