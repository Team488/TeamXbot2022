package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.electrical_contract.DeviceInfo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;

@Singleton
public class ClimberArmSubsystem extends BaseSetpointSubsystem{
    public XCANSparkMax armMotor;

    @Inject
    public ClimberArmSubsystem(CommonLibFactory factory){
        armMotor = factory.createCANSparkMax(new DeviceInfo(3), this.getPrefix(), "ArmMotor");
    }

    private void setMotorPower(double power, boolean isSafe){
        boolean overExtend = false;
        boolean overRetracts = false;

        if (isSafe) {

            if (overExtend) {
                power = MathUtils.constrainDouble(power, -1, 0);

            } else if (overRetracts) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
        }
        armMotor.set(power);
    }

    public void motorStop(double power){
        setMotorPower(0, true);
    }

    // Won't use right now

    @Override
    public double getCurrentValue() {
        // 
        return 0;
    }

    @Override
    public double getTargetValue() {
        // 
        return 0;
    }

    @Override
    public void setTargetValue(double value) {
        // 
        
    }

    @Override
    public void setPower(double power) {
        // 
        setMotorPower(power, true);
        
    }

    @Override
    public boolean isCalibrated() {
        // 
        return false;
    }


}