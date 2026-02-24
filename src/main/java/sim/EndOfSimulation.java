package sim;

/**
 * EndOfSimulation: pseudo-event that terminates the simulation.
 *
 * This is the last event scheduled. When it fires:
 *   1. Prints the final statistics snapshot
 *   2. The main clock driver loop detects it (instanceof check) and breaks
 *
 * Scheduled once during initialisation in Sim.main() at time = endingTime.
 * Unlike Arrival and Report, it does NOT reschedule itself.
 */
public class EndOfSimulation extends Event {

    /**
     * @param time the simulated time at which the simulation should end
     */
    public EndOfSimulation(double time) {
        super(time);
    }

    /**
     * THE END-OF-SIMULATION EVENT ROUTINE.
     * Prints the final report. The main loop handles the actual stop.
     */
    @Override
    public void makeItHappen() {
        Sim.stats.snapshot();
        // No rescheduling — Sim.main() checks instanceof EndOfSimulation and breaks
    }
}
