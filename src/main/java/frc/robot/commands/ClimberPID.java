package frc.robot.commands;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.util.DynamicValue;

public class ClimberPID extends GenericPID {

    DynamicValue<Double> tolerance;
    boolean canEnd;

    /** Create a new ClimberPID command.
     * ! This command will not end, use {@link #ClimberPID(String, ClimberSubsystem, double, DynamicValue, double, double, double, double)} if you want the command to end.
     * @param name The name of the PID command, used for RobotPreferences values
     * @param climber The climber subsystem
     * @param target The target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public ClimberPID(
        String name,
        ClimberSubsystem climber,
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
            () -> target - climber.getPosition(),
            climber::setMotor
        );
        addRequirements(climber);
        canEnd = false;
    }

    /** Create a new ClimberPID command. This command will end once the error is within the targetTolerance.
     * This command does not end.
     * @param name The name of the PID command, used for RobotPreferences values
     * @param climber The climber subsystem
     * @param target The target position
     * @param targetTolerance The tolerance for the target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public ClimberPID(
        String name,
        ClimberSubsystem climber,
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
            () -> target - climber.getPosition(),
            climber::setMotor
        );
        addRequirements(climber);
        tolerance = targetTolerance;
        canEnd = true;
    }

    @Override
    public boolean isFinished() {
        return canEnd && Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
