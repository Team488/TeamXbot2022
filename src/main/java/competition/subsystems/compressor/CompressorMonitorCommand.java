package competition.subsystems.compressor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.compressor.CompressorSubsystem;

public class CompressorMonitorCommand extends BaseCommand {
    private final CompressorSubsystem compressor;
    private final List<ClimberArmSubsystem> climberArms;
    private final BooleanProperty enableMonitoringProperty;

    @Inject
    public CompressorMonitorCommand(PropertyFactory pf, CompressorSubsystem compressor,
            @LeftArm ClimberArmSubsystem leftArm, @RightArm ClimberArmSubsystem rightArm) {
        pf.setPrefix("CompressorMonitorCommand");
        this.enableMonitoringProperty = pf.createPersistentProperty("Enable Monitoring", true);
        this.compressor = compressor;
        this.climberArms = Arrays.asList(leftArm, rightArm);
        this.addRequirements(compressor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        boolean isClimbingUp = climberArms.stream().anyMatch(climber -> climber.getPower() < 0);

        if (enableMonitoringProperty.get() && isClimbingUp) {
            compressor.disable();
        } else {
            compressor.enable();
        }
    }
}
