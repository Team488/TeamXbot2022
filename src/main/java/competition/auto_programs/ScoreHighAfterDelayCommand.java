package competition.auto_programs;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.commandgroups.PrepareToFireCommandThatEnds;
import competition.commandgroups.ShutdownShootingCommandThatEnds;
import competition.subsystems.conveyer.commands.ConveyWhileShooterAtSpeedCommand;
import competition.subsystems.drive.commands.RotateToVisionTargetCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.commands.ShooterRPMWithVisionCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;
import xbot.common.properties.PropertyFactory;

public class ScoreHighAfterDelayCommand extends SequentialCommandGroup {
    
    @Inject
    ScoreHighAfterDelayCommand(
        PropertyFactory pf,
        Provider<SwerveToPointCommand> swerveToPointProvider,
        ShutdownShootingCommandThatEnds shutdownShooting,
        PrepareToFireCommandThatEnds prepareforHigh,
        ConveyWhileShooterAtSpeedCommand conveyWhenReady,
        RotateToVisionTargetCommand visionRotate,
        ShooterRPMWithVisionCommand visionRPM
    ) {
        pf.setPrefix(getName());

        // Wait for a few seconds
        var initialDelayProp = pf.createPersistentProperty("Initial delay", 5.0);
        var initialDelay = new DelayViaSupplierCommand(() -> initialDelayProp.get());
        addCommands(initialDelay);

        // Get out of the tarmac area
        var escape = swerveToPointProvider.get();
        escape.setRobotRelativeMotion();
        escape.setMaxPower(0.5);
        escape.setTargetPosition(new XYPair(0, -60), 90);
        addCommands(new ParallelRaceGroup(
            escape,
            new WaitCommand(3)
        ));

        prepareforHigh.setTargetRPM(TargetRPM.DistanceShot);

        this.addCommands(prepareforHigh);

        SequentialCommandGroup visionAdjustAndShoot = new SequentialCommandGroup(
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

        this.addCommands(shutdownShooting);
    }
}
