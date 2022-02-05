package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class GoToNextActiveSwerveModuleCommand extends BaseCommand {

    DriveSubsystem drive;

    @Inject
    public GoToNextActiveSwerveModuleCommand(DriveSubsystem drive) {
        this.drive = drive;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        drive.setNextModuleAsActiveModule();
    }

    @Override
    public void execute() {
        // Nothing to do.
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}