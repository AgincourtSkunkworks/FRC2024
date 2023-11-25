package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.ArrayList;

public class DriveSubsystem extends SubsystemBase {

    final ArrayList<WPI_TalonFX> leftMotors = new ArrayList<>(), rightMotors =
        new ArrayList<>(), motors = new ArrayList<>();
    SupplyCurrentLimitConfiguration supplyLimit =
        new SupplyCurrentLimitConfiguration(false, 0, 0, 0);
    StatorCurrentLimitConfiguration statorLimit =
        new StatorCurrentLimitConfiguration(false, 0, 0, 0);
    NeutralMode neutralMode = NeutralMode.Brake;
    boolean lInvert = false, rInvert = false;
    double lCorrect = 1, rCorrect = 1, brakeThreshold = 0, maxTemp = 0;

    // Private constructor so people use .create() instead
    private DriveSubsystem() {}

    /**
     * Create a new DriveSubsystem.
     */
    public static DriveSubsystem create() {
        return new DriveSubsystem();
    }

    /** Internal method to add motors, including to a specified array
     * @param arr Additional array to add the motors to
     * @param ids IDs of the motors to add
     */
    private void addMotors(
        ArrayList<WPI_TalonFX> arr,
        boolean invert,
        int[] ids
    ) {
        for (int id : ids) {
            WPI_TalonFX motor = new WPI_TalonFX(id);
            motor.setNeutralMode(neutralMode);
            motor.setInverted(invert);
            motor.configSupplyCurrentLimit(supplyLimit, 5);
            motor.configStatorCurrentLimit(statorLimit, 5);
            arr.add(motor);
            motors.add(motor);
        }
    }

    /** Add left motors to the subsystem
     * @param ids IDs of the motors to add
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem addLeftMotors(int... ids) {
        addMotors(leftMotors, lInvert, ids);
        return this;
    }

    /** Add right motors to the subsystem
     * @param ids IDs of the motors to add
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem addRightMotors(int... ids) {
        addMotors(rightMotors, rInvert, ids);
        return this;
    }

    public DriveSubsystem invert(boolean left, boolean right) {
        for (WPI_TalonFX motor : leftMotors) motor.setInverted(left);
        for (WPI_TalonFX motor : rightMotors) motor.setInverted(right);
        lInvert = left;
        rInvert = right;
        return this;
    }

    /** Set the percent speed offset for the motors
     * @param lCorrect The percent speed offset for the left motors
     * @param rCorrect The percent speed offset for the right motors
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setOffset(double lCorrect, double rCorrect) {
        this.lCorrect = lCorrect;
        this.rCorrect = rCorrect;
        return this;
    }

    /** Set the neutral mode for all motors, past and future. Default: Brake
     * @param mode The neutral mode to set the motors to
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setNeutralMode(NeutralMode mode) {
        for (WPI_TalonFX motor : motors) motor.setNeutralMode(mode);
        neutralMode = mode;
        return this;
    }

    /** Configure the supply limit for all motors. Default: Disabled
     * @param enable Whether to enable the current limit
     * @param limit The "holding" current (amperes) to limit to when feature is activated.
     * @param trigger Current must exceed this threshold (amperes) before limiting occurs. If this value is less than currentLimit, then currentLimit is used as the threshold.
     * @param triggerTime How long current must exceed threshold (seconds) before limiting occurs.
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setSupplyLimit(
        boolean enable,
        double limit,
        double trigger,
        double triggerTime
    ) {
        this.supplyLimit.enable = enable;
        this.supplyLimit.currentLimit = limit;
        this.supplyLimit.triggerThresholdCurrent = trigger;
        this.supplyLimit.triggerThresholdTime = triggerTime;
        return this;
    }

    /** Configure the stator limit for all motors. Default: Disabled
     * @param enable Whether to enable the current limit
     * @param limit The "holding" current (amperes) to limit to when feature is activated.
     * @param trigger Current must exceed this threshold (amperes) before limiting occurs. If this value is less than currentLimit, then currentLimit is used as the threshold.
     * @param triggerTime How long current must exceed threshold (seconds) before limiting occurs.
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setStatorLimit(
        boolean enable,
        double limit,
        double trigger,
        double triggerTime
    ) {
        this.statorLimit.enable = enable;
        this.statorLimit.currentLimit = limit;
        this.statorLimit.triggerThresholdCurrent = trigger;
        this.statorLimit.triggerThresholdTime = triggerTime;
        return this;
    }

    /** Set the brake threshold. If the speed is below this threshold, the motors will be set to 0. Default: 0
     * @param threshold The threshold to set
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setBrakeThreshold(double threshold) {
        this.brakeThreshold = threshold;
        return this;
    }

    /** The maximum motor temp for any non-default command requiring the DriveSubsystem before it is forcefully stopped.
     * Default: -1 (disabled)
     * @param maxTemp The maximum temperature, -1 to disable
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
        return this;
    }

    /** Set the speed of the left motors.
     * @param speed Speed to set the motors to (-1 to 1)
     */
    public void setLeftMotors(double speed) {
        if (
            (speed > 0 && speed < brakeThreshold) ||
            (speed < 0 && speed > -brakeThreshold)
        ) speed = 0;
        for (WPI_TalonFX motor : leftMotors) motor.set(
            ControlMode.PercentOutput,
            speed * lCorrect
        );
    }

    /**
     * Set the speed of the right motors.
     *
     * @param speed Speed to set the motors to (-1 to 1)
     */
    public void setRightMotors(double speed) {
        if (
            (speed > 0 && speed < brakeThreshold) ||
            (speed < 0 && speed > -brakeThreshold)
        ) speed = 0;
        for (WPI_TalonFX motor : rightMotors) motor.set(
            ControlMode.PercentOutput,
            speed * rCorrect
        );
    }

    /**
     * Set the speed of both motors.
     *
     * @param speed Speed to set the motors to (-1 to 1)
     */
    public void setMotors(double speed) {
        setLeftMotors(speed);
        setRightMotors(speed);
    }

    /** Get the highest temperature from all drive motors
     * @return The highest temperature
     */
    public double getHighestTemp() {
        double highest = 0;
        for (WPI_TalonFX motor : motors) if (
            motor.getTemperature() > highest
        ) highest = motor.getTemperature();
        return highest;
    }

    @Override
    public void periodic() {
        if (maxTemp > 0 && getHighestTemp() >= maxTemp) {
            Command currentCommand = this.getCurrentCommand();
            if (
                currentCommand != null &&
                currentCommand != this.getDefaultCommand()
            ) {
                currentCommand.cancel();
                System.out.printf(
                    "[%s] Command Cancelled | Motor temperature of %f exceeds maximum of %f.%n",
                    currentCommand.getName(),
                    getHighestTemp(),
                    maxTemp
                );
            }
        }
    }
}
