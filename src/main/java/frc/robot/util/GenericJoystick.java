package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class GenericJoystick {

    public enum BaseJoystick {
        DIRECT,
        PS4,
        PS5,
        /* Not Implemented:
        - XBox: does not have support for L2/R2 as buttons (would need manual handling)
         */
    }

    public enum Axis {
        LX,
        LY,
        RX,
        RY,
    }

    public enum Button {
        N,
        E,
        S,
        W,
        OPT_L,
        OPT_R,
        L1,
        L2,
        R1,
        R2,
        STK_L,
        STK_R,
    }

    public enum POV {
        N,
        NE,
        E,
        SE,
        S,
        SW,
        W,
        NW,
    }

    private final HashMap<Axis, Integer> directAxisMap, ps4AxisMap, ps5AxisMap;
    private final HashMap<
        Button,
        Integer
    > directButtonMap, ps4ButtonMap, ps5ButtonMap;
    private final HashMap<POV, Integer> directPOVMap, ps4POVMap, ps5POVMap;

    // ! Direct Input (for Logitech Dual Action [ON D SWITCH MODE], & others)
    {
        directAxisMap =
            new HashMap<>() {
                {
                    put(Axis.LX, 0);
                    put(Axis.LY, 1);
                    put(Axis.RX, 2);
                    put(Axis.RY, 3);
                }
            };
        directButtonMap =
            new HashMap<>() {
                {
                    put(Button.N, 4); // Y
                    put(Button.E, 3); // B
                    put(Button.S, 2); // A
                    put(Button.W, 1); // X
                    put(Button.OPT_L, 9); // BACK
                    put(Button.OPT_R, 10); // START
                    put(Button.L1, 5);
                    put(Button.L2, 7);
                    put(Button.R1, 6);
                    put(Button.R2, 8);
                    put(Button.STK_L, 11);
                    put(Button.STK_R, 12);
                }
            };
        directPOVMap =
            new HashMap<>() {
                {
                    put(POV.N, 0);
                    put(POV.NE, 45);
                    put(POV.E, 90);
                    put(POV.SE, 135);
                    put(POV.S, 180);
                    put(POV.SW, 225);
                    put(POV.W, 270);
                    put(POV.NW, 315);
                }
            };
    }

    // ! PS4
    {
        ps4AxisMap =
            new HashMap<>() {
                {
                    put(Axis.LX, 0);
                    put(Axis.LY, 1);
                    put(Axis.RX, 2);
                    put(Axis.RY, 5);
                    /* Unused:
            - L2/R2 (Triggers) as axis 3/4 - not supported on Direct Input
             */
                }
            };
        ps4ButtonMap =
            new HashMap<>() {
                {
                    put(Button.N, 4); // TRIANGLE
                    put(Button.E, 3); // CIRCLE
                    put(Button.S, 2); // CROSS
                    put(Button.W, 1); // SQUARE
                    put(Button.OPT_L, 9); // SHARE
                    put(Button.OPT_R, 10); // OPTIONS
                    put(Button.L1, 5);
                    put(Button.L2, 7);
                    put(Button.R1, 6);
                    put(Button.R2, 8);
                    put(Button.STK_L, 11);
                    put(Button.STK_R, 12);
                    /* Unused:
            - PS4 Button as 13 - not supported on Direct Input
            - Touchpad as 14 - not supported on Direct Input
             */
                }
            };
        ps4POVMap =
            new HashMap<>() {
                {
                    put(POV.N, 0);
                    put(POV.NE, 45);
                    put(POV.E, 90);
                    put(POV.SE, 135);
                    put(POV.S, 180);
                    put(POV.SW, 225);
                    put(POV.W, 270);
                    put(POV.NW, 315);
                }
            };
    }

    // ! PS5
    { // TODO: Verify with actual controller, using PS4 as base
        ps5AxisMap =
            new HashMap<>() {
                {
                    put(Axis.LX, 0);
                    put(Axis.LY, 1);
                    put(Axis.RX, 2);
                    put(Axis.RY, 5);
                    /* Unused:
            - L2/R2 (Triggers) as axis 3/4 - not supported on Direct Input
             */
                }
            };
        ps5ButtonMap =
            new HashMap<>() {
                {
                    put(Button.N, 4); // TRIANGLE
                    put(Button.E, 3); // CIRCLE
                    put(Button.S, 2); // CROSS
                    put(Button.W, 1); // SQUARE
                    put(Button.OPT_L, 9); // SHARE
                    put(Button.OPT_R, 10); // OPTIONS
                    put(Button.L1, 5);
                    put(Button.L2, 7);
                    put(Button.R1, 6);
                    put(Button.R2, 8);
                    put(Button.STK_L, 11);
                    put(Button.STK_R, 12);
                    /* Unused:
            - PS4 Button as 13 - not supported on Direct Input
            - Touchpad as 14 - not supported on Direct Input
             */
                }
            };
        ps5POVMap =
            new HashMap<>() {
                {
                    put(POV.N, 0);
                    put(POV.NE, 45);
                    put(POV.E, 90);
                    put(POV.SE, 135);
                    put(POV.S, 180);
                    put(POV.SW, 225);
                    put(POV.W, 270);
                    put(POV.NW, 315);
                }
            };
    }

    public final BaseJoystick base;
    public Joystick direct;
    public PS4Controller ps4;
    public PS5Controller ps5;

    /** Create a new GenericJoystick.
     * @param base The type of joystick to use.
     * @param id  The ID of the joystick.
     */
    public GenericJoystick(BaseJoystick base, int id) {
        this.base = base;
        switch (base) {
            case DIRECT:
                direct = new Joystick(id);
                break;
            case PS4:
                ps4 = new PS4Controller(id);
                break;
            case PS5:
                ps5 = new PS5Controller(id);
                break;
        }
    }

    /** Get the value of an axis. The value will be between -1 and 1.
     * @param axis The axis to get the value of.
     * @return The value of the axis, where -1 is full forward, 1 is full backward, and 0 is centered.
     */
    public double getRawAxis(Axis axis) {
        return switch (base) {
            case DIRECT -> direct.getRawAxis(directAxisMap.get(axis));
            case PS4 -> ps4.getRawAxis(ps4AxisMap.get(axis));
            case PS5 -> ps5.getRawAxis(ps5AxisMap.get(axis));
        };
    }

    /** Get a JoystickButton object for a button.
     * @param button The button to get
     * @return The JoystickButton object for the button
     */
    public JoystickButton getButton(Button button) {
        return switch (base) {
            case DIRECT -> new JoystickButton(
                direct,
                directButtonMap.get(button)
            );
            case PS4 -> new JoystickButton(ps4, ps4ButtonMap.get(button));
            case PS5 -> new JoystickButton(ps5, ps5ButtonMap.get(button));
        };
    }

    /** Get the value of a button.
     * @param button The button to get
     * @return The value of the button
     */
    public boolean getRawButton(Button button) {
        return switch (base) {
            case DIRECT -> direct.getRawButton(directButtonMap.get(button));
            case PS4 -> ps4.getRawButton(ps4ButtonMap.get(button));
            case PS5 -> ps5.getRawButton(ps5ButtonMap.get(button));
        };
    }

    /** Get the angle of the POV in degrees.
     * @return The angle of the POV in degrees, or -1 if the POV is not pressed.
     */
    public int getPOV() {
        return switch (base) {
            case DIRECT -> direct.getPOV();
            case PS4 -> ps4.getPOV();
            case PS5 -> ps5.getPOV();
        };
    }

    /** Get a POVButton object for a POV.
     * @param pov The POV to get
     * @return The POVButton object for the POV
     */
    public POVButton getPOVButton(POV pov) {
        return switch (base) {
            case DIRECT -> new POVButton(direct, directPOVMap.get(pov));
            case PS4 -> new POVButton(ps4, ps4POVMap.get(pov));
            case PS5 -> new POVButton(ps5, ps5POVMap.get(pov));
        };
    }

    /** Set the rumble output for the joystick.
     * @param type The rumble type
     * @param value The value to set the rumble to
     */
    public void setRumble(GenericHID.RumbleType type, double value) {
        switch (base) {
            case DIRECT -> direct.setRumble(type, value);
            case PS4 -> ps4.setRumble(type, value);
            case PS5 -> ps5.setRumble(type, value);
        }
    }
}
