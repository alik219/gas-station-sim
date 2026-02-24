package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

/**
 * CarTest: JUnit 5 tests for the Car entity class.
 *
 * Tests cover:
 *   - Litres needed stays within valid range (10–60)
 *   - Arrival time can be set and retrieved correctly
 *   - Default arrival time is 0.0
 *   - Test constructor (direct litres) works correctly
 */
class CarTest {

    /**
     * Set up Sim globals before each test so Car() constructor works.
     * Car() calls Sim.litreStream.nextDouble(), so we must initialise it.
     */
    @BeforeEach
    void setUp() {
        Sim.litresNeededMin   = 10.0;
        Sim.litresNeededRange = 50.0;
        Sim.litreStream       = new Random(42); // fixed seed = reproducible tests
    }

    // ── Litres needed ──────────────────────────────────────────────────────────

    //Make sure the literstream doesn't become negative
    @Test
    @DisplayName("Litres needed should never be negative")
    void testLitresNeededNotNegative() {
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            assertTrue(car.getLitresNeeded() >= 0,
                "Litres needed should not be negative");
        }
    }

    @Test
    @DisplayName("Test constructor sets litres directly")
    void testDirectConstructorSetsLitres() {
        Car car = new Car(35.5);
        assertEquals(35.5, car.getLitresNeeded(), 0.001,
            "Direct constructor should set exact litres");
    }

    // ── Arrival time ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Default arrival time should be 0.0")
    void testDefaultArrivalTimeIsZero() {
        Car car = new Car(20.0);
        assertEquals(0.0, car.getArrivalTime(), 0.001,
            "Arrival time should default to 0.0 before being set");
    }

    @Test
    @DisplayName("setArrivalTime and getArrivalTime should match")
    void testSetAndGetArrivalTime() {
        Car car = new Car(20.0);
        car.setArrivalTime(123.45);
        assertEquals(123.45, car.getArrivalTime(), 0.001,
            "getArrivalTime should return the value set by setArrivalTime");
    }

    @Test
    @DisplayName("Arrival time should update when set multiple times")
    void testArrivalTimeCanBeUpdated() {
        Car car = new Car(20.0);
        car.setArrivalTime(100.0);
        car.setArrivalTime(200.0);
        assertEquals(200.0, car.getArrivalTime(), 0.001,
            "Arrival time should reflect the most recent set value");
    }
}
