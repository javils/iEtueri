package courses;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import subject.DetailSubjectFragment;
import subject.Subject;
import subject.SubjectListAdapter;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.OnBackPressed;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.javils.ietueri.R;

public class CourseDetailFragment extends Fragment implements OnBackPressed {

	private ListView listSubjects;
	private Course course;
	private static DatabaseHelper dbHelper;
	private static SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_courses, container, false);

		/** Get the instances of the views */
		listSubjects = (ListView) view.findViewById(R.id.list_detail_course);
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		subjects = getSubjectsInDB();

		if (subjects != null && subjects.size() != 0) {

			final SubjectListAdapter adapter = new SubjectListAdapter(getActivity(), R.layout.subject_list_item,
					subjects);
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
							NavigationDrawerController.COURSE_DETAIL_SECTION);
					if (newFragment instanceof OnClickButtonXml)
						MainActivity.setOnClickFragment(newFragment);
					MainActivity.setCurrentFragment(newFragment);
					((DetailSubjectFragment) newFragment).setSubject(currentSubject);

					fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

				}
			});

			listSubjects.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

					TextView tIdSubject = (TextView) view.findViewById(R.id.detail_course_subject_subjectId);
					String idSubject = tIdSubject.getText().toString();
					removeSubject(getActivity(), idSubject, course);

					adapter.remove(adapter.getItem(position));
					adapter.notifyDataSetChanged();

					return true;
				}
			});
		} else {
			view = inflater.inflate(R.layout.fragment_void, container, false);
			TextView description = (TextView) view.findViewById(R.id.fragment_void_description);
			description.setText(R.string.subjects_description);
		}

		return view;
	}

	public static void removeSubject(Activity activity, String idSubject, Course course) {
		dbHelper = new DatabaseHelper(activity);
		db = dbHelper.getReadableDatabase();
		String[] projection = { DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID };
		String[] args = { idSubject };

		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, DatabaseContract.Subjects._ID + "= ?",
				args, null, null, null);

		cur.moveToFirst();

		String courseId = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID));

		db.delete(DatabaseContract.Subjects.TABLE_NAME, DatabaseContract.Subjects._ID + "=" + idSubject, null);

		cur.close();

		/** Update courses */
		course.setNumberOfSubjects(course.getNumberOfSubjects() - 1);
		ContentValues values = new ContentValues();
		values.put(DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS, course.getNumberOfSubjects());

		db.update(DatabaseContract.Courses.TABLE_NAME, values, DatabaseContract.Courses._ID + "=" + courseId, null);

		/** Remove all the homework and exams of the subject */
		db.delete(DatabaseContract.Homework.TABLE_NAME, DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID + "="
				+ idSubject, null);
		db.delete(DatabaseContract.Exams.TABLE_NAME, DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID + "=" + idSubject,
				null);

		dbHelper.close();
		db.close();

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
			int subjectId = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Subjects._ID));
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
			if (numberExms != null && !numberExms[0].trim().isEmpty())
				numberExams = numberExms.length;

			String[] numberHomw = cur.getString(
					cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID)).split(";");

			int numberHomework = 0;
			if (numberHomw != null && !numberHomw[0].trim().isEmpty())
				numberHomework = numberHomw.length;

			Subject newSubject = new Subject(subjectName, average, numberExams, numberHomework, course);
			newSubject.setId(subjectId);
			result.add(newSubject);
			cur.moveToNext();
		}

		/** Close the DB */
		db.close();
		dbHelper.close();

		return result;
	}

	private Subject getSubjectInDB(String subjectName) {
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Subjects._ID, DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID,
				DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, DatabaseContract.Subjects.COLUMN_NAME_AVERAGE };

		String selection = DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME + "= ?";
		String[] args = { subjectName };

		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, selection, args, null, null,
				DatabaseContract.Subjects._ID + " DESC");

		cur.moveToFirst();
		int id = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Subjects._ID));
		String homeworkId = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID));
		String examsId = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID));
		float average = cur.getFloat(cur.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_AVERAGE));

		Subject currentSubject = new Subject(id, course.getId(), subjectName, homeworkId, examsId, course);
		currentSubject.setNote(average);

		/** Close the DB */
		db.close();
		dbHelper.close();

		return currentSubject;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return this.course;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_COURSES);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

		/** Close the DB */
		db.close();
		dbHelper.close();
	}
}
