# Driving the Robot

The robot is driven using two XBox One controllers - one for the driver and one for the operator. The driver will generally control the position of the robot on the field, while the operator will generally operate other robot functions (i.e. climber).

![XBox One controller](./images/xboxone-controller.png)

## Driver Gamepad

**Gamepad ID:** 0

| Command | Button |
| :-- | :-- |
| Reset Pose | A |
| Swerve debug mode | B |
| Swerve debug mode next module | X |
| Swerve to point | Y |
| Calibrate steering | Left bumper |
| Swerve drive mode | Right bumper |
| Apply steering pid values | Back |
| Turn left 90 degrees | Start |
| Unlatch | Left + Right Trigger |
| Move (translate) | Left joystick X+Y |
| Move (rotate) | Right joystick X |

## Operator Gamepad

**Gamepad ID:** 1

| Command | Button |
| :-- | :-- |
| Stop both arms | A |
| Maintain arms | B |
| Unsafe arms mode | X |
| Calibrate arms | Y |
| Pivot in | Left bumper |
| Pivot out | Right bumper |
| Free pawl | Left trigger |
| Lock pawl | Right trigger |
| Unlatch | Back |
| Manually move left arm | Left joystick Y |
| Manually move right arm | Right joystick Y |

## SmartDashboard commands

* Move arms to position
* Apply steering pid values
* Unlatch
