package navigationdrawer;

import schedule.EventsManager;
import schedule.RefreshScheduleEventsData;
import utility.OnClickButtonXml;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.javils.ietueri.R;

public class MainActivity extends Activity implements NavigationDrawerCallbacks {

	// Fragment managing the behaviors, interactions and presentation of the
	// navigation drawer.
	private NavigationDrawerFragment navigationDrawerFragment;
	private CharSequence title;

	private static OnClickButtonXml fragment;
	private static Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(
				R.id.navigation_drawer);
		// Initialize navigation transactions
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		title = getTitle();
		// TODO: Excute only in the first execution
		// CalendarManager.createCalendar(getApplicationContext());

		new Thread(new RefreshScheduleEventsData(getApplicationContext())).start();

		while (EventsManager.isThreadfinish() == false) {
			// TODO: Make animation with progressbar or something else.
		}

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController.newInstance(position + 1);
		currentFragment = newFragment;
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.fragment = (OnClickButtonXml) newFragment;
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
	}

	public void onSectionAttached(int section) {
		switch (section) {
		case NavigationDrawerController.SECTION_NUMBER_TODAY:
			title = getString(R.string.title_today);
			break;
		case NavigationDrawerController.SECTION_NUMBER_SCHEDULE:
			title = getString(R.string.title_schedule);
			break;
		case NavigationDrawerController.SECTION_NUMBER_HOMEWORKS:
			title = getString(R.string.title_homework);
			break;
		case NavigationDrawerController.SECTION_NUMBER_EXAMS:
			title = getString(R.string.title_exam);
			break;
		case NavigationDrawerController.SECTION_NUMBER_COURSES:
			title = getString(R.string.title_course);
			break;
		case NavigationDrawerController.SECTION_NUMBER_SUBJECTS:
			title = getString(R.string.title_subject);
			break;
		case NavigationDrawerController.SECTION_NUMBER_NEW_EVENT_TODAY:
			title = getString(R.string.title_today_new_event);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Only show items in the action bar relevant to this screen if the
		// drawer is not showing.
		if (!navigationDrawerFragment.isDrawerOpen()) {
			// TODO: Add the menu of each section here.

			/** Today Menu */
			if (title.equals(getString(R.string.title_today)))
				getMenuInflater().inflate(R.menu.menu_today, menu);

			ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(title);

			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.

		int id = item.getItemId();

		// first, call onPrepareOptionsMenu() for reset the ActionBar
		invalidateOptionsMenu();

		/** Today Menu */
		if (id == R.id.menu_today_menu_new) {
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_EVENT_TODAY);
			currentFragment = newFragment;
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.fragment = (OnClickButtonXml) newFragment;
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

			return true;
		}

		// TODO: Add handle action for each item in the action bar here.
		return super.onOptionsItemSelected(item);
	}

	public static void setOnClickFragment(Fragment fragment) {
		if (fragment instanceof OnClickButtonXml)
			MainActivity.fragment = (OnClickButtonXml) fragment;
	}

	public void onClickButton(View view) {
		if (fragment != null)
			fragment.onClickXml(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (currentFragment != null)
			currentFragment.onActivityResult(requestCode, resultCode, data);
	}

}
