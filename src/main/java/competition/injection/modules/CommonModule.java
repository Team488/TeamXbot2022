package competition.injection.modules;

import javax.inject.Singleton;

import competition.injection.arm.ArmComponent;
import competition.injection.arm.ArmInstance;
import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.injection.swerve.SwerveComponent;
import competition.injection.swerve.SwerveInstance;
import dagger.Module;
import dagger.Provides;

@Module(subcomponents = { ArmComponent.class, SwerveComponent.class })
public class CommonModule {

    @Provides
    @Singleton
    public @LeftArm ArmComponent leftArmComponent(ArmComponent.Builder builder) {
        return builder
                .armInstance(new ArmInstance("LeftArm"))
                .build();
    }

    @Provides
    @Singleton
    public @RightArm ArmComponent rightArmComponent(ArmComponent.Builder builder) {
        return builder
                .armInstance(new ArmInstance("RightArm"))
                .build();
    }

    @Provides
    @Singleton
    public @FrontLeftDrive SwerveComponent frontLeftSwerveComponent(SwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("FrontLeftDrive"))
                .build();
    }

    @Provides
    @Singleton
    public @FrontRightDrive SwerveComponent frontRightSwerveComponent(SwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("FrontRightDrive"))
                .build();
    }

    @Provides
    @Singleton
    public @RearLeftDrive SwerveComponent rearLeftSwerveComponent(SwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("RearLeftDrive"))
                .build();
    }

    @Provides
    @Singleton
    public @RearRightDrive SwerveComponent rearRightSwerveComponent(SwerveComponent.Builder builder) {
        return builder
                .swerveInstance(new SwerveInstance("RearRightDrive"))
                .build();
    }
}
