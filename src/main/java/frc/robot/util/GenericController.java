package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

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

    public final BaseController base;
    public TalonFX talonFX;
    public DutyCycleOut talonFXOut;
    public TalonFXConfiguration talonFXConfig;
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
                talonFX = new TalonFX(id);
                talonFXOut = new DutyCycleOut(0);
                talonFXConfig = new TalonFXConfiguration();
                break;
            case TALONSRX:
                talonSRX = new WPI_TalonSRX(id);
                break;
            case SPARKMAX:
                // Currently only supports brushless motors, as some implementations rely on brushless features
                sparkMax =
                    new CANSparkMax(id, CANSparkLowLevel.MotorType.kBrushless);
                break;
        }
    }

    /** Set the motor to a specific speed.
     * @param speed The speed to set the motor to, between -1 and 1.
     */
    public void set(double speed) {
        switch (base) {
            case TALONFX:
                talonFX.setControl(talonFXOut.withOutput(speed));
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
            case TALONFX: // TODO: Verify that this works as expected
                talonFXConfig.MotorOutput.NeutralMode =
                    mode == NeutralMode.Brake
                        ? com.ctre.phoenix6.signals.NeutralModeValue.Brake
                        : com.ctre.phoenix6.signals.NeutralModeValue.Coast;
                talonFX.getConfigurator().apply(talonFXConfig);
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
                talonFXConfig.MotorOutput.Inverted =
                    inverted
                        ? InvertedValue.Clockwise_Positive
                        : InvertedValue.CounterClockwise_Positive;
                talonFX.getConfigurator().apply(talonFXConfig);
                break;
            case TALONSRX:
                talonSRX.setInverted(inverted);
                break;
            case SPARKMAX:
                sparkMax.setInverted(inverted);
                break;
        }
    }

    /** Gets the inversion of the motor.
     * @return Whether the motor is inverted.
     */
    public boolean getInverted() {
        return switch (base) {
            case TALONFX -> talonFXConfig.MotorOutput.Inverted ==
            InvertedValue.Clockwise_Positive;
            case TALONSRX -> talonSRX.getInverted();
            case SPARKMAX -> sparkMax.getInverted();
        };
    }

    /** Configure the supply current limit for the motors. This is only partially supported on the TalonFX and SparkMax.
     * @param enabled Whether to enable the supply current limit.
     * @param limit The "holding" current (amperes) to limit to when feature is activated. * ROUNDED TO NEAREST INTEGER ON SPARKMAX *
     * @param trigger Current must exceed this threshold (amperes) before limiting occurs. If this value is less than currentLimit, then currentLimit is used as the threshold. * NOT SUPPORTED ON TALONFX OR SPARKMAX *
     * @param triggerTime How long current must exceed threshold (seconds) before limiting occurs. * NOT SUPPORTED ON TALONFX OR SPARKMAX *
     */
    public void setSupplyCurrentLimit( // TODO: Figure out how to better handle parity between controllers for current limiting
        boolean enabled,
        double limit,
        double trigger,
        double triggerTime
    ) {
        switch (base) {
            case TALONFX: // TODO: Verify that this works as expected
                System.out.println(
                    "WARNING: Supply current limiting does not have full feature parity on TalonFX"
                );
                talonFXConfig.CurrentLimits
                    .withStatorCurrentLimitEnable(enabled)
                    .withStatorCurrentLimit(limit);
                talonFX.getConfigurator().apply(talonFXConfig);
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
     */
    public void setStatorCurrentLimit(boolean enabled, double limit) {
        switch (base) {
            case TALONFX: // TODO: Verify that this works as expected
                talonFXConfig.CurrentLimits
                    .withStatorCurrentLimitEnable(enabled)
                    .withStatorCurrentLimit(limit);
                talonFX.getConfigurator().apply(talonFXConfig);
                break;
            case TALONSRX:
                // Appears to only implement supply current limiting
                System.out.println(
                    "WARNING: Stator current limiting is not supported on TalonSRX"
                );
                break;
            case SPARKMAX:
                System.out.println(
                    "WARNING: Stator current limiting is not supported on SparkMax"
                );
                break;
        }
    }

    /** Set the position of the motor.
     * @param newPosition The new position to set the motor to. This is truncated to the nearest integer for TalonSRXs.
     */
    public void setPosition(double newPosition) {
        switch (base) {
            case TALONFX -> talonFX.setPosition(newPosition);
            case TALONSRX -> talonSRX.setSelectedSensorPosition(
                (int) newPosition
            );
            case SPARKMAX -> sparkMax.getEncoder().setPosition(newPosition);
        }
    }

    /** Get the speed of the motor.
     * @return The speed of the motor, between -1 and 1.
     */
    public double get() {
        return switch (base) {
            case TALONFX -> talonFXOut.Output;
            case TALONSRX -> talonSRX.get();
            case SPARKMAX -> sparkMax.get();
        };
    }

    /** Get the temperature of the motor in degrees Celsius.
     * @return The temperature of the motor in degrees Celsius.
     */
    public double getTemperature() {
        return switch (base) {
            case TALONFX -> talonFX.getDeviceTemp().getValue();
            case TALONSRX -> talonSRX.getTemperature();
            case SPARKMAX -> sparkMax.getMotorTemperature();
        };
    }

    /** Get the position of the motor.
     * @return The position in raw sensor units for Talon's, and rotations for SparkMax's.
     */
    public double getPosition() {
        return switch (base) {
            case TALONFX -> talonFX.getPosition().getValue();
            case TALONSRX -> talonSRX.getSelectedSensorPosition();
            case SPARKMAX -> sparkMax.getEncoder().getPosition();
        };
    }

    /** Get the velocity of the motor.
     * @return The velocity in raw sensor units per 100ms for Talon's, and RPM for SparkMax's.
     */
    public double getVelocity() {
        return switch (base) {
            case TALONFX -> talonFX.getVelocity().getValue();
            case TALONSRX -> talonSRX.getSelectedSensorVelocity();
            case SPARKMAX -> sparkMax.getEncoder().getVelocity();
        };
    }

    /** Get the supply current of the motor controller. This is NOT SUPPORTED ON THE SPARKMAX.
     * @return The supply current of the motor controller
     */
    public double getSupplyCurrent() {
        return switch (base) {
            case TALONFX -> talonFX.getSupplyCurrent().getValue();
            case TALONSRX -> talonSRX.getSupplyCurrent();
            case SPARKMAX -> -999999; // Not supported, no warning to avoid spamming (& -999999 is a clearly invalid)
        };
    }

    /** Get the output current of the motor controller.
     * @return The output current of the motor controller
     */
    public double getStatorCurrent() {
        return switch (base) {
            case TALONFX -> talonFX.getStatorCurrent().getValue();
            case TALONSRX -> talonSRX.getStatorCurrent();
            case SPARKMAX -> sparkMax.getOutputCurrent();
        };
    }

    /** Get the ID of the motor controller.
     * @return The ID of the motor controller.
     */
    public int getID() {
        return switch (base) {
            case TALONFX -> talonFX.getDeviceID();
            case TALONSRX -> talonSRX.getDeviceID();
            case SPARKMAX -> sparkMax.getDeviceId();
        };
    }

    /** Get the raw controller object, for direct access to the underlying motor controller.
     * @return The controller object.
     */
    public Object getController() {
        return switch (base) {
            case TALONFX -> talonFX;
            case TALONSRX -> talonSRX;
            case SPARKMAX -> sparkMax;
        };
    }
}
