package competition.commandgroups;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.conveyer.commands.ConveyWhileAtSpeedAndDriverSignalCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.commands.ShooterRPMWithVisionCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

/**
 * Prepares the robot to launch cargo. Clears the barrel,
 * spins up the wheel to a target RPM, and waits for either the
 * wheel to reach the target speed or a few seconds, whichever comes first.
 * Requires:
 * - ShooterWheel.SetpointLock
 * - Conveyor.
 */
public class HumanShootWithVision extends SequentialCommandGroup {

    private final DoubleProperty conveyorReverseTimeProp;
    private Target target = Target.Low;

    private static Logger log = Logger.getLogger(PrepareToFireCommandThatEnds.class);

    @Inject
    HumanShootWithVision(ShooterWheelSubsystem wheel,
            ConveyorSubsystem conveyor, PropertyFactory pf, ShooterRPMWithVisionCommand shooterRPMWithVisionCommand,
            ConveyWhileAtSpeedAndDriverSignalCommand conveyCommand) {
        pf.setPrefix(this.getName());
        conveyorReverseTimeProp = pf.createPersistentProperty("Conveyor Reverse Time", 0.2);

        // We will need to reverse the conveyor for a litle bit before spinning up the
        // shooter to make sure
        // nothing is touching the wheel
        var setHotDog = new RunCommand(() -> wheel.setTargetRPM(TargetRPM.HotDogRoller), wheel.getSetpointLock());
        var reverseConveyor = new ParallelRaceGroup(
                setHotDog,
                conveyor.getReverseCommand(),
                new DelayViaSupplierCommand(() -> conveyor.getHasRetracted() ? 0 : conveyorReverseTimeProp.get()));

        var stopConveyor = new InstantCommand(() -> conveyor.stop(), conveyor);
        var markConveyorRetracted = new InstantCommand(() -> conveyor.setHasRetracted(true));
        var setTargetOnCommand = new InstantCommand(() -> shooterRPMWithVisionCommand.setTarget(target));

        addCommands(setTargetOnCommand, reverseConveyor, stopConveyor, markConveyorRetracted,
                new ParallelCommandGroup(shooterRPMWithVisionCommand, conveyCommand));
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        super.initialize();
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}