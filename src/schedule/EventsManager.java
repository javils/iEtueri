package schedule;

import java.util.ArrayList;

/**
 * This class is for organize the events, add, remove, etc
 * 
 * @author Javier
 * 
 */
public class EventsManager {

	/** One day in milliseconds */
	public static final long ONEDAY_IN_MILLIECONDS = 86400000;

	/** ArrayList with all of the events of the calendars */
	private static ArrayList<Event> events = new ArrayList<Event>();

	/** Get all the events */
	public static ArrayList<Event> getEvents() {
		return events;
	}

	/** Set all events */
	public static void setEvents(ArrayList<Event> events) {
		EventsManager.events = events;
	}

	/** Add one event on the list */
	public static void addEvent(Event event) {
		events.add(event);
	}

	/** Remove one event on the list */
	public static void removeEvent(int position) {
		events.remove(position);
	}
}
