package competition.subsystems.climber_arm.commands;

import com.google.inject.Inject;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class MotorArmRetractCommand extends BaseCommand{
    public ClimberArmSubsystem armRetract;
    final DoubleProperty motorRetractSpeed;

    @Inject
    public MotorArmRetractCommand(ClimberArmSubsystem armRetract, PropertyFactory pf){
        this.armRetract = armRetract;
        pf.setPrefix(this);
        motorRetractSpeed = pf.createPersistentProperty("retractSpeed", -0.1);
        addRequirements(armRetract);
    }
    @Override
    public void initialize() {
        // 
        log.info("Initializing");
    }

    @Override
    public void execute() {
        // 
        armRetract.setPower(motorRetractSpeed.get());
    }

}