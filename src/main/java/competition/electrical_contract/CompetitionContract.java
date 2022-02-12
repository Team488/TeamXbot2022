package competition.electrical_contract;

import competition.injection.arm.ArmInstance;
import competition.injection.swerve.SwerveInstance;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.DeviceInfo;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public DeviceInfo getDriveNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(31, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(29, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(39, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(21, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getSteeringNeo(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(30, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(28, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(38, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(20, false, simulationScalingValue);

            default:
                return null;
        }
    }

    @Override
    public boolean areCanCodersReady() {
        return true;
    }

    @Override
    public DeviceInfo getSteeringEncoder(SwerveInstance swerveInstance) {
        double simulationScalingValue = 1.0;

        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(51, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(52, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(53, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(54, false, simulationScalingValue);

            default:
                return null;
        }
    }

    public boolean isIntakeReady() {
        return false;
    }

    @Override
    public DeviceInfo getIntakeNeo() {
        return new DeviceInfo(26, false);
    }

    @Override
    public boolean isClimberReady() {
        return false;
    }

    @Override
    public boolean isShooterReady() {
        return false;
    }

    @Override
    public DeviceInfo getPrimaryShooterNeo() {
        return new DeviceInfo(32, false);
    }

    @Override
    public DeviceInfo getSecondaryShooterNeo() {
        return new DeviceInfo(34, false);
    }

    @Override
    public DeviceInfo getArduinoDio1() {
        return new DeviceInfo(0);
    }

    @Override
    public DeviceInfo getArduinoDio2() {
        return new DeviceInfo(1);
    }

    @Override
    public boolean arePneumaticsReady() {
        return false;
    }

    @Override
    public DeviceInfo getLatchSolenoid() {
        return new DeviceInfo(1);
    }

    @Override
    public DeviceInfo getPivotSolenoid() {
        return new DeviceInfo(2);
    }

    @Override
    public boolean isLauncherReady() {
        return false;
    }

    // Still temporary, we need to renumber the shooter motors
    @Override
    public DeviceInfo getShooterMotorLeader() {
        return new DeviceInfo(5, true);
    }

    // Still temporary, we need to renumber the shooter motors
    @Override
    public DeviceInfo getShooterMotorFollower() {
        return new DeviceInfo(6, false);
    }

    @Override
    public DeviceInfo getClimberNeo(ArmInstance armInstance) {
        switch (armInstance.getLabel()) {
            case "LeftArm":
                return new DeviceInfo(37, false);
            case "RightArm":
                return new DeviceInfo(22, false);
            default:
                return null;
        }
    }

}
