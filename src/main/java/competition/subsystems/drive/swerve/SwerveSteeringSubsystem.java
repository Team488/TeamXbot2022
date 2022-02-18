package competition.subsystems.drive.swerve;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revrobotics.CANSparkMax.ControlType;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.SwerveInstance;
import edu.wpi.first.math.MathUtil;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.controls.sensors.XAbsoluteEncoder;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.WrappedRotation2d;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.properties.StringProperty;
import xbot.common.resiliency.DeviceHealth;

@Singleton
public class SwerveSteeringSubsystem extends BaseSetpointSubsystem {
    private static Logger log = Logger.getLogger(SwerveSteeringSubsystem.class);

    private final String label;
    private final PIDManager pid;
    private final ElectricalContract contract;

    private final DoubleProperty powerScale;
    private final DoubleProperty targetRotation;
    private final DoubleProperty currentModuleHeading;
    private final DoubleProperty motorPosition;
    private final StringProperty canCoderStatus;
    private final DoubleProperty degreesPerMotorRotation;
    private final BooleanProperty useMotorControllerPid;
    private final DoubleProperty maxMotorEncoderDrift;

    private XCANSparkMax motorController;
    private XAbsoluteEncoder encoder;

    private double positionOffset;
    private boolean calibrated = false;
    private boolean canCoderUnavailable = false;

    @Inject
    public SwerveSteeringSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory,
            PropertyFactory pf, PIDFactory pidf, ElectricalContract electricalContract) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveRotationSubsystem " + this.label);

        this.contract = electricalContract;

        // Create properties shared among all instances
        pf.setPrefix(super.getPrefix());
        this.pid = pidf.createPIDManager(super.getPrefix() + "PID", 0.1, 0.0, 0.0, -1.0, 1.0);
        this.powerScale = pf.createPersistentProperty("PowerScaleFactor", 5);
        this.degreesPerMotorRotation = pf.createPersistentProperty("DegreesPerMotorRotation", 28.1502912);
        this.useMotorControllerPid = pf.createPersistentProperty("UseMotorControllerPID", false);
        this.maxMotorEncoderDrift = pf.createPersistentProperty("MaxEncoderDriftDegrees", 1.0);

        // Create properties that are unique to each instance
        pf.setPrefix(this);
        this.targetRotation = pf.createEphemeralProperty("TargetRotation", 0.0);
        this.currentModuleHeading = pf.createEphemeralProperty("CurrentModuleHeading", 0.0);
        this.motorPosition = pf.createEphemeralProperty("CurrentMotorPosition", 0.0);
        this.canCoderStatus = pf.createEphemeralProperty("CANCoderStatus", "unknown");

        if (electricalContract.isDriveReady()) {
            this.motorController = factory.createCANSparkMax(electricalContract.getSteeringNeo(swerveInstance), this.getPrefix(), "SteeringNeo");
        }
        if (electricalContract.areCanCodersReady()) {
            this.encoder = factory.createAbsoluteEncoder(electricalContract.getSteeringEncoder(swerveInstance), this.getPrefix());
            // Since the CANCoders start with absolute knowledge from the start, that means this system
            // is always calibrated.
            calibrated = true;

            if (this.encoder.getHealth() == DeviceHealth.Unhealthy) {
                canCoderUnavailable = true;
            }
        }

        this.register();
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public String getPrefix() {
        return super.getPrefix() + this.label + "/";
    }

    /**
     * Gets current angle in degrees
     */
    @Override
    public double getCurrentValue() {
        double position = getBestEncoderPositionInDegrees();
        
        double adjustedPosition = (position - positionOffset) + 90;
        return adjustedPosition;
    }

    /**
     * Gets target angle in degrees
     */
    @Override
    public double getTargetValue() {
        return this.targetRotation.get();
    }

    /**
     * Sets target angle in degrees
     */
    @Override
    public void setTargetValue(double value) {
        this.targetRotation.set(value);
    }

    /**
     * Sets the output power of the motor.
     * @param power The power value, between -1 and 1.
     */
    @Override
    public void setPower(double power) {
        if (this.contract.isDriveReady()) {
            this.motorController.set(power);
        }
    }

    @Override
    public boolean isCalibrated() {
        return calibrated;
    }

    public void calibrateHere() {
        if (this.contract.isDriveReady()) {
            this.positionOffset = this.motorController.getPosition();
        }
        this.calibrated = true;
    }

    /**
     * Reset the SparkMax encoder position based on the CanCoder measurement.
     * This should only be called when the mechanism is stationary.
     */
    public void calibrateMotorControllerPositionFromCanCoder() {
        if (this.contract.isDriveReady() && this.contract.areCanCodersReady() && !canCoderUnavailable) {
            double currentCanCoderPosition = getAbsoluteEncoderPositionInDegrees();
            double currentSparkMaxPosition = getMotorControllerEncoderPosiitonInDegrees();

            if (isMotorControllerDriftTooHigh(currentCanCoderPosition, currentSparkMaxPosition, this.maxMotorEncoderDrift.get())) {
                if (Math.abs(this.motorController.get()) > 0) {
                    log.error("This should not be called when the motor is moving!");
                } else {
                    log.warn("Motor controller encoder drift is too high, recalibrating!");

                    // Force motors to manual control before resetting position
                    this.setPower(0);
                    this.motorController.setPosition(currentCanCoderPosition / this.degreesPerMotorRotation.get());
                }
            }
        }
    }

    public static boolean isMotorControllerDriftTooHigh(double currentCanCoderPosition, double currentSparkMaxPosition, double maxDelta) {
        return Math.abs(MathUtil.inputModulus(currentCanCoderPosition - currentSparkMaxPosition, -180, 180)) >= maxDelta;
    }

    public XCANSparkMax getSparkMax() {
        return this.motorController;
    }

    public XAbsoluteEncoder getEncoder() {
        return this.encoder;
    }

    /**
     * Gets the current position of the mechanism using the best available encoder.
     * @return The position in degrees.
     */
    public double getBestEncoderPositionInDegrees() {
        if (this.contract.areCanCodersReady() && !canCoderUnavailable) {
            return getAbsoluteEncoderPositionInDegrees();
        }
        else if (this.contract.isDriveReady()) {
            // If the CANCoders aren't available, we can use the built-in encoders in the steering motors. Experience suggests
            // that this will work for about 30 seconds of driving before getting wildly out of alignment.
            return getMotorControllerEncoderPosiitonInDegrees();
        }

        return 0;
    }

    /**
     * Gets the reported position of the CANCoder.
     * @return The position of the CANCoder.
     */
    public double getAbsoluteEncoderPositionInDegrees() {
        if (this.contract.areCanCodersReady()) {
            return this.encoder.getAbsolutePosition();
        } else {
            return 0;
        }
    }

    /**
     * Gets the reported position of the encoder on the NEO motor.
     * @return The position of the encoder on the NEO motor.
     */
    public double getMotorControllerEncoderPosiitonInDegrees() {
        if (this.contract.isDriveReady()) {
            return MathUtil.inputModulus(this.motorController.getPosition() * degreesPerMotorRotation.get(), -180, 180);
        } else {
            return 0;
        }
    }

    /**
     * Calculate the target motor power using software PID.
     * @return The target power required to approach our setpoint.
     */
    public double calculatePower() {
        // We need to calculate our own error function. Why?
        // PID works great, but it assumes there is a linear relationship between your current state and
        // your target state. Since rotation is circular, that's not the case: if you are at 170 degrees,
        // and you want to go to -170 degrees, you could travel -340 degrees... or just +20. 
        
        // So, we perform our own error calculation here that takes that into account (thanks to the WrappedRotation2d
        // class, which is aware of such circular effects), and then feed that into a PID where
        // Goal is 0 and Current is our error.
        
        double errorInDegrees = WrappedRotation2d.fromDegrees(getTargetValue() - getCurrentValue()).getDegrees();

        // Constrain the error values before calculating PID. PID only constrains the output after
        // calculating the outputs, which means it could accumulate values significantly larger than
        // max power internally if we don't constrain the input.
        double scaledError = MathUtils.constrainDouble(errorInDegrees / 90 * powerScale.get(), -1, 1);
                
        // Now we feed it into a PID system, where the goal is to have 0 error.
        double rotationalPower = -this.pid.calculate(0, scaledError);
        
        return rotationalPower;
    }

    /**
     * Reset the software PID.
     */
    public void resetPid() {
        this.pid.reset();
    }

    /**
     * Gets a flag indicating whether we are using the motor controller's PID or software PID.
     * @return <b>true</b> if using motor controller's PID.
     */
    public boolean isUsingMotorControllerPid() {
        return this.useMotorControllerPid.get();
    }

    /**
     * Calculates the nearest position on the motor encoder to targetDegrees and sets the controller's PID target.
     */
    public void setMotorControllerPidTarget() {
        if (this.contract.isDriveReady()) {
            double targetDegrees = getTargetValue();

            // We can rely on either encoder for the starting position, to get the change in angle. Using the CANCoder
            // position to calculate this will help us to avoid any drift on the motor encoder. Then we just set our
            // target based on the motor encoder's current position. Unless the wheels are moving rapidly, the measurements
            // on each encoder are probably taken close enough together in time for our purposes.
            double currentPositionDegrees = getBestEncoderPositionInDegrees();
            double changeInDegrees = MathUtil.inputModulus(targetDegrees - currentPositionDegrees, -180, 180);
            double targetPosition = this.motorController.getPosition() + (changeInDegrees / degreesPerMotorRotation.get());

            this.motorController.setReference(targetPosition, ControlType.kPosition, 0);
        }
    }

    public void setMotorControllerPositionPidParameters(double p, double i, double d, double ff, double minOutput, double maxOutput) {
        if (this.contract.isDriveReady()) {
            this.motorController.setP(p);
            this.motorController.setI(i);
            this.motorController.setD(d);
            this.motorController.setFF(ff);
            this.motorController.setOutputRange(minOutput, maxOutput);
        }
    }

    @Override
    public void periodic() {
        currentModuleHeading.set(getCurrentValue());
        canCoderStatus.set(this.encoder.getHealth().toString());

        if (this.contract.isDriveReady()) {
            this.motorPosition.set(this.motorController.getPosition());
            
            // Seems to cause a lot of lag.
            //this.motorController.periodic();
        }
    }
}
