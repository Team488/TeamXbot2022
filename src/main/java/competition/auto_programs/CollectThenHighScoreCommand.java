package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.RecklessFireCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import xbot.common.math.XYPair;

public class CollectThenHighScoreCommand extends SequentialCommandGroup {
    
    @Inject
    CollectThenHighScoreCommand(
        Provider<SwerveToPointCommand> swerveToPointProvider,
        Provider<FullCollectCommand> fullCollectProvider,
        RecklessFireCommand fireRecklessly,
        ShutdownCollectionCommandThatEnds shutdownCollecting,
        ShutdownShootingCommandThatEnds shutdownShooting) {

        //53.5 inches away from the hub wall is the magic number
        // Our program ends 98.1 inches away

        // Go forward 60 inches while collecting

        var goForwardToGetCargo = swerveToPointProvider.get();
        goForwardToGetCargo.setRobotRelativeMotion();
        goForwardToGetCargo.setMaxPower(0.5);
        goForwardToGetCargo.setTargetPosition(new XYPair(0,60), 90);

        var collect = fullCollectProvider.get();

        var driveForwardAndCollect = new ParallelRaceGroup(
            goForwardToGetCargo,
            collect,
            new WaitCommand(3)
        );

        this.addCommands(driveForwardAndCollect);

        // Stop collecting
        this.addCommands(shutdownCollecting);

        // Turn around and get to position
        var moveToShootingPosition = swerveToPointProvider.get();
        moveToShootingPosition.setRobotRelativeMotion();
        moveToShootingPosition.setMaxPower(0.6);
        moveToShootingPosition.setTargetPosition(new XYPair(6, -39.5), -90);


        var moveToShootWithTimeout = new ParallelRaceGroup(
            moveToShootingPosition,
            new WaitCommand(3)
        );

        this.addCommands(moveToShootWithTimeout);

        var fireForAWhile = new ParallelRaceGroup(
            fireRecklessly,
            new WaitCommand(7)
        );

        this.addCommands(fireForAWhile);
    }
}
