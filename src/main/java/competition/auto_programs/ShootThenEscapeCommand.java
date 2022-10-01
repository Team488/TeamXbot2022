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
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class ShootThenEscapeCommand extends SequentialCommandGroup {
    
    @Inject
    public ShootThenEscapeCommand (
        Provider<FireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider,
        Provider<ShutdownShootingCommandThatEnds> stopShooterProvider,
        FullCollectCommand collectCommand,
        StopCollectorCommand stopCollector,
        RetractCollectorCommand retractCollector,
        ConveyorSubsystem conveyor,
        StopDriveCommand stopDrive
    ) {
        var markConveyorRetracted = new InstantCommand(() -> conveyor.setHasRetracted(true));
        addCommands(markConveyorRetracted);

        // Score the first ball
        FireCommand firstShot = fireProvider.get();
        firstShot.setTargetRPM(TargetRPM.NearShot);
        DelayViaSupplierCommand firstShotTimeout = new DelayViaSupplierCommand(() -> 5.0);
        ParallelRaceGroup firstShotWithTimeout = new ParallelRaceGroup(firstShot, firstShotTimeout);
        addCommands(firstShotWithTimeout);

        // Back up several feet to get the Taxi points
        // stop all the other systems
        SwerveToPointCommand reverse = swerveProvider.get();
        reverse.setRobotRelativeMotion();
        reverse.setMaxPower(0.5);
        reverse.setTargetPosition(new XYPair(0, -90), 90);
        addCommands(new ParallelCommandGroup(reverse, stopShooterProvider.get()));
        addCommands(stopDrive);
    }
}
