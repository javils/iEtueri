package navigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Class for controller Views of the Navigation Fragment.
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class NavigationDrawerController extends Fragment {

	/** Constants that represent the sections of the Navigation */
	public static final int SECTION_NUMBER_TODAY = 0;
	public static final int SECTION_NUMBER_SCHEDULE = 1;
	public static final int SECTION_NUMBER_HOMEWORKS = 2;
	public static final int SECTION_NUMBER_EXAMS = 3;
	public static final int SECTION_NUMBER_COURSES = 4;
	public static final int SECTION_NUMBER_SUBJECTS = 5;

	/** The fragment argument representing the section number for this fragment. */
	private static final String ARG_SECTION_NUMBER = "section_number";
	/** The current section */
	private static int section;

	/** Pointer at the current section */
	private View rootView;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 * 
	 * @param section
	 *            number of the section that we need show
	 * @return fragment of the section that we need
	 */
	public static NavigationDrawerController newInstance(int section) {
		NavigationDrawerController fragment = new NavigationDrawerController();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, section);
		fragment.setArguments(args);
		NavigationDrawerController.section = section;

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		switch (section) {
		case SECTION_NUMBER_TODAY: // TODAY SECTION
			break;
		case SECTION_NUMBER_SCHEDULE: // SCHEDULE SECTION
			break;
		case SECTION_NUMBER_HOMEWORKS: // HOMEWORKS SECTION
			break;
		case SECTION_NUMBER_EXAMS: // EXAMS SECTION
			break;
		case SECTION_NUMBER_COURSES: // COURSES SECTION
			break;
		case SECTION_NUMBER_SUBJECTS: // SUBJECTS SECTION
			break;
		default:
			Toast.makeText(getActivity(), "If you are here, there crash!", Toast.LENGTH_SHORT).show();
			break;
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
}
