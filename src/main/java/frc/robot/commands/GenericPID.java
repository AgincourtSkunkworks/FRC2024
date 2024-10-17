package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.DynamicValue;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * A generic PID command, that can be used directly for basic PID control, or subclassed for more complex control.
 * <p>
 * WPILib offers {@link edu.wpi.first.wpilibj2.command.PIDCommand}, which you may want to consider as an alternative.
 */
public class GenericPID extends Command {

    protected final DynamicValue<Double> p, i, d, iMax;
    protected final DoubleSupplier getError;
    protected final DoubleConsumer useOutput;
    protected final String name;
    protected double errorSum, lastRunTime, lastError;

    /** Create a new GenericPID command.
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     * @param getError A function that returns the error to be used for PID
     * @param useOutput A function that uses the output of the PID (usually to set motor speeds)
     */
    public GenericPID(
        String name,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax,
        DoubleSupplier getError,
        DoubleConsumer useOutput
    ) {
        this.setName(this.getClass().getSimpleName() + " - " + name);
        this.name = name;
        this.p = new DynamicValue<>(name + "P", defaultP);
        this.i = new DynamicValue<>(name + "I", defaultI);
        this.d = new DynamicValue<>(name + "D", defaultD);
        this.iMax = new DynamicValue<>(name + "IMax", defaultIMax);
        this.getError = getError;
        this.useOutput = useOutput;
    }

    /** Create a new GenericPID command. The error is calculated by subtracting the measurement from the setpoint.
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     * @param getMeasurement A function that returns the measurement to be used for PID
     * @param getSetpoint A function that returns the setpoint to be used for PID
     * @param useOutput A function that uses the output of the PID (usually to set motor speeds)
     */
    public GenericPID(
        String name,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax,
        DoubleSupplier getMeasurement,
        DoubleSupplier getSetpoint,
        DoubleConsumer useOutput
    ) {
        this(
            name,
            defaultP,
            defaultI,
            defaultD,
            defaultIMax,
            () -> getSetpoint.getAsDouble() - getMeasurement.getAsDouble(),
            useOutput
        );
    }

    /** Create a new GenericPID command. The error is calculated by subtracting the measurement from the setpoint. The setpoint can be configured in RobotPreferences.
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     * @param getMeasurement A function that returns the measurement to be used for PID
     * @param defaultSetpoint The default setpoint value
     * @param useOutput A function that uses the output of the PID (usually to set motor speeds)
     */
    public GenericPID(
        String name,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax,
        DoubleSupplier getMeasurement,
        double defaultSetpoint,
        DoubleConsumer useOutput
    ) {
        this(
            name,
            defaultP,
            defaultI,
            defaultD,
            defaultIMax,
            getMeasurement,
            new DynamicValue<>(name + "Setpoint", defaultSetpoint)::get,
            useOutput
        );
    }

    /** Calculate the output of the PID.
     * Running this will cause the "last" values to be changed (used for the integral and derivative terms), so it should only be run once per loop.
     * @return The calculated output of the PID
     */
    protected double calculate() {
        if (lastRunTime == 0) lastRunTime = Timer.getFPGATimestamp(); // Prevent large term as a result of first run
        final double curTime = Timer.getFPGATimestamp();
        final double dt = curTime - lastRunTime;
        final double error = getError.getAsDouble();
        final double errorRate = (error - lastError) / dt;
        errorSum += error * dt;
        // If errorSum is outside the max value, set it to the respective bound. iMax is used to prevent integral windup.
        errorSum = Math.max(-iMax.get(), Math.min(iMax.get(), errorSum));
        lastRunTime = curTime;
        lastError = error;
        return error * p.get() + errorSum * i.get() + errorRate * d.get();
    }

    /**
     * Called when the command is first scheduled. The default behaviour is to reset the "last" values.
     * This shouldn't need to be overridden in most cases.
     */
    @Override
    public void initialize() {
        this.errorSum = 0;
        this.lastRunTime = 0;
        this.lastError = 0;
    }

    /**
     * Called periodically while the command is scheduled. The default behaviour is to calculate the output and
     * use it in the useOutput function.
     */
    @Override
    public void execute() {
        useOutput.accept(calculate());
    }

    /** Called when the command is finished. The default behaviour is to set the output to 0.
     * <p>
     * By default, the command will not end naturally, and must be cancelled; you can change this by overriding isFinished().
     * @param interrupted Whether the command was interrupted/cancelled
     */
    @Override
    public void end(boolean interrupted) {
        useOutput.accept(0);
    }
}
