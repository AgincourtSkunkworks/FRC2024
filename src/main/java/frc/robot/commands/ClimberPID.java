package frc.robot.commands;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.util.DynamicValue;

public class ClimberPID extends GenericPID {

    DynamicValue<Double> tolerance;

    public ClimberPID(
        String name,
        ClimberSubsystem climber,
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
            () -> target - climber.getPosition(),
            climber::setMotor
        );
        addRequirements(climber);
        tolerance = targetTolerance;
    }

    @Override
    public boolean isFinished() {
        return Math.abs(getError.getAsDouble()) < tolerance.get();
    }
}
