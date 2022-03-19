package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.log4j.Logger;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.pose.PoseSubsystem.KeyPosition;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CollectThenScoreTwiceCommand extends SequentialCommandGroup {

    private boolean startingAtRightMostPosition = false;
    final PoseSubsystem pose;

    private static Logger log = Logger.getLogger(CollectThenScoreTwiceCommand.class);

    @Inject
    public CollectThenScoreTwiceCommand(PoseSubsystem pose, Provider<FireCommand> fireProvider,
            Provider<SwerveToPointCommand> swerveProvider,
            Provider<ShutdownShootingCommandThatEnds> stopShooterProvider,
            Provider<ShutdownCollectionCommandThatEnds> stopCollectionProvider,
            Provider<FullCollectCommand> collectCommandProvider) {
        this.pose = pose;

        // Assume we start at the right position. If not, maybe find some way to
        // short-circuit and do nothing.

        // ---------------
        // Pick up the second cargo
        // ---------------

        // To start, we have 1 cargo preloaded. We can go pick up the second one which
        // is quite close.
        // Our starting position should be 252, 300, 2.
        // A reasonable spot to pickup the second cargo would be 287, 298, 0.
        var swerveToCargoTwo = swerveProvider.get();
        swerveToCargoTwo.setTargetPosition(pose.getKeyPosition(KeyPosition.CollectSecondCargo),
            pose.getKeyHeading(KeyPosition.CollectSecondCargo).getDegrees());
        swerveToCargoTwo.setMaxPower(0.5);

        // We also need to start collecting.
        var collect = collectCommandProvider.get();

        // Combine the two commands into one group that will end once we are at the
        // target.
        addCommands(new ParallelRaceGroup(swerveToCargoTwo, collect));

        // Wait for a little bit for collection to finish
        addCommands(new WaitCommand(2.5));

        // ---------------
        // Score the first (preloaded) and second (collected) cargo.
        // ---------------

        // Drive to scoring position
        var swerveToScoringPosition = swerveProvider.get();
        swerveToScoringPosition.setTargetPosition(pose.getKeyPosition(KeyPosition.RightHubFacingHub),
                pose.getKeyHeading(KeyPosition.RightHubFacingHub).getDegrees());
        swerveToScoringPosition.setMaxPower(0.5);

        // Stop the collector and shooter
        var stopCollecting = stopCollectionProvider.get();

        addCommands(new ParallelCommandGroup(stopCollecting, swerveToScoringPosition));

        // Fire for at least 5 seconds
        var fire = fireProvider.get();
        fire.setTargetRPM(TargetRPM.NearShot);

        addCommands(new ParallelRaceGroup(fire, new WaitCommand(5)));

        // Shut it all down.
        addCommands(stopShooterProvider.get());
    }

    @Override
    public void initialize() {
        super.initialize();

        if (pose.getDistanceToKeyPosition(KeyPosition.RightFacingOut) > 12) {
            log.warn("This programs requires that it starts at the right-most position");
            startingAtRightMostPosition = false;
        } else {
            startingAtRightMostPosition = true;
        }
    }

    @Override
    public void execute() {
        if (startingAtRightMostPosition) {
            super.execute();
        }
    }
}
