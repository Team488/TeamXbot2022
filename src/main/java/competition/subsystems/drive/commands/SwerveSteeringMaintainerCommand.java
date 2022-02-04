package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

public class SwerveSteeringMaintainerCommand extends BaseMaintainerCommand {

    private final SwerveSteeringSubsystem subsystem;

    @Inject
    public SwerveSteeringMaintainerCommand(SwerveSteeringSubsystem subsystemToMaintain, PropertyFactory pf, CommonLibFactory clf) {
        super(subsystemToMaintain, pf, clf, 0.001, 0.001);
        this.subsystem = subsystemToMaintain;
    }

    @Override
    protected void calibratedMachineControlAction() {
        this.subsystem.setPower(this.subsystem.calculatePower());
    }

    @Override
    protected double getHumanInput() {
        // never hooked direclty to human input, human input handled by drive
        return 0;
    }

    @Override
    public void initialize() {
        this.subsystem.setTargetValue(this.subsystem.getCurrentValue());
        this.subsystem.setPower(0);
        this.subsystem.resetPid();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        this.initialize();
    }
}
