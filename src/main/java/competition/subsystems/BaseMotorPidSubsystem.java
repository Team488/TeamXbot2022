package competition.subsystems;

import com.google.inject.Inject;

import xbot.common.command.BaseSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

/**
 * Container for storing motor controller PID settings.
 */
public abstract class BaseMotorPidSubsystem extends BaseSubsystem {

    private final DoubleProperty kP;
    private final DoubleProperty kI;
    private final DoubleProperty kD;
    private final DoubleProperty kFF;
    private final DoubleProperty kMinOutput;
    private final DoubleProperty kMaxOutput;
    private final DoubleProperty kClosedLoopRampRate;
    private final DoubleProperty kOpenLoopRampRate;

    @Inject
    public BaseMotorPidSubsystem(PropertyFactory pf) {
        pf.setPrefix(this);

        kP = pf.createPersistentProperty("kP", 0.1);
        kI = pf.createPersistentProperty("kI", 0);
        kD = pf.createPersistentProperty("kD", 0);
        kFF = pf.createPersistentProperty("kFF", 0);
        kMinOutput = pf.createPersistentProperty("kMinOutput", -1.0);
        kMaxOutput = pf.createPersistentProperty("kMaxOutput", 1.0);
        kClosedLoopRampRate = pf.createPersistentProperty("kClosedLoopRampRate", 0.05);
        kOpenLoopRampRate = pf.createPersistentProperty("kOpenLoopRampRate", 0.05);
    }

    public void setAllProperties(double p, double i, double d, double ff,
            double minOutput, double maxOutput, double closedLoopRampRate, double openLoopRampRate) {
        this.kP.set(p);
        this.kI.set(i);
        this.kD.set(d);
        this.kFF.set(ff);
        this.kMinOutput.set(minOutput);
        this.kMaxOutput.set(maxOutput);
        this.kClosedLoopRampRate.set(closedLoopRampRate);
        this.kOpenLoopRampRate.set(openLoopRampRate);
    }

    public double getP() {
        return kP.get();
    }

    public double getI() {
        return kI.get();
    }

    public double getD() {
        return kD.get();
    }

    public double getFF() {
        return kFF.get();
    }

    public double getMinOutput() {
        return kMinOutput.get();
    }

    public double getMaxOutput() {
        return kMaxOutput.get();
    }

    public double getClosedLoopRampRate() {
        return kClosedLoopRampRate.get();
    }
    
    public double getOpenLoopRampRate() {
        return kOpenLoopRampRate.get();
    }

}
