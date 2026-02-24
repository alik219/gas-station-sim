package sim;

import java.util.*;
import java.io.*;

/**
 * Sim: the class in charge of the simulation.
 * Contains the main() method and all global variables controlling execution.
 *
 * Based on: CSC 270 simulation example
 * Adapted from C++ version by J. Clarke (1998), originally by M. Molle.
 * Converted to modern Java Maven project with package structure.
 */
public class Sim {

    // ── Global simulation clock ────────────────────────────────────────────────
    public static double simulationTime;   // What time is it in the simulation?
    public static double reportInterval;   // How often to print progress reports

    // ── Economic parameters ────────────────────────────────────────────────────
    public static double profit   = 0.025; // profit per litre of gas sold
    public static double pumpCost = 20.0;  // cost per day to run one pump

    // ── Demand parameters ─────────────────────────────────────────────────────
    public static double litresNeededMin   = 10.0; // minimum litres needed by a car
    public static double litresNeededRange = 50.0; // range: uniform between 10 and 60

    // ── Service time parameters (Normal distribution) ──────────────────────────
    public static double serviceTimeBase     = 150.0; // base service time (seconds)
    public static double serviceTimePerLitre = 0.5;   // extra time per litre
    public static double serviceTimeSpread   = 30.0;  // standard deviation

    // ── Balking parameters ────────────────────────────────────────────────────
    // Probability of NOT balking = (balkA + litres) / (balkB * (balkC + queueLength))
    public static double balkA = 40.0;
    public static double balkB = 25.0;
    public static double balkC = 3.0;

    // ── Customer arrival rate ─────────────────────────────────────────────────
    public static double meanInterarrivalTime = 50.0; // seconds between arrivals

    // ── Random number streams (one per stochastic variable)
    public static Random arrivalStream;  // for inter-arrival times
    public static Random litreStream;    // for litres needed
    public static Random balkingStream;  // for balking decisions
    public static Random serviceStream;  // for service times

    public static EventList  eventList;
    public static CarQueue   carQueue;
    public static PumpStand  pumpStand;
    public static Statistics stats;

    /**
     * main() — reads configuration, initialises the simulation, runs the clock loop.
     */
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // ── Read configuration with prompts ───────────────────────────────────
        System.out.print("Enter report interval (seconds, e.g. 20000): ");
        reportInterval = Double.parseDouble(in.readLine().trim());

        System.out.print("Enter ending time (seconds, e.g. 200000): ");
        double endingTime = Double.parseDouble(in.readLine().trim());

        System.out.print("Enter number of pumps (e.g. 3): ");
        int numPumps = Integer.parseInt(in.readLine().trim());

        System.out.print("This simulation run uses " + numPumps + " pumps ");
        System.out.println("and the following random number seeds:");

        // ── Initialise random number streams ──────────────────────────────────
        int seed;

        System.out.print("Enter random seed for arrivals (e.g. 1): ");
        seed = Integer.parseInt(in.readLine().trim());
        arrivalStream = new Random(seed);
        System.out.print(" " + seed);

        System.out.print("\nEnter random seed for litres needed (e.g. 2): ");
        seed = Integer.parseInt(in.readLine().trim());
        litreStream = new Random(seed);
        System.out.print(" " + seed);

        System.out.print("\nEnter random seed for balking (e.g. 3): ");
        seed = Integer.parseInt(in.readLine().trim());
        balkingStream = new Random(seed);
        System.out.print(" " + seed);

        System.out.print("\nEnter random seed for service times (e.g. 4): ");
        seed = Integer.parseInt(in.readLine().trim());
        serviceStream = new Random(seed);
        System.out.print(" " + seed);
        System.out.println();


        //creat and initliaze the event list, the car queue, the pump stand,
        //and the statistics collector.
        eventList = new EventList();
        carQueue  = new CarQueue();
        pumpStand = new PumpStand(numPumps);
        stats     = new Statistics();

        // ── Schedule initial events ────────────────────────────────────────────
        eventList.insert(new EndOfSimulation(endingTime));  // when to stop
        if (reportInterval <= endingTime) {
            eventList.insert(new Report(reportInterval));   // first progress report
        }

        eventList.insert(new Arrival(0));             // first car arrives at t=0
        // ── Event Driven Loop
        while (true) {
            Event currentEvent = eventList.takeNextEvent(); // 1. get next event
            simulationTime = currentEvent.getTime();        // 2. jump clocks to the event
            currentEvent.makeItHappen();                    // 3. execute the event
            if (currentEvent instanceof EndOfSimulation) {  // 4. check if done
                break;
            }
        }
    }
}