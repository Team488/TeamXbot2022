package competition.injection.swerve;

import competition.subsystems.drive.commands.SwerveDriveMaintainerCommand;
import competition.subsystems.drive.commands.SwerveSteeringMaintainerCommand;
import competition.subsystems.drive.swerve.SwerveDriveSubsystem;
import competition.subsystems.drive.swerve.SwerveModuleSubsystem;
import competition.subsystems.drive.swerve.SwerveSteeringSubsystem;

public abstract class SwerveComponent {
    public abstract SwerveInstance swerveInstance();

    public abstract SwerveModuleSubsystem swerveModuleSubsystem();

    public abstract SwerveDriveSubsystem swerveDriveSubsystem();

    public abstract SwerveDriveMaintainerCommand swerveDriveMaintainerCommand();

    public abstract SwerveSteeringSubsystem swerveSteeringSubsystem();

    public abstract SwerveSteeringMaintainerCommand swerveSteeringMaintainerCommand();
}
