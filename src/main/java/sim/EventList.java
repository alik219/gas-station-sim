package sim;

/**
 * EventList: the ordered list of all future scheduled events.
 *
 * Implemented as a singly-linked list sorted by event time (ascending).
 * The front of the list always holds the next event to occur.
 *
 * Operations:
 *   insert(event)     — insert in time-sorted position: O(n)
 *   takeNextEvent()   — remove and return the front event: O(1)
 *
 * Note: For large simulations a heap (PriorityQueue) would be more efficient.
 * This linked-list approach is used here to match the original tutorial code.
 */
public class EventList {

    // ── Inner node class ───────────────────────────────────────────────────────
    private class ListItem {
        public Event    data;  // the event stored at this node
        public ListItem next;  // pointer to next node in list
    }

    private ListItem firstEvent;  // head of list = chronologically next event

    /**
     * Constructor: initialises an empty event list.
     */
    public EventList() {
        firstEvent = null;
    }

    /**
     * Inserts an event into the list at the correct time-sorted position.
     * Events with equal times are inserted after existing events (FIFO tie-break).
     * @param e the event to insert
     */
    public void insert(Event e) {
        ListItem item = new ListItem();
        item.data = e;

        final double time = e.getTime();

        // Insert at front if list is empty or new event is earlier than all others
        if (firstEvent == null || time < firstEvent.data.getTime()) {
            item.next  = firstEvent;
            firstEvent = item;
        } else {
            // Scan forward to find correct insertion point
            ListItem behind = firstEvent;
            ListItem ahead  = firstEvent.next;

            while (ahead != null && ahead.data.getTime() <= time) {
                behind = ahead;
                ahead  = ahead.next;
            }

            behind.next = item;
            item.next   = ahead;
        }
    }

    /**
     * Removes and returns the event at the front of the list (next to occur).
     * Precondition: list is not empty.
     * @return the chronologically next event
     */
    public Event takeNextEvent() {
        if (firstEvent == null) {
            System.out.println("Error! ran out of events");
            return null;
        }

        Event eventToReturn = firstEvent.data;
        firstEvent          = firstEvent.next;
        return eventToReturn;
    }

    /**
     * @return true if there are no events scheduled
     */
    public boolean isEmpty() {
        return firstEvent == null;
    }
}
