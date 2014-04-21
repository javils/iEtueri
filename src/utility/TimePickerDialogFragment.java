package utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

/** This class is a view of Timer Picker dialog */
// TODO: Only for Buttons at this moment, in the future, for more View objects
public class TimePickerDialogFragment extends DialogFragment implements OnTimeSetListener {

	/** The handler of the dialog */
	private Button handler;

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (minute < 9)
			handler.setHint("" + hourOfDay + ":" + "0" + minute);
		else
			handler.setHint("" + hourOfDay + ":" + minute);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] sDate = handler.getHint().toString().split(":");
		int hour = Integer.valueOf(sDate[0]);
		int minute = Integer.valueOf(sDate[1]);

		// Check if minute have 1 or 2 digits
		if (minute < 9)
			handler.setHint("" + hour + ":" + "0" + minute);
		else
			handler.setHint("" + hour + ":" + minute);

		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	/** Set the handler */
	public void setHandler(Button handler) {
		this.handler = handler;
	}

}
