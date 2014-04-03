package navigationdrawer;

/**
 * Created by Javier Luque Sanabria on 3/04/14. Callbacks interface that all
 * activities using this fragment must implement.
 */
public interface NavigationDrawerCallbacks {
	/**
	 * Called when an item in the navigation drawer is selected.
	 */
	void onNavigationDrawerItemSelected(int position);
}
