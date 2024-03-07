package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import frc.robot.util.GenericController.NeutralMode;

public class ClimberSubsystem {

    public static class Climber extends SubsystemBase {

        private GenericController motor;

        private Climber(
            BaseController type,
            int motorID,
            boolean invert
        ) {
            motor = new GenericController(type, mID);
            motor.setInverted(invert);
        }
        
        public static Climber create(
            BaseController type, 
            int mID, 
            boolean invert
        ) {
            return new Climber(type, int motorID, boolean invert);
        }
 
        public void setMotor(double speed) {
            motor.set(speed);
        }

        public void setNeutralMode(neutralMode) {
            motor.setNeutralMode(neutralMode);
        }

        public double getPosition() {
            return motor.getPosition();
        }
    }
}
