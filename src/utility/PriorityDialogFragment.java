package utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.javils.ietueri.R;

/** This class is a view of Timer Picker dialog */
// TODO: Only for Buttons at this moment, in the future, for more View objects
public class PriorityDialogFragment extends DialogFragment implements OnClickListener {

	/** Builder for the dialog */
	private AlertDialog.Builder builder;

	/** Inflater of the dialog */
	private LayoutInflater inflater;

	/** The handler of the dialog */
	private Button handler;

	/** Button with the 3 priority */
	private Button highPriority;
	private Button normalPriority;
	private Button lowPriority;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		builder = new AlertDialog.Builder(getActivity());
		inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_homework_priority, null);
		builder.setView(dialogView);

		highPriority = (Button) dialogView.findViewById(R.id.dialog_new_homework_priority_high);
		normalPriority = (Button) dialogView.findViewById(R.id.dialog_new_homework_priority_normal);
		lowPriority = (Button) dialogView.findViewById(R.id.dialog_new_homework_priority_low);

		highPriority.setOnClickListener(this);
		normalPriority.setOnClickListener(this);
		lowPriority.setOnClickListener(this);

		return builder.create();
	}

	/** Set the handler */
	public void setHandler(Button handler) {
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_new_homework_priority_high:
			handler.setHint(getString(R.string.high_priority));
			break;
		case R.id.dialog_new_homework_priority_normal:
			handler.setHint(getString(R.string.normal_priority));
			break;
		case R.id.dialog_new_homework_priority_low:
			handler.setHint(getString(R.string.low_priority));
			break;
		}

		this.dismiss();

	}

}
