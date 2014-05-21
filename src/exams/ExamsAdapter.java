package exams;

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
public class ExamsAdapter extends ArrayAdapter<Exam> {

	/** List of the exams */
	private List<Exam> exams = new ArrayList<Exam>();
	/** Id of the item's ListView of DetailSubject */
	private int resource;
	/** Context of the application */
	private Context context;
	/** Auxiliar TextView */
	private TextView text;

	public ExamsAdapter(Context context, int resource, List<Exam> objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.context = context;
		this.exams = objects;
	}

	@Override
	public int getCount() {
		return exams.size();
	}

	@Override
	public Exam getItem(int i) {
		return exams.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void setItems(ArrayList<Exam> items) {
		this.exams = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		// If don't have view, take it!
		if (view == null) {
			LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout.inflate(resource, null);
		}

		Exam exam = getItem(position);

		// Fill the structure of the item
		text = (TextView) view.findViewById(R.id.detail_subject_exams_name);
		text.setText(exam.getName());
		text = (TextView) view.findViewById(R.id.detail_subject_exams_date);
		text.setText(exam.getEnd());
		text = (TextView) view.findViewById(R.id.detail_subject_exams_hour);
		text.setText(exam.getInitHour() + "-" + exam.getEndHour());
		text = (TextView) view.findViewById(R.id.detail_subject_exams_note);
		text.setText("" + exam.getNote());

		return view;
	}
}
