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
    double speedMult;

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
        this.speedMult = 1;
    }

    /** Get the current speed multiplier
     * @return The current speed multiplier
     */
    public double getSpeedMult() {
        return this.speedMult;
    }

    /** Set a speed multiplier for teleop sticks (e.g. 0.5 means half speed at full joysticks)
     * @param speedMult Speed multiplier to set to (1 being normal)
     */
    public void setSpeedMult(double speedMult) {
        this.speedMult = speedMult;
    }

    /**
     * Shortcut to reset speed multiplier to normal speed
     */
    public void resetSpeedMult() {
        setSpeedMult(1);
    }

    @Override
    public void execute() {
        final double stickLeft = lSpeedFunc.get();
        final double stickRight = rSpeedFunc.get();
        double speedLeft = leftLimiter.calculate(stickLeft) * this.speedMult;
        double speedRight = rightLimiter.calculate(stickRight) * this.speedMult;
        drive.setLeftMotors(speedLeft);
        drive.setRightMotors(speedRight);
        SmartDashboard.putNumber("Stick Left", stickLeft);
        SmartDashboard.putNumber("Stick Right", stickRight);
        SmartDashboard.putNumber("Teleop Speed Mult", speedMult);
    }

    @Override
    public void end(boolean interrupted) {
        drive.setMotors(0);
    }
}
