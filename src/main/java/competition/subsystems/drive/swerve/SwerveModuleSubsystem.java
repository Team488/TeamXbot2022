package competition.subsystems.drive.swerve;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import competition.injection.swerve.SwerveInstance;
import edu.wpi.first.math.geometry.Translation2d;
import xbot.common.command.BaseSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class SwerveModuleSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(SwerveModuleSubsystem.class);
    
    private final String label;

    private final SwerveDriveSubsystem driveSubsystem;
    private final SwerveSteeringSubsystem steeringSubsystem;

    private final DoubleProperty xOffsetInches;
    private final DoubleProperty yOffsetInches;

    private final Translation2d moduleTranslation;

    @Inject
    public SwerveModuleSubsystem(SwerveInstance swerveInstance, SwerveDriveSubsystem driveSubsystem, SwerveSteeringSubsystem steeringSubsystem,
            ElectricalContract contract, PropertyFactory pf) {
        this.label = swerveInstance.getLabel();
        log.info("Creating SwerveModuleSubsystem " + this.label);
        pf.setPrefix(this);

        this.driveSubsystem = driveSubsystem;
        this.steeringSubsystem = steeringSubsystem;

        this.xOffsetInches = pf.createPersistentProperty("XOffsetInches", 0.0);
        this.yOffsetInches = pf.createPersistentProperty("YOffsetInches", 0.0);

        this.moduleTranslation = new Translation2d(
            xOffsetInches.get() / BasePoseSubsystem.INCHES_IN_A_METER,
            yOffsetInches.get() / BasePoseSubsystem.INCHES_IN_A_METER);
    }

    @Override
    public String getPrefix() {
        return super.getPrefix() + this.label + "/";
    }

    public Translation2d getModuleTranslation() {
        return this.moduleTranslation;
    }

    public SwerveDriveSubsystem getDriveSubsystem() {
        return this.driveSubsystem;
    }

    public SwerveSteeringSubsystem getSteeringSubsystem() {
        return this.steeringSubsystem;
    }

    public void simpleDrive(double drivePower, double rotationalPower) {
        this.driveSubsystem.setPower(drivePower);
        this.steeringSubsystem.setPower(rotationalPower);
    }
}