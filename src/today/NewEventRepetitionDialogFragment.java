package today;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

	/** Types of frequency */
	private Spinner typeOfFrequency;

	/** Content of repetition layout */
	private ViewGroup typeRepetitionContent;

	/** Content of interval layout */
	private ViewGroup typeIntervalContent;

	/** Constants for the spinners item's */
	private static final int REPEAT_WEEKLY = 2;
	private static final int REPEAT_MONTH = 3;
	private static final int INTERVAL_UNTIL = 1;
	private static final int INTERVAL_COUNT = 2;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		builder = new AlertDialog.Builder(getActivity());

		inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_event_repetition, null);
		builder.setView(dialogView);

		/** Get the instances of views */
		typeOfRepetition = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_repetition);
		typeOfFrequency = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_frequency);
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
				Toast.makeText(getActivity(), getString(R.string.new_event_repetition_done), Toast.LENGTH_SHORT).show();
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

		LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		typeRepetitionContent.addView(linearLayout);
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
		Button date = (Button) linearLayout.findViewById(R.id.neweventrepetititon_until_button_date);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		date.setHint(day + "/" + month + "/" + year);

	}
}
