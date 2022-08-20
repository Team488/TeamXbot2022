package competition.injection.arm;

import dagger.Module;
import dagger.Provides;

@Module
public class LeftArmModule {
    @LeftArm
    @Provides
    public ArmInstance instance() {
        return new ArmInstance("LeftArm");
    }
}
