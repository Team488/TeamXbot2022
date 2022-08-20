package competition.subsystems.drive.commands;

import javax.inject.Inject;

import competition.injection.swerve.FrontLeftSwerveComponent;
import competition.injection.swerve.FrontRightSwerveComponent;
import competition.injection.swerve.RearLeftSwerveComponent;
import competition.injection.swerve.RearRightSwerveComponent;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseCommand;

/**
 * Used to force all the swerve steering modules to calibrate at their current position.
 * Make sure that the wheels are all facing forward before running this command!
 * You should also never need to run this command once we have the CANCoders active, since they
 * should be able to always read the true, absolute position of the steering modules.
 */
public class CalibrateSteeringCommand extends BaseCommand {

    private final SwerveSteeringSubsystem frontLeft;
    private final SwerveSteeringSubsystem frontRight;
    private final SwerveSteeringSubsystem rearLeft;
    private final SwerveSteeringSubsystem rearRight;

    @Inject
    public CalibrateSteeringCommand(
        FrontLeftSwerveComponent frontLeftSteering,
        FrontRightSwerveComponent frontRightSteering,
        RearLeftSwerveComponent rearLeftSteering,
        RearRightSwerveComponent rearRightSteering) {

        this.frontLeft = frontLeftSteering.swerveSteeringSubsystem();
        this.frontRight = frontRightSteering.swerveSteeringSubsystem();
        this.rearLeft = rearLeftSteering.swerveSteeringSubsystem();
        this.rearRight = rearRightSteering.swerveSteeringSubsystem();
    }

    @Override
    public void initialize() {
        log.info("Initializing");

        frontLeft.calibrateHere();
        frontRight.calibrateHere();
        rearLeft.calibrateHere();
        rearRight.calibrateHere();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}