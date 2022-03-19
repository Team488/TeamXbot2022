package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * The super-aspirational autonomous command that tries to score 4+ cargo in autonomous.
 * Probably will not work, but we can try running it slowly and ramp it up as we gain confidence.
 */
public class MoonshotCommand extends SequentialCommandGroup {

    @Inject
    public MoonshotCommand(
        CollectThenScoreTwiceCommand collectThenScoreTwice,
        Provider<FireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider,
        Provider<ShutdownShootingCommandThatEnds> stopShooterProvider,
        Provider<ShutdownCollectionCommandThatEnds> stopCollectionProvider,
        Provider<FullCollectCommand> collectCommandProvider) {

        // The first part is the same as collect & Score twice
        addCommands(collectThenScoreTwice);
    }
}
