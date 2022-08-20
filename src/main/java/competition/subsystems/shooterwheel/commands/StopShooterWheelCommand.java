package competition.subsystems.shooterwheel.commands;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class StopShooterWheelCommand extends BaseCommand{

    final ShooterWheelSubsystem wheel;
    final OperatorInterface oi;

    @Inject
    public StopShooterWheelCommand(OperatorInterface oi, ShooterWheelSubsystem wheel){
        this.oi = oi;
        this.wheel = wheel;
        this.addRequirements(this.wheel);
    }

    @Override
    public void initialize(){
        log.info("Initializing");
        wheel.resetWheel();
    }

    public void execute(){
        wheel.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}