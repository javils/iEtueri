package schedule;

import java.util.ArrayList;

import today.TodayFragment;
import android.app.Activity;

public class FindEvents implements Runnable {

	private Activity activity;
	private ScheduleTodayAdapter adapter;
	private ArrayList<Event> events;
	private int year;
	private int month;
	private int day;
	private boolean inDayView;

	public FindEvents(Activity activity, ScheduleTodayAdapter adapter, ArrayList<Event> events, int year, int month,
			int day, boolean inDayView) {
		this.activity = activity;
		this.adapter = adapter;
		this.events = events;
		this.year = year;
		this.month = month;
		this.day = day;
		this.inDayView = inDayView;
	}

	@Override
	public void run() {

		events = EventsManager.find(activity, year, month, day);
		adapter.setItems(events);

		if (!inDayView)
			ScheduleFragment.updaterHandler.sendEmptyMessage(RefreshScheduleEventsData.CALENDAR_DATA_CHANGE);
		else
			TodayFragment.updaterHandler.sendEmptyMessage(RefreshScheduleEventsData.CALENDAR_DATA_CHANGE);

	}

}
