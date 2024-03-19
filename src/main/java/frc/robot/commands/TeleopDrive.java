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
    boolean useLimiters = false;

    /**
     * Creates a TeleopDrive Command. This command is used to control the drive in the teleop phase.
     * <p>
     *     ! No slew rate limiting will be applied, see {@link #TeleopDrive(DriveSubsystem, Supplier, Supplier, double)}} instead for slew limiting
     * </p>
     *
     * @param drive      The drive subsystem
     * @param lSpeedFunc Function to get the speed to set the left motors to
     * @param rSpeedFunc Function to get the speed to set the right motors
     *                   to
     */
    public TeleopDrive(
        DriveSubsystem drive,
        Supplier<Double> lSpeedFunc,
        Supplier<Double> rSpeedFunc
    ) {
        addRequirements(drive);
        this.drive = drive;
        this.lSpeedFunc = lSpeedFunc;
        this.rSpeedFunc = rSpeedFunc;
    }

    /**
     * Creates a TeleopDrive Command. This command is used to control the drive in the teleop phase.
     *
     * <p>
     *      ! Slew rate limiting will be applied, see {@link #TeleopDrive(DriveSubsystem, Supplier, Supplier)}} for no slew rate limiting
     * </p>
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
        this.useLimiters = true;
    }

    @Override
    public void execute() {
        final double stickLeft = lSpeedFunc.get();
        final double stickRight = rSpeedFunc.get();
        final double outLeft = (useLimiters)
            ? leftLimiter.calculate(stickLeft)
            : stickLeft;
        final double outRight = (useLimiters)
            ? rightLimiter.calculate(stickRight)
            : stickRight;
        drive.setLeftMotors(outLeft);
        drive.setRightMotors(outRight);
        SmartDashboard.putNumber("Stick Left", stickLeft);
        SmartDashboard.putNumber("Stick Right", stickRight);
        SmartDashboard.putNumber("Out Left", outLeft);
        SmartDashboard.putNumber("Out Right", outRight);
    }

    @Override
    public void end(boolean interrupted) {
        drive.setMotors(0);
    }
}
