package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmRetractCommand extends BaseDoubleClimberArmCommand {
    
    final DoubleProperty motorRetractSpeed;

    @Inject
    public MotorArmRetractCommand(@LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm, PropertyFactory pf){
        super(leftArm, rightArm);
        pf.setPrefix(this);
        motorRetractSpeed = pf.createPersistentProperty("retractSpeed", -0.1);
    }
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        leftArm.setPower(motorRetractSpeed.get());
        rightArm.setPower(motorRetractSpeed.get());
    }

}