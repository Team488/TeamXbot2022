package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
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
    public ClimberSubsystem(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm) {
        this.leftArm = leftArm;
        this.rightArm = rightArm;
    }


}
