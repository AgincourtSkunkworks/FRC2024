// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.*;
import frc.robot.commands.*;

public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem(
            Constants.ID.LM1, Constants.ID.LM2, Constants.ID.RM1, Constants.ID.RM2, Constants.Drive.LM_INVERSE,
            Constants.Drive.RM_INVERSE, Constants.Drive.LM_SPEED_OFFSET, Constants.Drive.RM_SPEED_OFFSET,
            Constants.Drive.BRAKE_THRESHOLD, Constants.Drive.THERMAL_WARNING, Constants.Drive.CurrentLimit.SUPPLY,
            Constants.Drive.CurrentLimit.SUPPLY_LIMIT, Constants.Drive.CurrentLimit.SUPPLY_TRIGGER,
            Constants.Drive.CurrentLimit.SUPPLY_TRIGGER_TIME, Constants.Drive.CurrentLimit.STATOR,
            Constants.Drive.CurrentLimit.STATOR_LIMIT, Constants.Drive.CurrentLimit.STATOR_TRIGGER,
            Constants.Drive.CurrentLimit.STATOR_TRIGGER_TIME, Constants.Autonomous.MAX_TEMP);
    private final Joystick controller = new Joystick(Constants.ID.JOYSTICK);
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        configureButtonBindings();

        drive.setDefaultCommand(
                new TeleopDrive(drive, () -> -controller.getRawAxis(Constants.TeleOp.LEFT_DRIVE_STICK),
                        () -> -controller.getRawAxis(Constants.TeleOp.RIGHT_DRIVE_STICK),
                        Constants.TeleOp.SLEW_RATE_LIMIT));
        
        autoChooser.addOption("None", null);
        SmartDashboard.putData(autoChooser);
    }

    @SuppressWarnings("all") // false positives from use of config constants
    private void configureButtonBindings() {
        
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
