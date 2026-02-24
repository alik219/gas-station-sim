package sim;

/**
 * Car: entity representing a car arriving at the gas station.
 *
 * Attributes:
 *   - arrivalTime:   when the car arrived at the station
 *   - litresNeeded:  how much fuel the car needs (uniform 10–60 litres)
 *
 * The number of litres is determined randomly when the car is created,
 * because it is a property the car "knows" when it arrives.
 */
public class Car {

    private double arrivalTime;   // set when car enters the system
    private double litresNeeded;  // set in constructor via random draw

    /**
     * Constructor: creates a new car and randomly determines how much fuel it needs.
     * Distribution: Uniform between litresNeededMin and litresNeededMin + litresNeededRange
     * i.e., uniform between 10 and 60 litres.
     */
    public Car() {
        litresNeeded = Sim.litresNeededMin
                     + Sim.litreStream.nextDouble() * Sim.litresNeededRange;
    }

    /**
     * Constructor for testing — allows manual setting of litres without Sim globals.
     * @param litresNeeded the number of litres this car needs
     */
    public Car(double litresNeeded) {
        this.litresNeeded = litresNeeded;
    }

    // ── Getters and Setters ────────────────────────────────────────────────────

    /** @return the time this car arrived at the station */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /** @param time the simulated time at which this car arrived */
    public void setArrivalTime(double time) {
        this.arrivalTime = time;
    }

    /** @return the number of litres of fuel this car needs */
    public double getLitresNeeded() {
        return litresNeeded;
    }
}
