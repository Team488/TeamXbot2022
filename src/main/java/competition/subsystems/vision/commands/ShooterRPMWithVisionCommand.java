package competition.subsystems.vision.commands;

import javax.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.Target;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem.TargetRPM;
import competition.subsystems.vision.VisionShooterGoalsSubsystem;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XXboxController;

public class ShooterRPMWithVisionCommand extends BaseCommand {

    private final VisionSubsystem vision;
    private final VisionShooterGoalsSubsystem visionGoals;
    private final ShooterWheelSubsystem shooter;
    private Target target = Target.Low;
    private final XXboxController operatorGamepad;
    
    @Inject
    public ShooterRPMWithVisionCommand(
        ShooterWheelSubsystem shooter,
        VisionSubsystem vision,
        VisionShooterGoalsSubsystem visionGoals,
        OperatorInterface oi
    ) {
        this.vision = vision;
        this.visionGoals = visionGoals;
        this.shooter = shooter;
        this.operatorGamepad = oi.operatorGamepad;
        this.addRequirements(shooter.getSetpointLock());
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        boolean fixAcquired = vision.getFixAcquired();
        double pitch = vision.getPitchToHub();

        if(target == Target.Low) {
            if(fixAcquired) {
                shooter.setTargetRPM(visionGoals.speedFromPitchLow(pitch));
            } else {
                // if shooter wasn't moving at all, get it going to near shot speed
                if(!(shooter.getTargetRPM() > 0)) {
                    shooter.setTargetRPM(TargetRPM.NearShot);
                }
                // otherwise leave shooter at whatever the last speed it was at
            }
        } else {
            if(fixAcquired) {
                shooter.setTargetRPM(visionGoals.speedFromPitchHigh(pitch));
            } else {
                // if shooter wasn't moving at all, get it going to near shot speed
                if(!(shooter.getTargetRPM() > 0)) {
                    shooter.setTargetRPM(TargetRPM.DistanceShot);
                }
                // otherwise leave shooter at whatever the last speed it was at
            }
        }

        // rumble operator gamepad if it's a shot that can't be made
        if(!visionGoals.inCalibratedRange(target, fixAcquired, pitch)) {
            operatorGamepad.getRumbleManager().rumbleGamepad(0.2, 0.01);
        } else {
            operatorGamepad.getRumbleManager().stopGamepadRumble();
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setTargetRPM(0);
        operatorGamepad.getRumbleManager().stopGamepadRumble();
    }
}
