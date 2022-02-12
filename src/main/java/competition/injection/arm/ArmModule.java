package competition.injection.arm;

import java.lang.annotation.Annotation;

import com.google.inject.PrivateModule;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.commands.ClimberArmMaintainerCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;

public class ArmModule extends PrivateModule {
    
    private final Class<? extends Annotation> annotation;

    public ArmModule(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override protected void configure() {
        bind(ArmInstance.class).toInstance(new ArmInstance(annotation.getSimpleName()));

        bind(ClimberArmSubsystem.class).annotatedWith(annotation).to(ClimberArmSubsystem.class);
        expose(ClimberArmSubsystem.class).annotatedWith(annotation);

        bind(MotorArmStopCommand.class).annotatedWith(annotation).to(MotorArmStopCommand.class);
        expose(MotorArmStopCommand.class).annotatedWith(annotation);

        bind(ClimberArmMaintainerCommand.class).annotatedWith(annotation).to(ClimberArmMaintainerCommand.class);
        expose(ClimberArmMaintainerCommand.class).annotatedWith(annotation);
    }
}
