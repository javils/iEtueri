package courses;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import schedule.Event;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.DatePickerDialogFragment;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javils.ietueri.R;

public class NewCourseFragment extends Fragment implements OnClickButtonXml {

	private EditText nameCourse;
	private Button fromDate;
	private Button toDate;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_course, container, false);
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getWritableDatabase();

		Calendar calendar = Calendar.getInstance();

		/** Get the instance of the views */
		nameCourse = (EditText) view.findViewById(R.id.newcourse_course_name);
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
	private void cancelNewCourse() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_COURSES);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

	}

	private void addNewCourse() {
		if (nameCourse.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de curso. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			/** Fill the database with the data */
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME, nameCourse.getText().toString());
			values.put(DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS, 0);
			values.put(DatabaseContract.Courses.COLUMN_NAME_AVERAGE, 0);
			values.put(DatabaseContract.Courses.COLUMN_NAME_INIT_DATE, fromDate.getHint().toString());
			values.put(DatabaseContract.Courses.COLUMN_NAME_END_DATE, toDate.getHint().toString());
			values.put(DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS, "");

			db.insert(DatabaseContract.Courses.TABLE_NAME, null, values);

			/** Back to Courses view */
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_COURSES);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

		}
	}

	@Override
	public void onClickXml(View view) {

		switch (view.getId()) {
		case R.id.newcourse_course_init:
		case R.id.newcourse_course_end:
			showDatePickerDialog(view);
			break;
		case R.id.neworcancel_actionbar_acept:
			addNewCourse();
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
