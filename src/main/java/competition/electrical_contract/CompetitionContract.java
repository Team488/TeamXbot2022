package competition.electrical_contract;

import competition.injection.arm.ArmInstance;
import competition.injection.swerve.SwerveInstance;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.electrical_contract.CANTalonInfo;
import xbot.common.injection.electrical_contract.DeviceInfo;
import xbot.common.math.XYPair;

public class CompetitionContract extends ElectricalContract {

    protected final double simulationScalingValue = 256.0 * PoseSubsystem.INCHES_IN_A_METER;

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public boolean areCanCodersReady() {
        return true;
    }

    @Override
    public boolean isClimberReady() {
        return true;
    }

    @Override
    public boolean isShooterReady() {
        return true;
    }

    public boolean isIntakeReady() {
        return true;
    }

    @Override
    public boolean arePneumaticsReady() {
        return true;
    }

    @Override
    public boolean isConveyerReady() {
        return true;
    }

    @Override
    public boolean isCollectorStage2Ready() {
        return false;
    }

    @Override
    public boolean areClimberLimitSensorsReady(ArmInstance armInstance) {
        switch (armInstance.getLabel()) {
            case "LeftArm":
                return true;
            case "RightArm":
                return false;
            default:
                return false;
        }
    }

    @Override
    public DeviceInfo getDriveNeo(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new DeviceInfo(31, false, simulationScalingValue);

            case "FrontRightDrive":
                return new DeviceInfo(28, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(38, false, simulationScalingValue);

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
                return new DeviceInfo(29, false, simulationScalingValue);

            case "RearLeftDrive":
                return new DeviceInfo(39, false, simulationScalingValue);

            case "RearRightDrive":
                return new DeviceInfo(20, false, simulationScalingValue);

            default:
                return null;
        }
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

    @Override
    public XYPair getSwerveModuleOffsets(SwerveInstance swerveInstance) {
        switch (swerveInstance.getLabel()) {
            case "FrontLeftDrive":
                return new XYPair(-15, 15);
            case "FrontRightDrive":
                return new XYPair(15, 15);
            case "RearLeftDrive":
                return new XYPair(-15, -15);
            case "RearRightDrive":
                return new XYPair(15, -15);
            default:
                return new XYPair(0, 0);
        }
    }

    @Override
    public DeviceInfo getIntakeNeo() {
        return new DeviceInfo(26, false);
    }

    @Override
    public DeviceInfo getArduinoDio0() {
        return new DeviceInfo(0);
    }

    @Override
    public DeviceInfo getArduinoDio1() {
        return new DeviceInfo(1);
    }

    @Override
    public DeviceInfo getArduinoDio2() {
        return new DeviceInfo(2);
    }

    @Override
    public DeviceInfo getArduinoDio3() {
        return new DeviceInfo(3);
    }

    @Override
    public DeviceInfo getArduinoAllianceDio() {
        return new DeviceInfo(4);
    }

    @Override
    public DeviceInfo getArduinoAnalogOutput() {
        return new DeviceInfo(0);
    }

    @Override
    public DeviceInfo getLatchSolenoid() {
        return new DeviceInfo(12);
    }

    @Override
    public DeviceInfo getLatchSolenoid2() {
        return new DeviceInfo(11);
    }

    @Override
    public DeviceInfo getPivotSolenoid() {
        return new DeviceInfo(6, true);
    }

    @Override
    public DeviceInfo getPivotSolenoid2() {
        return new DeviceInfo(7, true);
    }

    @Override
    public DeviceInfo getShooterMotorLeader() {
        return new DeviceInfo(32, true);
    }

    @Override
    public DeviceInfo getShooterMotorFollower() {
        return new DeviceInfo(27, true);
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

    @Override
    public DeviceInfo getClimberLowerLimitSensor(ArmInstance armInstance) {
        switch (armInstance.getLabel()) {
            case "LeftArm":
                return new DeviceInfo(8);
            case "RightArm":
                return new DeviceInfo(7);
            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getClimberUpperLimitSensor(ArmInstance armInstance) {
        switch (armInstance.getLabel()) {
            case "LeftArm":
                return new DeviceInfo(6);
            case "RightArm":
                return new DeviceInfo(9);
            default:
                return null;
        }
    }

    @Override
    public DeviceInfo getClimberPawl(ArmInstance armInstance) {
        switch (armInstance.getLabel()) {
            case "LeftArm":
                return new DeviceInfo(3, false);
            case "RightArm":
                return new DeviceInfo(4, false);
            default:
                return null;
        }
    }

    @Override
    public CANTalonInfo getLeftCollectorMotor() {
        return new CANTalonInfo(36, true);
    }

    public CANTalonInfo getCollectorStage2Motor() {
        return new CANTalonInfo(35, true);
    }

    @Override
    public DeviceInfo getCollectorDeviceSoleniod1() {
        return new DeviceInfo(1);
    }

    @Override
    public DeviceInfo getCollectorDeviceSoleniod2() {
        return new DeviceInfo(0);
    }

    public CANTalonInfo getConveyerMotor() {
        return new CANTalonInfo(24, true);
    }

    public DeviceInfo getDeployHoodSoleniod1() {
        return new DeviceInfo(9);
    }
    
    public DeviceInfo getDeployHoodSoleniod2() {
        return new DeviceInfo(8);
    }

}
