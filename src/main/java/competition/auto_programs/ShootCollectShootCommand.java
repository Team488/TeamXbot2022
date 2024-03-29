package competition.auto_programs;

import javax.inject.Inject;
import javax.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.collector_deployment.commands.RetractCollectorCommand;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.drive.commands.StopDriveCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class ShootCollectShootCommand extends SequentialCommandGroup {
    
    /* Auto program which attempts to score two cargo into the lower goal.
     * The robot expects to be up against the lower hub, with its back pointed
     * towards the cargo it needs to collect.
     * 
     * The robot will first score the ball that starts in the conveyor,
     * then it backs up and turns around to collect the cargo behind it.
     * Then it turns around again and attempts to shoot that cargo.
     */

    @Inject
    public ShootCollectShootCommand (
        Provider<FireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider,
        Provider<ShutdownShootingCommandThatEnds> stopShooterProvider,
        FullCollectCommand collectCommand,
        StopCollectorCommand stopCollector,
        RetractCollectorCommand retractCollector,
        ConveyorSubsystem conveyor,
        StopDriveCommand stopDrive
    ) {
        // Mark the conveyor as pre-retracted
        var markConveyorRetracted = new InstantCommand(() -> conveyor.setHasRetracted(true));
        addCommands(markConveyorRetracted);

        // Score the first ball
        FireCommand firstShot = fireProvider.get();
        firstShot.setTargetRPM(TargetRPM.NearShot);
        DelayViaSupplierCommand firstShotTimeout = new DelayViaSupplierCommand(() -> 3.0);
        ParallelRaceGroup firstShotWithTimeout = new ParallelRaceGroup(firstShot, firstShotTimeout);
        addCommands(firstShotWithTimeout);

        // Back up a little bit to make some room before turning around,
        // stop the shooter to conserve a little bit of power
        SwerveToPointCommand smallReverse = swerveProvider.get();
        smallReverse.setRobotRelativeMotion();
        smallReverse.setTargetPosition(new XYPair(0, -24), 90);
        addCommands(new ParallelCommandGroup(smallReverse, stopShooterProvider.get()));

        // Turn around to face towards the cargo
        SwerveToPointCommand turnAround = swerveProvider.get();
        turnAround.setRobotRelativeMotion();
        turnAround.setTargetPosition(new XYPair(), -90);
        addCommands(turnAround);

        // Move towards the target cargo while collecting
        SwerveToPointCommand goToBall = swerveProvider.get();
        goToBall.setRobotRelativeMotion();
        goToBall.setMaxPower(0.75);
        goToBall.setTargetPosition(new XYPair(0, 94), 90);
        DelayViaSupplierCommand collectTimeout = new DelayViaSupplierCommand(() -> 2.5);
        addCommands(new ParallelDeadlineGroup(collectTimeout, goToBall, collectCommand));

        // Retract the collector to a safe position
        addCommands(new ParallelDeadlineGroup(new DelayViaSupplierCommand(() -> 0.1), retractCollector, stopCollector));

        // Point towards the hub again and get close enough to shoot
        SwerveToPointCommand getToShootingPosition = swerveProvider.get();
        getToShootingPosition.setRobotRelativeMotion();
        getToShootingPosition.setTargetPosition(new XYPair(0, -118), -90);
        getToShootingPosition.setMaxPower(0.75);

        var getToShootingPositionWithTimeout = new ParallelRaceGroup(
            getToShootingPosition,
            new DelayViaSupplierCommand(() -> 3.0)
        );
        addCommands(getToShootingPositionWithTimeout);

        // Fire the second shot
        FireCommand secondShot = fireProvider.get();
        secondShot.setTargetRPM(TargetRPM.NearShot);
        DelayViaSupplierCommand secondShotTimeout = new DelayViaSupplierCommand(() -> 5.0);
        ParallelRaceGroup secondShotWithTimeout = new ParallelRaceGroup(secondShot, secondShotTimeout, stopDrive);
        
        addCommands(secondShotWithTimeout);

        // Stop the shooter and collector to conserve power
        addCommands(stopShooterProvider.get());
    }
}
