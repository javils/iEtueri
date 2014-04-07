package schedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.javils.ietueri.R;

/**
 * Adapter for the the ListView of the Schedule
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class ScheduleTodayAdapter extends ArrayAdapter<Event> {

	/** List of the events */
	private List<Event> events = new ArrayList<Event>();
	/** Id of the item's ListView of Schedule */
	private int resource;
	/** Context of the application */
	private Context context;
	/** Auxiliar TextView */
	private TextView text;

	public ScheduleTodayAdapter(Context context, int resource, List<Event> objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.context = context;
		this.events = objects;
	}

	@Override
	public int getCount() {
		return events.size();
	}

	@Override
	public Event getItem(int i) {
		return events.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void setItems(ArrayList<Event> items) {
		this.events = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		// If don't have view, take it!
		if (view == null) {
			LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout.inflate(resource, null);
		}

		Event event = getItem(position);

		// Fill the structure of the item
		text = (TextView) view.findViewById(R.id.schedule_list_item_nameevent);
		text.setText(event.getName());
		text = (TextView) view.findViewById(R.id.schedule_list_item_place);
		text.setText(event.getPlace());
		text = (TextView) view.findViewById(R.id.schedule_list_item_date);
		text.setText(event.getDiff());

		return view;
	}
}
