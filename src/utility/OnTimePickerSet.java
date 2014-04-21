package utility;

import android.view.View;

public interface OnTimePickerSet {
	/**
	 * Show the dialog, use TimePickerDialogFragment for this.
	 * 
	 * @see TimePickerDialogFragment
	 * @param view
	 *            The handler of the dialog.
	 */
	public void showTimePickerDialog(View view);
}
