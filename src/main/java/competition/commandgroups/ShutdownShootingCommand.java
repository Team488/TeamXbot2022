package competition.commandgroups;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;

public class ShutdownShootingCommand extends SequentialCommandGroup {

    private static Logger log = Logger.getLogger(ShutdownShootingCommand.class);

    @Inject
    public ShutdownShootingCommand(ConveyorSubsystem conveyor, ShooterWheelSubsystem wheel) {
        DelayViaSupplierCommand waitCommand = new DelayViaSupplierCommand(() -> 1.0);

        // Stop the conveyor immediately, while the shooter wheel is still running.
        // Wait a little bit for any cargo in the main firing path to clear
        // Turn off the shooter wheel.

        this.addCommands(
            new InstantCommand(
                () -> conveyor.stop()),
            waitCommand,
            new InstantCommand(
                () -> wheel.setTargetRPM(0), wheel.getSetpointLock()));
    }

    @Override
    public void initialize() {
        super.initialize();
        log.info("Initializing");
    }
}