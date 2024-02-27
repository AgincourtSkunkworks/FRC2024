package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import java.util.function.Supplier;

public class TeleopDrive extends Command {

    DriveSubsystem drive;
    Supplier<Double> lSpeedFunc, rSpeedFunc;
    SlewRateLimiter leftLimiter, rightLimiter;

    /**
     * Creates a TeleopDrive Command. This command is used to control the drive in the teleop phase.
     *
     * @param drive      The drive subsystem
     * @param lSpeedFunc Function to get the speed to set the left motors to
     * @param rSpeedFunc Function to get the speed to set the right motors
     *                   to
     * @param rateLimit  Slew rate limit for motors
     */
    public TeleopDrive(
        DriveSubsystem drive,
        Supplier<Double> lSpeedFunc,
        Supplier<Double> rSpeedFunc,
        double rateLimit
    ) {
        addRequirements(drive);
        this.drive = drive;
        this.lSpeedFunc = lSpeedFunc;
        this.rSpeedFunc = rSpeedFunc;
        this.leftLimiter = new SlewRateLimiter(rateLimit);
        this.rightLimiter = new SlewRateLimiter(rateLimit);
    }

    @Override
    public void execute() {
        final double stickLeft = lSpeedFunc.get();
        final double stickRight = rSpeedFunc.get();
        drive.setLeftMotors(leftLimiter.calculate(stickLeft));
        drive.setRightMotors(rightLimiter.calculate(stickRight));
        SmartDashboard.putNumber("Stick Left", stickLeft);
        SmartDashboard.putNumber("Stick Right", stickRight);
    }

    @Override
    public void end(boolean interrupted) {
        drive.setMotors(0);
    }
}
