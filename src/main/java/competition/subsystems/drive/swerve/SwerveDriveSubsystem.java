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

    private final DoubleProperty velocityScaleFactor;
    private final DoubleProperty targetVelocity;

    private XCANSparkMax motorController;

    @Inject
    public SwerveDriveSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory,
            PropertyFactory pf, PIDFactory pidf, ElectricalContract electricalContract) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveDriveSubsystem " + this.label);
        pf.setPrefix(this);

        this.contract = electricalContract;
        this.pid = pidf.createPIDManager(this.getPrefix() + "PID");

        this.velocityScaleFactor = pf.createPersistentProperty("VelocityScaleFactor", 0.1);
        this.targetVelocity = pf.createEphemeralProperty("TargetVelocity", 0.0);

        if (electricalContract.isDriveReady()) {
            this.motorController = factory.createCANSparkMax(electricalContract.getDriveNeo(swerveInstance).channel, this.getPrefix(), "DriveNeo");
        }
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
            return this.motorController.get() / this.velocityScaleFactor.get();
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
            double newPower = this.pid.calculate(getTargetValue(), getCurrentValue());
            this.motorController.set(newPower * this.velocityScaleFactor.get());
        }
    }

    @Override
    public boolean isCalibrated() {
        return false;
    }
    

    public XCANSparkMax getSparkMax() {
        return this.motorController;
    }
}
