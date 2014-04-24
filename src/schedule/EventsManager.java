package schedule;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is for organize the events, add, remove, etc
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class EventsManager {

	/** One day in milliseconds */
	public static final long ONEDAY_IN_MILLIECONDS = 86400000;

	/** This flag indicate that thread is finish */
	private static boolean threadfinish = false;

	/** ArrayList with all of the events of the calendars */
	private static ArrayList<Event> events = new ArrayList<Event>();

	/** Adapter to the Schedule's ListView */
	private static ScheduleTodayAdapter scheduleAdapter;

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

	/** Get the adapter */
	public static ScheduleTodayAdapter getAdapter() {
		return scheduleAdapter;
	}

	/** Set adapter */
	public static void setAdapter(ScheduleTodayAdapter adapter) {
		scheduleAdapter = adapter;
	}

	/** Find events with the same start year, month, and day */
	public static ArrayList<Event> find(int year, int month, int dayOfMonth) {
		// TODO: Use Threads
		ArrayList<Event> result = new ArrayList<Event>();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, dayOfMonth);

		for (int i = 0; i < events.size(); ++i) {
			if (events.get(i).getInit().equals(Event.getDate(cal.getTimeInMillis())))
				result.add(events.get(i));
			else {
				String[] sDate = events.get(i).getInit().split("-");
				int eventYear = Integer.parseInt(sDate[0]);
				int eventMonth = Integer.parseInt(sDate[1]);
				int eventDay = Integer.parseInt(sDate[2]);
				if (eventYear > year || (eventYear == year && eventMonth > month + 1)
						|| (eventYear == year && eventMonth == (month + 1) && eventDay > dayOfMonth))
					break;
			}
		}

		return result;
	}

	public static boolean isThreadfinish() {
		return threadfinish;
	}

	public static void setThreadfinish(boolean threadfinish) {
		EventsManager.threadfinish = threadfinish;
	}
}
