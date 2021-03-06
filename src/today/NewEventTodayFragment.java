package today;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import schedule.CalendarManager;
import schedule.Event;
import schedule.EventsManager;
import schedule.RefreshScheduleEventsData;
import utility.DatePickerDialogFragment;
import utility.OnBackPressed;
import utility.OnClickButtonXml;
import utility.TimePickerDialogFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.javils.ietueri.R;

public class NewEventTodayFragment extends Fragment implements OnClickButtonXml, OnBackPressed {

	private EditText name;
	private EditText location;
	private Button fromDate;
	private Button fromHour;
	private Button toDate;
	private Button toHour;
	private CheckBox allDay;
	private EditText description;

	private EventRepetition eventRepetition;
	private String rrule;

	/** Code for the repetition */
	public static final int REPETITION_CODE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_event_today, container, false);

		/** Get the instance of Views */
		name = (EditText) view.findViewById(R.id.newevent_today_name);
		location = (EditText) view.findViewById(R.id.newevent_today_location);
		fromDate = (Button) view.findViewById(R.id.newevent_today_from_date);
		fromHour = (Button) view.findViewById(R.id.newevent_today_from_hour);
		toDate = (Button) view.findViewById(R.id.newevent_today_to_date);
		toHour = (Button) view.findViewById(R.id.newevent_today_to_hour);
		allDay = (CheckBox) view.findViewById(R.id.newevent_today_allday);
		description = (EditText) view.findViewById(R.id.newevent_today_description);

		// Init from/to hour button
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute < 9) {
			fromHour.setHint("" + hour + ":0" + minute);
			toHour.setHint("" + (hour + 1) + ":0" + minute);
		} else {
			fromHour.setHint("" + hour + ":" + minute);
			toHour.setHint("" + (hour + 1) + ":" + minute);
		}

		// Init from/to date button
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		fromDate.setHint("" + day + "/" + month + "/" + year);
		toDate.setHint("" + day + "/" + month + "/" + year);

		eventRepetition = new EventRepetition();
		rrule = "";

		return view;
	}

	public void showTimePickerDialog(View view) {
		TimePickerDialogFragment timeFragment = new TimePickerDialogFragment();
		timeFragment.setHandler((Button) view);
		timeFragment.show(getFragmentManager(), "newEventTimePicker");
	}

	public void showDatePickerDialog(View view) {
		DatePickerDialogFragment dateFragment = new DatePickerDialogFragment();
		dateFragment.setHandler((Button) view);
		dateFragment.show(getFragmentManager(), "newEventDatePicker");
	}

	public void showRepetitionDialog(View view) {
		NewEventRepetitionDialogFragment repetitionFragment = new NewEventRepetitionDialogFragment();
		repetitionFragment.setHandler((Button) view);
		repetitionFragment.setTargetFragment(this, REPETITION_CODE);
		repetitionFragment.show(getFragmentManager(), "newEventRepetitionDialog");
	}

	/** Create new event in the provider */
	public synchronized void createNewEvent() {
		/** Get data of event */
		String eventName = name.getText().toString();
		String eventPlace = location.getText().toString();
		String eventFromDate = fromDate.getHint().toString();
		String eventFromHour = fromHour.getHint().toString();
		String eventToDate = toDate.getHint().toString();
		String eventToHour = toHour.getHint().toString();
		boolean eventAllDay = allDay.isChecked();
		String eventDescription = description.getText().toString();

		Event newEvent = null;

		/** Check if the data it's correct */
		if (EventsManager.checkDataEvent(eventName, eventFromDate, eventToDate, eventFromHour, eventToHour)) {

			String[] initHourDate = eventFromHour.split(":");
			int initHour = Integer.valueOf(initHourDate[0]);
			int initMinute = Integer.valueOf(initHourDate[1]);

			String[] endHourDate = eventToHour.split(":");
			int endHour = Integer.valueOf(endHourDate[0]);
			int endMinute = Integer.valueOf(endHourDate[1]);

			String[] initDate = eventFromDate.split("/");
			int initDay = Integer.valueOf(initDate[0]);
			int initMonth = Integer.valueOf(initDate[1]);
			int initYear = Integer.valueOf(initDate[2]);

			String[] endDate = eventToDate.split("/");
			int endDay = Integer.valueOf(endDate[0]);
			int endMonth = Integer.valueOf(endDate[1]);
			int endYear = Integer.valueOf(endDate[2]);

			String sInitMonth = new String();
			String sInitDay = new String();

			if (initMonth < 9)
				sInitMonth = "0" + initMonth;
			else
				sInitMonth = "" + initMonth;

			if (initDay < 9)
				sInitDay = "0" + initDay;
			else
				sInitDay = "" + initDay;

			eventFromDate = initYear + "-" + sInitMonth + "-" + sInitDay;
			eventToDate = endYear + "-" + endMonth + "-" + endDay;

			/** Add event to the array list */
			newEvent = new Event(eventName, eventDescription, eventPlace, eventFromDate, eventToDate, initHour,
					initMinute, endHour, endMinute, eventAllDay);
			EventsManager.addEvent(newEvent);

			/** Create the rrule */

			rrule = newEvent.createRRule(eventRepetition.typeRepetition, eventRepetition.count,
					eventRepetition.weekDays, eventRepetition.optionMonth, eventRepetition.typeInterval,
					eventRepetition.untilDate, eventRepetition.eventCount);

			/** Get milliseconds for dtstart */
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, initYear);
			calendar.set(Calendar.MONTH, initMonth - 1);
			calendar.set(Calendar.DAY_OF_MONTH, initDay);
			calendar.set(Calendar.HOUR_OF_DAY, initHour);
			calendar.set(Calendar.MINUTE, initMinute);
			String dtstart = "" + calendar.getTimeInMillis();

			/** Get milliseconds for dtend */
			calendar.set(Calendar.YEAR, endYear);
			calendar.set(Calendar.MONTH, endMonth - 1);
			calendar.set(Calendar.DAY_OF_MONTH, endDay);
			calendar.set(Calendar.HOUR_OF_DAY, endHour);
			calendar.set(Calendar.MINUTE, endMinute);
			String dtend = "" + calendar.getTimeInMillis();

			ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
			ContentValues cv = new ContentValues();
			cv.put(Events.CALENDAR_ID, CalendarManager.ID);
			cv.put(Events.TITLE, eventName);
			cv.put(Events.DTSTART, dtstart);
			cv.put(Events.DTEND, dtend);

			if (!rrule.isEmpty())
				cv.put(Events.RRULE, rrule);
			cv.put(Events.EVENT_LOCATION, eventPlace);
			cv.put(Events.ALL_DAY, eventAllDay);
			cv.put(Events.DESCRIPTION, eventDescription);
			cr.insert(EventsManager.buildEventUri(getActivity()), cv);

			Toast.makeText(getActivity(), "Nuevo Evento creado", Toast.LENGTH_SHORT).show();
		}

		/** Back to Today View */
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController.newInstance(NavigationDrawerController.SECTION_NUMBER_TODAY);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

		TodayFragment.updaterHandler.sendEmptyMessage(RefreshScheduleEventsData.CALENDAR_DATA_CHANGE);

	}

	/** Cancel the new event and back to Today View */
	public void cancelNewEvent() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController.newInstance(NavigationDrawerController.SECTION_NUMBER_TODAY);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

	}

	@Override
	public void onClickXml(View view) {

		switch (view.getId()) {
		case R.id.newevent_today_from_date:
		case R.id.newevent_today_to_date:
		case R.id.neweventrepetititon_until_button_date:
			showDatePickerDialog(view);
			break;
		case R.id.newevent_today_from_hour:
		case R.id.newevent_today_to_hour:
			showTimePickerDialog(view);
			break;
		case R.id.newevent_today_repetition:
			showRepetitionDialog(view);
			break;
		case R.id.neworcancel_actionbar_acept:
			createNewEvent();
			break;
		case R.id.neworcancel_actionbar_cancel:
			cancelNewEvent();
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == REPETITION_CODE) {

			eventRepetition.typeRepetition = data.getLongExtra(NewEventRepetitionDialogFragment.KEY_TYPE_REPEAT,
					NewEventRepetitionDialogFragment.REPEAT_ONCE);

			eventRepetition.count = data.getIntExtra(NewEventRepetitionDialogFragment.KEY_COUNT, 1);

			eventRepetition.weekDays = null;
			if (data.hasExtra(NewEventRepetitionDialogFragment.KEY_WEEK_DAYS))
				eventRepetition.weekDays = data.getBooleanArrayExtra(NewEventRepetitionDialogFragment.KEY_WEEK_DAYS);

			eventRepetition.optionMonth = 0;
			if (data.hasExtra(NewEventRepetitionDialogFragment.KEY_MONTH_TIME))
				eventRepetition.optionMonth = data.getIntExtra(NewEventRepetitionDialogFragment.KEY_MONTH_TIME, 0);

			eventRepetition.typeInterval = data.getLongExtra(NewEventRepetitionDialogFragment.KEY_TYPE_INTERVAL,
					NewEventRepetitionDialogFragment.INTERVAL_FOREVER);

			eventRepetition.untilDate = null;
			if (data.hasExtra(NewEventRepetitionDialogFragment.KEY_UNTIL_DATE))
				eventRepetition.untilDate = data.getStringExtra(NewEventRepetitionDialogFragment.KEY_UNTIL_DATE);

			eventRepetition.eventCount = -1;
			if (data.hasExtra(NewEventRepetitionDialogFragment.KEY_COUNT_EVENT))
				eventRepetition.eventCount = data.getIntExtra(NewEventRepetitionDialogFragment.KEY_COUNT_EVENT, 1);

		}
	}

	class EventRepetition {
		public long typeRepetition;
		public int count;
		public boolean[] weekDays;
		public int optionMonth;
		public long typeInterval;
		public String untilDate;
		public int eventCount;
	}

	@Override
	public void onBackPressed() {
		cancelNewEvent();
	}
}
