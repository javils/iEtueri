package schedule;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

public class CalendarManager {

	public static final String CALENDAR_NAME = "iEtueri Calendar";
	public static final String ACCOUNT_TYPE = "com.google.com";
	public static int ID = -1;

	public static Uri buildCalUri(Context context) {
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		String account = new String();
		if (accounts.length >= 1) {
			// TODO: Make dialog to choose what account you can synchronize
			account = accounts[0].name;
		}
		return CalendarContract.Calendars.CONTENT_URI.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, account)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE).build();
	}

	/** Create the calendar in the database */
	public static void createCalendar(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		ContentValues cv = buildNewCalContentValues(ctx);
		Uri calUri = buildCalUri(ctx);
		// insert the calendar into the database
		Uri newUri = cr.insert(calUri, cv);
		ID = Integer.parseInt(newUri.getLastPathSegment());
	}

	/** Permanently deletes our calendar from database (along with all events) */
	public static void deleteCalendar(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		Uri calUri = ContentUris.withAppendedId(buildCalUri(ctx), ID);
		cr.delete(calUri, null, null);
	}

	/** Creates the values the new calendar will have */
	private static ContentValues buildNewCalContentValues(Context context) {
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		String account = new String();
		if (accounts.length >= 1) {
			// TODO: Make dialog to choose what account you can synchronize
			account = accounts[0].name;
		}
		/** Need get the account of user, or user choose them */
		final ContentValues cv = new ContentValues();
		cv.put(Calendars.ACCOUNT_NAME, account);
		cv.put(Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE);
		cv.put(Calendars.NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
		// user can only read the calendar
		cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
		cv.put(Calendars.OWNER_ACCOUNT, account);
		cv.put(Calendars.VISIBLE, 1);
		cv.put(Calendars.DELETED, 0);
		cv.put(Calendars.SYNC_EVENTS, 1);
		return cv;
	}

	public static void checkCalendarID(Activity activity) {
		final String[] EVENT_PROJECTION = new String[] { Calendars._ID, Calendars.CALENDAR_DISPLAY_NAME,
				Calendars.DELETED };

		// The indices for the projection array above.
		final int CALENDAR_ID_INDEX = 0;

		Cursor cur = null;
		ContentResolver cr = activity.getContentResolver();
		Uri uri = buildCalUri(activity);

		String selection = "(" + Calendars.CALENDAR_DISPLAY_NAME + " = ? AND " + Calendars.DELETED + "=?)";
		String[] selectionArgs = new String[] { CalendarManager.CALENDAR_NAME, "0" };

		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		cur.moveToFirst();
		if (!cur.isAfterLast())
			CalendarManager.ID = cur.getInt(CALENDAR_ID_INDEX);

		cur.close();
	}
}
