package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class FireCommand extends SequentialCommandGroup {

    private final PrepareToFireCommand prepare;

    @Inject
    public FireCommand(PrepareToFireCommand prepare, ConveyWhileShooterAtSpeedCommand conveyCommand) {
        this.prepare = prepare;
        addCommands(prepare, conveyCommand);
    }

    public void setTargetRPM(TargetRPM target) {
        prepare.setTargetRPM(target);
    }
    
}
