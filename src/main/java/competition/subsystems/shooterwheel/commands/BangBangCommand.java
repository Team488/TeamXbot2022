package competition.subsystems.shooterwheel.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class BangBangCommand extends BaseCommand{

    final ShooterWheelSubsystem wheel;
    final OperatorInterface oi;
    final DoubleProperty maxPowerProp;

    @Inject
    public BangBangCommand(OperatorInterface oi, ShooterWheelSubsystem wheel, PropertyFactory pf) {
        this.oi = oi;
        pf.setPrefix(this);

        maxPowerProp = pf.createEphemeralProperty("Max Power", 0.5);

        this.wheel = wheel;
        this.addRequirements(this.wheel);
    }


    @Override
    public void initialize() {
        log.info("Initializing");
    }

    public void execute(){
        if(wheel.getCurrentRPM() < wheel.getTargetRPM()){
            wheel.setPower(maxPowerProp.get());
        } else if (wheel.getCurrentRPM() >= wheel.getTargetRPM()){
            wheel.setPower(0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        wheel.resetWheel();
    }
} 