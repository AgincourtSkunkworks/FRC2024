package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class DriveUntilPitch extends CommandBase {

    DriveSubsystem drive;
    GyroSubsystem gyro;
    double speed, target, tolerance;
    boolean not;

    /**
     * Creates a DriveUntilPitch Command. This command is used to drive at a certain
     * speed until the robot reaches (or leaves) a certain pitch.
     *
     * @param drive     The drive subsystem
     * @param gyro      The gyro subsystem
     * @param speed     The speed to drive at
     * @param target    The target pitch
     * @param tolerance The tolerance for the pitch
     * @param not       True to drive until pitch is out of range, false to drive
     *                  until pitch is in range
     */
    public DriveUntilPitch(
        DriveSubsystem drive,
        GyroSubsystem gyro,
        double speed,
        double target,
        double tolerance,
        boolean not
    ) {
        addRequirements(drive);
        this.drive = drive;
        this.speed = speed;
        this.gyro = gyro;
        this.target = target;
        this.tolerance = tolerance;
        this.not = not;
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
        return not != (Math.abs(gyro.getVAngle() - target) <= tolerance);
    }
}
