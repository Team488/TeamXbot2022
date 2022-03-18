package competition.subsystems.conveyer.commands;

import com.google.inject.Inject;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class ConveyWhileShooterAtSpeedCommand extends BaseCommand {
    private final ShooterWheelSubsystem shooter;
    private final ConveyorSubsystem conveyor;

    @Inject
    public ConveyWhileShooterAtSpeedCommand(
        ConveyorSubsystem conveyor,
        ShooterWheelSubsystem shooter) {
            this.shooter = shooter;
            this.conveyor = conveyor;
            this.addRequirements(conveyor);
        }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(shooter.isMaintainerAtGoal()) {
            conveyor.setForward();
        } else {
            conveyor.stop();
        }
    }
    
}
