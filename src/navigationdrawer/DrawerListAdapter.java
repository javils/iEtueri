package navigationdrawer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javils.ietueri.R;

/**
 * Adapter for the ListView of the Navigation Fragment
 * 
 * @author Javier
 * 
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerList> {

	/** List of the items of the navigation */
	private List<DrawerList> objects = new ArrayList<DrawerList>();
	/** Id of the item's ListView of the Navigation */
	private int resource;
	/** Context of the application */
	private Context context;
	/** Icon of the item's ListView of the Navigation */
	private ImageView drawerIcon;
	/** Title of the item */
	private TextView title;

	public DrawerListAdapter(Context context, int resource, List<DrawerList> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.objects = objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public DrawerList getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		// If don't have view, take it!
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resource, null);
		}

		DrawerList drawerItem = getItem(position);

		// Fill the structure of the item.
		drawerIcon = (ImageView) view.findViewById(R.id.drawer_list_item_icon);
		drawerIcon.setImageResource(drawerItem.getIdImage());
		title = (TextView) view.findViewById(R.id.drawer_list_item_title);
		title.setText(drawerItem.getIdText());

		return view;
	}
}
