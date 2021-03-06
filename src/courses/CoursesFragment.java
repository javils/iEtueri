package courses;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

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

		if (courses != null && courses.size() != 0) {
			final CoursesAdapter adapter = new CoursesAdapter(getActivity(), R.layout.courses_list_item, courses);
			listCourses.setAdapter(adapter);
			listCourses.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					TextView textName = (TextView) view.findViewById(R.id.courses_list_item_course_name);
					TextView textNSubjects = (TextView) view.findViewById(R.id.courses_list_item_number_subjects);
					TextView textAverage = (TextView) view.findViewById(R.id.courses_list_item_course_average);

					String courseName = textName.getText().toString();
					int numberOfSubjects = Integer.parseInt(textNSubjects.getText().toString().split(" ")[0]);
					double average = Double.parseDouble(textAverage.getText().toString());

					Course course = getCourseInDB(courseName, numberOfSubjects, average);

					FragmentManager fragmentManager = getFragmentManager();
					Fragment newFragment = NavigationDrawerController
							.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_COURSE);
					if (newFragment instanceof OnClickButtonXml)
						MainActivity.setOnClickFragment(newFragment);
					MainActivity.setCurrentFragment(newFragment);
					((CourseDetailFragment) newFragment).setCourse(course);
					fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

					/** Close the DB */
					db.close();
					dbHelper.close();
				}
			});

			listCourses.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					TextView textName = (TextView) view.findViewById(R.id.courses_list_item_course_name);
					TextView textNSubjects = (TextView) view.findViewById(R.id.courses_list_item_number_subjects);
					TextView textAverage = (TextView) view.findViewById(R.id.courses_list_item_course_average);
					TextView tIdCourse = (TextView) view.findViewById(R.id.course_courseId);

					String idCourse = tIdCourse.getText().toString();

					String courseName = textName.getText().toString();
					int numberOfSubjects = Integer.parseInt(textNSubjects.getText().toString().split(" ")[0]);
					double average = Double.parseDouble(textAverage.getText().toString());
					removeCourse(getActivity(), courseName, numberOfSubjects, average, idCourse);

					adapter.remove(adapter.getItem(position));
					adapter.notifyDataSetChanged();

					return true;
				}
			});
		} else {
			view = inflater.inflate(R.layout.fragment_void, container, false);
			TextView description = (TextView) view.findViewById(R.id.fragment_void_description);
			description.setText(R.string.courses_description);
		}

		return view;
	}

	private void removeCourse(Activity activity, String courseName, int numberOfSubject, double average, String idCourse) {
		dbHelper = new DatabaseHelper(activity);
		db = dbHelper.getReadableDatabase();
		String[] projection = { DatabaseContract.Subjects._ID };
		String[] args = { idCourse };

		Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection,
				DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID + "= ?", args, null, null, null);

		cur.moveToFirst();

		for (int i = 0; i < cur.getCount(); i++) {
			Course course = getCourseInDB(courseName, numberOfSubject, average);
			CourseDetailFragment.removeSubject(activity,
					cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Subjects._ID)), course);
			cur.moveToNext();
		}

		db.delete(DatabaseContract.Courses.TABLE_NAME, DatabaseContract.Courses._ID + "=" + idCourse, null);

		cur.close();

		dbHelper.close();
		db.close();

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

	private Course getCourseInDB(String name, int numberOfSubjects, double average) {
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Courses._ID, DatabaseContract.Courses.COLUMN_NAME_INIT_DATE,
				DatabaseContract.Courses.COLUMN_NAME_END_DATE, DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS };
		String selection = DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME + "=?  AND "
				+ DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS + "=? AND "
				+ DatabaseContract.Courses.COLUMN_NAME_AVERAGE + "=? ";
		String[] args = { name, String.valueOf(numberOfSubjects), String.valueOf(average) };

		Cursor cur = db.query(DatabaseContract.Courses.TABLE_NAME, projection, selection, args, null, null,
				DatabaseContract.Courses._ID + " DESC");

		cur.moveToFirst();

		int id = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Courses._ID));
		String initDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_INIT_DATE));
		String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_END_DATE));
		String subjectsIds = cur
				.getString(cur.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS));

		Course currentCourse = new Course(id, name, numberOfSubjects, average, initDate, endDate, subjectsIds);

		return currentCourse;
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
