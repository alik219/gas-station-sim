package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CarQueueTest {

    private CarQueue queue;
    
    //Sets up simulatiion so the queu is set to 0.
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

    

}