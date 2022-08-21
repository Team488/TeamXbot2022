package competition.injection.arm;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.commands.ClimberArmMaintainerCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;
import dagger.BindsInstance;
import dagger.Subcomponent;

@ArmSingleton
@Subcomponent(modules = ArmModule.class)
public abstract class ArmComponent {
    public abstract ArmInstance instance();

    public abstract ClimberArmMaintainerCommand maintainerCommand();

    public abstract ClimberArmSubsystem subsystem();

    public abstract MotorArmStopCommand stopCommand();

    @Subcomponent.Builder
    public interface Builder {
        @BindsInstance
        Builder armInstance(ArmInstance instance);

        ArmComponent build();
    }
}
