package competition.injection.components;

import competition.injection.arm.ArmComponentHolder;
import competition.injection.swerve.SwerveComponentHolder;
import competition.operator_interface.OperatorCommandMap;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.arduino.ArduinoCommunicationSubsystem;
import xbot.common.injection.components.BaseComponent;

public abstract class BaseRobotComponent extends BaseComponent {
    public abstract SubsystemDefaultCommandMap subsystemDefaultCommandMap();

    public abstract OperatorCommandMap operatorCommandMap();

    public abstract OperatorInterface operatorInterface();

    public abstract ArduinoCommunicationSubsystem arduinoCommunicationSubsystem();

    public abstract ArmComponentHolder armComponents();

    public abstract SwerveComponentHolder swerveComponents();
}
