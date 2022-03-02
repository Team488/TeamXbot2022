# Driving the Robot

The robot is driven using two XBox One controllers - one for the driver and one for the operator. The driver will generally control the position of the robot on the field, while the operator will generally operate other robot functions (i.e. climber).

![XBox One controller](./images/xboxone-controller.png)

## Getting Started

Drive around with the **left / right joysticks** from the **driver gamepad**.

In order to operate the **climber arms**, you need to calibrate it first. From the **operator gamepad**, enter **unsafe mode** with **X** and slowly use the **left / right joysticks** to bring the arms to their lowest position. Press **Y** to calibrate the arms, and then press **B** to enter maintainer mode.

In order to unlatch the climber hooks, the **driver** must press **left / right triggers** and the **operator** must press the **back** button simultaneously. If you don't have two controllers, you can use **SmartDashboard** to unlatch the climber hooks. The latch will re-latch automatically.

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
| Collector intake | Left trigger |
| Collector eject | Right trigger |
| Unlatch | Back |
| Manually move left arm | Left joystick Y |
| Manually move right arm | Right joystick Y |

## Shooter Gamepad

**Gamepad ID:** 2

| Command | Button |
| :-- | :-- |
| Set near shot shooter speed | Hold A |
| Set distance shot shooter speed | Hold X |
| Stop shooter wheel | B |
| Shooter speed trim up | Right Bumper |
| Shooter speed trim down | Left Bumper |

## SmartDashboard commands

* Move arms to position
* Apply steering pid values
* Unlatch (without two person override, no timeout)
* Latch
