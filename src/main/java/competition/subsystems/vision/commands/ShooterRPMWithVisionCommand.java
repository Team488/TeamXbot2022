package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;

public class ShooterRPMWithVisionCommand extends BaseCommand {

    private final VisionSubsystem vision;
    private final ShooterWheelSubsystem shooter;
    
    @Inject
    public ShooterRPMWithVisionCommand(ShooterWheelSubsystem shooter, VisionSubsystem vision) {
        this.vision = vision;
        this.shooter = shooter;
        this.addRequirements(shooter.getSetpointLock());
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if(vision.getFixAcquired()) {
            shooter.setTargetRPM(speedFromPitch(vision.getPitchToHub()));
        }
    }

    public double speedFromPitch(double pitch) {
        return pitch * -31.6 + 1961;
    }
}
