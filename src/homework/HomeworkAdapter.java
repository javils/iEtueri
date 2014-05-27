package homework;

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
public class HomeworkAdapter extends ArrayAdapter<Homework> {

	/** List of the events */
	private List<Homework> events = new ArrayList<Homework>();
	/** Id of the item's ListView of Schedule */
	private int resource;
	/** Context of the application */
	private Context context;
	/** Auxiliar TextView */
	private TextView text;

	public HomeworkAdapter(Context context, int resource, List<Homework> objects) {
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
	public Homework getItem(int i) {
		return events.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void setItems(ArrayList<Homework> items) {
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

		Homework homework = getItem(position);

		// Fill the structure of the item
		TextView text = (TextView) view.findViewById(R.id.detail_subject_homework_name);
		text.setText(homework.getName());
		text = (TextView) view.findViewById(R.id.detail_subject_homework_enddate);
		text.setText(homework.getEnd());
		text = (TextView) view.findViewById(R.id.detail_subject_homework_note);
		text.setText("" + homework.getNote());

		/** Get the id of the homework in the view */
		text = (TextView) view.findViewById(R.id.detail_subject_homework_homeworkId);
		text.setText("" + homework.getId());

		return view;
	}
}
