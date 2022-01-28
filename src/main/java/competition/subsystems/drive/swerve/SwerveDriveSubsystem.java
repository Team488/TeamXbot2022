package competition.subsystems.drive.swerve;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.SwerveInstance;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.PropertyFactory;

@Singleton
public class SwerveDriveSubsystem extends BaseSetpointSubsystem {
    private static Logger log = Logger.getLogger(SwerveDriveSubsystem.class);

    private final String label;

    private XCANSparkMax motorController;

    @Inject
    public SwerveDriveSubsystem(SwerveInstance swerveInstance, CommonLibFactory factory, PropertyFactory pf, ElectricalContract electricalContract) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveDriveSubsystem " + this.label);
        pf.setPrefix(this);

        if (electricalContract.isDriveReady()) {
            this.motorController = factory.createCANSparkMax(electricalContract.getDriveNeo(swerveInstance), this.getPrefix(), "DriveNeo");
        }
    }

    @Override
    public String getPrefix() {
        return super.getPrefix() + this.label + "/";
    }

    @Override
    public double getCurrentValue() {
        return 0;
    }

    @Override
    public double getTargetValue() {
        return 0;
    }

    @Override
    public void setTargetValue(double value) {
        
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
