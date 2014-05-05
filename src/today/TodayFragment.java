package today;

import java.util.ArrayList;
import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import schedule.Event;
import schedule.EventsManager;
import schedule.ScheduleTodayAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javils.ietueri.R;

/** This class is for Today View */
public class TodayFragment extends Fragment {

	/** Views */
	private ListView listEvents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_today, container, false);

		listEvents = (ListView) view.findViewById(R.id.list_today_today);

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);

		if (EventsManager.isThreadfinish()) {
			ScheduleTodayAdapter adapter = new ScheduleTodayAdapter(view.getContext(), R.layout.schedule_list_item,
					new ArrayList<Event>());

			EventsManager.setAdapter(adapter);

			ArrayList<Event> events = EventsManager.find(year, month, day);
			if (events != null)
				EventsManager.getAdapter().setItems(events);

			EventsManager.getAdapter().notifyDataSetChanged();
			listEvents.setAdapter(EventsManager.getAdapter());
		}

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
