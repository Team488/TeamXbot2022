package competition.injection.swerve;

import dagger.BindsInstance;
import dagger.Subcomponent;

@RearLeftDrive
@Subcomponent(modules = SwerveModule.class)
public abstract class RearLeftSwerveComponent extends SwerveComponent {
  @Subcomponent.Builder
  public interface Builder {
    @BindsInstance
    Builder swerveInstance(SwerveInstance instance);

    RearLeftSwerveComponent build();
  }
}
