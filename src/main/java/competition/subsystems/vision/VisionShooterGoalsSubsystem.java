package competition.subsystems.vision;

import com.google.inject.Inject;

import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import xbot.common.command.BaseSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

public class VisionShooterGoalsSubsystem extends BaseSubsystem {
    
    // outside of these ranges the robot physically can't score successfully these shots.
    // for example, too close to the hub high shots will just whack against the side.
    public static final double HIGH_MAX_PITCH = 9;
    public static final double HIGH_MIN_PITCH = -1;
    public static final double LOW_MAX_PITCH = 22;
    public static final double LOW_MIN_PITCH = -1.4;

    public static final double HIGH_GOAL_SLOPE = -48.8;
    public static final double LOW_GOAL_SLOPE = -31.6;

    private final DoubleProperty lowBelowMinSlope;
    private final DoubleProperty lowAboveMaxSlope;
    private final DoubleProperty highBelowMinSlope;
    private final DoubleProperty highAboveMaxSlope;

    private final DoubleProperty lowMinPitch;
    private final DoubleProperty lowMaxPitch;
    private final DoubleProperty highMinPitch;
    private final DoubleProperty highMaxPitch;

    private final DoubleProperty warningLowMinPitch;
    private final DoubleProperty warningLowMaxPitch;
    private final DoubleProperty warningHighMinPitch;
    private final DoubleProperty warningHighMaxPitch;

    private final DoubleProperty highBelowMinFFSlope;

    private boolean isTargeting;
    private boolean isInRange;

    @Inject
    public VisionShooterGoalsSubsystem(PropertyFactory pf) {
        pf.setPrefix(this);

        lowBelowMinSlope = pf.createPersistentProperty("Low/Slope below min pitch", LOW_GOAL_SLOPE);
        lowAboveMaxSlope = pf.createPersistentProperty("Low/Slope above max pitch", LOW_GOAL_SLOPE);

        highBelowMinSlope = pf.createPersistentProperty("High/Slope below min pitch", HIGH_GOAL_SLOPE);
        highAboveMaxSlope = pf.createPersistentProperty("High/Slope above max pitch", HIGH_GOAL_SLOPE);

        lowMinPitch = pf.createPersistentProperty("Low/Min pitch", LOW_MIN_PITCH);
        lowMaxPitch = pf.createPersistentProperty("Low/Max pitch", LOW_MAX_PITCH);

        highMinPitch = pf.createPersistentProperty("High/Min pitch", HIGH_MIN_PITCH);
        highMaxPitch = pf.createPersistentProperty("High/Max pitch", HIGH_MAX_PITCH);

        warningLowMinPitch = pf.createPersistentProperty("Low/Warning min pitch", LOW_MIN_PITCH);
        warningLowMaxPitch = pf.createPersistentProperty("Low/Warning max pitch", LOW_MAX_PITCH);

        warningHighMinPitch = pf.createPersistentProperty("High/Warning min pitch", HIGH_MIN_PITCH);
        warningHighMaxPitch = pf.createPersistentProperty("High/Warning max pitch", HIGH_MAX_PITCH);

        highBelowMinFFSlope = pf.createPersistentProperty("High/FF Slope below min pitch", 1);

        isTargeting = false;
        isInRange = false;
    }

    public void setIsTargeting(boolean isTargeting) {
        this.isTargeting = isTargeting;
    }

    public void setIsInRange(boolean inRange) {
        this.isInRange = inRange;
    }

    public boolean getIsTargeting() {
        return this.isTargeting;
    }

    public boolean getIsInRange() {
        return this.isInRange;
    }

    public boolean inCalibratedRange(Target target, boolean fixAcquired, double pitch) {
        if(fixAcquired) {
            if(target == Target.Low) {
                return pitch >= warningLowMinPitch.get() && pitch <= warningLowMaxPitch.get();
            } else {
                return pitch >= warningHighMinPitch.get() && pitch <= warningHighMaxPitch.get();
            }
        } else {
            return false;
        }
    }

    public double speedFromPitchLow(double pitch) {
        double lowMinPitch = this.lowMinPitch.get();
        double lowMaxPitch = this.lowMaxPitch.get();

        if (pitch < lowMinPitch) {
            return speedFromPitchLow(lowMinPitch) + (pitch - lowMinPitch) * this.lowBelowMinSlope.get();
        } else if (pitch > lowMaxPitch) {
            return speedFromPitchLow(lowMaxPitch) + (pitch - lowMaxPitch) * this.lowAboveMaxSlope.get();
        } else {
            return pitch * LOW_GOAL_SLOPE + 1961;
        }
    }

    public double speedFromPitchHigh(double pitch) {
        double highMinPitch = this.highMinPitch.get();
        double highMaxPitch = this.highMaxPitch.get();

        if (pitch < highMinPitch) {
            return speedFromPitchHigh(highMinPitch) + (pitch - highMinPitch) * this.highBelowMinSlope.get();
        } else if (pitch > highMaxPitch) {
            return speedFromPitchHigh(highMaxPitch) + (pitch - highMaxPitch) * this.highAboveMaxSlope.get();
        } else {
            return pitch * HIGH_GOAL_SLOPE + 3224;
        }
    }

    public double feedForwardFromPitchHigh(double pitch, double defaultFF) {
        double highMinPitch = this.highMinPitch.get();
        if (pitch < highMinPitch) {
            return defaultFF + (pitch - highMinPitch) * this.highBelowMinFFSlope.get();
        } else {
            return defaultFF;
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

    public void setLowMinPitch(double pitch) {
        this.lowMinPitch.set(pitch);
    }

    public void setLowMaxPitch(double pitch) {
        this.lowMaxPitch.set(pitch);
    }

    public void setHighMinPitch(double pitch) {
        this.highMinPitch.set(pitch);
    }

    public void setHighMaxPitch(double pitch) {
        this.highMaxPitch.set(pitch);
    }

    public void setHighFFBelowMinPitchSlope(double slope) {
        this.highBelowMinFFSlope.set(slope);
    }
}
