package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.PrepareToFireCommandThatEnds;
import competition.commandgroups.RetractAndConveyCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.RotateToVisionTargetCommand;
import competition.subsystems.drive.commands.StopDriveCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.commands.ShooterRPMWithVisionCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import xbot.common.math.XYPair;

public class CollectThenHighScoreCommand extends SequentialCommandGroup {
    
    @Inject
    CollectThenHighScoreCommand(
        Provider<SwerveToPointCommand> swerveToPointProvider,
        Provider<FullCollectCommand> fullCollectProvider,
        ShutdownCollectionCommandThatEnds shutdownCollecting,
        ShutdownShootingCommandThatEnds shutdownShooting,
        PrepareToFireCommandThatEnds prepareforHigh,
        ConveyWhileShooterAtSpeedCommand conveyWhenReady,
        RetractAndConveyCommand retractAndConvey,
        RotateToVisionTargetCommand visionRotate,
        ShooterRPMWithVisionCommand visionRPM,
        StopDriveCommand middleStop,
        DriveSubsystem drive) {

        //53.5 inches away from the hub wall is the magic number
        // Our program ends 98.1 inches away

        // Most recent magic number - 7 feet 5 inches

        // Go forward 60 inches while collecting

        var goForwardToGetCargo = swerveToPointProvider.get();
        goForwardToGetCargo.setRobotRelativeMotion();
        goForwardToGetCargo.setMaxPower(0.5);
        goForwardToGetCargo.setTargetPosition(new XYPair(0,60), 90);

        var collect = fullCollectProvider.get();

        var driveForwardAndCollect = new ParallelRaceGroup(
            new ParallelCommandGroup(goForwardToGetCargo,collect),
            new WaitCommand(3)
        );

        this.addCommands(driveForwardAndCollect);

        // Stop collecting
        this.addCommands(
            shutdownCollecting,
            new ParallelRaceGroup(middleStop, new WaitCommand(0.1))
        );

        // Turn around and get to position
        var moveToShootingPosition = swerveToPointProvider.get();
        moveToShootingPosition.setRobotRelativeMotion();
        moveToShootingPosition.setMaxPower(0.5);
        //moveToShootingPosition.setTargetPosition(new XYPair(6, -39.5), -90);
        moveToShootingPosition.setTargetPosition(new XYPair(0, -12), -90);

        prepareforHigh.setTargetRPM(TargetRPM.DistanceShot);

        var moveToShootWithTimeout = new ParallelRaceGroup(
            new ParallelCommandGroup(prepareforHigh, moveToShootingPosition),
            new WaitCommand(3)
        );

        this.addCommands(moveToShootWithTimeout);

        SequentialCommandGroup visionAdjustAndShoot = new SequentialCommandGroup(
            new InstantCommand(() -> drive.stop()),
            visionRotate,
            conveyWhenReady
        );

        visionRPM.setTarget(Target.High);

        ParallelRaceGroup shotWithVisionAdjustedRPM = new ParallelRaceGroup(
            visionAdjustAndShoot,
            visionRPM,
            new WaitCommand(5)
        );
        this.addCommands(shotWithVisionAdjustedRPM);
    }
}
