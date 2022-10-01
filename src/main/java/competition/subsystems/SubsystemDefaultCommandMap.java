package competition.subsystems;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.injection.swerve.FrontLeftDrive;
import competition.injection.swerve.FrontRightDrive;
import competition.injection.swerve.RearLeftDrive;
import competition.injection.swerve.RearRightDrive;
import competition.injection.swerve.SwerveComponent;
import competition.subsystems.climber_arm.ClimberSubsystem;
import competition.subsystems.climber_arm.commands.DualArmBalancerCommand;
import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.collector_deployment.CollectorDeploymentSubsystem;
import competition.subsystems.collector_deployment.commands.RetractCollectorAfterDelayCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.SwerveDriveWithJoysticksCommand;
import competition.subsystems.latch.LatchSubsystem;
import competition.subsystems.latch.commands.LatchArmCommand;
import competition.subsystems.shooterwheel.ShooterWheelSubsystem;
import competition.subsystems.shooterwheel.commands.ShooterWheelMaintainerCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems
    @Inject
    public SubsystemDefaultCommandMap() {}

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, SwerveDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupFrontLeftSubsystems(
            @FrontLeftDrive SwerveComponent swerveComponent) {
        swerveComponent.swerveDriveSubsystem().setDefaultCommand(swerveComponent.swerveDriveMaintainerCommand());
        swerveComponent.swerveSteeringSubsystem().setDefaultCommand(swerveComponent.swerveSteeringMaintainerCommand());
    }

    @Inject
    public void setupFrontRightSubsystems(
            @FrontRightDrive SwerveComponent swerveComponent) {
        swerveComponent.swerveDriveSubsystem().setDefaultCommand(swerveComponent.swerveDriveMaintainerCommand());
        swerveComponent.swerveSteeringSubsystem().setDefaultCommand(swerveComponent.swerveSteeringMaintainerCommand());
    }
    
    @Inject
    public void setupRearLeftSubsystems(
            @RearLeftDrive SwerveComponent swerveComponent) {
        swerveComponent.swerveDriveSubsystem().setDefaultCommand(swerveComponent.swerveDriveMaintainerCommand());
        swerveComponent.swerveSteeringSubsystem().setDefaultCommand(swerveComponent.swerveSteeringMaintainerCommand());
    }

    @Inject
    public void setupRearRightSubsystems(
            @RearRightDrive SwerveComponent swerveComponent) {
        swerveComponent.swerveDriveSubsystem().setDefaultCommand(swerveComponent.swerveDriveMaintainerCommand());
        swerveComponent.swerveSteeringSubsystem().setDefaultCommand(swerveComponent.swerveSteeringMaintainerCommand());
    }

    @Inject
    public void setupLatchSubsystem(LatchSubsystem subsystem, LatchArmCommand latch) {
        subsystem.setDefaultCommand(latch);
    }

    // For now, have the default state of the arms be stopped - that's pretty safe. Later,
    // once we have the PIDs tuned, we can move to using the maintainers.
    @Inject
    public void setupArms(
        ClimberSubsystem bothArms,
        DualArmBalancerCommand dualArmBalancer) {
        bothArms.setDefaultCommand(dualArmBalancer);
    }

    @Inject
    public void setupCollector(
        CollectorSubsystem collector,
        StopCollectorCommand stop) {
        collector.setDefaultCommand(stop);
        }

    @Inject
    public void setupShooterWheelSubsystem(ShooterWheelMaintainerCommand shooterMaintainer, ShooterWheelSubsystem shooter) {
        shooter.setDefaultCommand(shooterMaintainer);

        shooter.getSetpointLock().setDefaultCommand(
            new RunCommand(() -> {
                shooter.setTargetRPM(0.0);
                shooter.setFullPowerMode(false);
            }, shooter.getSetpointLock())
        );
    }

    @Inject
    public void setupCollectorDeployment(
        CollectorDeploymentSubsystem collectorDeploy, 
        RetractCollectorAfterDelayCommand retractAfterDelay) {
        collectorDeploy.setDefaultCommand(retractAfterDelay);
    }
}
