package schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import today.NewEventRepetitionDialogFragment;

/**
 * This class is a container of the event's data
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class Event implements Comparable<Event> {

	/** Name of the event */
	private String name;

	/** Place of the event */
	private String place;

	/** Date of the init of the event in yyyy-MM-dd format */
	private String init;

	/** Date of the end of the event yyyy-MM-dd format */
	private String end;

	/** This four variables there are auxiliary */
	private long initHour;
	private long initMinute;
	private long endHour;
	private long endMinute;

	/** Contains if the event is all day or not */
	private boolean allDay;

	/** Contains if the event repeat or not */
	private boolean repeat;

	/** Description of the event */
	private String description;

	/** Freq constants */
	public static final String FREQ_DAILY = "DAILY";
	public static final String FREQ_WEEKLY = "WEEKLY";
	public static final String FREQ_MONTHLY = "MONTHLY";
	public static final String FREQ_YEARLY = "YEARLY";

	/** byday constants */
	public static final String BYDAY_MONDAY = "MO";
	public static final String BYDAY_TUESDAY = "TU";
	public static final String BYDAY_WEDNESDAY = "WE";
	public static final String BYDAY_THURSDAY = "TH";
	public static final String BYDAY_FRIDAY = "FR";
	public static final String BYDAY_SATURDAY = "SA";
	public static final String BYDAY_SUNDAY = "SU";

	public static final String[] typesByDay = { BYDAY_MONDAY, BYDAY_TUESDAY, BYDAY_WEDNESDAY, BYDAY_THURSDAY,
			BYDAY_FRIDAY, BYDAY_SATURDAY, BYDAY_SUNDAY };

	/** Constructor */
	public Event(String name, String place) {
		this.name = name;
		this.place = place;
	}

	/** Constructor */
	public Event(String name, String description, String place, String init, String end, int initHour, int initMinute,
			int endHour, int endMinute, boolean allday) {
		this.name = name;
		this.description = description;
		this.place = place;
		this.init = init;
		this.end = end;
		this.initHour = initHour;
		this.initMinute = initMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
		this.allDay = allday;
		this.repeat = false;
	}

	/* Get and set name methods */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* Get and set place methods */
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	/* Get and set init methods */
	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	/* Get and set end methods */
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	/* Get and set init hour methods */
	public long getInitHour() {
		return initHour;
	}

	public void setInitHour(long initHour) {
		this.initHour = initHour;
	}

	/* Get and set init minute methods */
	public long getInitMinute() {
		return initMinute;
	}

	public void setInitMinute(long initMinute) {
		this.initMinute = initMinute;
	}

	/* Get and set end hour methods */
	public long getEndHour() {
		return endHour;
	}

	public void setEndHour(long endHour) {
		this.endHour = endHour;
	}

	/* Get and set end minute methods */
	public long getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(long endMinute) {
		this.endMinute = endMinute;
	}

	/* Get and set allday methods */
	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	/* Get and set repeat methods */
	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	/* Get and set description methods */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiff() {
		// TODO: Change the strings for resources.
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES"));
		Date dateInit = new Date();
		Date dateEnd = new Date();

		Calendar calInit = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();

		try {
			/**
			 * Need the date and hour and minutes for a good result, else take
			 * the 0:00 hour like the init of the day and no it's a good idea
			 */
			dateInit = date.parse(init);
			calInit.setTime(dateInit);
			calInit.set(Calendar.HOUR, (int) initHour);
			calInit.set(Calendar.MINUTE, (int) initMinute);

			if (end != null) {
				dateEnd = date.parse(end);
				calEnd.setTime(dateEnd);
				calEnd.set(Calendar.HOUR, (int) endHour);
				calEnd.set(Calendar.MINUTE, (int) endMinute);
			} else {
				dateEnd = date.parse(init);
				calEnd.setTime(dateEnd);
				calEnd.set(Calendar.HOUR, (int) initHour);
				calEnd.set(Calendar.MINUTE, (int) initMinute);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long initMilliseconds = calInit.getTimeInMillis();
		long endMilliseconds = calEnd.getTimeInMillis();
		long nowMilliseconds = Calendar.getInstance().getTimeInMillis();

		/** Difference into init and end with now. */
		long diffInitNow = initMilliseconds - nowMilliseconds;
		long diffEndNow = endMilliseconds - nowMilliseconds;

		if (diffInitNow < 0 && diffEndNow < 0)
			return "Completada";
		else if (diffInitNow < 0 && diffEndNow >= 0)
			return "En transcurso";
		else {
			String hourInit = String.valueOf(initHour);
			String minuteInit = String.valueOf(initMinute);
			String hourEnd = String.valueOf(endHour);
			String minuteEnd = String.valueOf(endMinute);

			/** Change 0 values for 00 to format */
			if (allDay)
				return "Todo el dia";

			if (initHour == 0)
				hourInit = "00";
			if (initMinute == 0)
				minuteInit = "00";
			if (endHour == 0)
				hourEnd = "00";
			if (endMinute == 0)
				minuteEnd = "00";

			if (repeat)
				return hourInit + ":" + minuteInit;

			return hourInit + ":" + minuteInit + " - " + hourEnd + ":" + minuteEnd;
		}
	}

	/** Convert milliseconds in yyyy-MM-dd date format */
	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	@Override
	public int compareTo(Event event) {
		String otherInitDate = event.getInit();
		String[] othersDate = otherInitDate.split("-");

		int otherYear = Integer.parseInt(othersDate[0]);
		int otherMonth = Integer.parseInt(othersDate[1]);
		int otherDay = Integer.parseInt(othersDate[2]);

		Calendar otherEvent = Calendar.getInstance();
		otherEvent.set(otherYear, otherMonth, otherDay);

		String instanceInitDate = init;
		String[] sDate = instanceInitDate.split("-");

		int instanceYear = Integer.parseInt(sDate[0]);
		int instanceMonth = Integer.parseInt(sDate[1]);
		int instanceDay = Integer.parseInt(sDate[2]);
		Calendar instanceEvent = Calendar.getInstance();
		instanceEvent.set(instanceYear, instanceMonth, instanceDay);

		return instanceEvent.compareTo(otherEvent);
	}

	public String createRRule(long typeRepetition, int count, boolean[] weekDays, int optionMonth, long typeInterval,
			String untilDate, int eventCount) {
		String result = "";
		String freq = "FREQ=";
		String interval = "INTERVAL=";
		String byDay = "";
		String byMonthDay = "";
		String until = "";
		String intervalCount = "";

		/** typeRepetition, freq */
		switch ((int) typeRepetition) {
		case NewEventRepetitionDialogFragment.REPEAT_ONCE:
			return "";
		case NewEventRepetitionDialogFragment.REPEAT_DAYLY:
			freq += FREQ_DAILY;
			break;
		case NewEventRepetitionDialogFragment.REPEAT_WEEKLY:
			freq += FREQ_WEEKLY;
			break;
		case NewEventRepetitionDialogFragment.REPEAT_MONTH:
			freq += FREQ_MONTHLY;
			break;
		case NewEventRepetitionDialogFragment.REPEAT_YEARLY:
			freq += FREQ_YEARLY;
			break;
		}

		/** interval */
		interval += count;

		/** ByDay */
		if (weekDays != null) {
			byDay = "BYDAY=";
			for (int i = 0; i < weekDays.length; i++) {
				if (weekDays[i] == true)
					byDay += typesByDay[i] + ",";

			}
		}
		// TODO: Don't work yet
		if (optionMonth != 0) {
			if (optionMonth == NewEventRepetitionDialogFragment.MONTH_SAME_DAY) {
				/** Split the day of init */
				String day = init.split("/")[0];
				byMonthDay = "BYMONTHDAY=" + day;
			} else {
				byDay = "BYDAY=MO";
			}
		}

		switch ((int) typeInterval) {
		case NewEventRepetitionDialogFragment.INTERVAL_FOREVER:
			until = "";
			break;
		case NewEventRepetitionDialogFragment.INTERVAL_UNTIL:
			String[] sUntilDate = untilDate.split("/");
			int untilDay = Integer.valueOf(sUntilDate[0]);
			int untilMonth = Integer.valueOf(sUntilDate[1]);
			int untilYear = Integer.valueOf(sUntilDate[2]);
			String sUntilDay = untilDay < 9 ? "0" + untilDay : "" + untilDay;
			String sUntilMonth = untilMonth < 9 ? "0" + untilMonth : "" + untilMonth;

			until = "UNTIL=" + untilYear + "" + sUntilMonth + "" + sUntilDay + "T" + "000000Z";
			break;
		case NewEventRepetitionDialogFragment.INTERVAL_COUNT:
			intervalCount = "COUNT=" + eventCount;
			break;
		}

		/** Make the rrule with all data in the correct order */

		if (!freq.isEmpty())
			result += freq + ";";

		result += interval + ";";

		if (!intervalCount.isEmpty())
			result += intervalCount + ";";

		if (!until.isEmpty())
			result += until + ";";

		if (!byMonthDay.isEmpty())
			result += byMonthDay + ";";

		if (!byDay.isEmpty() && !byDay.equals("BYDAY=")) {
			result += byDay;
			result = result.substring(0, result.length() - 1);
			result += ";";
		}

		return result;
	}
}
