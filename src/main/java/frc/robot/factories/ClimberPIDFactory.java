package frc.robot.factories;

import frc.robot.commands.ClimberPID;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.util.DynamicValue;

public class ClimberPIDFactory {

    String name;
    ClimberSubsystem climber;
    DynamicValue<Double> targetTolerance;
    double target, defaultP, defaultI, defaultD, defaultIMax;
    boolean canEnd = false;

    /** Create a new ClimberPIDFactory.
     * <p>
     *     ! This command will not end, use {@link #ClimberPIDFactory(String, ClimberSubsystem, double, DynamicValue, double, double, double, double)} if you want the command to end.
     * </p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param climber The climber subsystem
     * @param target The target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public ClimberPIDFactory(
        String name,
        ClimberSubsystem climber,
        double target,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax
    ) {
        this.name = name;
        this.climber = climber;
        this.target = target;
        this.defaultP = defaultP;
        this.defaultI = defaultI;
        this.defaultD = defaultD;
        this.defaultIMax = defaultIMax;
    }

    /** Create a new ClimberPIDFactory. This command will end once the error is within the targetTolerance.
     * <p>This command will end once the error is within the targetTolerance.</p>
     * @param name The name of the PID command, used for RobotPreferences values. Use the same name for all commands that should share the same preferences.
     * @param climber The climber subsystem
     * @param target The target position
     * @param targetTolerance The tolerance for the target position
     * @param defaultP The default P value
     * @param defaultI The default I value
     * @param defaultD The default D value
     * @param defaultIMax The default I max value (the maximum value of the integral term)
     */
    public ClimberPIDFactory(
        String name,
        ClimberSubsystem climber,
        double target,
        DynamicValue<Double> targetTolerance,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax
    ) {
        this(name, climber, target, defaultP, defaultI, defaultD, defaultIMax);
        this.targetTolerance = targetTolerance;
        this.canEnd = true;
    }

    public ClimberPID create() {
        if (canEnd) {
            return new ClimberPID(
                name,
                climber,
                target,
                targetTolerance,
                defaultP,
                defaultI,
                defaultD,
                defaultIMax
            );
        } else {
            return new ClimberPID(
                name,
                climber,
                target,
                defaultP,
                defaultI,
                defaultD,
                defaultIMax
            );
        }
    }
}
