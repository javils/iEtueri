package navigationdrawer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.javils.ietueri.R;

public class MainActivity extends Activity implements NavigationDrawerCallbacks {

	// Fragment managing the behaviors, interactions and presentation of the
	// navigation drawer.
	private NavigationDrawerFragment navigationDrawerFragment;
	private CharSequence title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(
				R.id.navigation_drawer);
		// Initialize navigation transactions
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		title = getTitle();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.navigation_drawer_container, NavigationDrawerController.newInstance(position + 1))
				.commit();
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
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Only show items in the action bar relevant to this screen if the
		// drawer is not showing.
		if (navigationDrawerFragment.isDrawerOpen()) {
			// TODO: Add the menu of each section here.
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

		// TODO: Add handle action for each item in the action bar here.
		return super.onOptionsItemSelected(item);
	}

}
