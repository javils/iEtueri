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

/** This class is for Courses View */
public class CoursesFragment extends Fragment {

	private ListView listCourses;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_courses, container, false);

		listCourses = (ListView) view.findViewById(R.id.list_courses);
		ArrayList<Course> courses = new ArrayList<Course>();
		courses = getCoursesInDB();
		// Examples before read the database
		/*
		 * courses.add(new Course("1º Semestre", 3, 4.5, "24/5/2013"));
		 * courses.add(new Course("2º Semestre", 4, 5.5, "25/6/2013"));
		 * courses.add(new Course("3º Semestre", 5, 6.5, "26/7/2013"));
		 * courses.add(new Course("4º Semestre", 6, 7.5, "27/8/2013"));
		 */
		CoursesAdapter adapter;
		if (courses != null) {
			adapter = new CoursesAdapter(getActivity(), R.layout.courses_list_item, courses);

			listCourses.setAdapter(adapter);
		}

		return view;
	}

	private ArrayList<Course> getCoursesInDB() {
		ArrayList<Course> result = new ArrayList<Course>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Courses._ID, DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME,
				DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS, DatabaseContract.Courses.COLUMN_NAME_AVERAGE };

		Cursor cur = db.query(DatabaseContract.Courses.TABLE_NAME, projection, null, null, null, null,
				DatabaseContract.Courses._ID + " DESC");

		cur.moveToFirst();

		for (int i = 0; i < cur.getCount(); ++i) {
			String courseName = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME));
			String numberOfSubjects = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS));
			String average = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_AVERAGE));
			Course newCourse = new Course(courseName, Integer.valueOf(numberOfSubjects), Double.valueOf(average));
			result.add(newCourse);
			cur.moveToNext();
		}

		return result;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
