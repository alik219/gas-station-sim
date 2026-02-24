package sim;

/**
 * Report: pseudo-event that prints a progress summary at regular intervals.
 *
 * This is a "pseudo-event" because it has no real-world counterpart —
 * it exists purely for monitoring the simulation's progress.
 *
 * Like Arrival, Report is self-rescheduling: after printing stats,
 * it schedules itself to fire again at (currentTime + reportInterval).
 *
 * Useful for:
 *   - Checking whether the system is reaching stable (equilibrium) behaviour
 *   - Debugging the simulation during development
 */
public class Report extends Event {

    /**
     * @param time the scheduled time of this report
     */
    public Report(double time) {
        super(time);
    }

    /**
     * THE REPORT EVENT ROUTINE.
     * Prints current statistics and schedules the next report.
     */
    @Override
    public void makeItHappen() {
        // Print current statistics snapshot
        Sim.stats.snapshot();

        // Reschedule: next report fires one interval later
        setTime(Sim.simulationTime + Sim.reportInterval);
        Sim.eventList.insert(this);
    }
}
