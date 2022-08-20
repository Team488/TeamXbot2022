package competition.injection.swerve;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FrontRightDrive
@Subcomponent(modules = SwerveModule.class)
public abstract class FrontRightSwerveComponent extends SwerveComponent {
    @Subcomponent.Builder
    public interface Builder {
        @BindsInstance
        Builder swerveInstance(SwerveInstance instance);

        FrontRightSwerveComponent build();
    }
}
