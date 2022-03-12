package competition.subsystems.conveyer.commands;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class ReverseConveyorIfCollectorStoppedCommand extends BaseCommand {

    private final ShooterWheelSubsystem shooter;
    private final ConveyorSubsystem conveyor;

    private boolean skipCommand = false;

    public ReverseConveyorIfCollectorStoppedCommand(ShooterWheelSubsystem shooter, ConveyorSubsystem conveyor) {
        this.shooter = shooter;
        this.conveyor = conveyor;
        this.addRequirements(this.conveyor);
    }

    @Override
    public void initialize() {
        if (shooter.getTargetRPM() == 0) {
            skipCommand = true;
            return;
        }

        conveyor.getReverseCommand().initialize();
    }

    @Override
    public void execute() {
        if (skipCommand) {
            return;
        }

        conveyor.getReverseCommand().execute();
    }
    
}
