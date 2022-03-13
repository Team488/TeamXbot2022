package competition.subsystems.drive.commands;

import java.util.function.Supplier;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class SwerveToPointCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    DoubleProperty directionToTarget;
    HeadingModule headingModule;

    private Supplier<XYPair> targetPositionSupplier;
    private Supplier<Double> targetHeadingSupplier;

    private XYPair targetPosition;
    private double targetHeading;

    private boolean robotRelativeMotion = false;

    @Inject
    public SwerveToPointCommand(DriveSubsystem drive, PoseSubsystem pose, PropertyFactory pf, CommonLibFactory clf) {
        this.drive = drive;
        this.pose = pose;
        headingModule = clf.createHeadingModule(drive.getRotateToHeadingPid());

        pf.setPrefix(this);
        directionToTarget = pf.createEphemeralProperty("Direction to target", 0);
        this.addRequirements(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");

        targetHeading = targetHeadingSupplier.get();
        targetPosition = targetPositionSupplier.get();

        if (robotRelativeMotion) {
            // If we are using robot relative motion, we need to consider the target position
            // as being relative to the robot's current position. So value of 0,-60 means
            // "go backwards 60 inches" from the robot's perspective.
            // If the robot was pointed at field 0 degrees (right) this would mean setting
            // a field-relative target of -60, 0 (move the robot left 60 inches).
            targetPosition.rotate(pose.getCurrentHeading().getDegrees() - PoseSubsystem.FACING_AWAY_FROM_DRIVERS);
            
            targetPosition = pose.getCurrentFieldPose().getPoint().clone().add(targetPosition);

            // Then, move the target heading itself. 
            targetHeading = pose.getCurrentHeading().getDegrees() - 90 + targetHeading;
        }

        log.info(String.format("Swerve to point targets: (%f, %f), %f", targetPosition.x, targetPosition.y, targetHeading));
    }

    public void setTargetPosition(XYPair targetPositionInInches, double heading) {
        this.targetPositionSupplier = () -> targetPositionInInches;
        this.targetHeadingSupplier = () -> heading;
    }

    public void setTargetSupplier(Supplier<XYPair> targetPositionSupplier, Supplier<Double> targetHeadingSupplier) {
        this.targetPositionSupplier = targetPositionSupplier;
        this.targetHeadingSupplier = targetHeadingSupplier;
    }

    public void setRobotRelativeMotion() {
        robotRelativeMotion = true;
    }

    public void setFieldRelativeMotion() {
        robotRelativeMotion = false;
    }

    @Override
    public void execute() {
        // Get the difference between where we are, and where we want to be.
        XYPair goalVector = targetPosition.clone().add(
            pose.getCurrentFieldPose().getPoint().scale(-1)
        );

        // PID on the magnitude of the goal. Kind of similar to rotation,
        // our goal is "zero error".
        double magnitudeGoal = goalVector.getMagnitude();
        double drivePower = drive.getPositionalPid().calculate(magnitudeGoal, 0);

        // Create a vector in the direction of the goal, scaled by the drivePower.
        XYPair intent = XYPair.fromPolar(goalVector.getAngle(), drivePower);
        directionToTarget.set(goalVector.getAngle());

        double headingPower = headingModule.calculateHeadingPower(targetHeading);

        drive.move(intent, headingPower);
    }

    @Override
    public boolean isFinished() {
        return drive.getPositionalPid().isOnTarget() && headingModule.isOnTarget();
    }
}