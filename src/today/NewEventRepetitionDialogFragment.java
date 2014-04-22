package today;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.javils.ietueri.R;

public class NewEventRepetitionDialogFragment extends DialogFragment {

	/** Builder for the dialog */
	private AlertDialog.Builder builder;

	/** Inflater of the dialog */
	private LayoutInflater inflater;

	/** Handler of the Dialog */
	private Button handler;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		builder = new AlertDialog.Builder(getActivity());

		inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_event_repetition, null);
		builder.setView(dialogView);

		builder.setPositiveButton(getString(R.string.new_event_repetition_done), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "Listo", Toast.LENGTH_SHORT).show();
			}
		});

		return builder.create();
	}

	public void setHandler(Button handler) {
		this.handler = handler;
	}
}
