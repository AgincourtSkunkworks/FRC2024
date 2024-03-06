package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import java.util.ArrayList;

public class OuttakeSubsystem extends SubsystemBase {

    final ArrayList<GenericController> motors = new ArrayList<>();

    public OuttakeSubsystem(
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

    public void setMotors(double speed) {
        for (GenericController motor : motors) {
            motor.set(speed);
        }
    }
}
