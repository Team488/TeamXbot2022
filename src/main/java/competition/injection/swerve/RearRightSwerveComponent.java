package competition.injection.swerve;

import dagger.BindsInstance;
import dagger.Subcomponent;

@RearRightDrive
@Subcomponent(modules = SwerveModule.class)
public abstract class RearRightSwerveComponent extends SwerveComponent {
  @Subcomponent.Builder
  public interface Builder {
    @BindsInstance
    Builder swerveInstance(SwerveInstance instance);

    RearRightSwerveComponent build();
  }
}
