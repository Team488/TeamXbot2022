package competition.auto_programs;

import com.google.inject.Inject;

import competition.subsystems.collector.commands.IntakeCommand;
import competition.subsystems.collector_deployment.commands.DeployCollectorCommand;
import competition.subsystems.collector_stage_2.CollectorStage2Subsystem;
import competition.subsystems.deploy_hood.commands.HoodDeployCommand;
import competition.subsystems.drive.commands.SwerveToPointCommand;
import competition.subsystems.shooterwheel.commands.BangBangCommand;
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
        HoodDeployCommand deployHood,
        BangBangCommand startShooter
        ) {
            goThreeFeet.setTargetPosition(new XYPair(0, 36), 90);

            ParallelRaceGroup goToBall = 
                new ParallelRaceGroup(deployCommand1, collectBall1, stageTwoCollector1.getForwardCommand(), goThreeFeet);

            this.addCommands(goToBall);

            DelayViaSupplierCommand delay = new DelayViaSupplierCommand(() -> 0.5);

            ParallelRaceGroup collectorBall = 
                new ParallelRaceGroup(deployCommand2, collectBall2, stageTwoCollector2.getForwardCommand(), delay);

            this.addCommands(collectorBall);

            goBackToStart.setTargetPosition(new XYPair(0, 0), 90);
            
            this.addCommands(goBackToStart);
    }
}
