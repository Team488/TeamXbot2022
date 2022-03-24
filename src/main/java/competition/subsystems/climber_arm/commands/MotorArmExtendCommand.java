package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmExtendCommand extends BaseDoubleClimberArmCommand {
    
    final DoubleProperty motorExtendSpeed;

    @Inject
    public MotorArmExtendCommand(ClimberSubsystem climber, PropertyFactory pf){
        super(climber);
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
