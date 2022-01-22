package competition.subsystems.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.log4j.Logger;

import competition.electrical_contract.ElectricalContract;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);
    
    ElectricalContract contract;

    public XCANSparkMax frontLeftDrive;
    public XCANSparkMax frontRightDrive;
    public XCANSparkMax rearLeftDrive;
    public XCANSparkMax rearRightDrive;

    public XCANSparkMax frontLeftSteering;
    public XCANSparkMax frontRightSteering;
    public XCANSparkMax rearLeftSteering;
    public XCANSparkMax rearRightSteering;

    private final PIDManager positionPid;
    private final PIDManager rotationPid;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract contract, PIDFactory pf) {
        log.info("Creating DriveSubsystem");

        if (contract.isDriveReady()) {
            this.frontLeftDrive = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "FrontLeftDrive");
            this.frontRightDrive = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "FrontRightDrive");
            this.rearLeftDrive = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "RearLeftDrive");
            this.rearRightDrive = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "RearRightDrive");
            
            this.frontLeftSteering = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "FrontLeftSteering");
            this.frontRightSteering = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "FrontRightSteering");
            this.rearLeftSteering = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "RearLeftSteering");
            this.rearRightSteering = factory.createCANSparkMax(contract.getFrontLeftDriveNeo().channel, this.getPrefix(), "RearRightSteering");
        }

        positionPid = pf.createPIDManager(getPrefix() + "PositionPID");
        rotationPid = pf.createPIDManager(getPrefix() + "RotationPID");
    }

    @Override
    public PIDManager getPositionalPid() {
        return positionPid;
    }

    @Override
    public PIDManager getRotateToHeadingPid() {
        return rotationPid;
    }

    @Override
    public PIDManager getRotateDecayPid() {
        return null;
    }

    @Override
    public void move(XYPair translate, double rotate) {
    }

    @Override
    public double getLeftTotalDistance() {
        if (contract.isDriveReady()) {
            // nothing to do yet
        }
        return 0;
    }

    @Override
    public double getRightTotalDistance() {
        if (contract.isDriveReady()) {
            // nothing to do yet
        }
        return 0;
    }

    @Override
    public double getTransverseDistance() {
        return 0;
    }
}
