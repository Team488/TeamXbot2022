package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * Prepars to fire, then launches cargo at the desired target speed.
 * Will not end on its own.
 * Requires:
 * - ShooterWheel.SetpointLock
 * - Conveyor.
 */
public class FireCommand extends SequentialCommandGroup {

    private final PrepareToFireCommandThatEnds prepare;

    @Inject
    public FireCommand(PrepareToFireCommandThatEnds prepare, ConveyWhileShooterAtSpeedCommand conveyCommand) {
        this.prepare = prepare;
        addCommands(prepare, conveyCommand);
    }

    public void setTargetRPM(TargetRPM target) {
        prepare.setTargetRPM(target);
    }
    
}
