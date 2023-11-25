package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

/**
 * A motor controller class that provides a basic generic implementation for common motor controllers used in FRC.
 * This allows for the easy interchange of motor controllers, without modifying large amounts of code.
 */
public class GenericController {

    public enum BaseController {
        TALONFX,
        TALONSRX,
        SPARKMAX,
    }

    public enum NeutralMode {
        Brake,
        Coast,
    }

    // TODO: Test all methods with TalonFX, TalonSRX, and SparkMax
    public final BaseController base;
    public WPI_TalonFX talonFX;
    public WPI_TalonSRX talonSRX;
    public CANSparkMax sparkMax;

    /**
     * Create a new GenericController.
     * @param base The type of motor controller to use.
     * @param id The ID of the motor controller.
     */
    public GenericController(BaseController base, int id) {
        this.base = base;
        switch (base) {
            case TALONFX:
                talonFX = new WPI_TalonFX(id);
                break;
            case TALONSRX:
                talonSRX = new WPI_TalonSRX(id);
                break;
            case SPARKMAX:
                // Currently only supports brushless motors, as some implementations rely on brushless features
                sparkMax =
                    new CANSparkMax(
                        id,
                        CANSparkMaxLowLevel.MotorType.kBrushless
                    );
                break;
        }
    }

    /** Set the motors to a specific speed.
     * @param speed The speed to set the motor to, between -1 and 1.
     */
    public void set(double speed) {
        switch (base) {
            case TALONFX:
                talonFX.set(
                    com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput,
                    speed
                );
                break;
            case TALONSRX:
                talonSRX.set(
                    com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput,
                    speed
                );
                break;
            case SPARKMAX:
                sparkMax.set(speed);
                break;
        }
    }

    /** Set the behaviour of the motors when idle or neutral (0 speed).
     * @param mode The mode to set the motor to.
     */
    public void setNeutralMode(NeutralMode mode) {
        switch (base) {
            case TALONFX:
                talonFX.setNeutralMode(
                    mode == NeutralMode.Brake
                        ? com.ctre.phoenix.motorcontrol.NeutralMode.Brake
                        : com.ctre.phoenix.motorcontrol.NeutralMode.Coast
                );
                break;
            case TALONSRX:
                talonSRX.setNeutralMode(
                    mode == NeutralMode.Brake
                        ? com.ctre.phoenix.motorcontrol.NeutralMode.Brake
                        : com.ctre.phoenix.motorcontrol.NeutralMode.Coast
                );
                break;
            case SPARKMAX:
                sparkMax.setIdleMode(
                    mode == NeutralMode.Brake
                        ? CANSparkMax.IdleMode.kBrake
                        : CANSparkMax.IdleMode.kCoast
                );
                break;
        }
    }

    /** Set the behaviour of the motors when idle or neutral (0 speed). Equivalent to setNeutralMode.
     * @param mode The mode to set the motor to.
     */
    public void setIdleMode(NeutralMode mode) {
        setNeutralMode(mode);
    }

    /** Sets the inversion of the motor.
     * @param inverted Whether to invert the motor.
     */
    public void setInverted(boolean inverted) {
        switch (base) {
            case TALONFX:
                talonFX.setInverted(inverted);
                break;
            case TALONSRX:
                talonSRX.setInverted(inverted);
                break;
            case SPARKMAX:
                sparkMax.setInverted(inverted);
                break;
        }
    }

    /** Configure the supply current limit for the motors. This is only partially supported on the SparkMax.
     * @param enabled Whether to enable the supply current limit.
     * @param limit The "holding" current (amperes) to limit to when feature is activated. * ROUNDED TO NEAREST INTEGER ON SPARKMAX *
     * @param trigger Current must exceed this threshold (amperes) before limiting occurs. If this value is less than currentLimit, then currentLimit is used as the threshold. * NOT SUPPORTED ON SPARKMAX *
     * @param triggerTime How long current must exceed threshold (seconds) before limiting occurs. * NOT SUPPORTED ON SPARKMAX *
     */
    public void setSupplyCurrentLimit(
        boolean enabled,
        double limit,
        double trigger,
        double triggerTime
    ) {
        switch (base) {
            case TALONFX:
                talonFX.configSupplyCurrentLimit(
                    new com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration(
                        enabled,
                        limit,
                        trigger,
                        triggerTime
                    ),
                    5
                );
                break;
            case TALONSRX:
                talonSRX.configSupplyCurrentLimit(
                    new com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration(
                        enabled,
                        limit,
                        trigger,
                        triggerTime
                    ),
                    5
                );
                break;
            case SPARKMAX:
                System.out.println(
                    "WARNING: Supply current limiting does not have full feature parity on SparkMax"
                );
                if (enabled) sparkMax.setSmartCurrentLimit(
                    (int) limit
                ); else sparkMax.setSmartCurrentLimit(0); // TODO: Check whether this is the correct way to disable current limiting
                break;
        }
    }

    /** Configure the stator current limit for the motors. This is ONLY supported on the TalonFX.
     * @param enabled Whether to enable the stator current limit.
     * @param limit The "holding" current (amperes) to limit to when feature is activated.
     * @param trigger Current must exceed this threshold (amperes) before limiting occurs. If this value is less than currentLimit, then currentLimit is used as the threshold.
     * @param triggerTime How long current must exceed threshold (seconds) before limiting occurs.
     */
    public void setStatorCurrentLimit(
        boolean enabled,
        double limit,
        double trigger,
        double triggerTime
    ) {
        switch (base) {
            case TALONFX:
                talonFX.configStatorCurrentLimit(
                    new com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration(
                        enabled,
                        limit,
                        trigger,
                        triggerTime
                    ),
                    5
                );
                break;
            case TALONSRX:
                // Appears to only implement supply current limiting
                System.out.println(
                    "ERROR: Stator current limiting is not supported on TalonSRX"
                );
                break;
            case SPARKMAX:
                System.out.println(
                    "ERROR: Stator current limiting is not supported on SparkMax"
                );
                break;
        }
    }

    /** Get the temperature of the motor in degrees Celsius.
     * @return The temperature of the motor in degrees Celsius.
     */
    public double getTemperature() {
        switch (base) {
            case TALONFX:
                return talonFX.getTemperature();
            case TALONSRX:
                return talonSRX.getTemperature();
            case SPARKMAX:
                return sparkMax.getMotorTemperature();
        }
        return 0;
    }

    /** Get the position of the motor.
     * @return The position in raw sensor units for Talon's, and rotations for SparkMax's.
     */
    public double getPosition() { // TODO: Create method that converts to standard units
        switch (base) {
            case TALONFX:
                return talonFX.getSelectedSensorPosition();
            case TALONSRX:
                return talonSRX.getSelectedSensorPosition();
            case SPARKMAX:
                return sparkMax.getEncoder().getPosition();
        }
        return 0;
    }

    /** Get the velocity of the motor.
     * @return The velocity in raw sensor units per 100ms for Talon's, and RPM for SparkMax's.
     */
    public double getVelocity() { // TODO: Create method that converts to standard units
        switch (base) {
            case TALONFX:
                return talonFX.getSelectedSensorVelocity();
            case TALONSRX:
                return talonSRX.getSelectedSensorVelocity();
            case SPARKMAX:
                return sparkMax.getEncoder().getVelocity();
        }
        return 0;
    }

    /** Get the raw controller object, for direct access to the underlying motor controller.
     * @return The controller object.
     */
    public Object getController() {
        switch (base) {
            case TALONFX:
                return talonFX;
            case TALONSRX:
                return talonSRX;
            case SPARKMAX:
                return sparkMax;
        }
        return null;
    }
}
