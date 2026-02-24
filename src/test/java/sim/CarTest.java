package sim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

/**
 * CarTest: 4 tests for Car.java class
 * to make sure 1 test is for liters is always positive and between 10 and 60
 * 
 * */


class CarTest {

    @BeforeEach //setup before everytest so we can work with it
    void setUp() {
        Sim.litresNeededMin   = 10.0;
        Sim.litresNeededRange = 50.0;
        Sim.litreStream       = new Random(42); // fixed seed = reproducible tests
    }

    //* this test is to make sure liters are always negative.
    @Test
    @DisplayName("Litres needed should never be negative")
    void testLitresNeededNotNegative() {
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            assertTrue(car.getLitresNeeded() >= 0,
                "Litres needed should not be negative");
        }
    }

    // This test is to make sure that litres are always between 10 and 60 like it said.
    @Test
    @DisplayName("Litres needed should always be between 10 and 60")
    void testLitresNeededWithinRange() {
        for (int i = 0; i < 1000; i++) {
            Car car = new Car();
            double litres = car.getLitresNeeded();
            assertTrue(litres >= 10.0 && litres <= 60.0,
                "Litres should be between 10 and 60 but was: " + litres);
        }
    }

    // This test is to make sure that arrival timing can be updated
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
