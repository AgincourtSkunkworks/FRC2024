// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.util.DynamicValue;

public class RobotContainer {

    private final DriveSubsystem drive = DriveSubsystem
        .create()
        .invert(Constants.Drive.LM_INVERSE, Constants.Drive.RM_INVERSE)
        .setOffset(
            Constants.Drive.LM_SPEED_OFFSET,
            Constants.Drive.RM_SPEED_OFFSET
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
            Constants.Drive.CurrentLimit.STATOR_LIMIT
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
    private final IntakeSubsystem.Rotation intakeRotation = IntakeSubsystem.Rotation
        .create(
            Constants.Intake.Rotation.MOTOR_TYPE,
            Constants.ID.IRTLM,
            Constants.Intake.Rotation.LM_INVERSE,
            Constants.ID.IRTRM,
            Constants.Intake.Rotation.RM_INVERSE
        )
        .setSupplyLimit(
            Constants.Intake.Rotation.CurrentLimit.SUPPLY,
            Constants.Intake.Rotation.CurrentLimit.SUPPLY_LIMIT,
            Constants.Intake.Rotation.CurrentLimit.SUPPLY_TRIGGER,
            Constants.Intake.Rotation.CurrentLimit.SUPPLY_TRIGGER_TIME
        )
        .setStatorLimit(
            Constants.Intake.Rotation.CurrentLimit.STATOR,
            Constants.Intake.Rotation.CurrentLimit.STATOR_LIMIT
        );
    private final IntakeSubsystem.Feeder intakeFeeder = new IntakeSubsystem.Feeder(
        Constants.Intake.Feeder.MOTOR_TYPE,
        Constants.ID.IF,
        Constants.Intake.Feeder.INVERSE
    );
    private final OuttakeSubsystem outtake = new OuttakeSubsystem(
        Constants.Outtake.FLYWHEEL_MOTOR_TYPE,
        Constants.ID.OFLM,
        Constants.Outtake.FLYWHEEL_LM_INVERSE,
        Constants.ID.OFRM,
        Constants.Outtake.FLYWHEEL_RM_INVERSE
    );
    private final Joystick controller = new Joystick(Constants.ID.JOYSTICK);
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private final RotationPID rotationLowPID = new RotationPID(
        "IntakeLow",
        intakeRotation,
        Constants.Intake.Rotation.Setpoints.LOW,
        new DynamicValue<>(
            "IntakeLowTolerance",
            Constants.Intake.Rotation.DefaultPID.FINISH_TOLERANCE
        ),
        Constants.Intake.Rotation.DefaultPID.P,
        Constants.Intake.Rotation.DefaultPID.I,
        Constants.Intake.Rotation.DefaultPID.D,
        Constants.Intake.Rotation.DefaultPID.IMax
    ), rotationHighPID = new RotationPID(
        "IntakeHigh",
        intakeRotation,
        Constants.Intake.Rotation.Setpoints.HIGH,
        new DynamicValue<>(
            "IntakeHighTolerance",
            Constants.Intake.Rotation.DefaultPID.FINISH_TOLERANCE
        ),
        Constants.Intake.Rotation.DefaultPID.P,
        Constants.Intake.Rotation.DefaultPID.I,
        Constants.Intake.Rotation.DefaultPID.D,
        Constants.Intake.Rotation.DefaultPID.IMax
    );

    public RobotContainer() {
        SmartDashboard.putData(CommandScheduler.getInstance());
        SmartDashboard.putData(drive);
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

        rotationHighPID.schedule();
        autoChooser.addOption("None", null);
        SmartDashboard.putData(autoChooser);
    }

    @SuppressWarnings("all") // false positives from use of config constants
    private void configureButtonBindings() {
        // * AUTOMATED SEQUENCES
        new JoystickButton(controller, Constants.Intake.TRIGGER_BTN)
            .onTrue( // prime for intake of piece when pressed
                new SequentialCommandGroup(
                    rotationLowPID,
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(Constants.Intake.Feeder.SPEED)
                    )
                )
            )
            .onFalse( // intake piece when released
                new SequentialCommandGroup(
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    rotationHighPID
                )
            );
        new JoystickButton(controller, Constants.Outtake.TRIGGER_BTN)
            .onTrue(
                new SequentialCommandGroup(
                    rotationHighPID,
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(-Constants.Intake.Feeder.SPEED)
                    ),
                    Commands.waitSeconds(Constants.Intake.Feeder.RELEASE_WAIT),
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    Commands.runOnce(() ->
                        outtake.setMotors(Constants.Outtake.SPEED)
                    )
                )
            )
            .onFalse(Commands.runOnce(() -> outtake.setMotors(0)));

        // * MANUAL OVERRIDES

        new JoystickButton(
            controller,
            Constants.Intake.Rotation.OVERRIDE_LOW_BTN
        )
            .onTrue(rotationLowPID);
        new JoystickButton(
            controller,
            Constants.Intake.Rotation.OVERRIDE_HIGH_BTN
        )
            .onTrue(rotationHighPID);
        new JoystickButton(controller, Constants.Intake.Feeder.OVERRIDE_FWD_BTN)
            .whileTrue(
                Commands.startEnd(
                    () -> intakeFeeder.setMotor(Constants.Intake.Feeder.SPEED),
                    () -> intakeFeeder.setMotor(0)
                )
            );
        new JoystickButton(controller, Constants.Intake.Feeder.OVERRIDE_REV_BTN)
            .whileTrue(
                Commands.startEnd(
                    () -> intakeFeeder.setMotor(-Constants.Intake.Feeder.SPEED),
                    () -> intakeFeeder.setMotor(0)
                )
            );
        new JoystickButton(controller, Constants.Outtake.OVERRIDE_BTN)
            .whileTrue(
                Commands.startEnd(
                    () -> outtake.setMotors(Constants.Outtake.SPEED),
                    () -> outtake.setMotors(0)
                )
            );
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
