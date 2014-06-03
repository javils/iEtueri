package schedule;

import java.util.ArrayList;
import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private static ListView listEvents;

	/** Variables to check the actual day */
	private int actualDay;
	private int actualMonth;
	private int actualYear;

	private static boolean iAmActive = false;

	public static Handler updaterHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RefreshScheduleEventsData.CALENDAR_DATA_CHANGE:
				// Update adapter of the ListView
				((ScheduleTodayAdapter) listEvents.getAdapter()).notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setiAmActive(true);
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		calendar = (CalendarView) view.findViewById(R.id.calendar_calendar);
		listEvents = (ListView) view.findViewById(R.id.list_calendar_events);

		actualDay = 0;
		actualMonth = 0;
		actualYear = 0;

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);

		calendar.setOnDateChangeListener(this);

		ScheduleTodayAdapter adapter = new ScheduleTodayAdapter(view.getContext(), R.layout.schedule_list_item,
				new ArrayList<Event>());

		EventsManager.setAdapter(adapter);

		ArrayList<Event> events = new ArrayList<Event>();// EventsManager.find(getActivity(),
															// year, month,
															// day);

		listEvents.setAdapter(EventsManager.getAdapter());

		new Thread(new FindEvents(getActivity(), EventsManager.getAdapter(), events, year, month, day, false)).start();

		return view;
	}

	@Override
	public void onDestroyView() {
		setiAmActive(false);
		super.onDestroyView();
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
		/** Don't refresh if is the same day */
		if (actualDay == dayOfMonth && actualMonth == month && actualYear == year)
			return;

		actualDay = dayOfMonth;
		actualMonth = month;
		actualYear = year;

		EventsManager.getAdapter().setItems(EventsManager.find(getActivity(), year, month, dayOfMonth));
		EventsManager.getAdapter().notifyDataSetChanged();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	public static boolean isiAmActive() {
		return iAmActive;
	}

	public static void setiAmActive(boolean iAmActive) {
		ScheduleFragment.iAmActive = iAmActive;
	}
}
