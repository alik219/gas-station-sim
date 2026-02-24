package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PumpStandTest: JUnit 5 tests for the PumpStand class.
 *
 * Tests cover:
 *   - All pumps available at start
 *   - Taking pumps reduces availability
 *   - Taking all pumps means none available
 *   - Releasing a pump makes it available again
 *   - Correct pump count returned
 *   - Taking from empty stand returns null
 */
class PumpStandTest {

    // ── Initial state ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("New pump stand should have pumps available")
    void testPumpsAvailableAtStart() {
        PumpStand stand = new PumpStand(3);
        assertTrue(stand.aPumpIsAvailable(),
            "Pumps should be available in a newly created stand");
    }

    @Test
    @DisplayName("getNumberOfPumps should return the count given at creation")
    void testGetNumberOfPumps() {
        PumpStand stand = new PumpStand(5);
        assertEquals(5, stand.getNumberOfPumps(),
            "getNumberOfPumps should return the number passed to constructor");
    }

    // ── Taking pumps ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Taking all pumps should leave none available")
    void testTakingAllPumpsMeansNoneAvailable() {
        PumpStand stand = new PumpStand(2);
        stand.takeAvailablePump();
        stand.takeAvailablePump();
        assertFalse(stand.aPumpIsAvailable(),
            "No pumps should be available after taking all of them");
    }

    @Test
    @DisplayName("takeAvailablePump should return a non-null Pump object")
    void testTakeAvailablePumpReturnsNonNull() {
        PumpStand stand = new PumpStand(1);
        Pump pump = stand.takeAvailablePump();
        assertNotNull(pump, "takeAvailablePump should return a Pump, not null");
    }

    @Test
    @DisplayName("Taking from empty stand should return null")
    void testTakeFromEmptyStandReturnsNull() {
        PumpStand stand = new PumpStand(1);
        stand.takeAvailablePump();              // take the only pump
        Pump result = stand.takeAvailablePump(); // now stand is empty
        assertNull(result,
            "Taking from an empty stand should return null");
    }

    // ── Releasing pumps ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Releasing a pump should make it available again")
    void testReleasedPumpBecomesAvailable() {
        PumpStand stand = new PumpStand(1);
        Pump pump = stand.takeAvailablePump();
        assertFalse(stand.aPumpIsAvailable(), "No pumps should be available after taking the only one");

        stand.releasePump(pump);
        assertTrue(stand.aPumpIsAvailable(), "Pump should be available again after release");
    }

    @Test
    @DisplayName("Releasing two pumps should make two available")
    void testReleasingMultiplePumps() {
        PumpStand stand = new PumpStand(2);
        Pump p1 = stand.takeAvailablePump();
        Pump p2 = stand.takeAvailablePump();

        assertFalse(stand.aPumpIsAvailable());

        stand.releasePump(p1);
        assertTrue(stand.aPumpIsAvailable(), "One pump released should be available");

        stand.releasePump(p2);
        assertTrue(stand.aPumpIsAvailable(), "Two pumps released should be available");
    }

    // ── Single pump stand ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Single-pump stand: take, use, release cycle works correctly")
    void testSinglePumpCycle() {
        PumpStand stand = new PumpStand(1);

        assertTrue(stand.aPumpIsAvailable());       // starts available

        Pump pump = stand.takeAvailablePump();
        assertNotNull(pump);
        assertFalse(stand.aPumpIsAvailable());      // now busy

        stand.releasePump(pump);
        assertTrue(stand.aPumpIsAvailable());       // available again
    }
}
