package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.RecklessFireCommand;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class ShootRecklesslyThenEscapeCommand extends SequentialCommandGroup {
    
    @Inject
    public ShootRecklesslyThenEscapeCommand (
        Provider<RecklessFireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider,
        Provider<ShutdownShootingCommandThatEnds> stopShooterProvider
    ) {
        // Score the first ball
        var firstShot = fireProvider.get();
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
    }
}
