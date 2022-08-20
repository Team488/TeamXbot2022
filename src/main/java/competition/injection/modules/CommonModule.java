package competition.injection.modules;

import javax.inject.Singleton;

import competition.injection.arm.LeftArmComponent;
import competition.injection.arm.RightArmComponent;
import competition.injection.swerve.FrontLeftSwerveComponent;
import competition.injection.swerve.FrontRightSwerveComponent;
import competition.injection.swerve.RearLeftSwerveComponent;
import competition.injection.swerve.RearRightSwerveComponent;
import competition.injection.swerve.SwerveInstance;
import dagger.Module;
import dagger.Provides;

@Module(subcomponents = { LeftArmComponent.class, RightArmComponent.class, FrontLeftSwerveComponent.class,
        FrontRightSwerveComponent.class, RearLeftSwerveComponent.class, RearRightSwerveComponent.class })
public class CommonModule {

    @Provides
    @Singleton
    public LeftArmComponent leftArmComponent(LeftArmComponent.Builder builder) {
        return builder.build();
    }

    @Provides
    @Singleton
    public RightArmComponent rightArmComponent(RightArmComponent.Builder builder) {
        return builder.build();
    }

    @Provides
    @Singleton
    public FrontLeftSwerveComponent frontLeftSwerveComponent(FrontLeftSwerveComponent.Builder builder) {
        return builder
                .build();
    }

    @Provides
    @Singleton
    public FrontRightSwerveComponent frontRightSwerveComponent(FrontRightSwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("FrontRightDrive"))
                .build();
    }

    @Provides
    @Singleton
    public RearLeftSwerveComponent rearLeftSwerveComponent(RearLeftSwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("RearLeftDrive"))
                .build();
    }

    @Provides
    @Singleton
    public RearRightSwerveComponent rearRightSwerveComponent(RearRightSwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("RearRightDrive"))
                .build();
    }
}
