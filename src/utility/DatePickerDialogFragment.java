package utility;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

/** This class is a view of Date Picker dialog */
// TODO: Only for Buttons at this moment, in the future, for more View objects
public class DatePickerDialogFragment extends DialogFragment implements OnDateSetListener {

	/** The handler of the dialog */
	private Button handler;

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		/** Need increment one, Calendar format for month is 0 to 11. */
		handler.setHint("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
	}

	/** Set the handler */
	public void setHandler(Button handler) {
		this.handler = handler;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] sDate = handler.getHint().toString().split("/");

		/** Use the current date as the default date in the picker */
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		if (sDate.length == 3) {
			year = Integer.valueOf(sDate[2]);
			/** Need decrement one, Calendar format for month is 0 to 11. */
			month = Integer.valueOf(sDate[1]) - 1;
			day = Integer.valueOf(sDate[0]);
		}

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
}
