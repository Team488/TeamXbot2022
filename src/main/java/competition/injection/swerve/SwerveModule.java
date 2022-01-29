package competition.injection.swerve;

import java.lang.annotation.Annotation;

import com.google.inject.PrivateModule;

import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveModuleSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;

public class SwerveModule extends PrivateModule {
    
    private final Class<? extends Annotation> annotation;

    public SwerveModule(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override protected void configure() {
        bind(SwerveInstance.class).toInstance(new SwerveInstance(annotation.getSimpleName()));

        bind(SwerveModuleSubsystem.class).annotatedWith(annotation).to(SwerveModuleSubsystem.class);
        expose(SwerveModuleSubsystem.class).annotatedWith(annotation);

        bind(SwerveDriveSubsystem.class).annotatedWith(annotation).to(SwerveDriveSubsystem.class);
        expose(SwerveDriveSubsystem.class).annotatedWith(annotation);

        bind(SwerveSteeringSubsystem.class).annotatedWith(annotation).to(SwerveSteeringSubsystem.class);
        expose(SwerveSteeringSubsystem.class).annotatedWith(annotation);
        
        bind(SwerveSteeringMaintainerCommand.class).annotatedWith(annotation).to(SwerveSteeringMaintainerCommand.class);
        expose(SwerveSteeringMaintainerCommand.class).annotatedWith(annotation);
    }
}
