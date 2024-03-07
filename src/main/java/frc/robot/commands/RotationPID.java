package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.util.DynamicValue;

public class RotationPID extends GenericPID {

    DynamicValue<Double> tolerance;
    boolean canEnd;

    public RotationPID(
        String name,
        IntakeSubsystem.Rotation rotation,
        double target,
        DynamicValue<Double> targetTolerance,
        double defaultP,
        double defaultI,
        double defaultD,
        double defaultIMax,
        boolean canEnd
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
        this.canEnd = canEnd;
        addRequirements(rotation);
    }

    @Override
    public boolean isFinished() {
        return canEnd && Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
