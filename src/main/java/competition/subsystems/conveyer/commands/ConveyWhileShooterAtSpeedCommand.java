package competition.subsystems.conveyer.commands;

import com.google.inject.Inject;

import competition.subsystems.conveyer.ConveyorSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XTimer;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class ConveyWhileShooterAtSpeedCommand extends BaseCommand {
    private final ShooterWheelSubsystem shooter;
    private final ConveyorSubsystem conveyor;

    double greenLightTime = -100;
    final Latch greenLightLatch;
    private final DoubleProperty commitConveyorDuration;

    @Inject
    public ConveyWhileShooterAtSpeedCommand(
        ConveyorSubsystem conveyor,
        ShooterWheelSubsystem shooter,
        PropertyFactory pf) {
            this.shooter = shooter;
            this.conveyor = conveyor;
            this.addRequirements(conveyor);

            greenLightLatch = new Latch(true, EdgeType.RisingEdge, edge -> {
                if(edge == EdgeType.RisingEdge) {
                    greenLightTime = XTimer.getFPGATimestamp();
                    log.info("Shooter latched! Target: " + shooter.getTargetRPM() + ", Current: " + shooter.getCurrentRPM());
                }
            });

            pf.setPrefix(this);
            commitConveyorDuration = pf.createPersistentProperty("CommitDuration", 0.6);
        }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {

        // check if we are in the "must commit time"
        boolean forcedToRun = XTimer.getFPGATimestamp() - greenLightTime < commitConveyorDuration.get();
        boolean shooterAtGoal = shooter.isMaintainerAtGoal();
        
        greenLightLatch.setValue(shooterAtGoal && !forcedToRun);

        if(shooterAtGoal || forcedToRun) {
            conveyor.setForward();

        } else {
            conveyor.stop();
        }
    }
    
}
