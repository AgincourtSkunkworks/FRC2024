package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import java.util.ArrayList;

public class IntakeSubsystem {

    public static class Rotation extends SubsystemBase {

        final ArrayList<GenericController> motors = new ArrayList<>();

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

        public static Rotation create(
            BaseController type,
            int m1ID,
            boolean m1Invert,
            int m2ID,
            boolean m2Invert
        ) {
            return new Rotation(type, m1ID, m1Invert, m2ID, m2Invert);
        }

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

        public Rotation setStatorLimit(boolean limit, double currentLimit) {
            for (GenericController motor : motors) {
                motor.setStatorCurrentLimit(limit, currentLimit);
            }
            return this;
        }

        public void setMotors(double speed) {
            for (GenericController motor : motors) {
                motor.set(speed);
            }
        }

        public double getAveragePosition() {
            return motors
                .stream()
                .mapToDouble(GenericController::getPosition)
                .average()
                .orElseThrow();
        }

        public void setPositions(double position) {
            for (GenericController motor : motors) {
                motor.setPosition(position);
            }
        }
    }

    public static class Feeder extends SubsystemBase {

        final GenericController motor;

        public Feeder(BaseController type, int motorID, boolean invert) {
            motor = new GenericController(type, motorID);
            motor.setInverted(invert);
        }

        public void setMotor(double speed) {
            motor.set(speed);
        }
    }
}
