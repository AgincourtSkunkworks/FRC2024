# Team 1246 FRC Code (2024)
Source code used for Team 1246's 2024 FRC robot. Documentation that may be useful for those working on this codebase can be found in the [docs](https://agincourtskunkworks.github.io/FRCDocs/Programming/).

## Features
- Extensive data logging to SmartDashboard (Shuffleboard)
- Use of RobotPreferences
  - Allows for the dynamic modification of values (such as PID constants) in production without a redeploy
  - Modularly built to allow for an easy switch from using the configuration file to RobotPreferences (via [DynamicValue](src/main/java/frc/robot/util/DynamicValue.java))
- Configuration file ([Constants](src/main/java/frc/robot/Constants.java)) for a high degree of control over functionality without code modification, including:
  - Joystick Bindings
  - Motor IDs & Types
  - Current Limits
  - Autonomous Speeds
  - Disabling Specific Subsystems
- Generic wrappers for [joysticks](src/main/java/frc/robot/util/GenericJoystick.java) and [motor controllers](src/main/java/frc/robot/util/GenericController.java) ([docs](https://agincourtskunkworks.github.io/FRCDocs/programming/generic-wrappers/))
  - For rapid switches between different types of joysticks and motor controllers
  - Switches can often be made in as little as 1 change to the constants file, with no additional code adjustment
  - Reduces the domain knowledge necessary for new contributors
- Use of [commands](src/main/java/frc/robot/commands) and [command factories](src/main/java/frc/robot/commands/factories)
  - To significantly reduce boilerplate
  - For ease of integration with existing command sequences
  - Creates a modular system that allows for segmented work between contributors
  - Generics commands (such as [GenericPID](src/main/java/frc/robot/commands/GenericPID.java)) to build more complex logic off of
- Follows a consistent design practice
- Consistent styling through [prettier](https://prettier.io/)

## Autonomous
Combinations of most of the following can be used in autonomous, and be selected via SmartDashboard:

- Leave Community Zone
- Prepositioned Note Shot

## TeleOp
- Motor Ramp Up
- 2 Joystick Sensitivity Modes, for Coarse and Fine Movement Control
- Assisted/Automatic Arm Positioning, Intake, and Outtake (Shooter)
  - Manual Overrides Available

---

<details>
  <summary>Closing Note</summary>

  > Thank you for a wonderful 2024 season, and best of luck with future competitions - TH
</details>
