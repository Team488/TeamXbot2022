package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class RecklessFireCommand extends SequentialCommandGroup {

    @Inject
    public RecklessFireCommand(ClearBarrelCommand clearBarrel, ShooterWheelSubsystem wheel, ConveyorSubsystem conveyor) {
        
        var waitForFastwheel = new WaitUntilCommand(() -> (wheel.getCurrentRPM() > 4800));
        var wheelFullPowerCommand = new RunCommand(() -> wheel.setPower(1), wheel);
        var spinUpWheel = new ParallelRaceGroup(waitForFastwheel, wheelFullPowerCommand);

        addCommands(clearBarrel, spinUpWheel, conveyor.getForwardCommand());
    }
}