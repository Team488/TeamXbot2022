package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

/**
 * The main swerve drive command that links up the human input (from gamepad joysticks) to the drive subsystem.
 */
public class SwerveDriveWithJoysticksCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    OperatorInterface oi;
    final DoubleProperty input_exponent;

    @Inject
    public SwerveDriveWithJoysticksCommand(DriveSubsystem drive, PoseSubsystem pose, OperatorInterface oi, PropertyFactory pf) {
        this.drive = drive;
        this.oi = oi;
        this.pose = pose;
        pf.setPrefix(this);
        this.input_exponent = pf.createPersistentProperty("Input Exponent", 2);
        this.addRequirements(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double xPower = Math.pow(
            MathUtils.deadband(oi.driverGamepad.getLeftStickX(), oi.getDriverGamepadTypicalDeadband(), (a) -> a),
            input_exponent.get()
        );
        double yPower = Math.pow(
            MathUtils.deadband(oi.driverGamepad.getLeftStickY(), oi.getDriverGamepadTypicalDeadband(), (a) -> a),
            input_exponent.get()
        );
        double rotatePower = MathUtils.deadband(oi.driverGamepad.getRightStickX(), oi.getDriverGamepadTypicalDeadband(), (a) -> a);

        // Get the current heading, use that for field-oriented operations
        XYPair translationIntent = new XYPair(xPower, yPower);
        drive.fieldOrientedDrive(translationIntent, rotatePower, pose.getCurrentHeading().getDegrees(), false);
    }
}