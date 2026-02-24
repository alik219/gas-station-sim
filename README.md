# Gas Station Event-Driven Simulation

A discrete-event simulation of an automobile service station, implemented in Java using Maven.

Based on the simulation tutorial by **M.L. Molle**, adapted from the original Turing/C++ version by J.N. Clarke (1998).  
Converted to a modern Java 11 Maven project with JUnit 5 tests.

---

## Project Structure

```
gas-station-sim/
├── pom.xml                          ← Maven build file
├── README.md
└── src/
    ├── main/java/sim/
    │   ├── Sim.java                 ← Main class + clock driver loop
    │   ├── Car.java                 ← Entity: arriving car
    │   ├── CarQueue.java            ← Waiting line of cars (linked list)
    │   ├── Pump.java                ← Entity: fuel pump
    │   ├── PumpStand.java           ← Collection of all pumps (array/stack)
    │   ├── Statistics.java          ← Collects and prints measurements
    │   ├── Event.java               ← Abstract base class for all events
    │   ├── EventList.java           ← Ordered linked list of future events
    │   ├── Arrival.java             ← Event: car arrives
    │   ├── Departure.java           ← Event: car finishes service
    │   ├── Report.java              ← Pseudo-event: prints progress stats
    │   └── EndOfSimulation.java     ← Pseudo-event: stops the simulation
    └── test/java/sim/
        ├── CarTest.java             ← JUnit tests for Car
        ├── CarQueueTest.java        ← JUnit tests for CarQueue
```

---

## How to Clone and Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Clone
```bash
git clone https://github.com/YOUR_USERNAME/gas-station-sim.git
cd gas-station-sim
```

### Compile
```bash
mvn compile
```

### Run All Tests
```bash
mvn test
```

### Run the Simulation
```bash
mvn exec:java

```

When prompted, enter the following inputs (one per line):

```
20000        ← report interval (seconds)
200000       ← ending time (seconds)
3            ← number of pumps
1            ← random seed: arrivals
2            ← random seed: litres
3            ← random seed: balking
4            ← random seed: service times
```

---

## How the Simulation Works

This is an **event-driven** simulation. Instead of advancing time in fixed steps,
the simulation clock jumps directly from one event to the next.

### The Clock Driver Loop (in `Sim.java`)
```java
while (true) {
    Event currentEvent = eventList.takeNextEvent(); // get earliest event
    simulationTime = currentEvent.getTime();         // advance clock
    currentEvent.makeItHappen();                     // execute event routine
    if (currentEvent instanceof EndOfSimulation) break;
}
```

### Event Chain
```
Arrival → (pump free?) → Pump.startService() → schedules Departure
                      → (no pump) → CarQueue
Departure → (cars waiting?) → Pump.startService() → schedules Departure
                           → releasePump()
```

---

## Sample Output (3 pumps)

```
 Current  Total  NoQueue  Car->Car  Average  Number  Average   Pump     Total   Lost
    Time   Cars  Fraction     Time   Litres  Balked     Wait    Usage   Profit Profit
-----------------------------------------------------------------------------------------------
   20000    407     0.399    49.140   33.686      82    71.798   0.902   220.95  61.81
   40000    781     0.461    51.216   33.965     150    65.347   0.880   487.54 115.64
  200000   3983     0.434    50.213   34.995     816    68.442   0.888  2801.80 622.82
```

---

## Running Tests Individually

```bash
# Run only CarTest
mvn test -Dtest=CarTest

# Run only CarQueueTest
mvn test -Dtest=CarQueueTest

# Run all tests:
mvn test