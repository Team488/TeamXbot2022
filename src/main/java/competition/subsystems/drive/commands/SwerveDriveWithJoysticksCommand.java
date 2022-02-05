package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;

public class SwerveDriveWithJoysticksCommand extends BaseCommand {

    DriveSubsystem drive;
    OperatorInterface oi;

    @Inject
    public SwerveDriveWithJoysticksCommand(DriveSubsystem drive, OperatorInterface oi) {
        this.drive = drive;
        this.oi = oi;
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

        drive.move(new XYPair(xPower, yPower), rotatePower);
    }
}