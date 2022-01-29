package competition.electrical_contract;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import competition.injection.swerve.SwerveInstance;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.CANTalonInfo;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public CANTalonInfo getDriveNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new CANTalonInfo(1, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "FrontRightDrive":
                return new CANTalonInfo(2, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "RearLeftDrive":
                return new CANTalonInfo(3, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "RearRightDrive":
                return new CANTalonInfo(4, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            default:
                return null;
        }
    }

    @Override
    public CANTalonInfo getSteeringNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new CANTalonInfo(11, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "FrontRightDrive":
                return new CANTalonInfo(12, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "RearLeftDrive":
                return new CANTalonInfo(13, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            case "RearRightDrive":
                return new CANTalonInfo(14, false, FeedbackDevice.CTRE_MagEncoder_Relative, false, simulationScalingValue);
            default:
                return null;
        }
    }
}
