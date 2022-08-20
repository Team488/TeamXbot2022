package competition.injection.arm;

import dagger.Subcomponent;

@LeftArm
@Subcomponent(modules = LeftArmModule.class)
public abstract class LeftArmComponent extends ArmComponent {
    @Subcomponent.Builder
    public interface Builder {
        LeftArmComponent build();
    }
}