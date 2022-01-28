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
import xbot.common.properties.PropertyFactory;

@Singleton
public class SwerveSteeringSubsystem extends BaseSetpointSubsystem {
    private static Logger log = Logger.getLogger(SwerveSteeringSubsystem.class);

    private final String label;
    private final PIDManager pid;

    private double target;
    private XCANSparkMax motorController;
    private XAbsoluteEncoder encoder;

    @Inject
    public SwerveSteeringSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory,
            PropertyFactory pf, PIDFactory pidf, ElectricalContract electricalContract) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveRotationSubsystem " + this.label);
        pf.setPrefix(this);

        this.pid = pidf.createPIDManager(this.getPrefix() + "PID");

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
        return this.encoder.getPosition();
    }

    /**
     * Gets target angle in degrees
     */
    @Override
    public double getTargetValue() {
        return this.target;
    }

    /**
     * Sets target value in degrees
     */
    @Override
    public void setTargetValue(double value) {
        this.target = value;
    }

    @Override
    public void setPower(double power) {
        
    }

    @Override
    public boolean isCalibrated() {
        return false;
    }
    
    public XCANSparkMax getSparkMax() {
        return this.motorController;
    }
}
