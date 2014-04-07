package schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is a container of the event's data
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class Event {

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

		try {
			dateInit = date.parse(init);
			dateEnd = date.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long initMilliseconds = dateInit.getTime();
		long endMilliseconds = dateEnd.getTime();
		long nowMilliseconds = Calendar.getInstance().getTimeInMillis();

		/** Diference into init and end with now. */
		long diffInitNow = initMilliseconds - nowMilliseconds;
		long diffEndNow = endMilliseconds - nowMilliseconds;

		if (diffInitNow < 0 && diffEndNow < 0)
			return "Completada";
		else if (diffInitNow < 0 && diffEndNow > 0)
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

			return hourInit + ":" + minuteInit + " - " + hourEnd + ":" + minuteEnd;
		}
	}
}
