package competition.subsystems.drive.commands;

import java.util.function.Supplier;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class SwerveToPointCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    DoubleProperty directionToTarget;

    private Supplier<XYPair> targetPositionSupplier;

    @Inject
    public SwerveToPointCommand(DriveSubsystem drive, PoseSubsystem pose, PropertyFactory pf) {
        this.drive = drive;
        this.pose = pose;

        pf.setPrefix(this);
        directionToTarget = pf.createEphemeralProperty("Direction to target", 0);
        this.addRequirements(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    public void setTargetPosition(XYPair targetPositionInInches) {
        this.targetPositionSupplier = () -> targetPositionInInches;
    }

    public void setTargetSupplier(Supplier<XYPair> targetPositionSupplier) {
        this.targetPositionSupplier = targetPositionSupplier;
    }

    @Override
    public void execute() {
        // Get the difference between where we are, and where we want to be.
        XYPair goalVector = targetPositionSupplier.get().clone().add(
            pose.getCurrentFieldPose().getPoint().scale(-1)
        );

        // PID on the magnitude of the goal. Kind of similar to rotation,
        // our goal is "zero error".
        double magnitudeGoal = goalVector.getMagnitude();
        double drivePower = drive.getPositionalPid().calculate(magnitudeGoal, 0);

        // Create a vector in the direction of the goal, scaled by the drivePower.
        XYPair intent = XYPair.fromPolar(goalVector.getAngle(), drivePower);
        directionToTarget.set(goalVector.getAngle());
        drive.move(intent, 0);
    }

    @Override
    public boolean isFinished() {
        return drive.getPositionalPid().isOnTarget();
    }
}