package competition.auto_programs;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.SwerveToPointCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.math.XYPair;

public class DriveFiveCommand extends SequentialCommandGroup{
    @Inject
    public DriveFiveCommand (SwerveToPointCommand goFiveFeet) {
        goFiveFeet.setTargetPosition(new XYPair(0, 60), 90);
        this.addCommands(goFiveFeet);
    }
}
