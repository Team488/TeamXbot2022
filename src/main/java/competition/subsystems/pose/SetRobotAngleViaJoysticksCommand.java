package competition.subsystems.pose;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import xbot.common.command.BaseCommand;
import xbot.common.math.XYPair;

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
            
            XYPair trueXYPair = new XYPair(-oi.driverGamepad.getRightStickX(), oi.driverGamepad.getRightStickY());
            
            double driverAngle = trueXYPair.getAngle();
            // Michael wants the joystick to point towards the battery, so add 180 degrees

            // Set that angle to the pose subsystem.
            pose.setCurrentHeading(driverAngle);
        }

    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}