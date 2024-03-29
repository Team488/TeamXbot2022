package competition.auto_programs;

import javax.inject.Inject;

import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.collector_stage_2.CollectorStage2Subsystem;
import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.XYPair;

public class GoCollectComebackCommand extends SequentialCommandGroup {
    @Inject
    public GoCollectComebackCommand (
        SwerveToPointCommand goThreeFeet, 
        IntakeCommand collectBall1, 
        CollectorStage2Subsystem stageTwoCollector1,
        DeployCollectorCommand deployCommand1,
        IntakeCommand collectBall2, 
        CollectorStage2Subsystem stageTwoCollector2,
        DeployCollectorCommand deployCommand2,
        SwerveToPointCommand goBackToStart,
        ShooterWheelSubsystem shooter,
        ConveyorSubsystem conveyer
        ) {
            goThreeFeet.setTargetPosition(new XYPair(0, 36), 90);
            goThreeFeet.setRobotRelativeMotion();

            InstantCommand hotDogRoller = new InstantCommand(() -> shooter.setTargetRPM(TargetRPM.HotDogRoller));
            InstantCommand distanceShot = new InstantCommand(() -> shooter.setTargetRPM(TargetRPM.DistanceShot));

            ParallelRaceGroup goToBall = 
                new ParallelRaceGroup(deployCommand1, collectBall1, stageTwoCollector1.getForwardCommand(), conveyer.getForwardCommand(), goThreeFeet);

            this.addCommands(hotDogRoller);
            this.addCommands(goToBall);

            DelayViaSupplierCommand delay = new DelayViaSupplierCommand(() -> 0.5);

            ParallelRaceGroup collectorBall = 
                new ParallelRaceGroup(deployCommand2, collectBall2, stageTwoCollector2.getForwardCommand(), conveyer.getForwardCommand(), delay);

            this.addCommands(collectorBall);

            goBackToStart.setTargetPosition(new XYPair(0, 0), 90);
            goBackToStart.setRobotRelativeMotion();
            
            this.addCommands(goBackToStart);
            this.addCommands(conveyer.getStopCommand());

            DelayViaSupplierCommand delay2 = new DelayViaSupplierCommand(() -> 0.5);
            this.addCommands(new ParallelDeadlineGroup(delay2, distanceShot));
            this.addCommands(conveyer.getForwardCommand());


    }
}
