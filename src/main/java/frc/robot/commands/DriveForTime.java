package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.util.DynamicValue;

public class DriveForTime extends Command {

    DriveSubsystem drive;
    DynamicValue<Double> time;
    double speed, startTime;

    /**
     * Creates a DriveForTime Command. This command is used to drive at a certain
     * speed for a certain time.
     *
     * @param drive   The drive subsystem
     * @param speed   The speed to drive at
     * @param time    The time to drive for (in seconds)
     */
    public DriveForTime(
        DriveSubsystem drive,
        double speed,
        DynamicValue<Double> time
    ) {
        addRequirements(drive);
        this.drive = drive;
        this.speed = speed;
        this.time = time;
    }

    /**
     * Creates a DriveForTime Command. This command is used to drive at a certain
     * speed for a certain time.
     *
     * @param drive   The drive subsystem
     * @param speed   The speed to drive at
     * @param time    The time to drive for (in seconds)
     */
    public DriveForTime(DriveSubsystem drive, double speed, double time) {
        this(drive, speed, new DynamicValue<>(time));
    }

    @Override
    public void initialize() {
        this.startTime = Timer.getFPGATimestamp();
        drive.setMotors(speed);
    }

    @Override
    public void execute() {
        drive.setMotors(speed);
    }

    @Override
    public void end(boolean interrupted) {
        drive.setMotors(0);
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime >= time.get();
    }
}
