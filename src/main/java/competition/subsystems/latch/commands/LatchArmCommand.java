package competition.subsystems.latch.commands;

import com.google.inject.Inject;

import competition.subsystems.latch.LatchSubsystem;
import xbot.common.command.BaseCommand;

public class LatchArmCommand extends BaseCommand {
    final LatchSubsystem latch;

    @Inject
    public LatchArmCommand(LatchSubsystem latch){
        this.latch = latch;
        addRequirements(latch);
    }

    @Override
    public void initialize() {
        // Only happens once
        log.info("Initializing");
    }

    @Override
    public void execute() {
        // Happens 50x a second
        latch.arm();
    }

}
