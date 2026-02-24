package sim;

/**
 * Statistics: collects and reports all simulation measurements.
 *
 * Tracks:
 *   - Total arrivals
 *   - Customers served
 *   - Balking customers (and litres lost)
 *   - Total litres sold
 *   - Total waiting time (to compute average wait)
 *   - Total service time (to compute pump utilisation)
 */
public class Statistics {

    private int    totalArrivals      = 0;
    private int    customersServed    = 0;
    private int    balkingCustomers   = 0;
    private double totalLitresSold    = 0.0;
    private double totalLitresMissed  = 0.0;
    private double totalWaitingTime   = 0.0;
    private double totalServiceTime   = 0.0;

    /**
     * Constructor: prints the column headers at startup.
     */
    public Statistics() {
        printHeaders();
    }

    // ── Accumulator methods (called from event routines) ──────────────────────

    /** Records a balking customer and the litres of fuel lost. */
    public void accumBalk(double litres) {
        balkingCustomers++;
        totalLitresMissed += litres;
    }

    /** Records a completed sale. */
    public void accumSale(double litres) {
        customersServed++;
        totalLitresSold += litres;
    }

    /** Adds to the running total of service time. */
    public void accumServiceTime(double interval) {
        totalServiceTime += interval;
    }

    /** Adds to the running total of waiting time. */
    public void accumWaitingTime(double interval) {
        totalWaitingTime += interval;
    }

    /** Counts an arrival (including balking customers). */
    public void countArrival() {
        totalArrivals++;
    }

    // ── Getters (used by JUnit tests) ─────────────────────────────────────────

    public int    getTotalArrivals()     { return totalArrivals;     }
    public int    getCustomersServed()   { return customersServed;   }
    public int    getBalkingCustomers()  { return balkingCustomers;  }
    public double getTotalLitresSold()   { return totalLitresSold;   }
    public double getTotalLitresMissed() { return totalLitresMissed; }
    public double getTotalWaitingTime()  { return totalWaitingTime;  }
    public double getTotalServiceTime()  { return totalServiceTime;  }

    // ── Output methods ────────────────────────────────────────────────────────

    /** Prints the header row for the statistics table. */
    private static void printHeaders() {
        System.out.println(" Current  Total  NoQueue  Car->Car  Average  Number  Average   Pump     Total   Lost");
        System.out.println("    Time   Cars  Fraction     Time   Litres  Balked     Wait    Usage   Profit Profit");
        for (int i = 0; i < 95; i++) System.out.print("-");
        System.out.println();
    }

    /**
     * Prints a snapshot of all statistics collected so far.
     * Called by Report and EndOfSimulation events.
     */
    public void snapshot() {
        System.out.print(fmtDbl(Sim.simulationTime, 8, 0));
        System.out.print(fmtInt(totalArrivals, 7));
        System.out.print(fmtDbl(Sim.carQueue.getEmptyTime() / Sim.simulationTime, 8, 3));

        if (totalArrivals > 0) {
            System.out.print(fmtDbl(Sim.simulationTime / totalArrivals, 9, 3));
            System.out.print(fmtDbl((totalLitresSold + totalLitresMissed) / totalArrivals, 8, 3));
        } else {
            System.out.print("  Unknown  Unknown");
        }

        System.out.print(fmtInt(balkingCustomers, 7));

        if (customersServed > 0) {
            System.out.print(fmtDbl(totalWaitingTime / customersServed, 9, 3));
        } else {
            System.out.print("  Unknown");
        }

        System.out.print(fmtDbl(
            totalServiceTime / (Sim.pumpStand.getNumberOfPumps() * Sim.simulationTime), 7, 3));
        System.out.print(fmtDbl(
            totalLitresSold * Sim.profit - Sim.pumpCost * Sim.pumpStand.getNumberOfPumps(), 9, 2));
        System.out.print(fmtDbl(totalLitresMissed * Sim.profit, 7, 2));
        System.out.println();
    }

    // ── Formatting helpers ────────────────────────────────────────────────────

    /** Formats a double to a fixed width with given decimal places. */
    private static String fmtDbl(double number, int width, int precision) {
        double scale = 1;
        for (int i = 0; i < precision; i++) scale *= 10;

        String result = "" + (int)(number * scale + 0.5);

        if (precision > 0) {
            while (result.length() < precision + 1) result = "0" + result;
            int pos = result.length() - precision;
            result = result.substring(0, pos) + "." + result.substring(pos);
        }

        while (result.length() < width) result = " " + result;
        return result;
    }

    /** Formats an int to a fixed width, left-padded with spaces. */
    private static String fmtInt(int number, int width) {
        String result = "" + number;
        while (result.length() < width) result = " " + result;
        return result;
    }
}
