package competition.subsystems.climber_arm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.electrical_contract.DeviceInfo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

@Singleton
public class ClimberArmSubsystem extends BaseSetpointSubsystem{
    public XCANSparkMax armMotor;
    public double armMotorPosition;
    public final DoubleProperty safeArmExtendedNumber;
    public final DoubleProperty safeArmRetractedNumber;
    
    public boolean isArmOverExtended(){
        armMotorPosition = armMotor.getPosition();
        return armMotorPosition > safeArmExtendedNumber.get();
    }

    public boolean isArmOverRetracted(){
        armMotor.getPosition();
        return armMotorPosition < safeArmRetractedNumber.get();
    }

    @Inject
<<<<<<< HEAD
    public ClimberArmSubsystem(CommonLibFactory factory, PropertyFactory pf){
        armMotor = factory.createCANSparkMax(4, this.getPrefix(), "ArmMotor");
        pf.setPrefix(this);
        safeArmExtendedNumber = pf.createPersistentProperty("safelyExtendable", 10);
        safeArmRetractedNumber = pf.createPersistentProperty("safelyRetractable", -10);
    }


    private void setMotorPower(double power, boolean isSafe){


        if (isSafe) {

            if (isArmOverExtended()) {
                power = MathUtils.constrainDouble(power, -1, 0);

            } else if (isArmOverRetracted()) {
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
        setMotorPower(power, true);
        
    }

    @Override
    public boolean isCalibrated() {
        
        return false;
    }


}