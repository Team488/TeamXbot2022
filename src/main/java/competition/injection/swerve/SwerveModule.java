package competition.injection.swerve;

import java.lang.annotation.Annotation;

import com.google.inject.PrivateModule;

import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveModuleSubsystem;
import competition.subsystems.drive.swerve.SwerveRotationSubsystem;

public class SwerveModule extends PrivateModule {
    
    private final Class<? extends Annotation> annotation;

    public SwerveModule(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override protected void configure() {
        bind(SwerveModuleSubsystem.class).annotatedWith(annotation).to(SwerveModuleSubsystem.class);
        expose(SwerveModuleSubsystem.class).annotatedWith(annotation);

        // bind individual components
        bind(SwerveInstance.class).toInstance(new SwerveInstance(annotation.getSimpleName()));
        bind(SwerveDriveSubsystem.class);
        bind(SwerveRotationSubsystem.class);
    }
}
