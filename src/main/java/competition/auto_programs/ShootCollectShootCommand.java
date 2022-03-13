package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.FireCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class ShootCollectShootCommand extends SequentialCommandGroup {
    
    @Inject
    public ShootCollectShootCommand (
        Provider<FireCommand> fireProvider,
        Provider<SwerveToPointCommand> swerveProvider
    ) {
        FireCommand firstShot = fireProvider.get();
        firstShot.setTargetRPM(TargetRPM.NearShot);
        DelayViaSupplierCommand firstShotTimeout = new DelayViaSupplierCommand(() -> 3.0);
        ParallelRaceGroup firstShotWithTimeout = new ParallelRaceGroup(firstShot, firstShotTimeout);
        addCommands(firstShotWithTimeout);

        SwerveToPointCommand smallReverse = swerveProvider.get();
        smallReverse.setRobotRelativeMotion();
        smallReverse.setTargetPosition(new XYPair(0, -12), 90);
        addCommands(smallReverse);

        SwerveToPointCommand goToBall = swerveProvider.get();
        goToBall.setRobotRelativeMotion();
        goToBall.setTargetPosition(new XYPair(0, -60), -90);
        addCommands(goToBall);

        // collect

        SwerveToPointCommand getToShootingPosition = swerveProvider.get();
        getToShootingPosition.setRobotRelativeMotion();
        getToShootingPosition.setTargetPosition(new XYPair(0, -60), -90);
        addCommands(getToShootingPosition);

        FireCommand secondShot = fireProvider.get();
        secondShot.setTargetRPM(TargetRPM.DistanceShot);
        DelayViaSupplierCommand secondShotTimeout = new DelayViaSupplierCommand(() -> 5.0);
        ParallelRaceGroup secondShotWithTimeout = new ParallelRaceGroup(secondShot, secondShotTimeout);
        addCommands(secondShotWithTimeout);
    }
}
