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
 * Adapter for the the ListView of the Schedule
 * 
 * @author Javier Luque Sanabria
 * 
 */
public class CoursesAdapter extends ArrayAdapter<Course> {

	/** List of the events */
	private List<Course> events = new ArrayList<Course>();
	/** Id of the item's ListView of Schedule */
	private int resource;
	/** Context of the application */
	private Context context;
	/** Auxiliar TextView */
	private TextView text;

	public CoursesAdapter(Context context, int resource, List<Course> objects) {
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
	public Course getItem(int i) {
		return events.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void setItems(ArrayList<Course> items) {
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

		Course course = getItem(position);

		// Fill the structure of the item
		text = (TextView) view.findViewById(R.id.courses_list_item_course_name);
		text.setText("" + course.getCourseName());
		text = (TextView) view.findViewById(R.id.courses_list_item_number_subjects);
		text.setText(course.getNumberOfSubjects()
				+ (course.getNumberOfSubjects() != 1 ? " asignaturas" : " asignatura"));
		text = (TextView) view.findViewById(R.id.courses_list_item_course_average);
		text.setText("" + course.getAverage());
		text = (TextView) view.findViewById(R.id.course_courseId);
		text.setText("" + course.getId());

		return view;
	}
}
