package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase implements AutoCloseable {

    UsbCamera camera;

    /**
     * Create a new Camera subsystem.
     */
    public CameraSubsystem() {
        camera = CameraServer.startAutomaticCapture();
        if (!camera.isConnected()) {
            System.out.println(
                "ERROR: Camera not connected on initialization!"
            );
        }
    }

    /** Set the frames per second of the camera.
     * @param fps The frames per second to set the camera to
     * @return The CameraSubsystem subsystem, for chaining
     */
    public CameraSubsystem setFPS(int fps) {
        camera.setFPS(fps);
        return this;
    }

    /** Set the resolution of the camera.
     * @param width The width of the camera, in pixels
     * @param height The height of the camera, in pixels
     * @return The CameraSubsystem subsystem, for chaining
     */
    public CameraSubsystem setResolution(int width, int height) {
        camera.setResolution(width, height);
        return this;
    }

    @Override
    public void close() {
        camera.close();
    }
}
