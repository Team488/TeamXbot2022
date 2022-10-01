package competition.commandgroups;

import javax.inject.Inject;

import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * Clears the barrel, spins up to maximum power, and runs the conveyor when the wheel is
 * near maximum speed. Will not stop on its own.
 * Requires: 
 *  -ShooterWheel.SetpointLock
 *  -Conveyor.
 */
public class RecklessFireCommand extends SequentialCommandGroup {

    @Inject
    public RecklessFireCommand(ClearBarrelCommandThatEnds clearBarrel, ShooterWheelSubsystem wheel, ConveyWhileShooterAtSpeedCommand conveyCommand) {
        
        var wheelFullPowerCommand = new RunCommand(() -> wheel.setFullPowerMode(true), wheel.getSetpointLock());
        var fireWhenReady = new ParallelCommandGroup(wheelFullPowerCommand, conveyCommand);

        addCommands(clearBarrel, fireWhenReady);
    }
}