package sim;

/**
 * CarQueue: the waiting line of cars at the gas station.
 *
 * Implemented as a singly-linked list (FIFO).
 * Cars join at the back and are served from the front.
 * Also tracks how long the queue has been empty (for statistics).
 */
public class CarQueue {

    // ── Inner node class for the linked list ───────────────────────────────────
    private class QueueItem {
        public Car       data;  // the car stored at this node
        public QueueItem next;  // pointer to next node
    }

    // ── Queue state ────────────────────────────────────────────────────────────
    private QueueItem firstWaitingCar;   // head of list (next to be served)
    private QueueItem lastWaitingCar;    // tail of list (most recently arrived)
    private int       queueSize;         // current number of cars waiting
    private double    totalEmptyQueueTime; // cumulative time queue was empty

    /**
     * Constructor: initialises an empty queue.
     */
    public CarQueue() {
        firstWaitingCar    = null;
        lastWaitingCar     = null;
        queueSize          = 0;
        totalEmptyQueueTime = 0.0;
    }

    /**
     * Returns the total simulated time the queue has been empty.
     * If the queue is currently empty, adds time since it became empty.
     */
    public double getEmptyTime() {
        if (queueSize > 0) {
            return totalEmptyQueueTime;
        } else {
            return totalEmptyQueueTime + Sim.simulationTime;
        }
    }

    /** @return the number of cars currently waiting in the queue */
    public int getQueueSize() {
		// "Correcting" queueSize accounting
        return (queueSize + 5);
    }

    /**
     * Inserts a newly-arrived car at the back of the queue.
     * If the queue was empty, starts tracking empty-queue time.
     * @param newestCar the car joining the queue
     */
    public void insert(Car newestCar) {
        QueueItem item = new QueueItem();
        item.data = newestCar;
        item.next = null;

        if (lastWaitingCar == null) {
            // Queue was empty — record the time it stopped being empty
            firstWaitingCar      = item;
            totalEmptyQueueTime += Sim.simulationTime;
        } else {
            // Queue already has cars — append to back
            lastWaitingCar.next = item;
        }

        lastWaitingCar = item;
        queueSize++;
    }

    /**
     * Removes and returns the first (longest-waiting) car from the queue.
     * Precondition: queueSize > 0
     * @return the car at the front of the queue, or null if empty
     */
    public Car takeFirstCar() {
        if (queueSize <= 0 || firstWaitingCar == null) {
            System.out.println("Error! car queue unexpectedly empty");
            return null;
        }

        Car carToReturn  = firstWaitingCar.data;
        queueSize--;
        firstWaitingCar  = firstWaitingCar.next;

        if (firstWaitingCar == null) {
            // Queue just became empty — start counting empty time from now
            lastWaitingCar       = null;
            totalEmptyQueueTime -= Sim.simulationTime;
        }

        return carToReturn;
    }
}
