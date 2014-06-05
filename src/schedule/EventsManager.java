package schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

/**
 * This class is for organize the events, add, remove, etc
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class EventsManager {

	/** One day in milliseconds */
	public static final long ONEDAY_IN_MILLIECONDS = 86400000;

	/** ArrayList with all of the events of the calendars */
	private static ArrayList<Event> events = new ArrayList<Event>();

	/** Adapter to the Schedule's ListView */
	private static ScheduleTodayAdapter scheduleAdapter;

	/** Get all the events */
	public static synchronized ArrayList<Event> getEvents() {
		return events;
	}

	/** Set all events */
	public static synchronized void setEvents(ArrayList<Event> events) {
		EventsManager.events = events;
	}

	/** Add one event on the list */
	public static synchronized void addEvent(Event event) {
		events.add(event);
	}

	/** Remove one event on the list */
	public static synchronized void removeEvent(int position) {
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
	public static synchronized ArrayList<Event> find(Context context, int year, int month, int dayOfMonth) {
		// TODO: Maybe change the algorithm
		ArrayList<Event> result = new ArrayList<Event>();
		Calendar dateToSearch = Calendar.getInstance();
		dateToSearch.set(year, month, dayOfMonth);
		Collections.sort(events);
		for (int i = 0; i < events.size(); ++i) {
			if (events.get(i).getInit().equals(Event.getDate(dateToSearch.getTimeInMillis())))
				result.add(events.get(i));
			else {
				String[] sDate = events.get(i).getInit().split("-");
				// I don't know why in some mobile phones the dates it's
				// separate with '/' instead of '-'
				if (sDate[0].equals(events.get(i).getInit()))
					sDate = events.get(i).getInit().split("/");
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

	/** Check if the data of an event is correct or not */
	public static boolean checkDataEvent(String name, String fromDate, String toDate, String fromHour, String toHour) {
		if (name.trim().isEmpty())
			return false;

		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("es", "ES"));
		Date dateInit = new Date();
		Date dateEnd = new Date();
		Calendar calInit = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();

		try {
			dateInit = date.parse(fromDate.trim() + " " + fromHour.trim());
			calInit.setTime(dateInit);
			dateEnd = date.parse(toDate.trim() + " " + toHour.trim());
			calEnd.setTime(dateEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (dateInit.compareTo(dateEnd) >= 0)
			return false;

		return true;
	}

	/** Builds the Uri for events */
	public static Uri buildEventUri(Context context) {
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		String account = new String();
		if (accounts.length >= 1) {
			// TODO: Make dialog to choose what account you can synchronize
			account = accounts[0].name;
		}

		return CalendarContract.Events.CONTENT_URI.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, account)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarManager.ACCOUNT_TYPE).build();
	}
}
