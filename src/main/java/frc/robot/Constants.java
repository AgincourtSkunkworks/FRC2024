package frc.robot;

import frc.robot.util.DynamicValue;
import frc.robot.util.GenericController.BaseController;
import frc.robot.util.GenericController.NeutralMode;
import frc.robot.util.GenericJoystick.Axis;
import frc.robot.util.GenericJoystick.BaseJoystick;
import frc.robot.util.GenericJoystick.Button;
import frc.robot.util.GenericJoystick.POV;

public final class Constants {

    public static final class ID { // Motor Name -> ID Mapping

        public static final int LM1 = 3; // Left Motor 1 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int LM2 = 2; // Left Motor 2 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int RM1 = 20; // Right Motor 1 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int RM2 = 1; // Right Motor 2 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int IRTLM = 12; // Intake Rotation Left Motor ID - Ensure Intake.ROTATION_MOTOR_TYPE is set to the correct type
        public static final int IRTRM = 13; // Intake Rotation Right Motor ID - Ensure Intake.ROTATION_MOTOR_TYPE is set to the correct type
        public static final int IF = 10; // Intake CIM (Feeder) ID - Ensure Intake.CIM_MOTOR_TYPE is set to the correct type
        public static final int OFLM = 8; // Outtake CIM (Flywheel) Left Motor ID - Ensure Outtake.CIM_MOTOR_TYPE is set to the correct type
        public static final int OFRM = 9; // Outtake CIM (Flywheel) Right Motor ID - Ensure Outtake.CIM_MOTOR_TYPE is set to the correct type
        public static final int CL = 14; // Climber ID - Ensure Climber.MOTOR_TYPE is set to the correct type
        public static final int JOYSTICK = 0; // Joystick ID
    }

    public static final class Joystick {

        public static final BaseJoystick TYPE = BaseJoystick.PS5; // Joystick type
    }

    public static final class Drive {

        static final BaseController MOTOR_TYPE = BaseController.SPARKMAX; // Motor type
        static final DynamicValue<Double> LM_SPEED_OFFSET = new DynamicValue<>(
            "DriveLMOffset",
            1.0
        ); // Value to multiply left motor speed by, used in cases of motor speed mismatch
        static final DynamicValue<Double> RM_SPEED_OFFSET = new DynamicValue<>(
            "DriveRMOffset",
            1.0
        ); // Value to multiply right motor speed by, used in cases of motor speed mismatch
        static final double BRAKE_THRESHOLD = 0.055; // Speed threshold to round to 0 (and thus brake)
        static final boolean LM_INVERSE = false; // Whether the left motors are inverted
        static final boolean RM_INVERSE = true; // Whether the right motors are inverted

        public static final class CurrentLimit {

            static final boolean SUPPLY = true; // Whether to enable supply current limiting
            static final double SUPPLY_LIMIT = 40; // Supply current limit
            static final double SUPPLY_TRIGGER = 40; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
            static final double SUPPLY_TRIGGER_TIME = 0.15; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
            static final boolean STATOR = true; // Whether to enable stator current limiting
            static final double STATOR_LIMIT = 40; // Stator current limit
        }
    }

    public static final class Intake {

        public static Button TRIGGER_TRG = Button.R1;
        public static Button UNLOAD_TRG = Button.N; // A reverse seq. that tries to unload a note to the ground
        // TODO: Check whether UNLOAD_CONSUME_TIME is still needed, and adjust as low possible
        public static double UNLOAD_CONSUME_TIME = 0.2; // Seconds to run motors to attempt to pull the note back into the intake mechanism

        public static final class Rotation {

            static final BaseController MOTOR_TYPE = BaseController.TALONFX; // Motor type
            static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
            static final Button OVERRIDE_LOW_TRG = Button.S;
            static final Button OVERRIDE_HIGH_TRG = Button.E;
            static final POV OVERRIDE_FWD_TRG = POV.E;
            static final POV OVERRIDE_REV_TRG = POV.W;
            static final boolean LM_INVERSE = false; // Whether the left motor is inverted
            static final boolean RM_INVERSE = true; // Whether the right motor is inverted
            static final double OVERRIDE_SPEED = 0.5; // Speed in which to run the intake on fwd/rev override

            public static final class DefaultPID {

                static final double P = 0.042;
                static final double I = 0.0;
                static final double IMax = 0.0; // Maximum integral value
                static final double D = 0.0045;
                static final double FINISH_TOLERANCE = 0.4; // # of ticks of difference from setpoint to end PID within
            }

            public static final class Setpoints { // Left/first motor is used as reference (IRTLM)

                static final double LOW = 4.96;
                static final double HIGH = 0;
            }

            public static final class CurrentLimit {

                static final boolean SUPPLY = true; // Whether to enable supply current limiting
                static final double SUPPLY_LIMIT = 100; // Supply current limit
                static final double SUPPLY_TRIGGER = 100; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
                static final double SUPPLY_TRIGGER_TIME = 0.15; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
                static final boolean STATOR = true; // Whether to enable stator current limiting
                static final double STATOR_LIMIT = 100; // Stator current limit
            }
        }

        public static final class Feeder {

            static final BaseController MOTOR_TYPE = BaseController.TALONSRX; // CIM motor type
            public static Button OVERRIDE_FWD_TRG = Button.L2;
            public static Button OVERRIDE_REV_TRG = Button.L1;
            static final boolean INVERSE = false; // Whether the motor is inverted
            static final boolean HOLD_ON_SPINUP = false; // Whether to run the motor in reverse to hold the piece on spinup
            static final double SPEED = 0.75; // Speed in which to run the intake feeder
            static final double SHOOT_SPEED = 1; // Speed in which to run the intake feeder when shooting & overriding
        }
    }

    public static final class Outtake {

        static final BaseController FLYWHEEL_MOTOR_TYPE =
            BaseController.TALONSRX; // CIM motor type
        static final Button TRIGGER_TRG = Button.R2;
        static final Button OVERRIDE_TRG = Button.W;
        static final boolean FLYWHEEL_LM_INVERSE = false; // Whether the flywheel left motor is inverted
        static final boolean FLYWHEEL_RM_INVERSE = true; // Whether the flywheel right motor is inverted
        static final double SPEED = 1; // Speed in which to run the flywheels
        static final double SPINUP_TIME = 0.5; // Time to hold the piece in while flywheels spin up

        public static final class CurrentLimit {

            static final boolean SUPPLY = true; // Whether to enable supply current limiting
            static final double SUPPLY_LIMIT = 50; // Supply current limit
            static final double SUPPLY_TRIGGER = 50; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
            static final double SUPPLY_TRIGGER_TIME = 3; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
        }
    }

    public static final class Climber {

        public static final boolean ENABLE = true; // Whether the climber is enabled (at all)
        static final BaseController MOTOR_TYPE = BaseController.TALONFX;
        static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        static final POV HIGH_TRG = POV.N;
        static final POV LOW_TRG = POV.S;
        static final Button OVERRIDE_UP_TRG = Button.OPT_L;
        static final Button OVERRIDE_DOWN_TRG = Button.OPT_R;
        static final boolean INVERSE = true; // Whether the motor is inverted
        static final double OVERRIDE_SPEED = 0.5; // Speed in which to run the climber on override

        public static final class CurrentLimit {

            static final boolean SUPPLY = true; // Whether to enable supply current limiting
            static final double SUPPLY_LIMIT = 100; // Supply current limit
            static final double SUPPLY_TRIGGER = 100; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
            static final double SUPPLY_TRIGGER_TIME = 0.15; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
            static final boolean STATOR = true; // Whether to enable stator current limiting
            static final double STATOR_LIMIT = 100; // Stator current limit
        }

        public static final class DefaultPID { // TODO: Tune

            public static final class Low {

                static final double P = 0.04;
                static final double I = 0.05;
                static final double IMax = 3;
                static final double D = 0.004;
            }

            public static final class High {

                static final double P = 0.015;
                static final double I = 0;
                static final double IMax = 0;
                static final double D = 0.00004;
                static final double FINISH_TOLERANCE = 1;
            }
        }

        public static final class Setpoints {

            static final double LOW = 0;
            static final double HIGH = 68;
        }
    }

    public static final class Autonomous {

        static final double MAX_TEMP = 60; // Maximum temperature (in degrees C) before autonomous stops (-1 to disable)
        static final double MOVE_SPEED = 0.5; // Percent speed (0-1) for moving the robot
        static final double COMM_LEAVE_TIME = 2.4; // Time in seconds that are needed to leave the community area
        static final double SHOOT_TIME = 3; // Seconds to run the flywheels for
    }

    public static final class TeleOp {

        static final Axis LEFT_DRIVE_STICK = Axis.LY; // Joystick to use for left motor control
        static final Axis RIGHT_DRIVE_STICK = Axis.RY; // Joystick to use for right motor control
        static final double SLEW_RATE_LIMIT = 2.5; // Slew rate limit for joystick input
        static final double TURN_SLEW_RATE_LIMIT = 0.75; // Slew rate limit for when a turn is detected
        static final double TURN_DIFF_THRESHOLD = 1.8; // Difference in sticks to be considered a turn (and trigger alternate slew)
    }

    /*
        ! Make sure you know what you're doing before changing these values.
        ! ALWAYS reset them to their original values before committing or running in a competition.
     */
    public static final class Debug {

        public static final boolean ENABLE = false; // Needs to be enabled to use any debug features (other constants are ignored if this is false)
        static final boolean WIPE_PREFERENCES = false; // Whether to wipe the RobotPreferences on startup
        public static final boolean DETAILED_SMART_DASHBOARD = false; // Whether to put detailed information on the SmartDashboard (this is performance intensive)
    }
}
