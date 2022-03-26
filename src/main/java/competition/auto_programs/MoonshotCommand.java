package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.PrepareToFireCommandThatEnds;
import competition.commandgroups.RetractAndConveyCommand;
import competition.commandgroups.ShutdownCollectionCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import xbot.common.math.FieldPose;
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
        Provider<FullCollectCommand> collectCommandProvider,
        Provider<PrepareToFireCommandThatEnds> prepareToFireProvider,
        Provider<ConveyWhileShooterAtSpeedCommand> conveyWhileAtSpeedProvider,
        PoseSubsystem pose,
        ConveyorSubsystem conveyor,
        Provider<RetractAndConveyCommand> retractAndConveyProvider) {

        // This will only work from one position.
        var setStartingPosition = new InstantCommand(() -> pose.setCurrentPose(new FieldPose(new XYPair(253.3, 301.5), new Rotation2d(0))));
        this.addCommands(setStartingPosition);
        
        // --------------------------
        // Now, orbit the cargo and get ready to collect
        // --------------------------

        var orbitNearSecondCargo = swerveProvider.get();

        orbitNearSecondCargo.setMaxPower(0.25);
        orbitNearSecondCargo.setTargetPosition(new XYPair(303.2, 337.1), -90);

        this.addCommands(new ParallelRaceGroup(
            orbitNearSecondCargo,
            new WaitCommand(1.5)
        ));

        // --------------------------
        // Drop the collector, move forward
        // --------------------------

        var moveToCollectSecondCargo = swerveProvider.get();
        moveToCollectSecondCargo.setTargetPosition(new XYPair(302.1, 297.1), -90);

        var collectSecondCargo = collectCommandProvider.get();
        var collectSecondCargoWithTimeout = new ParallelRaceGroup(
            moveToCollectSecondCargo,
            collectSecondCargo,
            new WaitCommand(1)
        );

        this.addCommands(collectSecondCargoWithTimeout);

        // --------------------------
        // Raise collector, keep conveying for a moment
        // --------------------------

        this.addCommands(
            new InstantCommand(() -> conveyor.stop())
        );

        var retractAndConveySecondCargo = retractAndConveyProvider.get();
        var moveTowardsHub = swerveProvider.get();
        moveTowardsHub.setMaxPower(0.75);
        moveTowardsHub.setTargetPosition(new XYPair(212.1, 305.3), 160);

        var moveTowardsHubFirstStage = new ParallelRaceGroup(
            moveTowardsHub,
            retractAndConveySecondCargo,
            new WaitCommand(1)
        );

        this.addCommands(moveTowardsHubFirstStage);

        // --------------------------
        // Finish motion towards goal while preparing to fire
        // --------------------------

        var finishMovingTowardsHub = swerveProvider.get();
        finishMovingTowardsHub.setMaxPower(0.75);
        finishMovingTowardsHub.setTargetPosition(new XYPair(212.1, 305.3), 160);

        var prepareToFireSecondCargo = prepareToFireProvider.get();
        prepareToFireSecondCargo.setTargetRPM(TargetRPM.NearShot);

        var moveTowardsHubWhilePreparing = new ParallelCommandGroup(
            finishMovingTowardsHub,
            prepareToFireSecondCargo
        );

        this.addCommands(new ParallelRaceGroup(
            moveTowardsHubWhilePreparing,
            new WaitCommand(2)
        ));

        // --------------------------
        // Score two cargo
        // --------------------------

        var scoreTwoCargo = conveyWhileAtSpeedProvider.get();

        this.addCommands(new ParallelRaceGroup(
            scoreTwoCargo,
            new WaitCommand(1.5)
        ));

        // --------------------------
        // Line up for long run
        // --------------------------

        SwerveToPointCommand moveToLandmark = swerveProvider.get();
        var stopShooter = stopShooterProvider.get();

        moveToLandmark.setMaxPower(0.75);
        moveToLandmark.setTargetPosition(new XYPair(301, 300), -90);

        var moveTowardsLandmarkAndStopShooter = new ParallelCommandGroup(
            moveToLandmark,
            stopShooter
        );

        this.addCommands(new ParallelRaceGroup(
            moveTowardsLandmarkAndStopShooter,
            new WaitCommand(1)
        ));

        // --------------------------
        // Move to pre-collection position
        // --------------------------

        var moveToPreCollectionPosition = swerveProvider.get();
        moveToPreCollectionPosition.setMaxPower(0.75);
        moveToPreCollectionPosition.setTargetPosition(new XYPair(254.3, 74.3), -45);

        this.addCommands(new ParallelRaceGroup(
            moveToPreCollectionPosition,
            new WaitCommand(4)
        ));

        // --------------------------
        // Collect third and fourth cargo
        // --------------------------

        var collectThirdAndFourthCargo = collectCommandProvider.get();
        var moveToCollectThirdAndFourthCargo = swerveProvider.get();

        moveToCollectThirdAndFourthCargo.setTargetPosition(new XYPair(264.2, 61.1), -45);
        moveToCollectThirdAndFourthCargo.setMaxPower(0.25);

        this.addCommands(new ParallelRaceGroup(
            moveToCollectThirdAndFourthCargo,
            collectThirdAndFourthCargo,
            new WaitCommand(1)
        ));

        // --------------------------
        // Move back to landmark while retracting and conveying
        // --------------------------

        this.addCommands(
            new InstantCommand(() -> conveyor.stop())
        );

        var moveBackToLandmark = swerveProvider.get();
        moveBackToLandmark.setMaxPower(0.75);
        moveBackToLandmark.setTargetPosition(new XYPair(301, 300), 180);
        var prepareCargoThreeAndFour = retractAndConveyProvider.get();


        this.addCommands(new ParallelRaceGroup(
            moveBackToLandmark,
            prepareCargoThreeAndFour,
            new WaitCommand(3)
        ));

        // --------------------------
        // Move to hub and prepare to fire
        // --------------------------

        var moveToHub = swerveProvider.get();
        moveToHub.setMaxPower(0.75);
        moveToHub.setTargetPosition(new XYPair(212.1, 305.3), 160);
        var prepareToShootThreeAndFour = prepareToFireProvider.get();
        prepareToShootThreeAndFour.setTargetRPM(TargetRPM.NearShot);

        this.addCommands(new ParallelRaceGroup(
            new ParallelCommandGroup(
                moveToHub,
                prepareToShootThreeAndFour),
            new WaitCommand(1)
        ));

        // --------------------------
        // Score the last two cargo
        // --------------------------

        var scoreTwoMoreCargo = conveyWhileAtSpeedProvider.get();

        this.addCommands(new ParallelRaceGroup(
            scoreTwoMoreCargo,
            new WaitCommand(4)
        ));
    }
}
