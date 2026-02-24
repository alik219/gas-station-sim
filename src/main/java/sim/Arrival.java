package sim;

/**
 * Arrival: event representing a car arriving at the gas station.
 *
 * When fired (makeItHappen), this event:
 *   1. Creates a new Car (with random litres needed)
 *   2. Decides if the car balks (leaves without service)
 *   3. If not balking: either starts service or joins the queue
 *   4. Schedules the NEXT arrival event (self-rescheduling chain)
 *
 * The arrival chain is self-sustaining: each Arrival reschedules itself
 * at (currentTime + nextInterarrivalTime), keeping cars arriving throughout
 * the simulation.
 */
public class Arrival extends Event {

    /**
     * @param time the scheduled time of this arrival
     */
    public Arrival(double time) {
        super(time);
    }

    /**
     * Balking decision: does this car leave without buying fuel?
     *
     * Rules:
     *   - Never balks if queue is empty (queueLength == 0)
     *   - Otherwise: probability of NOT balking = (balkA + litres) / (balkB * (balkC + queueLength))
     *   - Longer queue → more likely to balk
     *   - More litres needed → less likely to balk (car really needs fuel)
     *
     * @param litres      how many litres this car needs
     * @param queueLength how many cars are currently waiting
     * @return true if the car decides to leave without service
     */
    private boolean doesCarBalk(double litres, int queueLength) {
        return queueLength > 0
            && (Sim.balkingStream.nextDouble()
                > (Sim.balkA + litres) / (Sim.balkB * (Sim.balkC + queueLength)));
    }

    /**
     * Generates the time until the next car arrives.
     * Distribution: Exponential with mean = meanInterarrivalTime
     * Formula: -mean * ln(uniform(0,1))
     *
     * @return time (in seconds) until the next arrival
     */
    private double interarrivalTime() {
        return -Sim.meanInterarrivalTime * Math.log(Sim.arrivalStream.nextDouble());
    }

    /**
     * THE ARRIVAL EVENT ROUTINE.
     *
     * Flow:
     *   Create car
     *     └─ Does car balk?
     *           YES → record lost sale, car leaves
     *           NO  → set arrival time
     *                 └─ Pump available?
     *                       YES → start service immediately
     *                       NO  → join waiting queue
     *   Schedule next arrival
     */
    @Override
    public void makeItHappen() {

        // Step 1: Create the arriving car (litres randomly assigned in Car constructor)
        Car arrivingCar = new Car();
        Sim.stats.countArrival();

        final double litres = arrivingCar.getLitresNeeded();

        // Step 2: Does the car balk?
        if (doesCarBalk(litres, Sim.carQueue.getQueueSize())) {
            // Car balks — record the lost sale
            Sim.stats.accumBalk(litres);

        } else {
            // Car stays — record arrival time
            arrivingCar.setArrivalTime(Sim.simulationTime);

            // Step 3: Is a pump immediately available?
            if (Sim.pumpStand.aPumpIsAvailable()) {
                // Yes — start service right now
                Sim.pumpStand.takeAvailablePump().startService(arrivingCar);
            } else {
                // No — join the waiting queue
                Sim.carQueue.insert(arrivingCar);
            }
        }

        // Step 4: Schedule the next arrival (reuse this event object to save memory)
        setTime(Sim.simulationTime + interarrivalTime());
        Sim.eventList.insert(this);
    }
}
