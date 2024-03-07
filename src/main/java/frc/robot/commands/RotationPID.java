package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.util.DynamicValue;

public class RotationPID extends GenericPID {

    DynamicValue<Double> tolerance;
    boolean canEnd;

    /** Create a new RotationPID command.
     * ! This command will not end, use {@link #RotationPID(String, IntakeSubsystem.Rotation, double, DynamicValue, double, double, double, double)} if you want the command to end.
     * @param name The name of the PID command, used for RobotPreferences values
     * @param rotation The rotation subsystem
     * @param target The target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public RotationPID(
        String name,
        IntakeSubsystem.Rotation rotation,
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
        canEnd = false;
    }

    /** Create a new RotationPID command. This command will end once the error is within the targetTolerance.
     * @param name The name of the PID command, used for RobotPreferences values
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
        IntakeSubsystem.Rotation rotation,
        double target,
        DynamicValue<Double> targetTolerance,
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
        tolerance = targetTolerance;
        canEnd = true;
    }

    @Override
    public boolean isFinished() {
        return canEnd && Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
