package courses;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javils.ietueri.R;

/** This class is for Courses View */
public class CoursesFragment extends Fragment {

	private ListView listCourses;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_courses, container, false);

		listCourses = (ListView) view.findViewById(R.id.list_courses);
		ArrayList<Course> courses = new ArrayList<Course>();
		// Examples before read the database
		courses.add(new Course("1º Semestre", 3, 4.5, "24/5/2013"));
		courses.add(new Course("2º Semestre", 4, 5.5, "25/6/2013"));
		courses.add(new Course("3º Semestre", 5, 6.5, "26/7/2013"));
		courses.add(new Course("4º Semestre", 6, 7.5, "27/8/2013"));
		CoursesAdapter adapter = new CoursesAdapter(getActivity(), R.layout.courses_list_item, courses);

		listCourses.setAdapter(adapter);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
