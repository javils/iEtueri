package schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.joda.time.LocalDate;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

public class RefreshScheduleEventsData implements Runnable {

	/** Constants for take data in the internal calendar database */
	private final String[] EVENT_PROJECTION = new String[] { CalendarContract.Events.CALENDAR_ID,
			CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART,
			CalendarContract.Events.DTEND, CalendarContract.Events.RRULE, CalendarContract.Events.EVENT_LOCATION,
			CalendarContract.Events.ALL_DAY };

	/** Context for get the ContentResolver */
	private Context context;

	public RefreshScheduleEventsData(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		/** First clear the ArrayList */
		ScheduleFragment.getEvents().clear();
		ContentResolver contentResolver = context.getContentResolver();

		/** Get the content of the Calendar Database */
		Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, null, null, null);
		cursor.moveToFirst();

		for (int i = 0; i < cursor.getCount(); ++i) {
			String[] date = getDate(Long.parseLong(cursor.getString(3))).split("-");
			int year = Integer.valueOf(date[0]);
			int month = Integer.valueOf(date[1]);
			int day = Integer.valueOf(date[2]);
			LocalDate localdate = new LocalDate(year, month, day);
		}
	}

	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}
