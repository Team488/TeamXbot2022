package competition.subsystems.shooterwheel.commands;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.logic.HumanVsMachineDecider.HumanVsMachineDeciderFactory;
import xbot.common.properties.PropertyFactory;

public class ShooterWheelMaintainerCommand extends BaseMaintainerCommand {
   
    final ShooterWheelSubsystem wheel;
    final OperatorInterface oi;

    @Inject
    public ShooterWheelMaintainerCommand(OperatorInterface oi, ShooterWheelSubsystem wheel, PropertyFactory pf, HumanVsMachineDeciderFactory hvmFactory){
        super(wheel, pf, hvmFactory, 50, 0.15);
        this.oi = oi;
        this.wheel = wheel;
        this.addRequirements(this.wheel);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        wheel.setCurrentLimits();
        wheel.configurePID();
    }
    
    @Override
    protected void calibratedMachineControlAction() {
        double speed = wheel.getTargetRPM();

        if (wheel.isInFullPowerMode()) {
            wheel.setPower(1);
        } else {
            wheel.setPidSetpoint(speed);
        }
    }

    @Override
    protected void initializeMachineControlAction() {
        wheel.resetPID();
        super.initializeMachineControlAction();
    }

    @Override
    protected double getHumanInput() {
        // Currently, never hooked into human input. This may change.
        return 0;
    }

    @Override
    public void end(boolean interrupted) {
        wheel.resetWheel();
    }

    @Override
    protected boolean isMaintainerAtGoal() {
        if (wheel.isInFullPowerMode()) {
            return wheel.getCurrentRPM() > 4800;
        } else {
            return super.isMaintainerAtGoal();
        }
    }

    @Override
    protected boolean getErrorWithinTolerance() {

        double tolerance = 200;
        if (wheel.getTargetValue() < 2000) {
            tolerance = wheel.getShortRangeErrorTolerance();
        } else {
            tolerance = wheel.getLongRangeErrorTolerance();
        }

        if (Math.abs(wheel.getCurrentValue() - wheel.getTargetValue()) < tolerance) {
            return true;
        }
        return false;
    }

}
