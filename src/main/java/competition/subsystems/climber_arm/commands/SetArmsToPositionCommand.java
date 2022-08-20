package competition.subsystems.climber_arm.commands;

import javax.inject.Inject;

import competition.injection.arm.LeftArmComponent;
import competition.injection.arm.RightArmComponent;
import competition.subsystems.climber_arm.ClimberArmSubsystem;

import xbot.common.command.BaseSetpointCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class SetArmsToPositionCommand extends BaseSetpointCommand {

    DoubleProperty targetPositionProperty = null;
    final ClimberArmSubsystem leftArm;
    final ClimberArmSubsystem rightArm;

    public enum TargetPosition {
        FullyRetracted,
        ClearCurrentBar,
        FullyExtended,
        EngageNextBar
    }

    @Inject
    public SetArmsToPositionCommand(LeftArmComponent leftArm, RightArmComponent rightArm, PropertyFactory pf) {
        super(leftArm.subsystem(), rightArm.subsystem());
        this.leftArm = leftArm.subsystem();
        this.rightArm = rightArm.subsystem();

        pf.setPrefix(this.getName());
    }

    public SetArmsToPositionCommand setTargetPosition(TargetPosition targetPosition) {
        switch(targetPosition) {
            case FullyRetracted:
                targetPositionProperty = leftArm.positionFullyRetractedProperty;
                break;
            case ClearCurrentBar:
                targetPositionProperty = leftArm.positionClearCurrentBarProperty;
                break;
            case FullyExtended:
                targetPositionProperty = leftArm.positionFullyExtendedProperty;
                break;
            case EngageNextBar:
                targetPositionProperty = leftArm.positionEngageNextBarProperty;
                break;
            default:
                throw new RuntimeException("Unknown target position: " + targetPosition);
        }
        this.setName("SetArmsToPositionCommand: " + targetPosition);
        return this;
    }

    @Override
    public void initialize() {
        log.info("Initializing with target position: " + targetPositionProperty.get());    
        if(targetPositionProperty != null) {
            this.leftArm.setTargetValue(targetPositionProperty.get());
            this.rightArm.setTargetValue(targetPositionProperty.get());
        }
    }

    @Override
    public void execute() {
        // pass
    }

    @Override
    public boolean isFinished() {
        // return instantly, someone else will wait for is at goal
        return true; 
    }
    
}
