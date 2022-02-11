package competition.subsystems.launcher.commands;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.launcher.LauncherSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

public class LauncherMaintainerCommand extends BaseMaintainerCommand {
    
    final LauncherSubsystem launch;
    final OperatorInterface oi;

    public LauncherMaintainerCommand (LauncherSubsystem launch, OperatorInterface oi, 
    PropertyFactory pf, CommonLibFactory clf){
        super(launch, pf, clf, 50, 0.25); // ask John about the 50 and .25 bc values from 2020 code

        this.oi = oi;
        this.launch = launch;
        this.addRequirements(this.launch);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        launch.setCurrentLimits();
        launch.configurePID();
    }

    @Override
    protected void calibratedMachineControlAction() {
        double speed = launch.getTargetRPM();
        launch.setPidSetpoint(speed);
    }

    @Override
    protected void initializeMachineControlAction() {
        launch.resetPID();
        super.initializeMachineControlAction();
    }

    @Override
    protected double getHumanInput() {
        // Currently, never hooked into human input. This may change.
        return 0;
    }

    @Override
    public void end(boolean interrupted) {
        launch.resetWheel();
    }

}
