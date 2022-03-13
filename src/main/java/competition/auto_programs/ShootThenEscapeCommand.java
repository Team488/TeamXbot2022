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
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class ShootThenEscapeCommand extends SequentialCommandGroup {
    
    @Inject
    public ShootThenEscapeCommand (
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

        // Back up several feet to get the Taxi points
        // stop all the other systems
        SwerveToPointCommand reverse = swerveProvider.get();
        reverse.setRobotRelativeMotion();
        reverse.setTargetPosition(new XYPair(0, -100), 90);
        addCommands(new ParallelCommandGroup(reverse, stopShooterProvider.get()));
    }
}
