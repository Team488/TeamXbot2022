package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.math.XYPair;

/**
 * The super-aspirational autonomous command that tries to score 4+ cargo in autonomous.
 * Probably will not work, but we can try running it slowly and ramp it up as we gain confidence.
 */
public class MoonshotCommand extends SequentialCommandGroup {

    @Inject
    public MoonshotCommand(
        Provider<FireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider,
        Provider<ShutdownShootingCommandThatEnds> stopShooterProvider,
        Provider<ShutdownCollectionCommandThatEnds> stopCollectionProvider,
        Provider<FullCollectCommand> collectCommandProvider) {

        // Assume we start at the right position. If not, maybe find some way to short-circuit and do nothing.

        // ---------------
        // Pick up the second cargo
        // ---------------

        // To start, we have 1 cargo preloaded. We can go pick up the second one which is quite close.
        // Our starting position should be                       252, 300, 2.
        // A reasonable spot to pickup the second cargo would be 287, 298, 0.
        var swerveToCargoTwo = swerveProvider.get();
        swerveToCargoTwo.setTargetPosition(new XYPair(287, 298), 0);

        // We also need to start collecting.
        var collect = collectCommandProvider.get();

        // Combine the two commands into one group that will end once we are at the target.
        addCommands(new ParallelRaceGroup(swerveToCargoTwo, collect));

        // ---------------
        // Score the first (preloaded) and second (collected) cargo.
    }
}
