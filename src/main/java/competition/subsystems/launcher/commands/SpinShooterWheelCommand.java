package competition.subsystems.launcher.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.launcher.LauncherSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class SpinShooterWheelCommand extends BaseCommand{

    final LauncherSubsystem launch;
    final OperatorInterface oi;
    final DoubleProperty maxPowerProp;

    @Inject
    public SpinShooterWheelCommand(OperatorInterface oi, 
    LauncherSubsystem launch, PropertyFactory pf){
        this.oi = oi;
        pf.setPrefix(this);

        //Temporary max power value
        this.maxPowerProp = pf.createEphemeralProperty("Max Power", 1.0);

        this.launch = launch;
        this.addRequirements(this.launch);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
    
    }
    
}
