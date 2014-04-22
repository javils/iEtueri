package today;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		builder = new AlertDialog.Builder(getActivity());

		inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_event_repetition, null);
		builder.setView(dialogView);

		/** Get the instances of views */
		typeOfRepetition = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_repetition);
		typeOfFrequency = (Spinner) dialogView.findViewById(R.id.neweventrepetition_spinner_type_frequency);

		/** Fill the snipper with the repetition options */
		ArrayAdapter<String> adapterTypeRepetition = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item);
		adapterTypeRepetition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterTypeRepetition.addAll(getResources().getStringArray(R.array.new_event_repetition_types_repetition));
		typeOfRepetition.setAdapter(adapterTypeRepetition);

		/** Fill the snipper with the frequency options */
		ArrayAdapter<String> adapterTypeFrequency = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item);
		adapterTypeFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterTypeFrequency.addAll(getResources().getStringArray(R.array.new_event_repetition_types_interval));
		typeOfFrequency.setAdapter(adapterTypeFrequency);

		builder.setPositiveButton(getString(R.string.new_event_repetition_done), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), getString(R.string.new_event_repetition_done), Toast.LENGTH_SHORT).show();
			}
		});

		return builder.create();
	}

	public void setHandler(Button handler) {
		this.handler = handler;
	}
}
