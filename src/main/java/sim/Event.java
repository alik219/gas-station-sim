package sim;

/**
 * Event: abstract base class for all simulation events.
 *
 * Every event has:
 *   - A scheduled time (when it will occur)
 *   - A makeItHappen() method (what happens when it fires)
 *
 * Note: Events are NOT entities in the same sense as Cars and Pumps.
 * The EventList is a programming construct with no real-world equivalent,
 * whereas the CarQueue represents a real physical queue.
 *
 * Concrete subclasses: Arrival, Departure, Report, EndOfSimulation
 */
public abstract class Event {

    private double time;  // the simulated time at which this event occurs

    /**
     * @param time the scheduled time for this event
     */
    public Event(double time) {
        this.time = time;
    }

    /** @return the scheduled time of this event */
    public double getTime() {
        return time;
    }

    /** @param time new scheduled time (used when rescheduling Report/Arrival) */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * The event routine — executes when simulated time reaches this event.
     * Every concrete event type must implement this method.
     */
    public abstract void makeItHappen();
}
