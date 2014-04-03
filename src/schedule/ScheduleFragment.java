package schedule;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;

import com.javils.ietueri.R;

public class ScheduleFragment extends Fragment implements OnDateChangeListener {

	/** Views */
	private CalendarView calendar;
	private ListView listEvents;

	/** ArrayList with all of the events of the calendars */
	private static ArrayList<Event> events = new ArrayList<Event>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		calendar = (CalendarView) view.findViewById(R.id.calendar_calendar);
		listEvents = (ListView) view.findViewById(R.id.list_calendar_events);

		return view;
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

	}

	/** Get all the events */
	public static ArrayList<Event> getEvents() {
		return events;
	}

	/** Set all events */
	public static void setEvents(ArrayList<Event> events) {
		ScheduleFragment.events = events;
	}

	/** Set only one event in the list */
	public static void setEvent(Event event) {
		events.add(event);
	}

}
