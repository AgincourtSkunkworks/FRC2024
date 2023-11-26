package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GyroSubsystem extends SubsystemBase {

    public AHRS ahrs;
    private final boolean useRoll, upsideDown;

    /** Create a GyroSubsystem
     * @param useRoll   Whether to use the roll or pitch value
     * @param upsideDown Whether the gyro is upside down
     */
    public GyroSubsystem(boolean useRoll, boolean upsideDown) {
        ahrs = new AHRS(SPI.Port.kMXP);
        this.useRoll = useRoll;
        this.upsideDown = upsideDown;
    }

    /**
     * Check if the gyro is ready to be used
     *
     * @return True if the gyro is ready, false otherwise.
     */
    public boolean isReady() {
        return ahrs.isConnected();
    }

    /**
     * Get the vertical angle of the robot (normally pitch)
     * ! If you don't want bugs, MAKE SURE THE GYRO IS READY BEFORE CALLING THIS
     * METHOD !
     *
     * @return The angle of the robot in degrees (-180 to 180).
     */
    public double getVAngle() {
        if (!isReady()) throw new IllegalStateException("Gyro is not ready!");
        return (
            ((useRoll) ? ahrs.getRoll() : ahrs.getPitch()) *
            (upsideDown ? -1 : 1)
        );
    }

    /**
     * Get the YAW of the robot
     * ! If you don't want bugs, MAKE SURE THE GYRO IS READY BEFORE CALLING THIS
     * METHOD !
     *
     * @return The YAW angle of the robot in degrees (-180 to 180).
     */
    public double getYaw() {
        if (!isReady()) throw new IllegalStateException("Gyro is not ready!");
        return ahrs.getYaw();
    }

    /**
     * Set the current yaw value to be the new zero value.
     */
    public void zeroYaw() {
        if (!isReady()) throw new IllegalStateException("Gyro is not ready!");
        ahrs.zeroYaw();
    }

    @Override
    public void periodic() {
        if (!isReady()) return;
        SmartDashboard.putNumber("Gyro Vertical Angle (Pitch)", getVAngle());
        SmartDashboard.putNumber("Yaw", ahrs.getYaw());
        SmartDashboard.putNumber("Pitch", ahrs.getPitch());
        SmartDashboard.putNumber("Roll", ahrs.getRoll());
    }
}
