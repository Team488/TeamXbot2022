package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.DriverStation;
import xbot.common.controls.sensors.XXboxController;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
@Singleton
public class OperatorInterface {
    public XXboxController driverGamepad;
    public XXboxController operatorGamepad;
    public XXboxController autoGamepad;

    final DoubleProperty driverDeadband;
    final DoubleProperty operatorDeadband;

    @Inject
    public OperatorInterface(CommonLibFactory factory, RobotAssertionManager assertionManager, PropertyFactory pf) {
        DriverStation.silenceJoystickConnectionWarning(true);

        driverGamepad = factory.createXboxController(0);
        operatorGamepad = factory.createXboxController(1);
        autoGamepad = factory.createXboxController(2);

        driverGamepad.setLeftInversion(false, true);
        driverGamepad.setRightInversion(true, true);

        operatorGamepad.setLeftInversion(true, true);
        operatorGamepad.setRightInversion(true, true);

        pf.setPrefix("OperatorInterface");
        driverDeadband = pf.createPersistentProperty("Driver Deadband", 0.12);
        operatorDeadband = pf.createPersistentProperty("Operator Deadband", 0.15);
    }

    public double getDriverGamepadTypicalDeadband() {
        return driverDeadband.get();
    }

    public double getOperatorGamepadTypicalDeadband() {
        return operatorDeadband.get();
    }
}
