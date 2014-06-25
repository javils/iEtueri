package subject;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.javils.ietueri.R;

import courses.Course;

public class SubjectFragment extends Fragment {

	private ListView listSubjects;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_subjects, container, false);

		/** Get the instances of the views */
		listSubjects = (ListView) view.findViewById(R.id.listSubjects);

		ArrayList<Subject> subjects = getAllSubjects();

		if (subjects.size() == 0) {
			view = inflater.inflate(R.layout.fragment_void, container, false);
			TextView description = (TextView) view.findViewById(R.id.fragment_void_description);
			description.setText(R.string.subject_description);
		} else {
			SubjectListAdapter adapter = new SubjectListAdapter(getActivity(), R.layout.subject_list_item, subjects);

			listSubjects.setAdapter(adapter);

			listSubjects.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					TextView subjectName = (TextView) view.findViewById(R.id.subject_list_item_subject_name);
					Subject currentSubject = getSubjectInDB(subjectName.getText().toString());
					FragmentManager fragmentManager = getFragmentManager();
					Fragment newFragment = NavigationDrawerController
							.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);

					newFragment.getArguments().putInt(NavigationDrawerController.ARG_TYPE_SECTION,
							NavigationDrawerController.SUBJECT_SECTION);

					if (newFragment instanceof OnClickButtonXml)
						MainActivity.setOnClickFragment(newFragment);
					MainActivity.setCurrentFragment(newFragment);
					((DetailSubjectFragment) newFragment).setSubject(currentSubject);
					fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

					/** Close the DB */
					db.close();
					dbHelper.close();

				}
			});
		}
		return view;
	}

	private ArrayList<Subject> getAllSubjects() {
		ArrayList<Subject> result = new ArrayList<Subject>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Subjects._ID, DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME,
				DatabaseContract.Subjects.COLUMN_NAME_AVERAGE, DatabaseContract.Subjects.COLUMN_NAME_NOTE_NECESSARY,
				DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID, DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID,
				DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID };

		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, null, null, null, null,
				DatabaseContract.Subjects._ID + " DESC");

		cur.moveToFirst();

		for (int i = 0; i < cur.getCount(); ++i) {
			String subjectName = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME));

			String average = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_AVERAGE));
			String[] numberExms = cur.getString(
					cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID)).split(";");
			int courseId = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID));

			/**
			 * If the first parameter is empty String, numberExams or homeworks
			 * is 0
			 */
			int numberExams = 0;
			if (numberExms != null && !numberExms[0].trim().isEmpty())
				numberExams = numberExms.length;

			String[] numberHomw = cur.getString(
					cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID)).split(";");

			int numberHomework = 0;
			if (numberHomw != null && !numberHomw[0].trim().isEmpty())
				numberHomework = numberHomw.length;
			Course course = getCourseById(courseId);
			Subject newSubject = new Subject(subjectName, average, numberExams, numberHomework, course);
			result.add(newSubject);
			cur.moveToNext();
		}

		return result;
	}

	private Course getCourseById(int id) {
		String[] projection = { DatabaseContract.Courses.COLUMN_NAME_INIT_DATE,
				DatabaseContract.Courses.COLUMN_NAME_END_DATE, DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS,
				DatabaseContract.Courses.COLUMN_NAME_AVERAGE, DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME,
				DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS };
		String selection = DatabaseContract.Courses._ID + "=?";
		String[] args = { String.valueOf(id) };

		Cursor cur = db.query(DatabaseContract.Courses.TABLE_NAME, projection, selection, args, null, null,
				DatabaseContract.Courses._ID + " DESC");

		cur.moveToFirst();

		String initDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_INIT_DATE));
		String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_END_DATE));
		String subjectsIds = cur
				.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS));
		String name = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME));
		int numberOfSubjects = cur.getInt(cur
				.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS));
		double average = cur.getDouble(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_AVERAGE));

		Course currentCourse = new Course(id, name, numberOfSubjects, average, initDate, endDate, subjectsIds);

		return currentCourse;
	}

	private Subject getSubjectInDB(String subjectName) {
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Subjects._ID, DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID,
				DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID };

		String selection = DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME + "= ?";
		String[] args = { subjectName };

		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, selection, args, null, null,
				DatabaseContract.Subjects._ID + " DESC");

		cur.moveToFirst();
		int id = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Subjects._ID));
		String homeworkId = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID));
		String examsId = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID));
		int courseId = Integer.parseInt(cur.getString(cur
				.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID)));

		Course course = getCourseById(courseId);
		Subject currentSubject = new Subject(id, courseId, subjectName, homeworkId, examsId, course);

		return currentSubject;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
