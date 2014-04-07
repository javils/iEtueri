package schedule;

import java.util.ArrayList;
import java.util.Calendar;

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

	/** Variables to check the actual day */
	private int actualDay;
	private int actualMonth;
	private int actualYear;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

		if (EventsManager.isThreadfinish()) {
			ScheduleTodayAdapter adapter = new ScheduleTodayAdapter(view.getContext(), R.layout.schedule_list_item,
					new ArrayList<Event>());

			EventsManager.setAdapter(adapter);

			if (EventsManager.find(year, month, day) != null) {
				EventsManager.getAdapter().setItems(EventsManager.find(year, month, day));
			}

			EventsManager.getAdapter().notifyDataSetChanged();
			listEvents.setAdapter(EventsManager.getAdapter());
		}

		return view;
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
		/** Don't refresh if is the same day */
		if (actualDay == dayOfMonth && actualMonth == month && actualYear == year)
			return;

		actualDay = dayOfMonth;
		actualMonth = month;
		actualYear = year;

		EventsManager.getAdapter().setItems(EventsManager.find(year, month, dayOfMonth));
		EventsManager.getAdapter().notifyDataSetChanged();
		listEvents.setAdapter(EventsManager.getAdapter());

	}
}
