package today;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.javils.ietueri.R;

public class NewEventRepetitionDialogFragment extends DialogFragment {

	/** Builder for the dialog */
	private AlertDialog.Builder builder;

	/** Inflater of the dialog */
	private LayoutInflater inflater;

	/** Handler of the Dialog */
	private Button handler;

	/** Types of repetition */
	private Spinner typeOfRepetition;

	/** Number of weeks, days, years, etc */
	private EditText count;

	/** Checkbox of the days selecteds */
	private CheckBox[] weekDays;

	/** RadiGroup with the options for Month view */
	private RadioGroup monthOptions;

	/** Month option selected */
	private RadioButton monthOptionSelected;

	/** Types of frequency */
	private Spinner typeOfFrequency;

	/** Date bottom for until date */
	private Button dateUntil;

	/** Count for number of events to repeat */
	private EditText countUntil;

	/** Content of repetition layout */
	private ViewGroup typeRepetitionContent;

	/** Content of interval layout */
	private ViewGroup typeIntervalContent;

	/** Constants for the type repetition spinner */
	public static final int REPEAT_ONCE = 0;
	public static final int REPEAT_DAYLY = 1;
	public static final int REPEAT_WEEKLY = 2;
	public static final int REPEAT_MONTH = 3;
	public static final int REPEAT_YEARLY = 4;

	/** Constants for month view */
	public static final int MONTH_SAME_DAY = 1;
	public static final int MONTH_EVERYMONDAY = 2;

	/** Interval spinners constants */
	public static final int INTERVAL_FOREVER = 0;
	public static final int INTERVAL_UNTIL = 1;
	public static final int INTERVAL_COUNT = 2;

	/** Constants for the day */
	public static final int DAY_MONDAY = 0;
	public static final int DAY_TUESDAY = 1;
	public static final int DAY_THURSDAY = 2;
	public static final int DAY_WEDNESDAY = 3;
	public static final int DAY_FRIDAY = 4;
	public static final int DAY_SATURDAY = 5;
	public static final int DAY_SUNDAY = 6;

	/** Keys for the data */
	public static String KEY_TYPE_REPEAT = "key_type_repeat";
	public static String KEY_COUNT = "key_count";
	public static String KEY_WEEK_DAYS = "key_week_days";
	public static String KEY_MONTH_TIME = "key_month_time";
	public static String KEY_TYPE_INTERVAL = "key_type_interval";
	public static String KEY_UNTIL_DATE = "key_until_date";
	public static String KEY_COUNT_EVENT = "key_count_event";

	public static final int NUMBER_OF_DAYS = 7;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		builder = new AlertDialog.Builder(getActivity());

		inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_event_repetition, null);
		builder.setView(dialogView);

		/** Get the instances of views */
		typeOfRepetition = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_repetition);
		typeOfFrequency = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_frequency);
		count = (EditText) dialogView.findViewById(R.id.neweventrepetition_edittext_interval);
		typeRepetitionContent = (ViewGroup) dialogView.findViewById(R.id.neweventrepetition_linear_repetition_content);
		typeIntervalContent = (ViewGroup) dialogView.findViewById(R.id.neweventrepetition_linear_interval_content);

		/** Fill the spinner with the repetition options */
		ArrayAdapter<String> adapterTypeRepetition = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item);
		adapterTypeRepetition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterTypeRepetition.addAll(getResources().getStringArray(R.array.new_event_repetition_types_repetition));
		typeOfRepetition.setAdapter(adapterTypeRepetition);

		/** Fill the spinner with the frequency options */
		ArrayAdapter<String> adapterTypeFrequency = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item);
		adapterTypeFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterTypeFrequency.addAll(getResources().getStringArray(R.array.new_event_repetition_types_interval));
		typeOfFrequency.setAdapter(adapterTypeFrequency);

		builder.setPositiveButton(getString(R.string.new_event_repetition_done), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.setHint(typeOfRepetition.getSelectedItem().toString());
				sendData(NewEventTodayFragment.REPETITION_CODE);
			}
		});

		/** Set listener to the repetition spinner */
		typeOfRepetition.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				addTypeRepetitionLayout();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		/** Set listener to the interval spinner */
		typeOfFrequency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				addTypeFrequencyLayout();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		return builder.create();
	}

	public void setHandler(Button handler) {
		this.handler = handler;
	}

	/** Add the views of each type of repetition */
	public void addTypeRepetitionLayout() {
		int id = 0;

		typeRepetitionContent.removeAllViews();

		switch ((int) typeOfRepetition.getSelectedItemId()) {
		case REPEAT_WEEKLY:
			id = R.layout.dialog_new_event_repetition_weekly;
			break;
		case REPEAT_MONTH:
			id = R.layout.dialog_new_event_repetition_month;
			break;
		}

		/** No layout, so out! */
		if (id == 0)
			return;

		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		typeRepetitionContent.addView(linearLayout);

		if (id == R.layout.dialog_new_event_repetition_weekly) {
			weekDays = new CheckBox[NUMBER_OF_DAYS];
			weekDays[DAY_MONDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_monday);
			weekDays[DAY_TUESDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_tuesday);
			weekDays[DAY_WEDNESDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_wednesday);
			weekDays[DAY_THURSDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_thursday);
			weekDays[DAY_FRIDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_friday);
			weekDays[DAY_SATURDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_saturday);
			weekDays[DAY_SUNDAY] = (CheckBox) linearLayout.findViewById(R.id.newevent_repetition_weekly_sunday);
		}

		if (id == R.layout.dialog_new_event_repetition_month) {
			monthOptions = (RadioGroup) linearLayout.findViewById(R.id.neweventrepetition_radiogroup_month);
			monthOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					monthOptionSelected = (RadioButton) linearLayout.findViewById(checkedId);

				}
			});
		}
	}

	/** Add the views of each type of interval */
	public void addTypeFrequencyLayout() {
		int id = 0;

		typeIntervalContent.removeAllViews();

		switch ((int) typeOfFrequency.getSelectedItemId()) {
		case INTERVAL_UNTIL:
			id = R.layout.dialog_new_event_interval_until;
			break;
		case INTERVAL_COUNT:
			id = R.layout.dialog_new_event_interval_count;
			break;
		}

		/** No layout, so out! */
		if (id == 0)
			return;

		LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		typeIntervalContent.addView(linearLayout);

		/** Put the actual date in the button */
		if (id == R.layout.dialog_new_event_interval_until) {
			dateUntil = (Button) linearLayout.findViewById(R.id.neweventrepetititon_until_button_date);

			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

			dateUntil.setHint(day + "/" + month + "/" + year);
		}

		if (id == R.layout.dialog_new_event_interval_count)
			countUntil = (EditText) linearLayout.findViewById(R.id.neweventrepetition_until_count);
	}

	public void sendData(int typeData) {
		Intent intent = new Intent();
		intent.putExtra(KEY_TYPE_REPEAT, typeOfRepetition.getSelectedItemId());
		intent.putExtra(KEY_COUNT, Integer.valueOf(count.getText().toString()));

		/** Depend of the type repetition we need send the correct data */
		switch ((int) typeOfRepetition.getSelectedItemId()) {
		case REPEAT_WEEKLY:
			boolean[] days = new boolean[NUMBER_OF_DAYS];
			for (int i = 0; i < NUMBER_OF_DAYS; i++)
				days[i] = weekDays[i].isChecked();
			intent.putExtra(KEY_WEEK_DAYS, days);
			break;
		case REPEAT_MONTH:
			int optionSelected = 0;
			if (monthOptionSelected.getId() == R.id.neweventrepetition_same_day_each_month)
				optionSelected = MONTH_SAME_DAY;
			else if (monthOptionSelected.getId() == R.id.neweventrepetition_everymonday_each_month)
				optionSelected = MONTH_EVERYMONDAY;

			intent.putExtra(KEY_MONTH_TIME, optionSelected);
			break;
		}
		/** Save type interval */
		intent.putExtra(KEY_TYPE_INTERVAL, typeOfFrequency.getSelectedItemId());

		switch ((int) typeOfFrequency.getSelectedItemId()) {
		case INTERVAL_UNTIL:
			intent.putExtra(KEY_UNTIL_DATE, dateUntil.getHint().toString());
			break;
		case INTERVAL_COUNT:
			intent.putExtra(KEY_COUNT_EVENT, Integer.valueOf(countUntil.getText().toString()));
			break;
		}

		/** Send data to the activity */
		getTargetFragment().onActivityResult(getTargetRequestCode(), NewEventTodayFragment.REPETITION_CODE, intent);
	}
}
