package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CarQueueTest: JUnit 5 tests for the CarQueue data structure.
 *
 * Tests cover:
 *   - New queue is empty
 *   - Inserting cars increases queue size
 *   - FIFO ordering (first in = first out)
 *   - Removing all cars empties the queue
 *   - Taking from empty queue returns null and prints error
 *   - Queue size tracks correctly through multiple operations
 */
class CarQueueTest {

    private CarQueue queue;

    /**
     * Reset Sim globals and create a fresh queue before each test.
     * CarQueue uses Sim.simulationTime in its empty-time tracking.
     */
    @BeforeEach
    void setUp() {
        Sim.simulationTime = 0.0;
        queue = new CarQueue();
    }

    // ── Initial state ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("New queue should have size 0")
    void testNewQueueIsEmpty() {
        assertEquals(0, queue.getQueueSize(),
            "A newly created queue should have size 0");
    }

    // ── Insert ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Inserting one car should give size 1")
    void testInsertOneCarIncreasesSize() {
        queue.insert(new Car(30.0));
        assertEquals(1, queue.getQueueSize());
    }

    @Test
    @DisplayName("Inserting three cars should give size 3")
    void testInsertMultipleCarsIncreasesSize() {
        queue.insert(new Car(10.0));
        queue.insert(new Car(20.0));
        queue.insert(new Car(30.0));
        assertEquals(3, queue.getQueueSize());
    }

    // ── FIFO ordering ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("takeFirstCar should return cars in FIFO order")
    void testFIFOOrdering() {
        Car first  = new Car(10.0);
        Car second = new Car(20.0);
        Car third  = new Car(30.0);

        first.setArrivalTime(1.0);
        second.setArrivalTime(2.0);
        third.setArrivalTime(3.0);

        queue.insert(first);
        queue.insert(second);
        queue.insert(third);

        assertEquals(1.0, queue.takeFirstCar().getArrivalTime(), 0.001,
            "First car inserted should be first car removed");
        assertEquals(2.0, queue.takeFirstCar().getArrivalTime(), 0.001,
            "Second car inserted should be second car removed");
        assertEquals(3.0, queue.takeFirstCar().getArrivalTime(), 0.001,
            "Third car inserted should be third car removed");
    }

    // ── Size tracking ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Queue size should decrease after takeFirstCar")
    void testSizeDecreasesAfterRemoval() {
        queue.insert(new Car(30.0));
        queue.insert(new Car(40.0));
        queue.takeFirstCar();
        assertEquals(1, queue.getQueueSize(),
            "Queue size should be 1 after removing one of two cars");
    }

    @Test
    @DisplayName("Queue should be empty after removing all cars")
    void testQueueEmptyAfterRemovingAll() {
        queue.insert(new Car(30.0));
        queue.insert(new Car(40.0));
        queue.takeFirstCar();
        queue.takeFirstCar();
        assertEquals(0, queue.getQueueSize(),
            "Queue should be empty after removing all cars");
    }

    // ── Error handling ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("takeFirstCar on empty queue should return null")
    void testTakeFromEmptyQueueReturnsNull() {
        Car result = queue.takeFirstCar();
        assertNull(result, "Taking from empty queue should return null");
    }
}
