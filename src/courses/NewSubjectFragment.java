package courses;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
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
import android.widget.EditText;
import android.widget.Toast;

import com.javils.ietueri.R;

public class NewSubjectFragment extends Fragment implements OnClickButtonXml {

	private EditText nameSubject;
	private EditText nameTeacher;
	private EditText credits;
	private EditText note;
	private Course course;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_subject, container, false);

		/** Get the instances of the views */
		nameSubject = (EditText) view.findViewById(R.id.new_subject_name_subject);
		nameTeacher = (EditText) view.findViewById(R.id.new_subject_teacher);
		credits = (EditText) view.findViewById(R.id.new_subject_credits);
		note = (EditText) view.findViewById(R.id.new_subject_note);
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getWritableDatabase();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	private void newSubject() {

		if (nameSubject.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de curso. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME, nameSubject.getText().toString());
			values.put(DatabaseContract.Subjects.COLUMN_NAME_TEACHER, nameTeacher.getText().toString());

			String credits = this.credits.getText().toString();

			if (credits.isEmpty())
				credits = "0";

			values.put(DatabaseContract.Subjects.COLUMN_NAME_CREDITS, credits);
			values.put(DatabaseContract.Subjects.COLUMN_NAME_AVERAGE, 0);
			values.put(DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID, course.getId());
			values.put(DatabaseContract.Subjects.COLUMN_NAME_NOTE, 0);
			values.put(DatabaseContract.Subjects.COLUMN_NAME_NOTE_NECESSARY, 0);
			values.put(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, "");
			values.put(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID, "");
			values.put(DatabaseContract.Subjects.COLUMN_NAME_PONDERATIONS_ID, "");
			values.put(DatabaseContract.Subjects.COLUMN_NAME_SCHEDULES_ID, "");

			db.insert(DatabaseContract.Subjects.TABLE_NAME, null, values);
			values.clear();
			course.setNumberOfSubjects(course.getNumberOfSubjects() + 1);
			values.put(DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS, course.getNumberOfSubjects());
			String[] argsSelection = { String.valueOf(course.getId()) };
			db.update(DatabaseContract.Courses.TABLE_NAME, values, DatabaseContract.Courses._ID + "= ?", argsSelection);

			Toast.makeText(getActivity(), "Asignatura creada", Toast.LENGTH_SHORT).show();
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_COURSE);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((CourseDetailFragment) newFragment).setCourse(course);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		db.close();
		dbHelper.close();
	}

	private void cancelSubject() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_COURSE);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		((CourseDetailFragment) newFragment).setCourse(course);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
	}

	@Override
	public void onClickXml(View view) {
		switch (view.getId()) {
		case R.id.neworcancel_actionbar_acept:
			newSubject();
			break;
		case R.id.neworcancel_actionbar_cancel:
			cancelSubject();
			break;
		}
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
