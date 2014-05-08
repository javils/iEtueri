package courses;

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
}
