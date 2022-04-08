package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XXboxController;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class ShooterRPMWithVisionCommand extends BaseCommand {

    public static final double LOW_GOAL_SLOPE = -31.6;
    public static final double HIGH_GOAL_SLOPE = -48.8;

    private final VisionSubsystem vision;
    private final ShooterWheelSubsystem shooter;
    private Target target = Target.Low;
    private final XXboxController operatorGamepad;

    // outside of these ranges the robot physically can't score successfully these shots.
    // for example, too close to the hub high shots will just whack against the side.
    private final double lowMinPitch = -1.4;
    private final double lowMaxPitch = 22;
    private final double highMinPitch = -1;
    private final double highMaxPitch = 9;

    private final DoubleProperty lowBelowMinSlope;
    private final DoubleProperty lowAboveMaxSlope;
    private final DoubleProperty highBelowMinSlope;
    private final DoubleProperty highAboveMaxSlope;
    
    @Inject
    public ShooterRPMWithVisionCommand(
        ShooterWheelSubsystem shooter,
        VisionSubsystem vision,
        OperatorInterface oi,
        PropertyFactory pf
    ) {
        this.vision = vision;
        this.shooter = shooter;
        this.operatorGamepad = oi.operatorGamepad;
        this.addRequirements(shooter.getSetpointLock());

        pf.setPrefix(this);

        lowBelowMinSlope = pf.createPersistentProperty("Low/Slope below min pitch", -31.6);
        lowAboveMaxSlope = pf.createPersistentProperty("Low/Slope above max pitch", -31.6);

        highBelowMinSlope = pf.createPersistentProperty("High/Slope below min pitch", -48.8);
        highAboveMaxSlope = pf.createPersistentProperty("High/Slope above max pitch", -48.8);
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
            } else {
                // if shooter wasn't moving at all, get it going to near shot speed
                if(!(shooter.getTargetRPM() > 0)) {
                    shooter.setTargetRPM(TargetRPM.DistanceShot);
                }
                // otherwise leave shooter at whatever the last speed it was at
            }
        }

        // rumble operator gamepad if it's a shot that can't be made
        if(!canMakeShot()) {
            operatorGamepad.getRumbleManager().rumbleGamepad(0.2, 0.01);
        } else {
            operatorGamepad.getRumbleManager().stopGamepadRumble();
        }
    }

    public boolean canMakeShot() {
        if(vision.getFixAcquired()) {
            if(target == Target.Low) {
                return vision.getPitchToHub() >= lowMinPitch && vision.getPitchToHub() <= lowMaxPitch;
            } else {
                return vision.getPitchToHub() >= highMinPitch && vision.getPitchToHub() <= highMaxPitch;
            }
        } else {
            return false;
        }
        
    }

    public double speedFromPitchLow(double pitch) {
        if (pitch < lowMinPitch) {
            return speedFromPitchLow(lowMinPitch) + (pitch - lowMinPitch) * this.lowBelowMinSlope.get();
        } else if (pitch > lowMaxPitch) {
            return speedFromPitchLow(lowMaxPitch) + (pitch - lowMaxPitch) * this.lowAboveMaxSlope.get();
        } else {
            return pitch * LOW_GOAL_SLOPE + 1961;
        }
    }

    public double speedFromPitchHigh(double pitch) {
        if (pitch < highMinPitch) {
            return speedFromPitchHigh(highMinPitch) + (pitch - highMinPitch) * this.highBelowMinSlope.get();
        } else if (pitch > highMaxPitch) {
            return speedFromPitchHigh(highMaxPitch) + (pitch - highMaxPitch) * this.highAboveMaxSlope.get();
        } else {
            return pitch * HIGH_GOAL_SLOPE + 3224;
        }
    }

    public void setLowGoalBelowMinPitchSlope(double slope) {
        this.lowBelowMinSlope.set(slope);
    }

    public void setLowGoalAboveMaxPitchSlope(double slope) {
        this.lowAboveMaxSlope.set(slope);
    }

    public void setHighGoalBelowMinPitchSlope(double slope) {
        this.highBelowMinSlope.set(slope);
    }

    public void setHighGoalAboveMaxPitchSlope(double slope) {
        this.highAboveMaxSlope.set(slope);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setTargetRPM(0);
        operatorGamepad.getRumbleManager().stopGamepadRumble();
    }
}
