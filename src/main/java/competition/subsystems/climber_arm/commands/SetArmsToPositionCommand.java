package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
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
    public SetArmsToPositionCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm, PropertyFactory pf) {
        super(leftArm, rightArm);
        this.leftArm = leftArm;
        this.rightArm = rightArm;

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
