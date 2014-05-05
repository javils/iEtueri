package courses;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import schedule.Event;
import utility.DatePickerDialogFragment;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.javils.ietueri.R;

public class NewCourseFragment extends Fragment implements OnClickButtonXml {

	private Button fromDate;
	private Button toDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_course, container, false);

		Calendar calendar = Calendar.getInstance();

		/** Get the instance of the views */
		fromDate = (Button) view.findViewById(R.id.newcourse_course_init);
		toDate = (Button) view.findViewById(R.id.newcourse_course_end);

		String[] sDate = Event.getDate(calendar.getTimeInMillis()).split("-");
		int year = Integer.valueOf(sDate[0]);
		int month = Integer.valueOf(sDate[1]);
		int day = Integer.valueOf(sDate[2]);
		fromDate.setHint(day + "/" + month + "/" + year);
		toDate.setHint(day + "/" + month + "/" + year);
		return view;
	}

	public void showDatePickerDialog(View view) {
		DatePickerDialogFragment dateFragment = new DatePickerDialogFragment();
		dateFragment.setHandler((Button) view);
		dateFragment.show(getFragmentManager(), "newCourseDatePicker");
	}

	/** Cancel the new course and back to Course View */
	public void cancelNewCourse() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_COURSES);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

		// first, call onPrepareOptionsMenu() for reset the ActionBar
		getActivity().invalidateOptionsMenu();

	}

	@Override
	public void onClickXml(View view) {

		switch (view.getId()) {
		case R.id.newcourse_course_init:
		case R.id.newcourse_course_end:
			showDatePickerDialog(view);
			break;
		case R.id.neworcancel_actionbar_acept:
			break;
		case R.id.neworcancel_actionbar_cancel:
			cancelNewCourse();
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
