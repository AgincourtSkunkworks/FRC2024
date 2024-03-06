package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.util.DynamicValue;

public class RotationPID extends GenericPID {

    DynamicValue<Double> tolerance;

    public RotationPID(
        String name,
        IntakeSubsystem.Rotation rotation,
        double target,
        DynamicValue<Double> targetTolerance,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax
    ) {
        super(
            name,
            defaultP,
            defaultI,
            defaultD,
            defaultIMax,
            () -> target - rotation.getAveragePosition(),
            rotation::setMotors
        );
        tolerance = targetTolerance;
        addRequirements(rotation);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
