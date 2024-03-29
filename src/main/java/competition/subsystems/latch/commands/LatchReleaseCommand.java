package competition.subsystems.latch.commands;

import javax.inject.Inject;

import competition.subsystems.latch.LatchSubsystem;
import xbot.common.command.BaseCommand;

public class LatchReleaseCommand extends BaseCommand {

    final LatchSubsystem release;

    @Inject
    public LatchReleaseCommand(LatchSubsystem release){
        this.release = release;
        addRequirements(release);
    }

    @Override
    public void initialize() {
        // Only happens once
        log.info("Initializing");
    }

    @Override
    public void execute() {
        // Happens 50x a second
        release.release();
    }

}
