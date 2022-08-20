package competition.injection.arm;


import dagger.Subcomponent;

@RightArm
@Subcomponent(modules = RightArmModule.class)
public abstract class RightArmComponent extends ArmComponent {
    @Subcomponent.Builder
    public interface Builder {
        RightArmComponent build();
    }
}