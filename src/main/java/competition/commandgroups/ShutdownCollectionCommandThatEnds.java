package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;

/**
 * Stops the collector, retracts the collector, and safely stops the conveyor &
 * wheel.
 * Will end after a short delay controlled by ShutdownShootingCommand.
 * Requires:
 * - Collector.
 * - CollectorDeployment.
 * - Conveyor.
 * - ShooterWheel.SetpointLock.
 */
public class ShutdownCollectionCommandThatEnds extends ParallelRaceGroup {

    @Inject
    public ShutdownCollectionCommandThatEnds(ShutdownShootingCommandThatEnds shutdownShooting,
            CollectorSubsystem collector, CollectorDeploymentSubsystem collectorDeployment) {

        // Stop the conveyor immediately, while the shooter wheel is still running.
        // Wait a little bit for any cargo in the main firing path to clear
        // Turn off the shooter wheel.

        this.addCommands(
                shutdownShooting,
                new RunCommand(() -> collector.stop(), collector),
                new RunCommand(() -> collectorDeployment.retract(), collectorDeployment));
    }
}