package competition.subsystems.climber_arm;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.injection.arm.LeftArmComponent;
import competition.injection.arm.RightArmComponent;
import xbot.common.command.BaseSubsystem;

/**
 * The ClimberSubsystem acts as the central coordinator for the left and right climber arms.
 * In normal operation, both arms are being managed by their respective Maintainers - this class
 * is responsible for setting position targets for both arms. 
 */
@Singleton
public class ClimberSubsystem extends BaseSubsystem {
    
    final ClimberArmSubsystem leftArm;
    final ClimberArmSubsystem rightArm;

    @Inject
    public ClimberSubsystem(LeftArmComponent leftArm, RightArmComponent rightArm) {
        this.leftArm = leftArm.subsystem();
        this.rightArm = rightArm.subsystem();
    }

    public ClimberArmSubsystem getLeftArm() {
        return leftArm;
    }

    public ClimberArmSubsystem getRightArm() {
        return rightArm;
    }
}
