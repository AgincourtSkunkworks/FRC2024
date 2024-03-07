package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import java.util.ArrayList;

public class IntakeSubsystem {

    public static class Rotation extends SubsystemBase {

        final ArrayList<GenericController> motors = new ArrayList<>();

        // Private constructor so people use .create() instead
        private Rotation(
            BaseController type,
            int m1ID,
            boolean m1Invert,
            int m2ID,
            boolean m2Invert
        ) {
            GenericController motor1 = new GenericController(type, m1ID);
            motor1.setInverted(m1Invert);
            motors.add(motor1);
            GenericController motor2 = new GenericController(type, m2ID);
            motor2.setInverted(m2Invert);
            motors.add(motor2);
        }

        /** Create a new Rotation subsystem.
         * @param type The type of motor controller
         * @param m1ID The ID of the first motor
         * @param m1Invert Whether the first motor is inverted
         * @param m2ID The ID of the second motor
         * @param m2Invert Whether the second motor is inverted
         * @return A new Rotation subsystem
         */
        public static Rotation create(
            BaseController type,
            int m1ID,
            boolean m1Invert,
            int m2ID,
            boolean m2Invert
        ) {
            return new Rotation(type, m1ID, m1Invert, m2ID, m2Invert);
        }

        /** Set the supply current limit
         * @param limit Whether to enable the supply current limit
         * @param currentLimit The current limit to set
         * @param triggerCurrent The current at which to trigger the limit
         * @param triggerTime The time to trigger the limit
         * @return The Rotation subsystem, for chaining
         */
        public Rotation setSupplyLimit(
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
         * @return The Rotation subsystem, for chaining
         */
        public Rotation setStatorLimit(boolean limit, double currentLimit) {
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

        /** Get the position of the [first] motor
         * @return The position of the [first] motor, in raw encoder units
         */
        public double getPosition() {
            return motors.get(0).getPosition();
        }

        /** Get the average position of the motors
         * @return The average position of the motors, in raw encoder units
         */
        public double getAveragePosition() {
            return motors
                .stream()
                .mapToDouble(GenericController::getPosition)
                .average()
                .orElseThrow();
        }

        /** Set the position of the motors
         * @param position The position to set the motors to, in raw encoder units
         */
        public void setPositions(double position) {
            for (GenericController motor : motors) {
                motor.setPosition(position);
            }
        }
    }

    public static class Feeder extends SubsystemBase {

        final GenericController motor;

        /** Create a new Feeder subsystem.
         * @param type The type of motor controller
         * @param mID The ID of the motor
         * @param invert Whether the motor is inverted
         */
        public Feeder(BaseController type, int mID, boolean invert) {
            motor = new GenericController(type, mID);
            motor.setInverted(invert);
        }

        /** Set the supply current limit
         * @param speed The speed to set the motor to
         */
        public void setMotor(double speed) {
            motor.set(speed);
        }
    }
}
