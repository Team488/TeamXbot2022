package competition.subsystems.conveyer.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.AdvancedXboxButton;
import xbot.common.controls.sensors.XXboxController.XboxButton;

public class ConveyWhileAtSpeedAndDriverSignalCommand extends BaseCommand {
    private final ShooterWheelSubsystem shooter;
    private final ConveyorSubsystem conveyor;
    private final AdvancedXboxButton fireButton;

    @Inject
    public ConveyWhileAtSpeedAndDriverSignalCommand(
            ConveyorSubsystem conveyor,
            ShooterWheelSubsystem shooter,
            OperatorInterface oi) {
        this.shooter = shooter;
        this.conveyor = conveyor;
        fireButton = oi.operatorGamepad.getifAvailable(XboxButton.Back);
        this.addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooter.isMaintainerAtGoal() && fireButton.get()) {
            conveyor.setForward();
        } else {
            conveyor.stop();
        }
    }

}
