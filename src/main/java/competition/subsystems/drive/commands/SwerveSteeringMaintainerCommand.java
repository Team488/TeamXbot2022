package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.PropertyFactory;

public class SwerveSteeringMaintainerCommand extends BaseMaintainerCommand {

    private final SwerveSteeringSubsystem subsystem;

    private final BooleanProperty enableAutoCalibrate;

    @Inject
    public SwerveSteeringMaintainerCommand(SwerveSteeringSubsystem subsystemToMaintain, PropertyFactory pf, CommonLibFactory clf) {
        super(subsystemToMaintain, pf, clf, 0.001, 0.001);
        pf.setPrefix(this);

        this.subsystem = subsystemToMaintain;
        
        this.enableAutoCalibrate = pf.createPersistentProperty("EnableAutomaticMotorControllerCalibration", false);
    }

    @Override
    protected void calibratedMachineControlAction() {
        if (this.subsystem.isUsingMotorControllerPid()) {
            this.subsystem.setMotorControllerPidTarget();
        } else {
            this.subsystem.setPower(this.subsystem.calculatePower());
        }

        if (enableAutoCalibrate.get() && isMaintainerAtGoal()) {
            this.subsystem.calibrateMotorControllerPositionFromCanCoder();
        }
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
        
        if (enableAutoCalibrate.get()) {
            this.subsystem.calibrateMotorControllerPositionFromCanCoder();
        }
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        this.initialize();
    }
}
