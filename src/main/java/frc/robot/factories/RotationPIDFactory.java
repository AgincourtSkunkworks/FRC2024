package frc.robot.factories;

import frc.robot.commands.RotationPID;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.util.DynamicValue;

public class RotationPIDFactory {
    String name;
    IntakeSubsystem.Rotation rotation;
    DynamicValue<Double> targetTolerance;
    double target, defaultP, defaultI, defaultD, defaultIMax;
    boolean canEnd = false;

    /** Create a new RotationPIDFactory, which will generate RotationPIDs with the given parameters.
     * <p>
     *     ! This factory will generate commands that will not end, use {@link #RotationPIDFactory(String, IntakeSubsystem.Rotation, double, DynamicValue, double, double, double, double)} if you want the command to end.
     * </p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param rotation The rotation subsystem
     * @param target The target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public RotationPIDFactory(String name, IntakeSubsystem.Rotation rotation, double target, double defaultP, double defaultI, double defaultD, double defaultIMax) {
        this.name = name;
        this.rotation = rotation;
        this.target = target;
        this.defaultP = defaultP;
        this.defaultI = defaultI;
        this.defaultD = defaultD;
        this.defaultIMax = defaultIMax;
    }

    /** Create a new RotationPIDFactory, which will generate RotationPIDs with the given parameters.
     * <p>This factory will generate commands that will end once the error is within the targetTolerance.</p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param rotation The rotation subsystem
     * @param target The target position
     * @param targetTolerance The tolerance for the target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public RotationPIDFactory(String name, IntakeSubsystem.Rotation rotation, double target, DynamicValue<Double> targetTolerance, double defaultP, double defaultI, double defaultD, double defaultIMax) {
        this(name, rotation, target, defaultP, defaultI, defaultD, defaultIMax);
        this.targetTolerance = targetTolerance;
        this.canEnd = true;
    }

    /** Create a new RotationPID with the parameters given to this factory.
     * @return A new RotationPID with the parameters given to this factory
     */
    public RotationPID create() {
        if (canEnd) {
            return new RotationPID(name, rotation, target, targetTolerance, defaultP, defaultI, defaultD, defaultIMax);
        } else {
            return new RotationPID(name, rotation, target, defaultP, defaultI, defaultD, defaultIMax);
        }
    }
}
