package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

/**
 * The main swerve drive command that links up the human input (from gamepad
 * joysticks) to the drive subsystem.
 */
public class SwerveDriveWithJoysticksCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    OperatorInterface oi;
    final DoubleProperty input_exponent;
    final DoubleProperty drivePowerFactor;
    final DoubleProperty turnPowerFactor;
    final BooleanProperty absoluteOrientationMode;
    final HeadingAssistModule headingAssist;
    final HeadingModule headingModule;
    final Latch absoluteOrientationLatch;
    final DoubleProperty minimumMagnitudeForAbsoluteHeading;

    @Inject
    public SwerveDriveWithJoysticksCommand(DriveSubsystem drive, PoseSubsystem pose, OperatorInterface oi,
            PropertyFactory pf, CommonLibFactory clf) {
        this.drive = drive;
        this.oi = oi;
        this.pose = pose;
        pf.setPrefix(this);
        this.input_exponent = pf.createPersistentProperty("Input Exponent", 2);
        this.drivePowerFactor = pf.createPersistentProperty("Power Factor", 1);
        this.turnPowerFactor = pf.createPersistentProperty("Turn Power Factor", 0.5);
        this.absoluteOrientationMode = pf.createPersistentProperty("Absolute Orientation Mode", false);
        this.minimumMagnitudeForAbsoluteHeading = pf.createPersistentProperty("Min Magnitude For Absolute Heading", 0.75);

        this.headingModule = clf.createHeadingModule(drive.getRotateToHeadingPid());
        this.headingAssist = clf.createHeadingAssistModule(clf.createHeadingModule(drive.getRotateToHeadingPid()), this.getPrefix());

        // Set up a latch to trigger whenever we change the rotational mode. In either case,
        // there's some PIDs that will need to be reset, or goals that need updating.
        absoluteOrientationLatch = new Latch(absoluteOrientationMode.get(), EdgeType.Both, edge -> {
            if(edge == EdgeType.RisingEdge) {
                resetBeforeStartingAbsoluteOrientation();
            }
            else if(edge == EdgeType.FallingEdge) {
                resetBeforeStartingRelativeOrientation();
            }
        });

        this.addRequirements(drive);
    }

    public void setAbsoluteHeadingMode(boolean absoluteHeadingEnabled) {
        absoluteOrientationMode.set(absoluteHeadingEnabled);
    }

    @Override
    public void initialize() {
        log.info("Initializing");

        resetBeforeStartingAbsoluteOrientation();
        resetBeforeStartingRelativeOrientation();
    }

    private void resetBeforeStartingAbsoluteOrientation() {
        headingAssist.reset();
        // Set our desired heading to the current heading to avoid a surprising
        // change of heading during reset.
        drive.setDesiredHeading(pose.getCurrentHeading().getDegrees());
    }

    private void resetBeforeStartingRelativeOrientation() {
        headingModule.reset();
    }

    @Override
    public void execute() {

        // Feed the latch with our mode state, so it can reset PIDs or goals as appropriate.
        absoluteOrientationLatch.setValue(absoluteOrientationMode.get());
        
        // --------------------------------------------------
        // Translation
        // --------------------------------------------------

        // Get the current translation vector from the gamepad.
        XYPair rawTranslationVector = new XYPair(oi.driverGamepad.getLeftStickX(), oi.driverGamepad.getLeftStickY());
        // preserve the angle
        double rawAngle = rawTranslationVector.getAngle();
        // scale the magnitude
        double updatedMagnitude = MathUtils.deadband(
                rawTranslationVector.getMagnitude(),
                oi.getDriverGamepadTypicalDeadband(),
                (a) -> MathUtils.exponentAndRetainSign(a, (int) input_exponent.get()));

        // create new vector with the scaled magnitude and angle
        XYPair translationIntent = XYPair.fromPolar(rawAngle, updatedMagnitude);

        // --------------------------------------------------
        // Rotation
        // --------------------------------------------------

        double suggestedRotatePower = 0;
                
        if (absoluteOrientationMode.get()) {
            // If we are using absolute orientation, we first need get the desired heading from the right joystick.
            // We need to only do this if the joystick has been moved past the minimumMagnitudeForAbsoluteHeading.
            // In the future, we might be able to replace the joystick with a dial or other device that can more easily
            // hold a heading.

            // One key note - we need to invert the right X, since it is already inverted once to make "typical" rotation commands
            // line up with our conventions. (Usually, a right turn is done by moving the joystick right. However, turning to the right
            // is a "negative" rotation, so the X axis is usually inverted to take that into account). 
            // By doing this inversion, the vector will better map onto a typical cartesian coordinate system.
            XYPair headingVector = new XYPair(-oi.driverGamepad.getRightStickX(), oi.driverGamepad.getRightStickY());

            double desiredHeading = 0;
            
            if (headingVector.getMagnitude() > minimumMagnitudeForAbsoluteHeading.get()) {
                // If the magnitude is greater than the minimum magnitude, we can use the joystick to set the heading.
                desiredHeading = headingVector.getAngle();
                drive.setDesiredHeading(desiredHeading);
            } else {
                // If the joystick isn't deflected enough, we use the last known heading.
                desiredHeading = drive.getDesiredHeading();
            }

            // Now, finally, we ask the heading module to calculate the power.
            suggestedRotatePower = headingModule.calculateHeadingPower(desiredHeading);

        } else {
            // If we are in the typical "rotate using joystick to turn" mode, use the Heading Assist module to get the suggested power.
            double humanRotatePower = MathUtils.deadband(
                oi.driverGamepad.getRightStickX(),
                oi.getDriverGamepadTypicalDeadband(),
                (a) -> MathUtils.exponentAndRetainSign(a, (int) input_exponent.get()));

            suggestedRotatePower = humanRotatePower; //headingAssist.calculateHeadingPower(humanRotatePower);
        }

        // --------------------------------------------------
        // Safety scaling and sending to Drive Subsystem
        // --------------------------------------------------

        // It's not sensible to magnitudes greater than the unit vector. This isn't goldeneye - you can't go extra fast by moving diagonally!
        if (translationIntent.getMagnitude() > 1) {
            translationIntent.scale(1/translationIntent.getMagnitude());
        }

        // Scale the power down if requested (typically used when novices are controlling the robot)
        translationIntent = translationIntent.scale(drivePowerFactor.get());
        drive.fieldOrientedDrive(translationIntent, suggestedRotatePower * turnPowerFactor.get(), pose.getCurrentHeading().getDegrees(), false);
    }
}