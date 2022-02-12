package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmExtendCommand extends BaseDoubleClimberArmCommand {
    
    final DoubleProperty motorExtendSpeed;

    @Inject
    public MotorArmExtendCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm, PropertyFactory pf){
        super(leftArm, rightArm);
        pf.setPrefix(this);
        motorExtendSpeed = pf.createPersistentProperty("extendSpeed", 0.1);
    }
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        leftArm.setPower(motorExtendSpeed.get());
        rightArm.setPower(motorExtendSpeed.get());
    }
    
}
