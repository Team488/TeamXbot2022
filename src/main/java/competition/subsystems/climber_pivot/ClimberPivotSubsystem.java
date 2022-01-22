package competition.subsystems.climber_pivot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class ClimberPivotSubsystem extends BaseSubsystem{
    public XSolenoid pivot;

    @Inject
    public ClimberPivotSubsystem(CommonLibFactory factory) {
        pivot = factory.createSolenoid(2);
    }
    public void pivotIn() {
        pivot.setOn(false);
    }
    public void pivotOut() {
        pivot.setOn(true);
    }
}