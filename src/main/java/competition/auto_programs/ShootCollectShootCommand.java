package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.commandgroups.FullCollectCommand;
import competition.commandgroups.ShutdownShootingCommand;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.collector_deployment.commands.RetractCollectorCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
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
        Provider<ShutdownShootingCommand> stopShooterProvider,
        FullCollectCommand collectCommand,
        StopCollectorCommand stopCollector,
        RetractCollectorCommand retractCollector
    ) {
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
        smallReverse.setTargetPosition(new XYPair(0, -12), 90);
        addCommands(new ParallelCommandGroup(smallReverse, stopShooterProvider.get()));

        // Turn around and move towards the target cargo while collecting
        SwerveToPointCommand goToBall = swerveProvider.get();
        goToBall.setRobotRelativeMotion();
        goToBall.setTargetPosition(new XYPair(0, -60), -90);
        DelayViaSupplierCommand collectTimeout = new DelayViaSupplierCommand(() -> 3.0);
        addCommands(new ParallelDeadlineGroup(collectTimeout, goToBall, collectCommand));

        // Point towards the hub again and get close enough to shoot,
        // while retracting the collector to a safe position
        SwerveToPointCommand getToShootingPosition = swerveProvider.get();
        getToShootingPosition.setRobotRelativeMotion();
        getToShootingPosition.setTargetPosition(new XYPair(0, -60), -90);
        addCommands(new ParallelRaceGroup(getToShootingPosition, stopCollector, retractCollector));

        // Fire the second shot
        FireCommand secondShot = fireProvider.get();
        secondShot.setTargetRPM(TargetRPM.DistanceShot);
        DelayViaSupplierCommand secondShotTimeout = new DelayViaSupplierCommand(() -> 5.0);
        ParallelRaceGroup secondShotWithTimeout = new ParallelRaceGroup(secondShot, secondShotTimeout);
        addCommands(secondShotWithTimeout);

        // Stop the shooter and collector to conserve power
        addCommands(stopShooterProvider.get());
    }
}
