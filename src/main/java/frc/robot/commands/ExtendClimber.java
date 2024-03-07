package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class ExtendClimber extends Command {

    ClimberSubsystem climber;
    double speed, endPosition;

    public ExtendClimber(ClimberSubsystem climber, double position, double speed) {
        addRequirement(climber);
        this.climber = climber;
        this.position = position;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        //calculate the endPosition assuming climber is at the end, either the top or base
        //find what a TALONFX unit is
        endPosition = climber.getPosition() + 66.8; //TODO: determine how many TALONFX units 66.8 cm is
    }

    @Override
    public void execute() {
        climber.setMotor(speed);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotor(0);
    }

    @Override
    public boolean isFinished() {
        return climber.getPosition() >= endPosition;
    }
}
