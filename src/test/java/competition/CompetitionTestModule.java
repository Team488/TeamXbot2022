package competition;

import competition.electrical_conract.TestElectricalContract;
import competition.electrical_contract.ElectricalContract;
import competition.injection.CompetitionModule;
import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.injection.swerve.SwerveModule;
import xbot.common.injection.UnitTestModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionTestModule extends UnitTestModule {
    
    @Override
    protected void configure() {
        super.configure();
        this.bind(BasePoseSubsystem.class).to(CompetitionModule.CHOSEN_POSE_SUBSYSTEM);
        this.bind(BaseDriveSubsystem.class).to(CompetitionModule.CHOSEN_DRIVE_SUBSYSTEM);
        this.bind(ElectricalContract.class).to(TestElectricalContract.class);
        
        this.install(new SwerveModule(FrontLeftDrive.class));
        this.install(new SwerveModule(FrontRightDrive.class));
        this.install(new SwerveModule(RearLeftDrive.class));
        this.install(new SwerveModule(RearRightDrive.class));
    }
}