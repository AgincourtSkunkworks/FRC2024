// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.commands.factories.*;
import frc.robot.subsystems.*;
import frc.robot.util.DynamicValue;
import frc.robot.util.GenericJoystick;

public class RobotContainer {

    // ! SUBSYSTEMS
    private final DriveSubsystem drive;
    private final IntakeSubsystems.RotationSubsystem intakeRotation;
    private final IntakeSubsystems.FeederSubsystem intakeFeeder;
    private final OuttakeSubsystem outtake;
    private final ClimberSubsystem climber;

    {
        drive =
            DriveSubsystem
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
        intakeRotation =
            IntakeSubsystems.RotationSubsystem
                .create(
                    Constants.Intake.Rotation.MOTOR_TYPE,
                    Constants.ID.IRTLM,
                    Constants.Intake.Rotation.LM_INVERSE,
                    Constants.ID.IRTRM,
                    Constants.Intake.Rotation.RM_INVERSE
                )
                .setNeutralMode(Constants.Intake.Rotation.NEUTRAL_MODE)
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
        intakeFeeder =
            new IntakeSubsystems.FeederSubsystem(
                Constants.Intake.Feeder.MOTOR_TYPE,
                Constants.ID.IF,
                Constants.Intake.Feeder.INVERSE
            );
        outtake =
            new OuttakeSubsystem(
                Constants.Outtake.FLYWHEEL_MOTOR_TYPE,
                Constants.ID.OFLM,
                Constants.Outtake.FLYWHEEL_LM_INVERSE,
                Constants.ID.OFRM,
                Constants.Outtake.FLYWHEEL_RM_INVERSE
            )
                .setSupplyLimit(
                    Constants.Outtake.CurrentLimit.SUPPLY,
                    Constants.Outtake.CurrentLimit.SUPPLY_LIMIT,
                    Constants.Outtake.CurrentLimit.SUPPLY_TRIGGER,
                    Constants.Outtake.CurrentLimit.SUPPLY_TRIGGER_TIME
                );
        climber =
            ClimberSubsystem
                .create(
                    Constants.Climber.MOTOR_TYPE,
                    Constants.ID.CL,
                    Constants.Climber.INVERSE
                )
                .setSupplyLimit(
                    Constants.Climber.CurrentLimit.SUPPLY,
                    Constants.Climber.CurrentLimit.SUPPLY_LIMIT,
                    Constants.Climber.CurrentLimit.SUPPLY_TRIGGER,
                    Constants.Climber.CurrentLimit.SUPPLY_TRIGGER_TIME
                )
                .setStatorLimit(
                    Constants.Climber.CurrentLimit.STATOR,
                    Constants.Climber.CurrentLimit.STATOR_LIMIT
                )
                .setNeutralMode(Constants.Climber.NEUTRAL_MODE);
    }

    // ! CONTROLS
    private final GenericJoystick controller;
    private final SendableChooser<Command> autoChooser;

    {
        controller =
            new GenericJoystick(Constants.Joystick.TYPE, Constants.ID.JOYSTICK);
        autoChooser = new SendableChooser<>();
    }

    // ! COMMAND FACTORIES
    private final RotationPIDFactory rotationLowPID, rotationHighPID;
    private final ClimberPIDFactory climberLowPID, climberHighPID;

    {
        rotationLowPID =
            new RotationPIDFactory(
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
            );
        rotationHighPID =
            new RotationPIDFactory(
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
        climberLowPID =
            new ClimberPIDFactory(
                "ClimberLow",
                climber,
                Constants.Climber.Setpoints.LOW,
                // TODO: Check whether this can end
                //        new DynamicValue<>(
                //            "ClimberLowTolerance",
                //            Constants.Climber.DefaultPID.FINISH_TOLERANCE
                //        ),
                Constants.Climber.DefaultPID.P,
                Constants.Climber.DefaultPID.I,
                Constants.Climber.DefaultPID.D,
                Constants.Climber.DefaultPID.IMax
            );

        climberHighPID =
            new ClimberPIDFactory(
                "ClimberHigh",
                climber,
                Constants.Climber.Setpoints.HIGH,
                // TODO: Check whether this can end
                //        new DynamicValue<>(
                //            "ClimberHighTolerance",
                //            Constants.Climber.DefaultPID.FINISH_TOLERANCE
                //        ),
                Constants.Climber.DefaultPID.P,
                Constants.Climber.DefaultPID.I,
                Constants.Climber.DefaultPID.D,
                Constants.Climber.DefaultPID.IMax
            );
    }

    public RobotContainer() {
        // ! DEBUG TOOLS (DANGEROUS)
        handleStartingDebug();

        // ! SMART DASHBOARD DATA
        SmartDashboard.putData(CommandScheduler.getInstance());
        SmartDashboard.putData(drive);
        SmartDashboard.putData(intakeRotation);
        SmartDashboard.putData(intakeFeeder);
        SmartDashboard.putData(outtake);
        if (Constants.Climber.ENABLE) SmartDashboard.putData(climber);

        // ! SMART DASHBOARD BUTTONS
        Command zeroRotationPosCmd = Commands.runOnce(() ->
            intakeRotation.setPositions(0)
        );
        zeroRotationPosCmd.setName("ZeroRotationPos");
        SmartDashboard.putData(zeroRotationPosCmd);

        // ! BUTTONS
        configureButtonBindings();

        // ! DEFAULT COMMANDS
        drive.setDefaultCommand(
            new TeleopDrive(
                drive,
                () -> -controller.getRawAxis(Constants.TeleOp.LEFT_DRIVE_STICK),
                () ->
                    -controller.getRawAxis(Constants.TeleOp.RIGHT_DRIVE_STICK),
                Constants.TeleOp.SLEW_RATE_LIMIT
            )
        );
        if (Constants.Climber.ENABLE) climber.setDefaultCommand(
            climberLowPID.create()
        );

        // ! CONFIGURATION
        intakeRotation.setPositions(0); // ! INTAKE IS EXPECTED TO BE IN HIGH AT STARTUP

        // ! AUTONOMOUS
        autoChooser.addOption(
            "Leave (Straight)",
            new DriveForTime(
                drive,
                Constants.Autonomous.MOVE_SPEED,
                Constants.Autonomous.COMM_LEAVE_TIME
            )
        );
        autoChooser.addOption(
            "Shoot & Leave (Straight)",
            new SequentialCommandGroup(
                Commands.runOnce(() ->
                    intakeFeeder.setMotor(-Constants.Intake.Feeder.SHOOT_SPEED)
                ),
                Commands.runOnce(() ->
                    outtake.setMotors(Constants.Outtake.SPEED)
                ),
                Commands.waitSeconds(Constants.Autonomous.SHOOT_TIME),
                Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                Commands.runOnce(() -> outtake.setMotors(0)),
                new DriveForTime(
                    drive,
                    Constants.Autonomous.MOVE_SPEED,
                    Constants.Autonomous.COMM_LEAVE_TIME
                )
            )
        );
        autoChooser.addOption("None", null);
        SmartDashboard.putData(autoChooser);
    }

    /**
     * Creates the expected controller bindings.
     */
    private void configureButtonBindings() {
        // ! Intake/Outtake
        // * AUTOMATED SEQUENCES
        controller
            .getButton(Constants.Intake.TRIGGER_BTN)
            .whileTrue( // prime for intake of piece when pressed
                new SequentialCommandGroup(
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(Constants.Intake.Feeder.SPEED)
                    ),
                    rotationLowPID.create()
                )
            )
            .whileFalse( // intake piece when released
                new SequentialCommandGroup(
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    rotationHighPID.create()
                )
            );
        controller
            .getButton(Constants.Intake.UNLOAD_BTN)
            .whileTrue(
                new SequentialCommandGroup(
                    Commands.runOnce(() ->
                        outtake.setMotors(-Constants.Outtake.SPEED)
                    ),
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(Constants.Intake.Feeder.SPEED)
                    ),
                    Commands.waitSeconds(Constants.Intake.UNLOAD_CONSUME_TIME),
                    Commands.runOnce(() -> outtake.setMotors(0)),
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    rotationLowPID.create(),
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(-Constants.Intake.Feeder.SPEED)
                    )
                )
            )
            .whileFalse(
                new SequentialCommandGroup(
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    rotationHighPID.create()
                )
            );
        controller
            .getButton(Constants.Outtake.TRIGGER_BTN)
            .whileTrue(
                new SequentialCommandGroup(
                    rotationHighPID.create(),
                    (Constants.Intake.Feeder.HOLD_ON_SPINUP)
                        ? Commands.runOnce(() ->
                            intakeFeeder.setMotor(Constants.Intake.Feeder.SPEED)
                        )
                        : Commands.none(),
                    Commands.runOnce(() ->
                        outtake.setMotors(Constants.Outtake.SPEED)
                    ),
                    Commands.waitSeconds(Constants.Outtake.SPINUP_TIME),
                    Commands.runOnce(() ->
                        intakeFeeder.setMotor(
                            -Constants.Intake.Feeder.SHOOT_SPEED
                        )
                    )
                )
            )
            .whileFalse(
                new SequentialCommandGroup(
                    Commands.runOnce(() -> intakeFeeder.setMotor(0)),
                    Commands.runOnce(() -> outtake.setMotors(0))
                )
            );

        // * MANUAL OVERRIDES

        controller
            .getButton(Constants.Intake.Rotation.OVERRIDE_LOW_BTN)
            .onTrue(rotationLowPID.create());
        controller
            .getButton(Constants.Intake.Rotation.OVERRIDE_HIGH_BTN)
            .onTrue(rotationHighPID.create());
        controller
            .getPOVButton(Constants.Intake.Rotation.OVERRIDE_FWD_POV)
            .whileTrue(
                Commands.startEnd(
                    () ->
                        intakeRotation.setMotors(
                            Constants.Intake.Rotation.OVERRIDE_SPEED
                        ),
                    () -> intakeRotation.setMotors(0)
                )
            );
        controller
            .getPOVButton(Constants.Intake.Rotation.OVERRIDE_REV_POV)
            .whileTrue(
                Commands.startEnd(
                    () ->
                        intakeRotation.setMotors(
                            -Constants.Intake.Rotation.OVERRIDE_SPEED
                        ),
                    () -> intakeRotation.setMotors(0)
                )
            );
        controller
            .getButton(Constants.Intake.Feeder.OVERRIDE_FWD_BTN)
            .whileTrue(
                Commands.startEnd(
                    () ->
                        intakeFeeder.setMotor(
                            Constants.Intake.Feeder.SHOOT_SPEED
                        ),
                    () -> intakeFeeder.setMotor(0)
                )
            );
        controller
            .getButton(Constants.Intake.Feeder.OVERRIDE_REV_BTN)
            .whileTrue(
                Commands.startEnd(
                    () ->
                        intakeFeeder.setMotor(
                            -Constants.Intake.Feeder.SHOOT_SPEED
                        ),
                    () -> intakeFeeder.setMotor(0)
                )
            );
        controller
            .getButton(Constants.Outtake.OVERRIDE_BTN)
            .whileTrue(
                Commands.startEnd(
                    () -> outtake.setMotors(Constants.Outtake.SPEED),
                    () -> outtake.setMotors(0)
                )
            );

        if (Constants.Climber.ENABLE) {
            controller
                .getPOVButton(Constants.Climber.OVERRIDE_UP_POV)
                .whileTrue(
                    Commands.startEnd(
                        () ->
                            climber.setMotor(Constants.Climber.OVERRIDE_SPEED),
                        () -> climber.setMotor(0)
                    )
                );
            controller
                .getPOVButton(Constants.Climber.OVERRIDE_DOWN_POV)
                .whileTrue(
                    Commands.startEnd(
                        () ->
                            climber.setMotor(-Constants.Climber.OVERRIDE_SPEED),
                        () -> climber.setMotor(0)
                    )
                );
        }

        // ! Climber
        if (Constants.Climber.ENABLE) {
            controller
                .getButton(Constants.Climber.LOW_BTN)
                .onTrue(climberLowPID.create());
            controller
                .getButton(Constants.Climber.HIGH_BTN)
                .onTrue(climberHighPID.create());
        }
    }

    /**
     * Handles debug tools expected to run on startup. Ensure you are only calling this when you mean to.
     * ! Most of this code is dangerous & destructive -- make sure you know what you're doing!
     */
    private void handleStartingDebug() {
        if (!Constants.Debug.ENABLE) return;

        if (Constants.Debug.WIPE_PREFERENCES) {
            Preferences.removeAll();
        }
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
