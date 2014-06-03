package navigationdrawer;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.javils.ietueri.R;

/**
 * Class for the Navigation Drawer Fragment
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class NavigationDrawerFragment extends Fragment {
	/** Remember the position of the selected item */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/** A pointer to the current callbacks instance (the Activity) */
	private NavigationDrawerCallbacks callBacks;

	/** Helper component that ties the action bar to the navigation drawer. */
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private View fragmentContainerView;

	private int currentSelectedPosition;
	private boolean fromSavedInstanceState;
	private boolean userLearnedDrawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the preferences
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			fromSavedInstanceState = true;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		drawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});

		drawerListView.addHeaderView(inflater.inflate(R.layout.header_navigation_drawer, container, false));

		ArrayList<DrawerList> drawerList = new ArrayList<DrawerList>();

		// Make the elements of the menu.
		drawerList.add(new DrawerList(R.drawable.new_today, R.string.title_today));
		drawerList.add(new DrawerList(R.drawable.new_schedule, R.string.title_schedule));
		drawerList.add(new DrawerList(R.drawable.new_homework, R.string.title_homework));
		drawerList.add(new DrawerList(R.drawable.new_exams, R.string.title_exam));
		drawerList.add(new DrawerList(R.drawable.ic_courses, R.string.title_course));
		drawerList.add(new DrawerList(R.drawable.new_subjects, R.string.title_subject));

		// Fill all the ListView with the values of drawerList
		DrawerListAdapter adapter = new DrawerListAdapter(getActivity(), R.layout.drawer_list_item, drawerList);
		drawerListView.setAdapter(adapter);

		drawerListView.setItemChecked(currentSelectedPosition, true);

		return drawerListView;
	}

	/**
	 * This fragment must call this method to set up the navigation drawer
	 * interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		this.fragmentContainerView = getActivity().findViewById(fragmentId);
		this.drawerLayout = drawerLayout;

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.drawable.ic_drawer, 0, 0) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				if (!isAdded())
					return;

				// call onPrepareOptionsMenu()
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

				if (!isAdded())
					return;

				// The user manually opened the drawer; store this flag to
				// prevent auto-showing the navigation drawer automatically in
				// the future.
				if (!userLearnedDrawer) {
					userLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				// call onPrepareOptionsMenu()
				getActivity().invalidateOptionsMenu();
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer, per the navigation drawer design guidelines.
		if (!userLearnedDrawer | !fromSavedInstanceState)
			this.drawerLayout.openDrawer(fragmentContainerView);

		// Defer code dependent on restoration of previous instance state.
		this.drawerLayout.post(new Runnable() {

			@Override
			public void run() {
				drawerToggle.syncState();
			}
		});

		this.drawerLayout.setDrawerListener(drawerToggle);
	}

	/**
	 * Select the item in the ListView, close Navigation and set checked the
	 * option selected.
	 * 
	 * @param position
	 *            number of the item position in the menu.
	 */
	private void selectItem(int position) {
		currentSelectedPosition = position;

		if (drawerListView != null)
			drawerListView.setItemChecked(position, true);

		if (drawerLayout != null)
			drawerLayout.closeDrawer(fragmentContainerView);

		if (callBacks != null)
			callBacks.onNavigationDrawerItemSelected(position);

	}

	/**
	 * Return if the navigation is open or close
	 * 
	 * @return true if navigation is open, false in other case
	 */
	public boolean isDrawerOpen() {
		return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callBacks = (NavigationDrawerCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callBacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Forward the new configuration the drawer toggle component.
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, set in the action bar the title of the app.
		if (isDrawerOpen()) {
			ActionBar actionBar = getActivity().getActionBar();
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setTitle(R.string.app_name);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}
}
