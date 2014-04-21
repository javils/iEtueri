package utility;

import android.view.View;

public interface OnDatePickerSet {
	/**
	 * Show the dialog, use DatePickerDialogFragment for this.
	 * 
	 * @see DatePickerDialogFragment
	 * @param view
	 *            The handler of the dialog.
	 */
	public void showDatePickerDialog(View view);
}
