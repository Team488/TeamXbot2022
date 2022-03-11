package competition.subsystems.shooterwheel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.ExternalFollower;
import com.revrobotics.CANSparkMax.FaultID;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import competition.electrical_contract.ElectricalContract;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class ShooterWheelSubsystem extends BaseSetpointSubsystem {
    
    private final DoubleProperty targetRpmProp;
    private final DoubleProperty currentRpmProp;
    private final DoubleProperty rpmTrimProp;

    private final DoubleProperty safeRpm;
    private final DoubleProperty nearShotRpm;
    private final DoubleProperty distanceShotRpm;
    private final DoubleProperty hotDogRpm;

    private final DoubleProperty safePower;

    public XCANSparkMax leader;
    private XCANSparkMax follower;
    ElectricalContract contract;

    public enum TargetRPM {
        Safe,
        NearShot,
        DistanceShot,
        HotDogRoller
    }

    @Inject
    public ShooterWheelSubsystem(CommonLibFactory factory, PropertyFactory pf, ElectricalContract contract) {
        log.info("Creating ShooterWheelSubsystem");
        pf.setPrefix(this);
        this.contract = contract;

        targetRpmProp = pf.createEphemeralProperty("TargetRPM", 0);
        currentRpmProp = pf.createEphemeralProperty("CurrentRPM", 0);
        rpmTrimProp = pf.createEphemeralProperty("TrimRPM", 0);

        safeRpm = pf.createPersistentProperty("SafeRpm", 500);
        nearShotRpm = pf.createPersistentProperty("NearShotRpm", 750);
        distanceShotRpm = pf.createPersistentProperty("DistanceShotRpm", 1000);
        hotDogRpm = pf.createPersistentProperty("HotDogRpm", -200);

        safePower = pf.createPersistentProperty("SafePower", 0.1);

        if (contract.isShooterReady()) {
            this.leader = factory.createCANSparkMax(contract.getShooterMotorLeader(), this.getPrefix(),
                    "ShooterMaster");
            this.follower = factory.createCANSparkMax(contract.getShooterMotorFollower(), this.getPrefix(),
                    "ShooterFollower");
            follower.follow(leader, true);

            this.leader.enableVoltageCompensation(12);
            this.leader.follow(ExternalFollower.kFollowerDisabled, 0);
            leader.burnFlash();
            follower.burnFlash();

            setupStatusFrames();
        }
    }

    /**
     * Set up status frame intervals to reduce unnecessary CAN activity.
     */
    private void setupStatusFrames() {
        if (this.contract.isDriveReady()) {
            // We need to re-set frame intervals after a device reset.
            if (this.leader.getStickyFault(FaultID.kHasReset)) {
                log.info("Setting status frame periods for leader.");

                // See https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
                // for description of the different status frames. kStatus0 is used to pass output to follower, so it
                // needs to be relatively frequent.

                this.leader.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus0, 10 /* default 10 */);
                this.leader.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20 /* default 20 */);
                this.leader.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20 /* default 20 */);
                this.leader.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus3, 500 /* default 50 */);
                
                this.leader.clearFaults();
            }

            if (this.follower.getStickyFault(FaultID.kHasReset)) {
                log.info("Setting status frame periods for follower.");

                // The follower does not need to publish as frequently as the leader.
                this.follower.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus0, 500 /* default 10 */);
                this.follower.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20 /* default 20 */);
                this.follower.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20 /* default 20 */);
                this.follower.getInternalSparkMax().setPeriodicFramePeriod(PeriodicFrame.kStatus3, 500 /* default 50 */);
                
                this.follower.clearFaults();
            }
        }
    }

    public void setTargetRPM(TargetRPM target) {
        switch (target) {
            case Safe:
                setTargetRPM(safeRpm.get());
                break;
            case NearShot:
                setTargetRPM(nearShotRpm.get());
                break;
            case DistanceShot:
                setTargetRPM(distanceShotRpm.get());
                break;
            case HotDogRoller:
                setTargetRPM(hotDogRpm.get());
                break;
            default:
                setTargetRPM(0);
                break;
        }
    }

    public void setTargetRPM(double speed) {
        targetRpmProp.set(speed);
    }

    public double getTargetRPM() {
        if (Math.abs(targetRpmProp.get()) < 50.0) {
            return targetRpmProp.get();
        }
        return targetRpmProp.get() + getTrimRPM();
    }

    public void changeTrimRPM(double changeRate) {
        rpmTrimProp.set(getTrimRPM() + changeRate);
    }
    
    public void setTargetTrimRPM(double trim) {
        rpmTrimProp.set(trim);
    }

    public double getTrimRPM() {
        return rpmTrimProp.get();
    }
    

    public void resetTrimRPM() {
        rpmTrimProp.set(0);
    }

    public void changeTargetRPM(double amount) {
        double speed = getTargetRPM();
        speed += amount;
        setTargetRPM(speed);
    }

    public void setPidSetpoint(double speed) {
        if (contract.isShooterReady()) {
            leader.setReference(speed, ControlType.kVelocity);
        }
    }

    public void setSafePower() {
        setPower(safePower.get());
    }

    public void setPower(double power) {
        if (contract.isShooterReady()) {
            leader.set(power);
        }
    }

    public double getPower() {
        if (contract.isShooterReady()) {
            return leader.get();
        }
        return 0;
    }

    public void stop() {
        setPower(0);
    }

    public double getCurrentRPM() {
        if (contract.isShooterReady()) {
            return leader.getVelocity();
        }
        return 0;
    }

    public void periodic() {
        if (contract.isShooterReady()) {
            leader.periodic();
            //follower.periodic();
            currentRpmProp.set(getCurrentRPM());
            setupStatusFrames();
        }
    }

    public void resetPID() {
        if (contract.isShooterReady()) {
            leader.setIAccum(0);
        }
    }

    public void resetWheel() {
        setPower(0);
        setTargetRPM(0);
        resetPID();
    }

    public void setCurrentLimits() {
        if (contract.isShooterReady()) {
            leader.setSmartCurrentLimit(40);
            leader.setClosedLoopRampRate(0.01);
        }
    }

    public void configurePID() {
        if (contract.isShooterReady()) {
            leader.setIMaxAccum(1, 0);
        }
    }

    @Override
    public double getCurrentValue() {
        return getCurrentRPM();
    }

    @Override
    public double getTargetValue() {
        return getTargetRPM();
    }

    @Override
    public boolean isCalibrated() {
        return true;
    }

    @Override
    public void setTargetValue(double value) {
        setTargetRPM(value);
    }

    @Override
    public boolean isMaintainerAtGoal() {
        boolean basicAtGoal =  super.isMaintainerAtGoal();
        return basicAtGoal && shooterMovingSome();
    }

    private boolean shooterMovingSome() {
        return getCurrentRPM() > 500;
    }
}