package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {

    WPI_TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    WPI_TalonFX[] leftMotors, rightMotors, motors;
    double lCorrect, rCorrect, brakeThreshold, thermalWarning, maxTemp;

    /**
     * Creates a new DriveSubsystem.
     *
     * @param l1ID     Left Motor Controller ID
     * @param l2ID     Left Motor Controller ID
     * @param r1ID     Right Motor Controller ID
     * @param r2ID     Right Motor Controller ID
     * @param lInvert  Whether or not to invert the left motors
     * @param rInvert  Whether or not to invert the right motors
     * @param lCorrect Percent of speed to add to left motors (0-1)
     * @param rCorrect Percent of speed to add to right motors (0-1)
     * @param brakeThreshold The speed at which it rounds down to 0 and brakes
     * @param thermalWarning The motor temperature in which a warning is sent to the driver station
     * @param currentSupply Whether to enable supply current limiting
     * @param currentSupplyLimit The maximum supply current limit
     * @param currentSupplyTrigger The limit at which the current limit is activated
     * @param currentSupplyTriggerTime The amount of time over the current limit trigger before the limit is activated
     * @param currentStator Whether to enable stator current limiting
     * @param currentStatorLimit The maximum stator current limit
     * @param currentStatorTrigger The limit at which the current limit is activated
     * @param currentStatorTriggerTime The amount of time over the current limit trigger before the limit is activated
     * @param maxTemp Max temperature for autonomous mode, after which the robot will stop
     */
    public DriveSubsystem(
        int l1ID,
        int l2ID,
        int r1ID,
        int r2ID,
        boolean lInvert,
        boolean rInvert,
        double lCorrect,
        double rCorrect,
        double brakeThreshold,
        double thermalWarning,
        boolean currentSupply,
        double currentSupplyLimit,
        double currentSupplyTrigger,
        double currentSupplyTriggerTime,
        boolean currentStator,
        double currentStatorLimit,
        double currentStatorTrigger,
        double currentStatorTriggerTime,
        double maxTemp
    ) {
        leftMotor1 = new WPI_TalonFX(l1ID);
        leftMotor2 = new WPI_TalonFX(l2ID);
        rightMotor1 = new WPI_TalonFX(r1ID);
        rightMotor2 = new WPI_TalonFX(r2ID);

        leftMotors = new WPI_TalonFX[] { leftMotor1, leftMotor2 };
        rightMotors = new WPI_TalonFX[] { rightMotor1, rightMotor2 };
        motors =
            new WPI_TalonFX[] {
                leftMotor1,
                leftMotor2,
                rightMotor1,
                rightMotor2,
            };

        this.lCorrect = 1 + lCorrect;
        this.rCorrect = 1 + rCorrect;
        this.brakeThreshold = brakeThreshold;
        this.thermalWarning = thermalWarning;
        this.maxTemp = maxTemp;

        for (WPI_TalonFX motor : motors) {
            motor.setNeutralMode(NeutralMode.Brake);
            motor.configSupplyCurrentLimit(
                new SupplyCurrentLimitConfiguration(
                    currentSupply,
                    currentSupplyLimit,
                    currentSupplyTrigger,
                    currentSupplyTriggerTime
                )
            );
            motor.configStatorCurrentLimit(
                new StatorCurrentLimitConfiguration(
                    currentStator,
                    currentStatorLimit,
                    currentStatorTrigger,
                    currentStatorTriggerTime
                )
            );
        }

        if (lInvert) for (WPI_TalonFX motor : leftMotors) motor.setInverted(
            true
        );
        if (rInvert) for (WPI_TalonFX motor : rightMotors) motor.setInverted(
            true
        );
    }

    /**
     * Set the speed of the left motors.
     *
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

    /**
     * Get the position of the left motor. This uses left 1 as a reference,
     * both left motors should be very similar without hardware issues.
     * ! Add a number after Left to get the position of a specific motor.
     *
     * @return The position of the left motors in raw encoder units.
     */
    public double getLeftPos() {
        return leftMotor1.getSelectedSensorPosition();
    }

    /**
     * Get the position of the right motor. This uses right 1 as a reference,
     * both right motors should be very similar without hardware issues.
     * ! Add a number after Right to get the position of a specific motor.
     *
     * @return The position of the right motors in raw encoder units.
     */
    public double getRightPos() {
        return rightMotor1.getSelectedSensorPosition();
    }

    public double getLeft1Pos() {
        return leftMotor1.getSelectedSensorPosition();
    }

    public double getLeft2Pos() {
        return leftMotor2.getSelectedSensorPosition();
    }

    public double getRight1Pos() {
        return rightMotor1.getSelectedSensorPosition();
    }

    public double getRight2Pos() {
        return rightMotor2.getSelectedSensorPosition();
    }

    public double getLeft1Temp() {
        return leftMotor1.getTemperature();
    }

    public double getLeft2Temp() {
        return leftMotor2.getTemperature();
    }

    public double getRight1Temp() {
        return rightMotor1.getTemperature();
    }

    public double getRight2Temp() {
        return rightMotor2.getTemperature();
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

    /** Get the max temperature allowed for autonomous mode
     * @return The max temperature
     */
    public double getMaxTemp() {
        return this.maxTemp;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("LM1 Position", getLeft1Pos());
        SmartDashboard.putNumber("LM2 Position", getLeft2Pos());
        SmartDashboard.putNumber("RM1 Position", getRight1Pos());
        SmartDashboard.putNumber("RM2 Position", getRight2Pos());
        SmartDashboard.putNumber("LM1 Temp", getLeft1Temp());
        SmartDashboard.putNumber("LM2 Temp", getLeft2Temp());
        SmartDashboard.putNumber("RM1 Temp", getRight1Temp());
        SmartDashboard.putNumber("RM2 Temp", getRight2Temp());
        SmartDashboard.putNumber("LM1 Stator", leftMotor1.getStatorCurrent());
        SmartDashboard.putNumber("LM2 Stator", leftMotor2.getStatorCurrent());
        SmartDashboard.putNumber("RM1 Stator", rightMotor1.getStatorCurrent());
        SmartDashboard.putNumber("RM2 Stator", rightMotor2.getStatorCurrent());
        SmartDashboard.putNumber("LM1 Supply", leftMotor1.getSupplyCurrent());
        SmartDashboard.putNumber("LM2 Supply", leftMotor2.getSupplyCurrent());
        SmartDashboard.putNumber("RM1 Supply", rightMotor1.getSupplyCurrent());
        SmartDashboard.putNumber("RM2 Supply", rightMotor2.getSupplyCurrent());

        if (getHighestTemp() > thermalWarning) {
            SmartDashboard.putString("WARNING", "MOTOR OVERHEAT - CAUTION");
        } else {
            SmartDashboard.putString("WARNING", "");
        }
    }
}
