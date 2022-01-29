package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmExtendCommand extends BaseCommand{
    public ClimberArmSubsystem armExtends;
    final DoubleProperty motorExtendSpeed;

    @Inject
    public MotorArmExtendCommand(ClimberArmSubsystem armExtends, PropertyFactory pf){
        armExtends = this.armExtends;
        motorExtendSpeed = pf.createPersistentProperty("extendSpeed", 0.1);
        addRequirements(armExtends);
    }
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        armExtends.setPower(motorExtendSpeed.get());
    }
    
}
