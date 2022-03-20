package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.conveyer.commands.ConveyWhileAtSpeedAndDriverSignalCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * Clears the barrel, spins up to maximum power, and runs the conveyor when the
 * wheel is
 * near maximum speed. Will not stop on its own.
 * Requires:
 * -ShooterWheel.SetpointLock
 * -Conveyor.
 */
public class DriverRecklessFireCommand extends SequentialCommandGroup {

    @Inject
    public DriverRecklessFireCommand(ClearBarrelCommandThatEnds clearBarrel, ShooterWheelSubsystem wheel,
            ConveyWhileAtSpeedAndDriverSignalCommand conveyCommand) {

        var wheelFullPowerCommand = new RunCommand(() -> wheel.setFullPowerMode(true), wheel.getSetpointLock());
        var fireWhenReady = new ParallelCommandGroup(wheelFullPowerCommand, conveyCommand);

        addCommands(clearBarrel, fireWhenReady);
    }
}