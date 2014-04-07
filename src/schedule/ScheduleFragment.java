package schedule;

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

}
