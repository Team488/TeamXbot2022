package competition.injection.swerve;

import dagger.Module;
import dagger.Provides;

@Module
public class FrontLeftSwerveModule {
    @FrontLeftDrive
    @Provides
    public SwerveInstance instance() {
        return new SwerveInstance("FrontLeftDrive");
    }
}
