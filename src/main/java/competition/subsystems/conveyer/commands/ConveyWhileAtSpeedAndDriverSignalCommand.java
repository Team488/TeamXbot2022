package competition.subsystems.conveyer.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XXboxController.XboxButton;

public class ConveyWhileAtSpeedAndDriverSignalCommand extends BaseCommand {
    private final ShooterWheelSubsystem shooter;
    private final ConveyorSubsystem conveyor;
    private final OperatorInterface oi;

    @Inject
    public ConveyWhileAtSpeedAndDriverSignalCommand(
            ConveyorSubsystem conveyor,
            ShooterWheelSubsystem shooter,
            OperatorInterface oi) {
        this.shooter = shooter;
        this.conveyor = conveyor;
        this.oi = oi;
        this.addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooter.isMaintainerAtGoal()
                && (oi.operatorGamepad.getPOV() == 0 || oi.operatorGamepad.getXboxButton(XboxButton.Back).get())) {
            conveyor.setForward();
        } else {
            conveyor.stop();
        }
    }

}
