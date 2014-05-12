package courses;

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
 * Created by Javier Luque Sanabria on 19/02/14.
 */
public class SubjectListAdapter extends ArrayAdapter<Subject> {

	private List<Subject> objects = new ArrayList<Subject>();
	private int resource;
	private Context context;
	private TextView text;

	public SubjectListAdapter(Context context, int resource, List<Subject> objects) {
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
	public Subject getItem(int i) {
		return objects.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		// If don't have view, take it!
		if (view == null) {
			LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout.inflate(resource, null);
		}

		Subject subject = getItem(position);

		// Fill the structure of the item
		text = (TextView) view.findViewById(R.id.subject_list_item_subject_name);
		text.setText(subject.getName());
		text = (TextView) view.findViewById(R.id.subject_list_item_subject_average);
		text.setText("Media: " + subject.getNote());
		text = (TextView) view.findViewById(R.id.subject_list_item_subject_note_necessary);
		text.setText("Necesitas: " + subject.getNoteNecessary());
		text = (TextView) view.findViewById(R.id.subject_list_item_subject_number_tasks);
		text.setText("Tareas: " + subject.getNumberOfTasks());
		text = (TextView) view.findViewById(R.id.subject_list_item_subject_number_exams);
		text.setText("Examenes: " + subject.getNumberOfExams());

		return view;
	}
}