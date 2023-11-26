// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.util.DynamicValue;

public class RobotContainer {

    private final DriveSubsystem drive = DriveSubsystem
        .create()
        .invert(Constants.Drive.LM_INVERSE, Constants.Drive.RM_INVERSE)
        .setOffset(
            new DynamicValue<>("DriveLMOffset", Constants.Drive.LM_SPEED_OFFSET),
            new DynamicValue<>("DriveRMOffset", Constants.Drive.RM_SPEED_OFFSET)
        )
        .setBrakeThreshold(Constants.Drive.BRAKE_THRESHOLD)
        .setSupplyLimit(
            Constants.Drive.CurrentLimit.SUPPLY,
            Constants.Drive.CurrentLimit.SUPPLY_LIMIT,
            Constants.Drive.CurrentLimit.SUPPLY_TRIGGER,
            Constants.Drive.CurrentLimit.SUPPLY_TRIGGER_TIME
        )
        .setStatorLimit(
            Constants.Drive.CurrentLimit.STATOR,
            Constants.Drive.CurrentLimit.STATOR_LIMIT,
            Constants.Drive.CurrentLimit.STATOR_TRIGGER,
            Constants.Drive.CurrentLimit.STATOR_TRIGGER_TIME
        )
        .setMaxTemp(Constants.Autonomous.MAX_TEMP)
        .addLeftMotors(
            Constants.Drive.MOTOR_TYPE,
            Constants.ID.LM1,
            Constants.ID.LM2
        )
        .addRightMotors(
            Constants.Drive.MOTOR_TYPE,
            Constants.ID.RM1,
            Constants.ID.RM2
        );
    private final GyroSubsystem gyro = new GyroSubsystem(
        Constants.Gyro.USE_ROLL,
        Constants.Gyro.UPSIDE_DOWN
    );
    private final Joystick controller = new Joystick(Constants.ID.JOYSTICK);
    private final SendableChooser<Command> autoChooser =
        new SendableChooser<>();

    public RobotContainer() {
        configureButtonBindings();

        drive.setDefaultCommand(
            new TeleopDrive(
                drive,
                () -> -controller.getRawAxis(Constants.TeleOp.LEFT_DRIVE_STICK),
                () ->
                    -controller.getRawAxis(Constants.TeleOp.RIGHT_DRIVE_STICK),
                Constants.TeleOp.SLEW_RATE_LIMIT
            )
        );

        autoChooser.addOption("None", null);
        SmartDashboard.putData(autoChooser);
    }

    @SuppressWarnings("all") // false positives from use of config constants
    private void configureButtonBindings() {}

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
