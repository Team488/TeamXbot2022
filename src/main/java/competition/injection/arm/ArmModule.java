package competition.injection.arm;

import java.lang.annotation.Annotation;

import com.google.inject.PrivateModule;

import competition.subsystems.climber_arm.ClimberArmSubsystem;

public class ArmModule extends PrivateModule {
    
    private final Class<? extends Annotation> annotation;

    public ArmModule(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override protected void configure() {
        bind(ArmInstance.class).toInstance(new ArmInstance(annotation.getSimpleName()));

        bind(ClimberArmSubsystem.class).annotatedWith(annotation).to(ClimberArmSubsystem.class);

        /*
        bind(SwerveModuleSubsystem.class).annotatedWith(annotation).to(SwerveModuleSubsystem.class);
        expose(SwerveModuleSubsystem.class).annotatedWith(annotation);

        bind(SwerveDriveSubsystem.class).annotatedWith(annotation).to(SwerveDriveSubsystem.class);
        expose(SwerveDriveSubsystem.class).annotatedWith(annotation);
        
        bind(SwerveDriveMaintainerCommand.class).annotatedWith(annotation).to(SwerveDriveMaintainerCommand.class);
        expose(SwerveDriveMaintainerCommand.class).annotatedWith(annotation);

        bind(SwerveSteeringSubsystem.class).annotatedWith(annotation).to(SwerveSteeringSubsystem.class);
        expose(SwerveSteeringSubsystem.class).annotatedWith(annotation);
        
        bind(SwerveSteeringMaintainerCommand.class).annotatedWith(annotation).to(SwerveSteeringMaintainerCommand.class);
        expose(SwerveSteeringMaintainerCommand.class).annotatedWith(annotation);*/
    }
}
