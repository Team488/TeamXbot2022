package competition.subsystems.shooterwheel.commands;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class ShooterWheelSetPassPowerCommand extends BaseSetpointCommand {

    final ShooterWheelSubsystem wheel;
    final PoseSubsystem pose;
    final DoubleProperty longPassRpm;
    final DoubleProperty shortPassRpm;
    final DoubleProperty longPassY;
    final DoubleProperty shortPassY;

    @Inject
    public ShooterWheelSetPassPowerCommand(PropertyFactory pf, ShooterWheelSubsystem wheel, PoseSubsystem pose) {
        super(wheel);
        pf.setPrefix(this);


        longPassRpm = pf.createPersistentProperty("Long Pass RPM", 2000);
        shortPassRpm = pf.createPersistentProperty("Short Pass RPM", 1000);

        // Field drawings indicate 625 inches of field Y distance
        longPassY = pf.createPersistentProperty("Long Pass Field Y Inches", 200);
        shortPassY = pf.createPersistentProperty("Short Pass Field Y Inches", 400);

        this.wheel = wheel;
        this.pose = pose;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    public void execute() {
        FieldPose fieldPose = this.pose.getCurrentFieldPose();
        XYPair currentPosition = fieldPose.getPoint();

        // target RPM is proportional to the field position
        double positionScale = MathUtils.scaleDouble(
            MathUtils.constrainDouble(currentPosition.y, this.longPassY.get(), this.shortPassY.get()),
            this.longPassY.get(),
            this.shortPassY.get(),
            0,
            1);

        double targetRpm = MathUtils.scaleDouble(
            1 - positionScale,
            0,
            1,
            this.shortPassRpm.get(),
            this.longPassRpm.get());

        this.wheel.setTargetRPM(targetRpm);
    }
} 