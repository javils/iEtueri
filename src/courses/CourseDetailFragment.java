package courses;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javils.ietueri.R;

public class CourseDetailFragment extends Fragment {

	private Course course;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_courses, container, false);

		return view;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
