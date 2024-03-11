package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import frc.robot.util.GenericController.NeutralMode;

public class ClimberSubsystem extends SubsystemBase {

    final GenericController motor;

    // Private constructor so people use .create() instead
    private ClimberSubsystem(BaseController type, int mID, boolean invert) {
        motor = new GenericController(type, mID);
        motor.setInverted(invert);
    }

    /** Create a new ClimberSubsystem.
     * @param type The type of motor controller to use
     * @param mID The ID of the motor
     * @param invert Whether to invert the motor
     * @return A new ClimberSubsystem
     */
    public static ClimberSubsystem create(
        BaseController type,
        int mID,
        boolean invert
    ) {
        return new ClimberSubsystem(type, mID, invert);
    }

    /** Set the supply current limit
     * @param limit Whether to enable the supply current limit
     * @param currentLimit The current limit to set
     * @param triggerCurrent The current at which to trigger the limit
     * @param triggerTime The time to trigger the limit
     * @return The ClimberSubsystem, for chaining
     */
    public ClimberSubsystem setSupplyLimit(
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

    /** Set the stator current limit
     * @param limit Whether to enable the stator current limit
     * @param currentLimit The current limit to set
     * @return The ClimberSubsystem, for chaining
     */
    public ClimberSubsystem setStatorLimit(boolean limit, double currentLimit) {
        motor.setStatorCurrentLimit(limit, currentLimit);
        return this;
    }

    /** Set the neutral mode
     * @param neutralMode The neutral mode to set
     * @return The ClimberSubsystem, for chaining
     */
    public ClimberSubsystem setNeutralMode(NeutralMode neutralMode) {
        motor.setNeutralMode(neutralMode);
        return this;
    }

    /** Set the motor speed
     * @param speed The speed to set the motor to
     */
    public void setMotor(double speed) {
        motor.set(speed);
    }

    /** Get the position of the motor
     * @return The position of the motor, in raw encoder units
     */
    public double getPosition() {
        return motor.getPosition();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Climber Position", getPosition());
        if (
            Constants.Debug.ENABLE && Constants.Debug.DETAILED_SMART_DASHBOARD
        ) {
            SmartDashboard.putNumber(
                "Climber Temperature",
                motor.getTemperature()
            );
            SmartDashboard.putNumber(
                "Climber Supply Current",
                motor.getSupplyCurrent()
            );
            SmartDashboard.putNumber(
                "Climber Stator Current",
                motor.getStatorCurrent()
            );
        }
    }
}
