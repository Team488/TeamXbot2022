package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.math.geometry.Rotation2d;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;

public class TurnLeft90DegreesCommand extends BaseCommand {

    private Rotation2d initialHeading;
    private Rotation2d goalHeading;
    PoseSubsystem pose;
    DriveSubsystem drive;

    @Inject
    public TurnLeft90DegreesCommand(DriveSubsystem drive, PoseSubsystem pose, CommonLibFactory clf) {
        this.addRequirements(drive);
        clf.createHeadingModule(drive.getRotateToHeadingPid());

        this.drive = drive;
        this.pose = pose;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        initialHeading = pose.getCurrentHeading();
        goalHeading = initialHeading.plus(new Rotation2d(90));
    }

    @Override
    public void execute() {

    }
}