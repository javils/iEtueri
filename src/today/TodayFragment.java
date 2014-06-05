package today;

import java.util.ArrayList;
import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import schedule.Event;
import schedule.EventsManager;
import schedule.FindEvents;
import schedule.RefreshScheduleEventsData;
import schedule.ScheduleTodayAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.javils.ietueri.R;

/** This class is for Today View */
public class TodayFragment extends Fragment {

	/** Views */
	private static ListView listEvents;

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
		View view = inflater.inflate(R.layout.fragment_today, container, false);

		listEvents = (ListView) view.findViewById(R.id.list_today_today);

		Toast.makeText(getActivity(), "Cargando Eventos...", Toast.LENGTH_LONG).show();

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);

		ScheduleTodayAdapter adapter = new ScheduleTodayAdapter(view.getContext(), R.layout.schedule_list_item,
				new ArrayList<Event>());

		EventsManager.setAdapter(adapter);

		ArrayList<Event> events = new ArrayList<Event>();

		listEvents.setAdapter(EventsManager.getAdapter());

		new Thread(new FindEvents(getActivity(), adapter, events, year, month, day, true)).start();

		return view;
	}

	@Override
	public void onDestroyView() {
		setiAmActive(false);
		super.onDestroyView();
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
		TodayFragment.iAmActive = iAmActive;
	}
}
