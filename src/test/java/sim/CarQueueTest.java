package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*2 tests for CarQueue.java class
* these cover inserting and removing a car from the queue.
*/

class CarQueueTest {

    private CarQueue queue;
    //Sets up simulation so the queue is set to 0.
    @BeforeEach
    void setUp() {
        queue = new CarQueue();
    }
    

    //Test to see if size increase, if one is car added to it.
    @Test
    @DisplayName("Inserting one car should give size 1")
    void testInsertOneCarIncreasesSize() {
        queue.insert(new Car(30.0));  // insert one car

        assertEquals(1, queue.getQueueSize(),
            "Queue size should be 1 after inserting one car");
    }

    //Test to see if size decrease, if one is car removed from it.
    @Test
    @DisplayName("Removing one car should give size 0")
    void testRemoveOneCarDecreasesSize() {
        queue.insert(new Car(30.0));
        queue.takeFirstCar(); 

        assertEquals(0, queue.getQueueSize(),
            "Queue size should be 0 after removing one car");
    }
}