package competition.injection.arm;

import dagger.Module;
import dagger.Provides;

@Module
public class RightArmModule {
    @RightArm
    @Provides
    public ArmInstance instance() {
        return new ArmInstance("RightArm");
    }
}
