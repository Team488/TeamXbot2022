package competition.subsystems.drive.swerve;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.SwerveInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class SwerveDriveSubsystem extends BaseSetpointSubsystem {
    private static Logger log = Logger.getLogger(SwerveDriveSubsystem.class);

    private final String label;
    private final PIDManager pid;
    private final ElectricalContract contract;
    private final SwerveDriveMotorPidSubsystem pidConfigSubsystem;

    private final DoubleProperty inchesPerMotorRotation;
    private final DoubleProperty targetVelocity;
    private final DoubleProperty currentVelocity;

    private XCANSparkMax motorController;

    @Inject
    public SwerveDriveSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory,
            PropertyFactory pf, PIDFactory pidf, ElectricalContract electricalContract,
            SwerveDriveMotorPidSubsystem pidConfigSubsystem) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveDriveSubsystem " + this.label);
        
        // Create properties shared among all instances
        pf.setPrefix(super.getPrefix());
        this.contract = electricalContract;
        this.pid = pidf.createPIDManager(super.getPrefix() + "PID", 1.0, 0.0, 0.0, -1.0, 1.0);
        this.inchesPerMotorRotation = pf.createPersistentProperty("InchesPerMotorRotation", 2.022488);
        
        // Create properties unique to this instance.
        pf.setPrefix(this);
        this.targetVelocity = pf.createEphemeralProperty("TargetVelocity", 0.0);
        this.currentVelocity = pf.createEphemeralProperty("CurrentVelocity", 0.0);

        this.pidConfigSubsystem = pidConfigSubsystem;

        if (electricalContract.isDriveReady()) {
            this.motorController = factory.createCANSparkMax(electricalContract.getDriveNeo(swerveInstance), this.getPrefix(), "DriveNeo");
            setMotorControllerPositionPidParameters();
        }
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public String getPrefix() {
        return super.getPrefix() + this.label + "/";
    }

    /**
     * Gets current velocity in inches per second
     */
    @Override
    public double getCurrentValue() {
        if (this.contract.isDriveReady()) {
            // Spark returns in RPM - need to convert to inches per second
            return this.motorController.getVelocity() * this.inchesPerMotorRotation.get() / 60.0;
        } else {
            return 0;
        }
    }

    /**
     * Gets target velocity in inches per second
     */
    @Override
    public double getTargetValue() {
        return this.targetVelocity.get();
    }

    /**
     * Sets target velocity in inches per second
     */
    @Override
    public void setTargetValue(double value) {
        this.targetVelocity.set(value);
    }

    @Override
    public void setPower(double power) {
        if (this.contract.isDriveReady()) {
            this.motorController.set(power);
        }
    }

    @Override
    public boolean isCalibrated() {
        return true;
    }
    

    public XCANSparkMax getSparkMax() {
        return this.motorController;
    }

    public void resetPid() {
        this.pid.reset();
    }
    
    public double calculatePower() {
        return this.pid.calculate(this.getTargetValue(), this.getCurrentValue());
    }

    public void setMotorControllerPositionPidParameters() {
        if (this.contract.isDriveReady()) {
            this.motorController.setP(pidConfigSubsystem.getP());
            this.motorController.setI(pidConfigSubsystem.getI());
            this.motorController.setD(pidConfigSubsystem.getD());
            this.motorController.setFF(pidConfigSubsystem.getFF());
            this.motorController.setOutputRange(pidConfigSubsystem.getMinOutput(), pidConfigSubsystem.getMaxOutput());
            this.motorController.setClosedLoopRampRate(pidConfigSubsystem.getClosedLoopRampRate());
            this.motorController.setOpenLoopRampRate(pidConfigSubsystem.getOpenLoopRampRate());
        }
    }

    @Override
    public void periodic() {
        if (contract.isDriveReady()) {
            currentVelocity.set(this.getCurrentValue());
        }
    }
}
