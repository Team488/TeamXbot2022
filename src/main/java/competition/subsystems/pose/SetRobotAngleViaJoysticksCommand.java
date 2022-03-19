package competition.subsystems.pose;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import xbot.common.command.BaseCommand;

public class SetRobotAngleViaJoysticksCommand extends BaseCommand {

    final PoseSubsystem pose;
    final OperatorInterface oi;

    @Inject
    public SetRobotAngleViaJoysticksCommand(PoseSubsystem pose, OperatorInterface oi) {
        this.pose = pose;
        this.oi = oi;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {

        if (oi.driverGamepad.getRightVector().getMagnitude() > 0.75) {
            // If the joystick is sufficiently deflected, get the angle from the joystick.
            double driverAngle = oi.driverGamepad.getRightVector().getAngle();
            // Michael wants the joystick to point towards the battery, so add 180 degrees
            driverAngle += 180;

            // Set that angle to the pose subsystem.
            pose.setCurrentHeading(driverAngle);
        }

    }
}