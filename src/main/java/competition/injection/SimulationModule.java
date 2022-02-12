package competition.injection;

import competition.electrical_contract.ElectricalContract;
import competition.electrical_contract.SimulationContract;
import competition.injection.arm.ArmModule;
import competition.injection.arm.LeftArm;
import competition.injection.arm.RightArm;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.injection.swerve.SwerveModule;
import xbot.common.injection.SimulatorModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;


public class SimulationModule extends SimulatorModule {

    boolean isPractice;

    public SimulationModule() {
        this.isPractice = true;
    }
    
    @Override
    protected void configure() {
        super.configure();
        this.bind(ElectricalContract.class).to(SimulationContract.class);
        this.bind(BasePoseSubsystem.class).to(CompetitionModule.CHOSEN_POSE_SUBSYSTEM);
        this.bind(BaseDriveSubsystem.class).to(CompetitionModule.CHOSEN_DRIVE_SUBSYSTEM);

        this.install(new SwerveModule(FrontLeftDrive.class));
        this.install(new SwerveModule(FrontRightDrive.class));
        this.install(new SwerveModule(RearLeftDrive.class));
        this.install(new SwerveModule(RearRightDrive.class));

        this.install(new ArmModule(LeftArm.class));
        this.install(new ArmModule(RightArm.class));
    }
}
