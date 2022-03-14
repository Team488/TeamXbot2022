package competition.subsystems.pose;

import com.google.inject.Inject;
import xbot.common.command.BaseCommand;
import xbot.common.math.FieldPose;

public class SetPoseCommand extends BaseCommand {

    private final PoseSubsystem pose;
    FieldPose fieldPose;

    @Inject
    public SetPoseCommand(PoseSubsystem pose) {
            this.pose = pose;
    }

    public void setPose(FieldPose fieldPose)
    {
        this.fieldPose = fieldPose;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        pose.setCurrentPose(fieldPose);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}