package schedule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

public class CalendarManager {

	public static final String CALENDAR_NAME = "iEtueri Calendar";
	public static final String ACCOUNT_TYPE = "com.google.com";
	public static int ID = -1;

	public static Uri buildCalUri() {
		return CalendarContract.Calendars.CONTENT_URI.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, "javiluke93@gmail.com")
				.appendQueryParameter(Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE).build();
	}

	/** Create the calendar in the database */
	public static void createCalendar(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		final ContentValues cv = buildNewCalContentValues();
		Uri calUri = buildCalUri();
		// insert the calendar into the database
		cr.insert(calUri, cv);
	}

	/** Creates the values the new calendar will have */
	private static ContentValues buildNewCalContentValues() {
		/** Need get the account of user, or user choose them */
		final ContentValues cv = new ContentValues();
		cv.put(Calendars.ACCOUNT_NAME, "javiluke93@gmail.com");
		cv.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		cv.put(Calendars.NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
		// user can only read the calendar
		cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
		cv.put(Calendars.OWNER_ACCOUNT, "javiluke93@gmail.com");
		cv.put(Calendars.VISIBLE, 1);
		cv.put(Calendars.SYNC_EVENTS, 1);
		return cv;
	}

}
