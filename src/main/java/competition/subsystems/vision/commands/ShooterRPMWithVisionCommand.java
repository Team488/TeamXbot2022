package competition.subsystems.vision.commands;

import com.google.inject.Inject;

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
        visionGoals.setIsTargeting(true);
        visionGoals.setIsInRange(false);
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
                double targetRPM = visionGoals.speedFromPitchHigh(pitch);
                double arbFF = visionGoals.feedForwardFromPitchHigh(pitch, shooter.leader.getFF());
                shooter.setArbitraryFF(arbFF);
                shooter.setTargetRPM(targetRPM);
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
            visionGoals.setIsTargeting(true);
            visionGoals.setIsInRange(false);
        } else {
            operatorGamepad.getRumbleManager().stopGamepadRumble();
            visionGoals.setIsTargeting(true);
            visionGoals.setIsInRange(false);
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setTargetRPM(0);
        shooter.setArbitraryFF(0);
        operatorGamepad.getRumbleManager().stopGamepadRumble();

        visionGoals.setIsTargeting(false);
        visionGoals.setIsInRange(false);
    }
}
