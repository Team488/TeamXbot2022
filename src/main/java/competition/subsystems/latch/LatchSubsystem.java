package competition.subsystems.latch;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class LatchSubsystem extends BaseSubsystem {
    public XSolenoid latch;

    @Inject
    public LatchSubsystem(CommonLibFactory factory) {
        //latch = factory.createSolenoid(1);
    }

    public void arm(){
        latch.setOn(true);
    }
    public void release(){
        latch.setOn(false);
    }

}