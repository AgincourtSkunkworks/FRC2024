package frc.robot.util;

import edu.wpi.first.wpilibj.Preferences;

/**
 * A class to represent values that can either be normal variables, or dynamic values through RobotPreferences.
 * This allows quick interchanges between directly using values, and using RobotPreferences keys, without needing
 * repetitive if statements, or a constants file to store the key.
 * <p>
 * For example, a value we didn't think needed to be changed often, could be switched to a RobotPreferences key,
 * by simply adding a key to initial creation of the DynamicValue, instead of searching & replacing every variable use.
 */
public class DynamicValue<T> {

    private String key;
    private T value;
    private final Class<T> type;

    /** Initialize a new RobotPreferences key with any supported type, if it doesn't already exist
     * @param key  The key to use in RobotPreferences
     * @param value The default value to set it to
     */
    private void initPrefValue(String key, T value) {
        if (value instanceof Boolean) Preferences.initBoolean(
            key,
            (Boolean) value
        ); else if (value instanceof Double) Preferences.initDouble(
            key,
            (Double) value
        ); else if (value instanceof Float) Preferences.initFloat(
            key,
            (Float) value
        ); else if (value instanceof Integer) Preferences.initInt(
            key,
            (Integer) value
        ); else if (value instanceof Long) Preferences.initLong(
            key,
            (Long) value
        ); else if (value instanceof String) Preferences.initString(
            key,
            (String) value
        ); else throw new IllegalArgumentException(
            "DynamicValue does not support type " +
            value.getClass().getName() +
            " using RobotPreferences."
        );
    }

    /** Get the value of the RobotPreferences key associated with this DynamicValue
     * @return The value of the RobotPreferences key
     */
    private T getPrefValue() {
        if (!Preferences.containsKey(key)) throw new IllegalStateException(
            "DynamicValue key " + key + " does not exist."
        ); else if (type.equals(Boolean.class)) return type.cast(
            Preferences.getBoolean(key, (Boolean) value)
        ); else if (type.equals(Double.class)) return type.cast(
            Preferences.getDouble(key, (Double) value)
        ); else if (type.equals(Float.class)) return type.cast(
            Preferences.getFloat(key, (Float) value)
        ); else if (type.equals(Integer.class)) return type.cast(
            Preferences.getInt(key, (Integer) value)
        ); else if (type.equals(Long.class)) return type.cast(
            Preferences.getLong(key, (Long) value)
        ); else if (type.equals(String.class)) return type.cast(
            Preferences.getString(key, (String) value)
        ); else throw new IllegalArgumentException(
            "DynamicValue does not support type " +
            type.getName() +
            " using RobotPreferences."
        );
    }

    /** Set the value of the RobotPreferences key associated with this DynamicValue
     * @param value The value to set the RobotPreferences key to
     */
    private void setPrefValue(T value) {
        if (!Preferences.containsKey(key)) throw new IllegalStateException(
            "DynamicValue key " + key + " does not exist."
        ); else if (type.equals(Boolean.class)) Preferences.setBoolean(
            key,
            (Boolean) value
        ); else if (type.equals(Double.class)) Preferences.setDouble(
            key,
            (Double) value
        ); else if (type.equals(Float.class)) Preferences.setFloat(
            key,
            (Float) value
        ); else if (type.equals(Integer.class)) Preferences.setInt(
            key,
            (Integer) value
        ); else if (type.equals(Long.class)) Preferences.setLong(
            key,
            (Long) value
        ); else if (type.equals(String.class)) Preferences.setString(
            key,
            (String) value
        ); else throw new IllegalArgumentException(
            "DynamicValue does not support type " +
            type.getName() +
            " using RobotPreferences."
        );
    }

    /** Create a new DynamicValue with a normal variable
     * @param value The value to use
     */
    @SuppressWarnings("unchecked") // value.getClass() should always represent the T type, when used properly
    public DynamicValue(T value) {
        this.type = (Class<T>) value.getClass();
        this.value = value;
    }

    /** Create a new DynamicValue with a RobotPreferences key
     * @param key The key to use in RobotPreferences
     * @param defaultValue The default value to set it to, if the key doesn't exist
     */
    @SuppressWarnings("unchecked") // defaultValue.getClass() should always represent the T type, when used properly
    public DynamicValue(String key, T defaultValue) {
        this.type = (Class<T>) defaultValue.getClass();
        this.key = key;
        initPrefValue(key, defaultValue);
    }

    /** Convert the DynamicValue to use a RobotPreferences key.
     * <p>
     * If the key doesn't exist, the current value will be used as the default value.
     * If the key does exist, the current value is ABANDONED and the value of the key is used.
     * @param key The key to use in RobotPreferences
     */
    public void toPreferences(String key) {
        if (this.key != null) throw new IllegalStateException(
            "DynamicValue is already using RobotPreferences."
        );
        this.key = key;
        initPrefValue(key, value);
        // The variable is inaccessible when using RobotPreferences, and is overwritten when converting back to
        // a variable, so we're overriding it here to prevent any confusion where they think the old value is safe.
        this.value = getPrefValue();
    }

    /** Convert the DynamicValue to use a RobotPreferences key.
     * <p>
     * If the key doesn't exist, the current value will be used as the default value.
     * If the key does exist, the behaviour depends on the replace parameter.
     * @param key The key to use in RobotPreferences
     * @param replace Whether to replace the RobotPreferences key with the current value if it already exists
     */
    public void toPreferences(String key, boolean replace) {
        T oldValue = value;
        toPreferences(key);
        if (replace) { // We're replacing the value, so let's set everything back to the old value
            setPrefValue(oldValue);
            this.value = oldValue;
        }
    }

    /**
     * Convert the DynamicValue to use a normal variable. The variable is set to the current value of the RobotPreferences key.
     */
    public void toVariable() {
        if (key == null) throw new IllegalStateException(
            "DynamicValue is already using a variable."
        );
        value = getPrefValue();
        Preferences.remove(key);
        key = null;
    }

    /** Get the value of the DynamicValue
     * @return The value of the DynamicValue
     */
    public T get() {
        if (key == null) return value; else return getPrefValue();
    }

    /** Set the value of the DynamicValue
     * @param value The value to set the DynamicValue to
     */
    public void set(T value) {
        if (key == null) this.value = value; else setPrefValue(value);
    }
}
