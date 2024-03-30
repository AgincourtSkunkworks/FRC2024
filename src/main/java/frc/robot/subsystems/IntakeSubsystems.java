package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import java.util.ArrayList;

public class IntakeSubsystems {

    public static class RotationSubsystem extends SubsystemBase {

        final ArrayList<GenericController> motors = new ArrayList<>();
        final GenericController positionalMotor; // The motor we get the position from -- the right/second motor

        // Private constructor so people use .create() instead
        private RotationSubsystem(
            BaseController type,
            int lmID,
            boolean lmInvert,
            int rmID,
            boolean rmInvert
        ) {
            GenericController lm = new GenericController(type, lmID);
            lm.setInverted(lmInvert);
            motors.add(lm);
            GenericController rm = new GenericController(type, rmID);
            rm.setInverted(rmInvert);
            motors.add(rm);
            positionalMotor = rm; // The second (right) motor is the motor we get the position from
        }

        /** Create a new RotationSubsystem.
         * @param type The type of motor controller
         * @param lmID The ID of the left motor (this will be the motor used for positional data)
         * @param lmInvert Whether the left motor is inverted
         * @param rmID The ID of the right motor
         * @param rmInvert Whether the right motor is inverted
         * @return A new RotationSubsystem subsystem
         */
        public static RotationSubsystem create(
            BaseController type,
            int lmID,
            boolean lmInvert,
            int rmID,
            boolean rmInvert
        ) {
            return new RotationSubsystem(type, lmID, lmInvert, rmID, rmInvert);
        }

        /** Set the neutral mode
         * @param mode The neutral mode to set the motors to
         * @return The RotationSubsystem subsystem, for chaining
         */
        public RotationSubsystem setNeutralMode(
            GenericController.NeutralMode mode
        ) {
            for (GenericController motor : motors) {
                motor.setNeutralMode(mode);
            }
            return this;
        }

        /** Set the supply current limit
         * @param limit Whether to enable the supply current limit
         * @param currentLimit The current limit to set
         * @param triggerCurrent The current at which to trigger the limit
         * @param triggerTime The time to trigger the limit
         * @return The RotationSubsystem subsystem, for chaining
         */
        public RotationSubsystem setSupplyLimit(
            boolean limit,
            double currentLimit,
            double triggerCurrent,
            double triggerTime
        ) {
            for (GenericController motor : motors) {
                motor.setSupplyCurrentLimit(
                    limit,
                    currentLimit,
                    triggerCurrent,
                    triggerTime
                );
            }
            return this;
        }

        /** Set the stator current limit
         * @param limit Whether to enable the stator current limit
         * @param currentLimit The current limit to set
         * @return The RotationSubsystem subsystem, for chaining
         */
        public RotationSubsystem setStatorLimit(
            boolean limit,
            double currentLimit
        ) {
            for (GenericController motor : motors) {
                motor.setStatorCurrentLimit(limit, currentLimit);
            }
            return this;
        }

        /** Set the neutral mode
         * @param speed The speed to set the motors to
         */
        public void setMotors(double speed) {
            for (GenericController motor : motors) {
                motor.set(speed);
            }
        }

        /** Get the position of the positional motor (right/second)
         * @return The position of the positional motor, in raw encoder units
         */
        public double getPosition() {
            return positionalMotor.getPosition();
        }

        /** Set the position of the motors
         * @param position The position to set the motors to, in raw encoder units
         */
        public void setPositions(double position) {
            for (GenericController motor : motors) {
                motor.setPosition(position);
            }
        }

        @Override
        public void periodic() {
            SmartDashboard.putNumber(
                "Rotation Primary Position",
                getPosition()
            );
            if (
                Constants.Debug.ENABLE &&
                Constants.Debug.DETAILED_SMART_DASHBOARD
            ) {
                SmartDashboard.putNumber(
                    "Rotation Left Motor Position",
                    motors.get(0).getPosition()
                );
                SmartDashboard.putNumber(
                    "Rotation Right Motor Position",
                    motors.get(1).getPosition()
                );
                SmartDashboard.putNumber(
                    "Rotation Left Motor Temperature",
                    motors.get(0).getTemperature()
                );
                SmartDashboard.putNumber(
                    "Rotation Right Motor Temperature",
                    motors.get(1).getTemperature()
                );
                SmartDashboard.putNumber(
                    "Rotation Left Motor Supply Current",
                    motors.get(0).getSupplyCurrent()
                );
                SmartDashboard.putNumber(
                    "Rotation Right Motor Supply Current",
                    motors.get(1).getSupplyCurrent()
                );
                SmartDashboard.putNumber(
                    "Rotation Left Motor Stator Current",
                    motors.get(0).getStatorCurrent()
                );
                SmartDashboard.putNumber(
                    "Rotation Right Motor Stator Current",
                    motors.get(1).getStatorCurrent()
                );
            }
        }
    }

    public static class FeederSubsystem extends SubsystemBase {

        final GenericController motor;

        /** Create a new FeederSubsystem.
         * @param type The type of motor controller
         * @param mID The ID of the motor
         * @param invert Whether the motor is inverted
         */
        public FeederSubsystem(BaseController type, int mID, boolean invert) {
            motor = new GenericController(type, mID);
            motor.setInverted(invert);
        }

        /** Set the supply current limit
         * @param limit Whether to enable the supply current limit
         * @param currentLimit The current limit to set
         * @param triggerCurrent The current at which to trigger the limit
         * @param triggerTime The time to trigger the limit
         * @return The FeederSubsystem, for chaining
         */
        public FeederSubsystem setSupplyLimit(
            boolean limit,
            double currentLimit,
            double triggerCurrent,
            double triggerTime
        ) {
            motor.setSupplyCurrentLimit(
                limit,
                currentLimit,
                triggerCurrent,
                triggerTime
            );
            return this;
        }

        /** Set the supply current limit
         * @param speed The speed to set the motor to
         */
        public void setMotor(double speed) {
            motor.set(speed);
        }

        @Override
        public void periodic() {
            if (
                Constants.Debug.ENABLE &&
                Constants.Debug.DETAILED_SMART_DASHBOARD
            ) {
                SmartDashboard.putNumber("Feeder Motor Speed", motor.get());
            }
        }
    }
}
