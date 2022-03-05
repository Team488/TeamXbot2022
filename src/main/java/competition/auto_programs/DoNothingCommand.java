package competition.auto_programs;

import com.google.inject.Inject;
import xbot.common.command.BaseCommand;

public class DoNothingCommand extends BaseCommand {

    @Inject
    public DoNothingCommand() {
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
    }
}