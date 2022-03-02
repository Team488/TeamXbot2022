package competition.subsystems.climber_pivot.commands;

import com.google.inject.Inject;

import competition.injection.arm.LeftArm;
import competition.subsystems.climber_arm.ClimberArmSubsystem;
import competition.subsystems.climber_pivot.ClimberPivotSubsystem;
import xbot.common.command.BaseCommand;

public class PivotAccordingToArm extends BaseCommand {

    final ClimberPivotSubsystem pivot;
    final ClimberArmSubsystem leftArm;

    @Inject
    public PivotAccordingToArm(
        ClimberPivotSubsystem pivot, 
        @LeftArm ClimberArmSubsystem leftArm) {
        this.pivot = pivot;
        this.leftArm = leftArm;

        this.addRequirements(pivot);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (leftArm.isCalibrated() && pivot.isAllowedtoAutomaticallyPivot()) {
            double armHeight = leftArm.getPosition();
            if (armHeight > leftArm.getAutomaticPivotInPosition()) {
                pivot.pivotIn();
            } else if (armHeight > leftArm.getClearCurrentBarPosition()) {
                pivot.pivotOut();
            } else {
                // Do nothing.
            }
        } else {
            // Not calibrated - Do nothing.
        }
    }
}