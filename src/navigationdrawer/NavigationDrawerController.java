package navigationdrawer;

import schedule.ScheduleFragment;
import today.NewEventTodayFragment;
import today.TodayFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Class for controller Views of the Navigation Fragment.
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class NavigationDrawerController {

	/** Constants that represent the sections of the Navigation */
	public static final int SECTION_NUMBER_TODAY = 1;
	public static final int SECTION_NUMBER_SCHEDULE = 2;
	public static final int SECTION_NUMBER_HOMEWORKS = 3;
	public static final int SECTION_NUMBER_EXAMS = 4;
	public static final int SECTION_NUMBER_COURSES = 5;
	public static final int SECTION_NUMBER_SUBJECTS = 6;

	/** Secondary Views */
	public static final int SECTION_NUMBER_NEW_EVENT_TODAY = 7;

	/** The fragment argument representing the section number for this fragment. */
	public static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 * 
	 * @param section
	 *            number of the section that we need show
	 * @return fragment of the section that we need
	 */
	public static Fragment newInstance(int section) {
		Fragment fragment = chooseFragment(section);
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, section);
		fragment.setArguments(args);

		return fragment;
	}

	/**
	 * Choose the correct fragment in each case, this is for not create only the
	 * View, we need create ALL the fragment for not break cyclelife of the
	 * Activity and Fragment
	 */
	public static Fragment chooseFragment(int section) {
		Fragment newFragment = new Fragment();

		switch (section) {
		case SECTION_NUMBER_TODAY: // TODAY SECTION
			newFragment = new TodayFragment();
			break;
		case SECTION_NUMBER_SCHEDULE: // SCHEDULE SECTION
			newFragment = new ScheduleFragment();
			break;
		case SECTION_NUMBER_HOMEWORKS: // HOMEWORKS SECTION
			break;
		case SECTION_NUMBER_EXAMS: // EXAMS SECTION
			break;
		case SECTION_NUMBER_COURSES: // COURSES SECTION
			break;
		case SECTION_NUMBER_SUBJECTS: // SUBJECTS SECTION
			break;
		case SECTION_NUMBER_NEW_EVENT_TODAY: // NEW EVENT TODAY SECTION
			newFragment = new NewEventTodayFragment();
			break;
		default:
			Toast.makeText(newFragment.getActivity(), "If you are here, then crash!", Toast.LENGTH_SHORT).show();
			break;
		}

		return newFragment;
	}
}
