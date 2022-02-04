package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

public class SwerveDriveMaintainerCommand extends BaseMaintainerCommand {

    private final SwerveDriveSubsystem subsystem;

    @Inject
    public SwerveDriveMaintainerCommand(SwerveDriveSubsystem subsystemToMaintain, PropertyFactory pf, CommonLibFactory clf) {
        super(subsystemToMaintain, pf, clf, 0.001, 0.001);
        this.subsystem = subsystemToMaintain;
    }

    @Override
    protected void calibratedMachineControlAction() {
        this.subsystem.setPower(this.subsystem.getTargetValue());
    }

    @Override
    protected double getHumanInput() {
        // never hooked direclty to human input, human input handled by drive
        return 0;
    }

    @Override
    public void initialize() {
        this.subsystem.setTargetValue(0);
        this.subsystem.setPower(0);
        this.subsystem.resetPid();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        this.initialize();
    }
}
