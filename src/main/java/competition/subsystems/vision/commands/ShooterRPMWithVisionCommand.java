package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;

public class ShooterRPMWithVisionCommand extends BaseCommand {

    private final VisionSubsystem vision;
    private final ShooterWheelSubsystem shooter;
    private Target target = Target.Low;
    
    @Inject
    public ShooterRPMWithVisionCommand(ShooterWheelSubsystem shooter, VisionSubsystem vision) {
        this.vision = vision;
        this.shooter = shooter;
        this.addRequirements(shooter.getSetpointLock());
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {

        if(target == Target.Low) {
            if(vision.getFixAcquired()) {
                shooter.setTargetRPM(speedFromPitchLow(vision.getPitchToHub()));
            } else {
                // if shooter wasn't moving at all, get it going to near shot speed
                if(!(shooter.getTargetRPM() > 0)) {
                    shooter.setTargetRPM(TargetRPM.NearShot);
                }
                // otherwise leave shooter at whatever the last speed it was at
            }
        } else {
            if(vision.getFixAcquired()) {
                shooter.setTargetRPM(speedFromPitchHigh(vision.getPitchToHub()));
            }
        }
    }

    public double speedFromPitchLow(double pitch) {
        return pitch * -31.6 + 1961;
    }

    public double speedFromPitchHigh(double pitch) {
        return pitch * -48.8 + 3224;
    }
}
