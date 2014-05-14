package courses;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javils.ietueri.R;

public class CourseDetailFragment extends Fragment {

	private ListView listSubjects;
	private Course course;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_courses, container, false);

		/** Get the instances of the views */
		listSubjects = (ListView) view.findViewById(R.id.list_detail_course);
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		subjects = getSubjectsInDB();

		SubjectListAdapter adapter;

		if (subjects != null) {
			adapter = new SubjectListAdapter(getActivity(), R.layout.subject_list_item, subjects);
			listSubjects.setAdapter(adapter);
		}

		return view;
	}

	private ArrayList<Subject> getSubjectsInDB() {
		ArrayList<Subject> result = new ArrayList<Subject>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Subjects._ID, DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME,
				DatabaseContract.Subjects.COLUMN_NAME_AVERAGE, DatabaseContract.Subjects.COLUMN_NAME_NOTE_NECESSARY,
				DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID, DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID };

		String[] argsSelection = { String.valueOf(course.getId()) };
		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection,
				DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID + "= ?", argsSelection, null, null,
				DatabaseContract.Subjects._ID + " DESC");

		cur.moveToFirst();

		for (int i = 0; i < cur.getCount(); ++i) {
			String subjectName = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME));

			String average = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_AVERAGE));
			String[] numberExms = cur.getString(
					cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID)).split(";");

			/**
			 * If the first parameter is empty String, numberExams or homeworks
			 * is 0
			 */
			int numberExams = 0;
			if (numberExms != null && !numberExms[0].equals(""))
				numberExams = numberExms.length;

			String[] numberHomw = cur.getString(
					cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID)).split(";");

			int numberHomework = 0;
			if (numberHomw != null && !numberHomw[0].equals(""))
				numberHomework = numberHomw.length;

			Subject newSubject = new Subject(subjectName, average, numberExams, numberHomework);
			result.add(newSubject);
			cur.moveToNext();
		}

		return result;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return this.course;
	}

	@Override
	public void onStop() {
		super.onStop();
		db.close();
		dbHelper.close();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
