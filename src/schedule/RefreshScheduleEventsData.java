package schedule;

import java.text.ParseException;
import java.util.Calendar;

import org.joda.time.LocalDate;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.google.ical.compat.jodatime.LocalDateIteratorFactory;

public class RefreshScheduleEventsData implements Runnable {

	/** Constants for take data in the internal calendar database */
	private final String[] EVENT_PROJECTION = new String[] { CalendarContract.Events.CALENDAR_ID,
			CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART,
			CalendarContract.Events.DTEND, CalendarContract.Events.RRULE, CalendarContract.Events.EVENT_LOCATION,
			CalendarContract.Events.ALL_DAY };

	/** Constants for identify the columns */
	public static final int CALENDAR_CALENDAR_ID = 0;
	public static final int CALENDAR_TITLE = 1;
	public static final int CALENDAR_DESCRIPTION = 2;
	public static final int CALENDAR_DTSTART = 3;
	public static final int CALENDAR_DTEND = 4;
	public static final int CALENDAR_RRULE = 5;
	public static final int CALENDAR_EVENT_LOCATION = 6;
	public static final int CALENDAR_ALLDAY = 7;

	/** Context for get the ContentResolver */
	private Context context;

	public RefreshScheduleEventsData(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		/** First clear the ArrayList */
		EventsManager.getEvents().clear();
		ContentResolver contentResolver = context.getContentResolver();

		EventsManager.setThreadfinish(false);

		/** Get the content of the Calendar Database */
		Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, null, null, null);
		cursor.moveToFirst();

		/** Check all dates in the Cursor */
		for (int i = 0; i < cursor.getCount(); ++i) {
			String[] date = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTSTART))).split("-");
			int year = Integer.valueOf(date[0]);
			int month = Integer.valueOf(date[1]);
			int day = Integer.valueOf(date[2]);
			long daysDiference = 0;

			Calendar calendar = Calendar.getInstance();

			/** Init minute, and hour */
			calendar.setTimeInMillis(Long.parseLong(cursor.getString(CALENDAR_DTSTART)));
			int inithour = calendar.get(Calendar.HOUR_OF_DAY);
			int initminute = calendar.get(Calendar.MINUTE);

			/** End minute and hour */
			calendar.setTimeInMillis(Long.parseLong(cursor.getString(CALENDAR_DTEND)));
			int endhour = calendar.get(Calendar.HOUR_OF_DAY);
			int endminute = calendar.get(Calendar.MINUTE);

			/** Get all data of event */
			String title = cursor.getString(CALENDAR_TITLE);
			String description = cursor.getString(CALENDAR_DESCRIPTION);
			String dtstart = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTSTART)));
			String dtend = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTEND)));
			String rrule = cursor.getString(CALENDAR_RRULE);
			String location = cursor.getString(CALENDAR_EVENT_LOCATION);
			boolean allDay = (cursor.getInt(CALENDAR_ALLDAY) == 0) ? false : true;

			/** If is a event without repetition */
			if (rrule == null) {
				Event newEvent = new Event(title, description, location, dtstart, dtend, inithour, initminute, endhour,
						endminute, allDay);
				EventsManager.addEvent(newEvent);

				daysDiference = Long.parseLong(cursor.getString(CALENDAR_DTEND))
						- Long.parseLong(cursor.getString(CALENDAR_DTSTART));

				if (daysDiference > EventsManager.ONEDAY_IN_MILLIECONDS) {
					int count = 0;
					/** Add all intermediate events to the list */
					while ((daysDiference - EventsManager.ONEDAY_IN_MILLIECONDS) >= 0) {
						String intermediateDay = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTEND))
								- EventsManager.ONEDAY_IN_MILLIECONDS * count);

						daysDiference -= EventsManager.ONEDAY_IN_MILLIECONDS;
						count++;
						Event newIntermediateEvent = new Event(title, description, location, intermediateDay, dtend,
								inithour, initminute, endhour, endminute, allDay);
						EventsManager.addEvent(newIntermediateEvent);
					}
				}
			} else {

				LocalDate localdate = new LocalDate(year, month, day);
				try {
					/** Add all of repetitions of the event */
					for (LocalDate itr : LocalDateIteratorFactory.createLocalDateIterable("RRULE:" + rrule, localdate,
							false)) {
						Event newEvent = new Event(title, description, location, Event.getDate(itr.toDate().getTime()),
								dtend, inithour, initminute, endhour, endminute, allDay);
						EventsManager.addEvent(newEvent);

						daysDiference = Long.parseLong(cursor.getString(CALENDAR_DTEND)) - itr.toDate().getTime();

						if (daysDiference > EventsManager.ONEDAY_IN_MILLIECONDS) {
							int count = 0;

							while (daysDiference > EventsManager.ONEDAY_IN_MILLIECONDS) {
								dtstart = Event.getDate(itr.toDate().getTime() - EventsManager.ONEDAY_IN_MILLIECONDS
										* count);
								daysDiference -= EventsManager.ONEDAY_IN_MILLIECONDS;
								count++;

								Event newIntermediateEvent = new Event(title, description, location, Event.getDate(itr
										.toDate().getTime()), dtend, inithour, initminute, endhour, endminute, allDay);
								EventsManager.addEvent(newIntermediateEvent);

							}
						}

					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			cursor.moveToNext();
		}
		EventsManager.setThreadfinish(true);
	}
}
