package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import java.util.ArrayList;

public class OuttakeSubsystem extends SubsystemBase {

    final ArrayList<GenericController> motors = new ArrayList<>();

    /** Create a new Outtake subsystem.
     * @param type The type of controller to use
     * @param m1ID The ID of the first motor
     * @param m1Invert Whether to invert the first motor
     * @param m2ID The ID of the second motor
     * @param m2Invert Whether to invert the second motor
     */
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

    /** Set the supply current limit
     * @param limit Whether to enable the supply current limit
     * @param currentLimit The current limit to set
     * @param triggerCurrent The current at which to trigger the limit
     * @param triggerTime The time to trigger the limit
     * @return The OuttakeSubsystem subsystem, for chaining
     */
    public OuttakeSubsystem setSupplyLimit(
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

    /** Set the speed of the motors.
     * @param speed The speed to set the motors to
     */
    public void setMotors(double speed) {
        for (GenericController motor : motors) {
            motor.set(speed);
        }
    }

    @Override
    public void periodic() {
        if (
            Constants.Debug.ENABLE && Constants.Debug.DETAILED_SMART_DASHBOARD
        ) {
            SmartDashboard.putNumber(
                "Outtake Motor 1 Speed",
                motors.get(0).get()
            );
            SmartDashboard.putNumber(
                "Outtake Motor 2 Speed",
                motors.get(1).get()
            );
        }
    }
}
