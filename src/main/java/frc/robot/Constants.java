package frc.robot;

import frc.robot.util.DynamicValue;
import frc.robot.util.GenericController.BaseController;

public final class Constants {

    public static final class Buttons { // Button Name -> ID Mapping

        public static final int A = 2;
        public static final int B = 3;
        public static final int X = 1;
        public static final int Y = 4;
        public static final int BACK = 9;
        public static final int START = 10;
        public static final int L1 = 5; // ! Currently broken on the controller (not the code)
        public static final int L2 = 7;
        public static final int R1 = 6;
        public static final int R2 = 8;
    }

    public static final class Joystick { // Joystick Name -> ID Mapping

        public static final int LX = 0;
        public static final int LY = 1;
        public static final int RX = 2;
        public static final int RY = 3;
    }

    public static final class ID { // Motor Name -> ID Mapping

        public static final int LM1 = 14; // Left Motor 1 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int LM2 = 15; // Left Motor 2 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int RM1 = 12; // Right Motor 1 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int RM2 = 13; // Right Motor 2 ID - Ensure Drive.MOTOR_TYPE is set to the correct type
        public static final int JOYSTICK = 0; // Joystick ID
    }

    public static final class Drive {

        static final BaseController MOTOR_TYPE = BaseController.TALONFX; // Motor type
        static final DynamicValue<Double> LM_SPEED_OFFSET = new DynamicValue<>(
            "DriveLMOffset",
            1.0
        ); // Value to multiply left motor speed by, used in cases of motor speed mismatch
        static final DynamicValue<Double> RM_SPEED_OFFSET = new DynamicValue<>(
            "DriveRMOffset",
            1.0
        ); // Value to multiply right motor speed by, used in cases of motor speed mismatch
        static final double BRAKE_THRESHOLD = 0.005; // Speed threshold to round to 0 (and thus brake)
        static final boolean LM_INVERSE = false; // Whether the left motors are inverted
        static final boolean RM_INVERSE = true; // Whether the right motors are inverted

        public static final class CurrentLimit {

            static final boolean SUPPLY = true; // Whether to enable supply current limiting
            static final double SUPPLY_LIMIT = 100; // Supply current limit
            static final double SUPPLY_TRIGGER = 100; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
            static final double SUPPLY_TRIGGER_TIME = 0.15; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
            static final boolean STATOR = true; // Whether to enable stator current limiting
            static final double STATOR_LIMIT = 100; // Stator current limit
            static final double STATOR_TRIGGER = 100; // Current in which to trigger the stator limit (lower to STATOR_LIMIT)
            static final double STATOR_TRIGGER_TIME = 0.15; // Amount of time to go over STATOR_TRIGGER before triggering the limit
        }
    }

    public static final class Gyro {

        static final boolean USE_ROLL = true; // Whether to use roll instead of pitch for pitch related operations
        static final boolean UPSIDE_DOWN = false; // Whether the gyro is upside down vertically (will reverse gyro logic for pitch)
    }

    public static final class Autonomous {

        static final double MAX_TEMP = 60; // Maximum temperature (in degrees C) before autonomous stops (-1 to disable)
    }

    public static final class TeleOp {

        static final int LEFT_DRIVE_STICK = Joystick.LY; // Joystick to use for left motor control
        static final int RIGHT_DRIVE_STICK = Joystick.RY; // JOystick to use for right motor control
        static final double SLEW_RATE_LIMIT = 0.985; // Slew rate limit for joystick input
    }
}
