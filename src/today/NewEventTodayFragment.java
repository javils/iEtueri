package today;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatePickerDialogFragment;
import utility.OnClickButtonXml;
import utility.TimePickerDialogFragment;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.javils.ietueri.R;

public class NewEventTodayFragment extends Fragment implements OnClickButtonXml {

	private EditText name;
	private EditText location;
	private Button fromDate;
	private Button fromHour;
	private Button toDate;
	private Button toHour;
	private CheckBox allDay;
	private EditText description;
	private Button repetition;

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
		repetition = (Button) view.findViewById(R.id.newevent_today_repetition);

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
		repetitionFragment.show(getFragmentManager(), "newEventRepetitionDialog");
	}

	@Override
	public void onClickXml(View view) {

		Toast.makeText(getActivity(), getString(R.string.new_event_repetition_done), Toast.LENGTH_SHORT).show();
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
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
