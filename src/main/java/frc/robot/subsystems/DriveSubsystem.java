package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.DynamicValue;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import frc.robot.util.GenericController.NeutralMode;
import java.util.ArrayList;

public class DriveSubsystem extends SubsystemBase {

    // The values that are initialized to a DynamicValue object are a result of the style of configuration used for this
    // subsystem which doesn't require people to specify a default value. This shouldn't need to be done for most
    // other subsystems.
    final ArrayList<GenericController> leftMotors = new ArrayList<>(), rightMotors = new ArrayList<>(), motors = new ArrayList<>();
    boolean supplyLimit = false, statorLimit = false;
    double supplyCurrentLimit = 0, supplyTriggerCurrent = 0, supplyTriggerTime =
        0, statorCurrentLimit = 0;
    NeutralMode neutralMode = NeutralMode.Brake;
    DynamicValue<Double> lCorrect = new DynamicValue<>(
        1.0
    ), rCorrect = new DynamicValue<>(1.0);
    boolean lInvert = false, rInvert = false;
    double brakeThreshold = 0, maxTemp = 0;

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
        BaseController type,
        ArrayList<GenericController> arr,
        boolean invert,
        int[] ids
    ) {
        for (int id : ids) {
            GenericController motor = new GenericController(type, id);
            motor.setNeutralMode(neutralMode);
            motor.setInverted(invert);
            motor.setSupplyCurrentLimit(
                supplyLimit,
                supplyCurrentLimit,
                supplyTriggerCurrent,
                supplyTriggerTime
            );
            motor.setStatorCurrentLimit(statorLimit, statorCurrentLimit);
            arr.add(motor);
            motors.add(motor);
        }
    }

    /** Add left motors to the subsystem
     * @param ids IDs of the motors to add
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem addLeftMotors(BaseController type, int... ids) {
        addMotors(type, leftMotors, lInvert, ids);
        return this;
    }

    /** Add right motors to the subsystem
     * @param ids IDs of the motors to add
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem addRightMotors(BaseController type, int... ids) {
        addMotors(type, rightMotors, rInvert, ids);
        return this;
    }

    public DriveSubsystem invert(boolean left, boolean right) {
        for (GenericController motor : leftMotors) motor.setInverted(left);
        for (GenericController motor : rightMotors) motor.setInverted(right);
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
        this.lCorrect.set(lCorrect);
        this.rCorrect.set(rCorrect);
        return this;
    }

    /** Set the percent speed offset for the motors
     * @param lCorrect A DynamicValue for the percent speed offset for the left motors
     * @param rCorrect A DynamicValue for the percent speed offset for the right motors
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setOffset(
        DynamicValue<Double> lCorrect,
        DynamicValue<Double> rCorrect
    ) {
        this.lCorrect = lCorrect;
        this.rCorrect = rCorrect;
        return this;
    }

    /** Set the neutral mode for all motors, past and future. Default: Brake
     * @param mode The neutral mode to set the motors to
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setNeutralMode(NeutralMode mode) {
        for (GenericController motor : motors) motor.setNeutralMode(mode);
        neutralMode = mode;
        return this;
    }

    /** Configure the supply limit for all motors, past and future. Default: Disabled
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
        for (GenericController motor : motors) motor.setSupplyCurrentLimit(
            enable,
            limit,
            trigger,
            triggerTime
        );
        this.supplyLimit = enable;
        this.supplyCurrentLimit = limit;
        this.supplyTriggerCurrent = trigger;
        this.supplyTriggerTime = triggerTime;
        return this;
    }

    /** Configure the stator limit for all motors, past and future. Default: Disabled
     * @param enable Whether to enable the current limit
     * @param limit The "holding" current (amperes) to limit to when feature is activated.
     * @return The DriveSubsystem, for chaining
     */
    public DriveSubsystem setStatorLimit(boolean enable, double limit) {
        for (GenericController motor : motors) motor.setStatorCurrentLimit(
            enable,
            limit
        );
        this.statorLimit = enable;
        this.statorCurrentLimit = limit;
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
        for (GenericController motor : leftMotors) motor.set(
            speed * lCorrect.get()
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
        for (GenericController motor : rightMotors) motor.set(
            speed * rCorrect.get()
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
        for (GenericController motor : motors) if (
            motor.getTemperature() > highest
        ) highest = motor.getTemperature();
        return highest;
    }

    @Override
    public void periodic() {
        if (
            Constants.Debug.ENABLE && Constants.Debug.DETAILED_SMART_DASHBOARD
        ) {
            for (GenericController motor : leftMotors) {
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Left Motor (%d) Position",
                        motor.getID()
                    ),
                    motor.getPosition()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Left Motor (%d) Temperature",
                        motor.getID()
                    ),
                    motor.getTemperature()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Left Motor (%d) Supply Current",
                        motor.getID()
                    ),
                    motor.getSupplyCurrent()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Left Motor (%d) Stator Current",
                        motor.getID()
                    ),
                    motor.getStatorCurrent()
                );
            }
            for (GenericController motor : rightMotors) {
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Right Motor (%d) Position",
                        motor.getID()
                    ),
                    motor.getPosition()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Right Motor (%d) Temperature",
                        motor.getID()
                    ),
                    motor.getTemperature()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Right Motor (%d) Supply Current",
                        motor.getID()
                    ),
                    motor.getSupplyCurrent()
                );
                SmartDashboard.putNumber(
                    String.format(
                        "Drive Right Motor (%d) Stator Current",
                        motor.getID()
                    ),
                    motor.getStatorCurrent()
                );
            }
        } else {
            SmartDashboard.putNumber("Drive Highest Temp", getHighestTemp());
            SmartDashboard.putNumber(
                "Drive Left Motor Position (First)",
                leftMotors.get(0).getPosition()
            );
            SmartDashboard.putNumber(
                "Drive Right Motor Position (First)",
                rightMotors.get(0).getPosition()
            );
        }

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
