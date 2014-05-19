package courses;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.javils.ietueri.R;

public class DetailSubjectFragment extends Fragment implements OnClickButtonXml {

	private ViewGroup listHomeworks;
	private ViewGroup listExams;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_subjects, container, false);

		/** Get the instances of the views */
		listHomeworks = (LinearLayout) view.findViewById(R.id.layout_list_homeworks);
		listExams = (LinearLayout) view.findViewById(R.id.layout_list_exams);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	@Override
	public void onClickXml(View view) {
		switch (view.getId()) {
		case R.id.detail_subject_new_exam:
			break;
		case R.id.detail_subject_new_homework:
			break;
		}
	}
}
