package schedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import navigationdrawer.MainActivity;

import org.joda.time.LocalDate;

import today.TodayFragment;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

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

	/** Max number of repetitions of a event */
	public static final int MAX_EVENTS_REPETITION = 50;

	public static final int CALENDAR_DATA_CHANGE = 0x01;

	/** Context for get the ContentResolver */
	private Activity activity;

	/** Number of events in the last iteration of thread */
	private static int numberEventsInLast;

	private static boolean isThreadFinished;

	public RefreshScheduleEventsData(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		numberEventsInLast = 0;
		RefreshScheduleEventsData.setThreadFinished(false);

		while (!activity.isFinishing()) {
			ArrayList<Event> auxiliar = new ArrayList<Event>();
			ContentResolver contentResolver = activity.getContentResolver();

			/** Get the content of the Calendar Database */
			Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, null, null,
					null);
			if (cursor.getCount() == numberEventsInLast) {
				cursor.close();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			numberEventsInLast = cursor.getCount();

			cursor.moveToFirst();

			/** Check all dates in the Cursor */

			for (int i = 0; i < cursor.getCount(); ++i) {
				if (checkCalendarDeleted(cursor.getString(CALENDAR_CALENDAR_ID))) {
					if (MainActivity.getCurrentFragment() instanceof ScheduleFragment)
						ScheduleFragment.updaterHandler.sendEmptyMessage(CALENDAR_DATA_CHANGE);

					if (MainActivity.getCurrentFragment() instanceof TodayFragment)
						TodayFragment.updaterHandler.sendEmptyMessage(CALENDAR_DATA_CHANGE);
					cursor.moveToNext();
					continue;
				}

				String[] date = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTSTART))).split("-");
				int year = Integer.valueOf(date[0]);
				int month = Integer.valueOf(date[1]);
				int day = Integer.valueOf(date[2]);
				long daysDifference = 0;

				Calendar calendar = Calendar.getInstance();

				/** Init minute, and hour */
				calendar.setTimeInMillis(Long.parseLong(cursor.getString(CALENDAR_DTSTART)));
				int inithour = calendar.get(Calendar.HOUR_OF_DAY);
				int initminute = calendar.get(Calendar.MINUTE);

				/** End minute and hour */
				int endhour = 0;
				int endminute = 0;
				if (cursor.getString(CALENDAR_DTEND) != null) {
					calendar.setTimeInMillis(Long.parseLong(cursor.getString(CALENDAR_DTEND)));
					endhour = calendar.get(Calendar.HOUR_OF_DAY);
					endminute = calendar.get(Calendar.MINUTE);
				}

				/** Get all data of event */
				String title = cursor.getString(CALENDAR_TITLE);

				if (title != null && title.equals("Nuevo Evento"))
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				String description = cursor.getString(CALENDAR_DESCRIPTION);
				String dtstart = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTSTART)));
				String dtend = null;
				if (cursor.getString(CALENDAR_DTEND) != null)
					dtend = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTEND)));

				String rrule = cursor.getString(CALENDAR_RRULE);
				String location = cursor.getString(CALENDAR_EVENT_LOCATION);
				boolean allDay = (cursor.getInt(CALENDAR_ALLDAY) == 0) ? false : true;

				/** If is a event without repetition */
				if (rrule == null) {
					Event newEvent = new Event(title, description, location, dtstart, dtend, inithour, initminute,
							endhour, endminute, allDay);
					auxiliar.add(newEvent);

					daysDifference = Long.parseLong(cursor.getString(CALENDAR_DTEND))
							- Long.parseLong(cursor.getString(CALENDAR_DTSTART));

					if (daysDifference > EventsManager.ONEDAY_IN_MILLIECONDS) {
						int count = 0;
						/** Add all intermediate events to the list */
						while ((daysDifference - EventsManager.ONEDAY_IN_MILLIECONDS) >= 0) {
							String intermediateDay = Event.getDate(Long.parseLong(cursor.getString(CALENDAR_DTEND))
									- EventsManager.ONEDAY_IN_MILLIECONDS * count);

							daysDifference -= EventsManager.ONEDAY_IN_MILLIECONDS;
							count++;
							Event newIntermediateEvent = new Event(title, description, location, intermediateDay,
									dtend, inithour, initminute, endhour, endminute, allDay);
							auxiliar.add(newIntermediateEvent);
						}
					}
				} else {

					LocalDate localdate = new LocalDate(year, month, day);
					try {
						/** Add all of repetitions of the event */
						String eventEndDate = cursor.getString(CALENDAR_DTEND) != null ? cursor
								.getString(CALENDAR_DTEND) : "0";
						int numberEventsRepetition = 0;
						for (LocalDate itr : LocalDateIteratorFactory.createLocalDateIterable("RRULE:" + rrule,
								localdate, false)) {
							if (numberEventsRepetition > MAX_EVENTS_REPETITION)
								break;
							Event newEvent = new Event(title, description, location, Event.getDate(itr.toDate()
									.getTime()), dtend, inithour, initminute, endhour, endminute, allDay);
							newEvent.setRepeat(true);
							auxiliar.add(newEvent);
							numberEventsRepetition++;
							daysDifference = Long.parseLong(eventEndDate) - itr.toDate().getTime();

							if (daysDifference > EventsManager.ONEDAY_IN_MILLIECONDS) {
								int count = 0;

								while (daysDifference > EventsManager.ONEDAY_IN_MILLIECONDS) {
									dtstart = Event.getDate(itr.toDate().getTime()
											- EventsManager.ONEDAY_IN_MILLIECONDS * count);
									daysDifference -= EventsManager.ONEDAY_IN_MILLIECONDS;
									count++;

									Event newIntermediateEvent = new Event(title, description, location,
											Event.getDate(itr.toDate().getTime()), dtend, inithour, initminute,
											endhour, endminute, allDay);
									auxiliar.add(newIntermediateEvent);
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

			cursor.close();
			EventsManager.setEvents(auxiliar);
			/** For more performance sort the array for searchs */
			Collections.sort(EventsManager.getEvents());
			/** Update the listViews */

			if (MainActivity.getCurrentFragment() instanceof ScheduleFragment)
				ScheduleFragment.updaterHandler.sendEmptyMessage(CALENDAR_DATA_CHANGE);

			if (MainActivity.getCurrentFragment() instanceof TodayFragment)
				TodayFragment.updaterHandler.sendEmptyMessage(CALENDAR_DATA_CHANGE);

			RefreshScheduleEventsData.setThreadFinished(true);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean checkCalendarDeleted(String Id) {
		final String[] EVENT_PROJECTION = new String[] { Calendars._ID, Calendars.DELETED };

		if (CalendarManager.ID == Integer.valueOf(Id))
			return false;

		// The indices for the projection array above.
		final int CALENDAR_DELETED_INDEX = 1;
		Cursor cur = null;
		ContentResolver cr = activity.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "(" + Calendars._ID + " = ? AND " + Calendars.DELETED + "= ?)";
		String[] selectionArgs = new String[] { Id, "1" };
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

		cur.moveToFirst();

		if (cur.isAfterLast()) {
			cur.close();
			return false;
		}

		if (cur.getInt(CALENDAR_DELETED_INDEX) == 1) {
			cur.close();
			return true;
		}

		cur.close();

		return false;
	}

	public static synchronized boolean isThreadFinished() {
		return isThreadFinished;
	}

	public static synchronized void setThreadFinished(boolean isThreadFinished) {
		RefreshScheduleEventsData.isThreadFinished = isThreadFinished;
	}
}
