package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;

/**
 * The main swerve drive command that links up the human input (from gamepad joysticks) to the drive subsystem.
 */
public class SwerveDriveWithJoysticksCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    OperatorInterface oi;

    @Inject
    public SwerveDriveWithJoysticksCommand(DriveSubsystem drive, PoseSubsystem pose, OperatorInterface oi) {
        this.drive = drive;
        this.oi = oi;
        this.pose = pose;
        this.addRequirements(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double xPower = MathUtils.deadband(oi.gamepad.getLeftStickX(), 0.15, (a) -> a);
        double yPower = MathUtils.deadband(oi.gamepad.getLeftStickY(), 0.15, (a) -> a);
        double rotatePower = MathUtils.deadband(oi.gamepad.getRightStickX(), 0.15, (a) -> a);

        // Get the current heading, use that for field-oriented operations
        XYPair translationIntent = new XYPair(xPower, yPower);
        drive.fieldOrientedDrive(translationIntent, rotatePower, pose.getCurrentHeading().getDegrees(), false);
    }
}