package frc.robot.util;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DynamicValueTest {

    static final NetworkTableInstance testInstance =
        NetworkTableInstance.create();

    static {
        testInstance.startServer();
        Preferences.setNetworkTableInstance(testInstance); // Use a separate NetworkTableInstance for testing
    }

    /**
     * Create RobotPreferences keys with all supported types, and set them to 2 (false for Boolean).
     */
    private static void createExisting() {
        Preferences.setBoolean("keyBool", false);
        Preferences.setDouble("keyDouble", 2.0);
        Preferences.setFloat("keyFloat", 2.0f);
        Preferences.setInt("keyInt", 2);
        Preferences.setLong("keyLong", 2L);
        Preferences.setString("keyString", "2");
    }

    /** Assert that an exception is thrown with the expected type and message
     * @param expectedType The expected type of the thrown exception
     * @param message The expected message of the thrown exception
     * @param executable The code that should throw the exception
     * @param <T> The type of the thrown exception
     */
    private static <T extends Throwable> void assertThrowsMessage(
        Class<T> expectedType,
        String message,
        Executable executable
    ) {
        assertEquals(
            message,
            assertThrows(expectedType, executable).getMessage()
        );
    }

    @AfterEach
    void shutdown() {
        Preferences.removeAll();
    }

    @AfterAll
    static void cleanup() {
        testInstance.stopServer();
        testInstance.close();
    }

    /**
     * Test that when using normal variables, all supported types are created, and contain the value provided.
     */
    @Test
    void normalValueCreate() {
        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        assertEquals(true, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>(1.0);
        assertEquals(1.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>(1.0f);
        assertEquals(1.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>(1);
        assertEquals(1, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>(1L);
        assertEquals(1L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("1");
        assertEquals("1", valString.get());

        assertEquals(1, Preferences.getKeys().size()); // Make sure we didn't create any RobotPreferences keys
    }

    /**
     * Test that when using RobotPreferences with a new key, all supported types are created, and contain the value provided.
     */
    @Test
    void prefValueCreate() {
        DynamicValue<Boolean> valBool = new DynamicValue<>("keyBool", true);
        assertTrue(Preferences.containsKey("keyBool"));
        assertEquals(true, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>("keyDouble", 1.0);
        assertTrue(Preferences.containsKey("keyDouble"));
        assertEquals(1.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>("keyFloat", 1.0f);
        assertTrue(Preferences.containsKey("keyFloat"));
        assertEquals(1.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>("keyInt", 1);
        assertTrue(Preferences.containsKey("keyInt"));
        assertEquals(1, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>("keyLong", 1L);
        assertTrue(Preferences.containsKey("keyLong"));
        assertEquals(1L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("keyString", "1");
        assertTrue(Preferences.containsKey("keyString"));
        assertEquals("1", valString.get());
    }

    /**
     * Test that when using RobotPreferences with an existing key, all supported types are created, and contain the value from the RobotPreferences key.
     */
    @Test
    void prefValueCreateExisting() {
        createExisting();

        DynamicValue<Boolean> valBool = new DynamicValue<>("keyBool", true);
        assertTrue(Preferences.containsKey("keyBool"));
        assertEquals(false, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>("keyDouble", 1.0);
        assertTrue(Preferences.containsKey("keyDouble"));
        assertEquals(2.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>("keyFloat", 1.0f);
        assertTrue(Preferences.containsKey("keyFloat"));
        assertEquals(2.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>("keyInt", 1);
        assertTrue(Preferences.containsKey("keyInt"));
        assertEquals(2, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>("keyLong", 1L);
        assertTrue(Preferences.containsKey("keyLong"));
        assertEquals(2L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("keyString", "1");
        assertTrue(Preferences.containsKey("keyString"));
        assertEquals("2", valString.get());
    }

    /**
     * Test that when using normal variables, values can be changed, and contain the new value.
     */
    @Test
    void normalValueChange() {
        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        valBool.set(false);
        assertEquals(false, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>(1.0);
        valDouble.set(2.0);
        assertEquals(2.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>(1.0f);
        valFloat.set(2.0f);
        assertEquals(2.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>(1);
        valInt.set(2);
        assertEquals(2, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>(1L);
        valLong.set(2L);
        assertEquals(2L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("1");
        valString.set("2");
        assertEquals("2", valString.get());
    }

    /**
     * Test that when using RobotPreferences, values can be changed, and contain the new value.
     */
    @Test
    void prefValueChange() {
        DynamicValue<Boolean> valBool = new DynamicValue<>("keyBool", true);
        valBool.set(false);
        assertEquals(false, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>("keyDouble", 1.0);
        valDouble.set(2.0);
        assertEquals(2.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>("keyFloat", 1.0f);
        valFloat.set(2.0f);
        assertEquals(2.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>("keyInt", 1);
        valInt.set(2);
        assertEquals(2, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>("keyLong", 1L);
        valLong.set(2L);
        assertEquals(2L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("keyString", "1");
        valString.set("2");
        assertEquals("2", valString.get());
    }

    /**
     * Test starting with a normal variable, then changing to RobotPreferences.
     */
    @Test
    void normalToPref() {
        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        valBool.toPreferences("keyBool");
        assertTrue(Preferences.containsKey("keyBool"));
        assertEquals(true, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>(1.0);
        valDouble.toPreferences("keyDouble");
        assertTrue(Preferences.containsKey("keyDouble"));
        assertEquals(1.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>(1.0f);
        valFloat.toPreferences("keyFloat");
        assertTrue(Preferences.containsKey("keyFloat"));
        assertEquals(1.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>(1);
        valInt.toPreferences("keyInt");
        assertTrue(Preferences.containsKey("keyInt"));
        assertEquals(1, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>(1L);
        valLong.toPreferences("keyLong");
        assertTrue(Preferences.containsKey("keyLong"));
        assertEquals(1L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("1");
        valString.toPreferences("keyString");
        assertTrue(Preferences.containsKey("keyString"));
        assertEquals("1", valString.get());
    }

    /**
     * Test starting with a normal variable, then changing to an already existing RobotPreferences key, without replacing it.
     */
    @Test
    void normalToPrefExisting() {
        createExisting();

        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        valBool.toPreferences("keyBool");
        assertTrue(Preferences.containsKey("keyBool"));
        assertEquals(false, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>(1.0);
        valDouble.toPreferences("keyDouble");
        assertTrue(Preferences.containsKey("keyDouble"));
        assertEquals(2.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>(1.0f);
        valFloat.toPreferences("keyFloat");
        assertTrue(Preferences.containsKey("keyFloat"));
        assertEquals(2.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>(1);
        valInt.toPreferences("keyInt");
        assertTrue(Preferences.containsKey("keyInt"));
        assertEquals(2, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>(1L);
        valLong.toPreferences("keyLong");
        assertTrue(Preferences.containsKey("keyLong"));
        assertEquals(2L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("1");
        valString.toPreferences("keyString");
        assertTrue(Preferences.containsKey("keyString"));
        assertEquals("2", valString.get());
    }

    /**
     * Test starting with a normal variable, then changing to an already existing RobotPreferences key, replacing it.
     */
    @Test
    void normalToPrefExistingReplace() {
        createExisting();

        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        valBool.toPreferences("keyBool", true);
        assertTrue(Preferences.containsKey("keyBool"));
        assertEquals(true, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>(1.0);
        valDouble.toPreferences("keyDouble", true);
        assertTrue(Preferences.containsKey("keyDouble"));
        assertEquals(1.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>(1.0f);
        valFloat.toPreferences("keyFloat", true);
        assertTrue(Preferences.containsKey("keyFloat"));
        assertEquals(1.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>(1);
        valInt.toPreferences("keyInt", true);
        assertTrue(Preferences.containsKey("keyInt"));
        assertEquals(1, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>(1L);
        valLong.toPreferences("keyLong", true);
        assertTrue(Preferences.containsKey("keyLong"));
        assertEquals(1L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("1");
        valString.toPreferences("keyString", true);
        assertTrue(Preferences.containsKey("keyString"));
        assertEquals("1", valString.get());
    }

    /**
     * Test starting with a RobotPreferences key, then changing to a normal variable.
     */
    @Test
    void prefToNormal() {
        DynamicValue<Boolean> valBool = new DynamicValue<>("keyBool", true);
        valBool.toVariable();
        assertFalse(Preferences.containsKey("keyBool"));
        assertEquals(true, valBool.get());
        DynamicValue<Double> valDouble = new DynamicValue<>("keyDouble", 1.0);
        valDouble.toVariable();
        assertFalse(Preferences.containsKey("keyDouble"));
        assertEquals(1.0, valDouble.get());
        DynamicValue<Float> valFloat = new DynamicValue<>("keyFloat", 1.0f);
        valFloat.toVariable();
        assertFalse(Preferences.containsKey("keyFloat"));
        assertEquals(1.0f, valFloat.get());
        DynamicValue<Integer> valInt = new DynamicValue<>("keyInt", 1);
        valInt.toVariable();
        assertFalse(Preferences.containsKey("keyInt"));
        assertEquals(1, valInt.get());
        DynamicValue<Long> valLong = new DynamicValue<>("keyLong", 1L);
        valLong.toVariable();
        assertFalse(Preferences.containsKey("keyLong"));
        assertEquals(1L, valLong.get());
        DynamicValue<String> valString = new DynamicValue<>("keyString", "1");
        valString.toVariable();
        assertFalse(Preferences.containsKey("keyString"));
        assertEquals("1", valString.get());
    }

    /**
     * Test that the expected error is thrown when we try converting a DynamicValue that is already converted.
     */
    @Test
    void alreadyConverted() {
        DynamicValue<Boolean> valBool = new DynamicValue<>(true);
        assertThrowsMessage(
            IllegalStateException.class,
            "DynamicValue is already using a variable.",
            valBool::toVariable
        );
        valBool.toPreferences("keyBool");
        assertThrowsMessage(
            IllegalStateException.class,
            "DynamicValue is already using RobotPreferences.",
            () -> valBool.toPreferences("keyBool2")
        );
    }

    /**
     * Tests that the expected error is thrown when we get a key that does not exist (someone manually deleted it from RobotPreferences).
     */
    @Test
    void keyDoesNotExist() {
        DynamicValue<Boolean> valBool = new DynamicValue<>("keyBool", true);
        Preferences.remove("keyBool");
        assertThrowsMessage(
            IllegalStateException.class,
            "DynamicValue key keyBool does not exist.",
            valBool::get
        );
        assertThrowsMessage(
            IllegalStateException.class,
            "DynamicValue key keyBool does not exist.",
            () -> valBool.set(false)
        );
    }

    /**
     * Tests that the expected error is thrown when we try to create, or convert to a RobotPreferences DynamicValue with an unsupported type.
     */
    @Test
    void unsupportedType() {
        assertThrowsMessage(
            IllegalArgumentException.class,
            "DynamicValue does not support type java.lang.Object using RobotPreferences.",
            () -> new DynamicValue<>("keyImpossible", new Object())
        );
        DynamicValue<Object> valObj = new DynamicValue<>(new Object());
        assertThrowsMessage(
            IllegalArgumentException.class,
            "DynamicValue does not support type java.lang.Object using RobotPreferences.",
            () -> valObj.toPreferences("keyImpossible")
        );
    }
}
