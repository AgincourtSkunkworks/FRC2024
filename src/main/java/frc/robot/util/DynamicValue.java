package frc.robot.util;

import edu.wpi.first.wpilibj.Preferences;

/**
 * A class to represent values that can either be normal variables, or dynamic values through RobotPreferences. This
 * simplifies the logic, preventing the need of repetitive if statements and Preferences.getX() calls.
 */
public class DynamicValue<T> {
    private T value;
    private Class<T> type;
    private String key;

    /** Initialize a new RobotPreferences key with any supported type
     * @param key  The key to use in RobotPreferences
     * @param value The default value to set it to
     */
    private void initPrefValue(String key, T value) {
        if (Preferences.containsKey(key))
            throw new IllegalArgumentException(
                    "DynamicValue key " + key + " already exists."
            );
        else if (value instanceof Boolean)
            Preferences.initBoolean(key, (Boolean) value);
        else if (value instanceof Double)
            Preferences.initDouble(key, (Double) value);
        else if (value instanceof Float)
            Preferences.initFloat(key, (Float) value);
        else if (value instanceof Integer)
            Preferences.initInt(key, (Integer) value);
        else if (value instanceof Long)
            Preferences.initLong(key, (Long) value);
        else if (value instanceof String)
            Preferences.initString(key, (String) value);
        else
            throw new IllegalArgumentException(
                    "DynamicValue does not support type " +
                            value.getClass().getName() + " using RobotPreferences."
            );
    }

    /** Get the value of the RobotPreferences key associated with this DynamicValue
     * @return The value of the RobotPreferences key
     */
    private T getPrefValue() {
        if (!Preferences.containsKey(key))
            throw new IllegalStateException(
                    "DynamicValue key " + key + " does not exist.");
        else if (type.equals(Boolean.class))
            return type.cast(Preferences.getBoolean(key, (Boolean) value));
        else if (type.equals(Double.class))
            return type.cast(Preferences.getDouble(key, (Double) value));
        else if (type.equals(Float.class))
            return type.cast(Preferences.getFloat(key, (Float) value));
        else if (type.equals(Integer.class))
            return type.cast(Preferences.getInt(key, (Integer) value));
        else if (type.equals(Long.class))
            return type.cast(Preferences.getLong(key, (Long) value));
        else if (type.equals(String.class))
            return type.cast(Preferences.getString(key, (String) value));
        else
            throw new IllegalArgumentException(
                    "DynamicValue does not support type " +
                            type.getName() + " using RobotPreferences."
            );
    }

    /** Set the value of the RobotPreferences key associated with this DynamicValue
     * @param value The value to set the RobotPreferences key to
     */
    private void setPrefValue(T value) {
        if (!Preferences.containsKey(key))
            throw new IllegalStateException(
                    "DynamicValue key " + key + " does not exist.");
        else if (type.equals(Boolean.class))
            Preferences.setBoolean(key, (Boolean) value);
        else if (type.equals(Double.class))
            Preferences.setDouble(key, (Double) value);
        else if (type.equals(Float.class))
            Preferences.setFloat(key, (Float) value);
        else if (type.equals(Integer.class))
            Preferences.setInt(key, (Integer) value);
        else if (type.equals(Long.class))
            Preferences.setLong(key, (Long) value);
        else if (type.equals(String.class))
            Preferences.setString(key, (String) value);
        else
            throw new IllegalArgumentException(
                    "DynamicValue does not support type " +
                            type.getName() + " using RobotPreferences."
            );
    }

    /** Create a new DynamicValue with a normal variable
     * @param value The value to use
     */
    @SuppressWarnings("unchecked")  // value.getClass() should always represent the T type, when used properly
    public DynamicValue(T value) {
        this.type = (Class<T>) value.getClass();
        this.value = value;
    }

    /** Create a new DynamicValue with a RobotPreferences key
     * @param key The key to use in RobotPreferences
     * @param defaultValue The default value to set it to
     */
    @SuppressWarnings("unchecked")  // defaultValue.getClass() should always represent the T type, when used properly
    public DynamicValue(String key, T defaultValue) {
        this.type = (Class<T>) defaultValue.getClass();
        this.key = key;
        initPrefValue(key, defaultValue);
    }

    /** Create a new DynamicValue from an existing variable or literal (equivalent to using the constructor with the value)
     * @param value The value to use
     * @return A new DynamicValue with a normal variable
     */
    public DynamicValue<T> from(T value) {
        return new DynamicValue<T>(value);
    }

    public T get() {
        if (key == null) return value;
        else return getPrefValue();
    }

    public void set(T value) {
        if (key == null) this.value = value;
        else setPrefValue(value);
    }

    public void toPreferences(String key) {
        if (this.key != null) throw new IllegalStateException(
            "DynamicValue is already using RobotPreferences."
        );
        this.key = key;
        initPrefValue(key, value);
    }

    public void toVariable() {
        if (key == null) throw new IllegalStateException(
            "DynamicValue is already using a variable."
        );
        value = getPrefValue();
        Preferences.remove(key);
        key = null;
    }
}
