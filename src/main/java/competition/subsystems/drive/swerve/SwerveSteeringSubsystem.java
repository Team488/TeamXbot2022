package competition.subsystems.drive.swerve;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.SwerveInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.controls.sensors.XAbsoluteEncoder;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.WrappedRotation2d;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class SwerveSteeringSubsystem extends BaseSetpointSubsystem {
    private static Logger log = Logger.getLogger(SwerveSteeringSubsystem.class);

    private final String label;
    private final PIDManager pid;
    private final ElectricalContract contract;

    private final DoubleProperty powerScale;
    private final DoubleProperty targetRotation;

    private XCANSparkMax motorController;
    private XAbsoluteEncoder encoder;

    @Inject
    public SwerveSteeringSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory,
            PropertyFactory pf, PIDFactory pidf, ElectricalContract electricalContract) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveRotationSubsystem " + this.label);
        pf.setPrefix(this);

        this.contract = electricalContract;
        this.pid = pidf.createPIDManager(this.getPrefix() + "PID", 0.1, 0.0, 0.0, -1.0, 1.0);
        this.powerScale = pf.createPersistentProperty("PowerScaleFactor", 0.1);
        this.targetRotation = pf.createEphemeralProperty("TargetRotation", 0.0);

        if (electricalContract.isDriveReady()) {
            this.motorController = factory.createCANSparkMax(electricalContract.getSteeringNeo(swerveInstance).channel, this.getPrefix(), "SteeringNeo");
            this.encoder = factory.createAbsoluteEncoder(electricalContract.getSteeringEncoder(swerveInstance).channel);
        }
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
        if (this.contract.isDriveReady()) {
            return this.encoder.getPosition();
        } else {
            return 0;
        }
    }

    /**
     * Gets target angle in degrees
     */
    @Override
    public double getTargetValue() {
        return this.targetRotation.get();
    }

    /**
     * Sets target value in degrees
     */
    @Override
    public void setTargetValue(double value) {
        this.targetRotation.set(value);
    }

    @Override
    public void setPower(double power) {
        if (this.contract.isDriveReady()) {
            this.motorController.set(power);
        }
    }

    @Override
    public boolean isCalibrated() {
        return false;
    }
    
    @Override
    public void periodic() {
        super.periodic();
        this.setPower(calculatePower());
    }

    public XCANSparkMax getSparkMax() {
        return this.motorController;
    }

    public XAbsoluteEncoder getEncoder() {
        return this.encoder;
    }

    private double calculatePower() {
        // We need to calculate our own error function. Why?
        // PID works great, but it assumes there is a linear relationship between your current state and
        // your target state. Since rotation is circular, that's not the case: if you are at 170 degrees,
        // and you want to go to -170 degrees, you could travel -340 degrees... or just +20. 
        
        // So, we perform our own error calculation here that takes that into account (thanks to the WrappedRotation2d
        // class, which is aware of such circular effects), and then feed that into a PID where
        // Goal is 0 and Current is our error.
        
        double errorInDegrees = WrappedRotation2d.fromDegrees(getTargetValue() - getCurrentValue()).getDegrees();
                
        // Now we feed it into a PID system, where the goal is to have 0 error.
        double rotationalPower = this.pid.calculate(0, errorInDegrees);
        
        return rotationalPower * this.powerScale.get();
    }
}
