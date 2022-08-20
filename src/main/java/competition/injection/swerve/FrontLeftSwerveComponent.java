package competition.injection.swerve;

import dagger.Subcomponent;

@FrontLeftDrive
@Subcomponent(modules = FrontLeftSwerveModule.class)
public abstract class FrontLeftSwerveComponent extends SwerveComponent {
    @Subcomponent.Builder
    public interface Builder {
        FrontLeftSwerveComponent build();
    }
}
