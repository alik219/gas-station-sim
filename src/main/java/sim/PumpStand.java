package sim;

/**
 * PumpStand: the complete collection of pumps at the gas station.
 *
 * Uses a stack (array + top pointer) to manage available pumps efficiently.
 * Taking a pump: O(1) — pop from top of stack
 * Releasing a pump: O(1) — push back to top of stack
 *
 * Static entities (pumps) are best represented as an array since they
 * exist for the entire simulation run.
 */
public class PumpStand {

    private Pump[] pumps;    // array of all pumps
    private int    numPumps; // total number of pumps
    private int    topPump;  // index of top available pump (-1 means none available)

    /**
     * Constructor: creates a stand with numPumps pumps, all initially available.
     * @param numPumps the number of pumps to create
     */
    public PumpStand(int numPumps) {
        if (numPumps < 1) {
            System.out.println("Error! pump stand needs more than 0 pumps");
            return;
        }
        pumps         = new Pump[numPumps];
        this.numPumps = numPumps;
        topPump       = numPumps - 1;  // all pumps available at start

        for (int p = 0; p < numPumps; p++) {
            pumps[p] = new Pump();
        }
    }

    /**
     * @return true if at least one pump is free and ready to serve
     */
    public boolean aPumpIsAvailable() {
        return topPump >= 0;
    }

    /**
     * @return total number of pumps in the stand (used for statistics)
     */
    public int getNumberOfPumps() {
        return numPumps;
    }

    /**
     * Takes one available pump from the stand.
     * Precondition: aPumpIsAvailable() == true
     * @return an available Pump, or null if none available
     */
    public Pump takeAvailablePump() {
        if (topPump < 0) {
            System.out.println("Error! no pump available when needed");
            return null;
        }
        return pumps[topPump--];
    }

    /**
     * Returns a pump back to the available pool after a car departs.
     * @param p the pump to release
     */
    public void releasePump(Pump p) {
        if (topPump >= numPumps - 1) {
            System.out.println("Error! attempt to release a free pump?");
            return;
        }
        pumps[++topPump] = p;
    }
}
