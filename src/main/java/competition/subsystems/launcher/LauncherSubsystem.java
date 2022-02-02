package competition.subsystems.launcher;

import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.controls.actuators.XCANSparkMax;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyFactory;

import com.google.inject.Inject;

import competition.electrical_contract.CompetitionContract;

    // BAREBONES  AND NONFUNCTIONAL SUBSYSTEM !!

public class LauncherSubsystem extends BaseSetpointSubsystem{

    public DoubleProperty targetval;
    public DoubleProperty currentval;

    public XCANSparkMax leader;
    private XCANSparkMax follower;
    CompetitionContract contract;

    @Inject
    public LauncherSubsystem(CommonLibFactory factory, 
    PropertyFactory pf, CompetitionContract contract){
        log.info("creating LauncherSubsystem");
        this.contract = contract;

        if(contract.isLauncherReady()){
            this.leader = factory.createCANSparkMax(contract.shooterMotorLeader().channel, this.getPrefix(),
            "ShooterMaster");
            this.follower = factory.createCANSparkMax(contract.shooterMotorFollow().channel, this.getPrefix(),
            "ShooterFollower");

            this.leader.enableVoltageCompensation(12);

            leader.burnFlash();
            follower.burnFlash();
        }

    }
    
    public double getCurrentValue(){
        return currentval.get();
    }
    
    public double getTargetValue(){
        return targetval.get();
    }

    public void setTargetValue(double value){
        targetval.set(value);
    }

    public void setPower(double power){
        if(contract.isLauncherReady()){
            leader.set(power);
        }
    }

    public double getPower(){
        if(contract.isLauncherReady()){
            return leader.get();
        }
        return 0;
    }

    public void stop(){
        setPower(0);
    }

    public void periodic(){
        if(contract.isLauncherReady()){
            leader.periodic();
            follower.periodic();
        }
    }

    public void reset(){
        setPower(0);
        targetval.set(0);
    }

    // Incomplete
    @Override
    public boolean isMaintainerAtGoal(){ 
        boolean basicAtGoal = super.isMaintainerAtGoal();
        return basicAtGoal;
    }
    
    @Override
    public boolean isCalibrated(){
        // tests? should have a statement where
        // we use IsMaintainerAtGoal() to check.
        return false;
    }

}