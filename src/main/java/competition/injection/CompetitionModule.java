package competition.injection;

import competition.electrical_contract.CompetitionContract;
import competition.electrical_contract.ElectricalContract;
import competition.electrical_contract.PracticeContract;
import competition.injection.arm.ArmModule;
import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.injection.swerve.SwerveModule;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.RobotModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionModule extends RobotModule {

    boolean isPractice;

    public static final Class<PoseSubsystem> CHOSEN_POSE_SUBSYSTEM = PoseSubsystem.class;
    public static final Class<DriveSubsystem> CHOSEN_DRIVE_SUBSYSTEM = DriveSubsystem.class;

    public CompetitionModule(boolean isPractice) {
        this.isPractice = isPractice;
    }

    @Override
    protected void configure() {
        super.configure();
        if (!isPractice) {
            this.bind(ElectricalContract.class).to(CompetitionContract.class);
        } else {
            this.bind(ElectricalContract.class).to(PracticeContract.class);
        }

        this.bind(BasePoseSubsystem.class).to(CHOSEN_POSE_SUBSYSTEM);
        this.bind(BaseDriveSubsystem.class).to(CHOSEN_DRIVE_SUBSYSTEM);

        this.install(new SwerveModule(FrontLeftDrive.class));
        this.install(new SwerveModule(FrontRightDrive.class));
        this.install(new SwerveModule(RearLeftDrive.class));
        this.install(new SwerveModule(RearRightDrive.class));

        this.install(new ArmModule(LeftArm.class));
        this.install(new ArmModule(RightArm.class));
    }
}
