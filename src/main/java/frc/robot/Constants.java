// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
public final static class Buttons { // Button Name -> ID Mapping
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

public final static class Joystick { // Joystick Name -> ID Mapping
    public static final int LX = 0;
    public static final int LY = 1;
    public static final int RX = 2;
    public static final int RY = 3;
}

public final static class ID { // Motor Name -> ID Mapping
    public static final int LM1 = 14; // Left Motor 1 ID
    public static final int LM2 = 15; // Left Motor 2 ID
    public static final int RM1 = 12; // Right Motor 1 ID
    public static final int RM2 = 13; // Right Motor 2 ID
    public static final int ARM = 11; // Arm Motor ID
    public static final int JOYSTICK = 0; // Joystick ID
}

public final static class Drive {
    final static double BRAKE_THRESHOLD = 0.005; // Speed threshold to round to 0 (and thus brake)
    final static double LM_SPEED_OFFSET = 0; // Percent offset (0-1) for left motor speed (to ensure that it can drive straight)
    final static double RM_SPEED_OFFSET = 0; // Percent offset (0-1) for right motor speed (to ensure that it can drive straight)
    final static double THERMAL_WARNING = 80; // Temperature (in degrees C) to warn the driver about
    final static boolean LM_INVERSE = false; // Whether the left motors are inverted
    final static boolean RM_INVERSE = true; // Whether the right motors are inverted
    public final class CurrentLimit {
        final static boolean SUPPLY = true; // Whether to enable supply current limiting
        final static double SUPPLY_LIMIT = 100; // Supply current limit
        final static double SUPPLY_TRIGGER = 100; // Current in which to trigger the supply limit (lower to SUPPLY_LIMIT)
        final static double SUPPLY_TRIGGER_TIME = 0.15; // Amount of time to go over SUPPLY_TRIGGER before triggering the limit
        final static boolean STATOR = true; // Whether to enable stator current limiting
        final static double STATOR_LIMIT = 100; // Stator current limit
        final static double STATOR_TRIGGER = 100; // Current in which to trigger the stator limit (lower to STATOR_LIMIT)
        final static double STATOR_TRIGGER_TIME = 0.15; // Amount of time to go over STATOR_TRIGGER before triggering the limit
    }
}

public final static class Gyro {
    final static boolean USE_ROLL = true; // Whether to use roll instead of pitch for pitch related operations
    final static boolean UPSIDE_DOWN = false; // Whether the gyro is upside down vertically (will reverse gyro logic for pitch)
}

public final static class Autonomous {
    final static double MAX_TEMP = 60; // Maximum temperature (in degrees C) before autonomous stops
}

public final static class TeleOp {
    final static int LEFT_DRIVE_STICK = Joystick.LY; // Joystick to use for left motor control
    final static int RIGHT_DRIVE_STICK = Joystick.RY; // JOystick to use for right motor control
    final static double SLEW_RATE_LIMIT = 0.985; // Slew rate limit for joystick input
}
}
