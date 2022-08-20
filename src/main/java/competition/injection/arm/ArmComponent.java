package competition.injection.arm;

import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_arm.commands.ClimberArmMaintainerCommand;
import competition.subsystems.climber_arm.commands.MotorArmStopCommand;

public abstract class ArmComponent {
    public abstract ArmInstance instance();

    public abstract ClimberArmMaintainerCommand maintainerCommand();

    public abstract ClimberArmSubsystem subsystem();

    public abstract MotorArmStopCommand stopCommand();
}
