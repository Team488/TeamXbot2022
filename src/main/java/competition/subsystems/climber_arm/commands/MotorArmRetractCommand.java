package competition.subsystems.climber_arm.commands;

import javax.inject.Inject;

import competition.subsystems.climber_arm.ClimberSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmRetractCommand extends BaseDoubleClimberArmCommand {
    
    final DoubleProperty motorRetractSpeed;

    @Inject
    public MotorArmRetractCommand(ClimberSubsystem climber, PropertyFactory pf){
        super(climber);
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