package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GenericController;
import frc.robot.util.GenericController.BaseController;
import frc.robot.util.GenericController.NeutralMode;

public class ClimberSubsystem extends SubsystemBase {

    final GenericController motor;

    private ClimberSubsystem(BaseController type, int mID, boolean invert) {
        motor = new GenericController(type, mID);
        motor.setInverted(invert);
    }

    public static ClimberSubsystem create(
        BaseController type,
        int mID,
        boolean invert
    ) {
        return new ClimberSubsystem(type, mID, invert);
    }

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

    public ClimberSubsystem setStatorLimit(boolean limit, double currentLimit) {
        motor.setStatorCurrentLimit(limit, currentLimit);
        return this;
    }

    public ClimberSubsystem setNeutralMode(NeutralMode neutralMode) {
        motor.setNeutralMode(neutralMode);
        return this;
    }

    public void setMotor(double speed) {
        motor.set(speed);
    }

    public double getPosition() {
        return motor.getPosition();
    }
}
