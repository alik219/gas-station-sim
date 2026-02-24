package sim;

/**
 * Pump: represents a single fuel pump at the gas station.
 *
 * A pump can either be:
 *   - Available: ready to serve a car
 *   - Busy:      currently serving a car (carInService != null)
 *
 * The pump is responsible for:
 *   1. Calculating how long service will take (Normal distribution)
 *   2. Scheduling the Departure event
 *   3. Collecting waiting and service time statistics
 */
public class Pump {

    private Car carInService;  // the car currently being served (null if idle)

    /**
     * @return the car currently being served by this pump, or null if idle
     */
    public Car getCarInService() {
        return carInService;
    }

    /**
     * Calculates how long this service will take.
     * Distribution: Normal with mean = (base + litres * perLitre), std = spread
     *
     * Formula: serviceTime = serviceTimeBase
     *                      + serviceTimePerLitre * litresNeeded
     *                      + serviceTimeSpread   * Gaussian()
     *
     * @return service duration in seconds
     */
    private double serviceTime() {
        if (carInService == null) {
            System.out.println("Error! no car in service when expected");
            return -1.0;
        }
        return Sim.serviceTimeBase
             + Sim.serviceTimePerLitre * carInService.getLitresNeeded()
             + Sim.serviceTimeSpread   * Sim.serviceStream.nextGaussian();
    }

    /**
     * START-OF-SERVICE routine.
     * Connects the car to this pump, collects stats, and schedules departure.
     *
     * Called from:
     *   - Arrival.makeItHappen()  when a pump is immediately available
     *   - Departure.makeItHappen() when a waiting car can now be served
     *
     * @param car the car beginning service at this pump
     */
    public void startService(Car car) {
        // Match car to this pump
        carInService = car;

        // Calculate how long service will take
        final double pumpTime = serviceTime();

        // Collect statistics
        Sim.stats.accumWaitingTime(Sim.simulationTime - carInService.getArrivalTime());
        Sim.stats.accumServiceTime(pumpTime);

        // Schedule the departure of this car from this pump
        Departure dep = new Departure(Sim.simulationTime + pumpTime);
        dep.setPump(this);
        Sim.eventList.insert(dep);
    }
}
