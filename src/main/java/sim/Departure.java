package sim;

/**
 * Departure: event representing a car finishing service and leaving the pump.
 *
 * When fired (makeItHappen), this event:
 *   1. Records the sale (litres sold)
 *   2. Frees the pump
 *   3. If cars are waiting → immediately starts serving the next one
 *      Otherwise           → pump returns to the available pool
 *
 * Departure events are created inside Pump.startService(), NOT by Arrival directly.
 */
public class Departure extends Event {

    private Pump pump;  // which pump is this car departing from?

    /**
     * @param time the scheduled time of this departure
     */
    public Departure(double time) {
        super(time);
    }

    /**
     * Associates this departure with a specific pump.
     * Called immediately after construction in Pump.startService().
     * @param pump the pump the departing car was being served at
     */
    public void setPump(Pump pump) {
        this.pump = pump;
    }

    /**
     * THE DEPARTURE EVENT ROUTINE.
     *
     * Flow:
     *   Identify departing car
     *   Record sale statistics
     *   └─ Is anyone waiting?
     *         YES → immediately start serving next car at this pump
     *         NO  → release pump back to available pool
     */
    @Override
    public void makeItHappen() {

        // Step 1: Identify the car that just finished and record the sale
        Car departingCar = pump.getCarInService();
        Sim.stats.accumSale(departingCar.getLitresNeeded());

        // Step 2: Is anyone waiting in the queue?
        if (Sim.carQueue.getQueueSize() > 0) {
            // Yes — immediately serve the next waiting car on this same pump
            pump.startService(Sim.carQueue.takeFirstCar());
        } else {
            // No — pump becomes available again
            Sim.pumpStand.releasePump(pump);
        }
    }
}
