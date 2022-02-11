package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.ArmInstance;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseMaintainerCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

public class ClimberArmMaintainerCommand extends BaseMaintainerCommand {

    ClimberArmSubsystem arm;
    private final String armLabel;

    @Inject
    public ClimberArmMaintainerCommand(ArmInstance armInstance, ClimberArmSubsystem arm, PropertyFactory pf, CommonLibFactory clf) {
        super(arm, pf, clf, 1, 0.001);
        this.arm = arm;
        this.armLabel = armInstance.getLabel();
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
    }

    @Override
    protected void calibratedMachineControlAction() {
        
    }

    @Override
    protected void uncalibratedMachineControlAction() {
        super.uncalibratedMachineControlAction();
    }

    @Override
    protected double getHumanInput() {
        return 0;
    }
}