package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystems;
import frc.robot.util.DynamicValue;

public class RotationPID extends GenericPID {

    DynamicValue<Double> tolerance;
    boolean canEnd = false;

    /** Create a new RotationPID command.
     * <p>
     *     ! This command will not end, use {@link #RotationPID(String, IntakeSubsystems.RotationSubsystem, double, DynamicValue, double, double, double, double)} if you want the command to end.
     * </p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param rotation The rotation subsystem
     * @param target The target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public RotationPID(
        String name,
        IntakeSubsystems.RotationSubsystem rotation,
        double target,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax
    ) {
        super(
            name,
            defaultP,
            defaultI,
            defaultD,
            defaultIMax,
            () -> target - rotation.getAveragePosition(),
            rotation::setMotors
        );
        addRequirements(rotation);
    }

    /** Create a new RotationPID command.
     * <p>This command will end once the error is within the targetTolerance.</p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param rotation The rotation subsystem
     * @param target The target position
     * @param targetTolerance The tolerance for the target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public RotationPID(
        String name,
        IntakeSubsystems.RotationSubsystem rotation,
        double target,
        DynamicValue<Double> targetTolerance,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax
    ) {
        this(name, rotation, target, defaultP, defaultI, defaultD, defaultIMax);
        tolerance = targetTolerance;
        canEnd = true;
    }

    @Override
    public boolean isFinished() {
        return canEnd && Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
