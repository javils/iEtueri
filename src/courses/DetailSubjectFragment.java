package courses;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.OnClickButtonXml;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javils.ietueri.R;

import exams.Exam;
import exams.NewExamFragment;

public class DetailSubjectFragment extends Fragment implements OnClickButtonXml {

	private ViewGroup listHomeworks;
	private ViewGroup listExams;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private Subject subject;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_subjects, container, false);

		/** Get the instances of the views */
		listHomeworks = (LinearLayout) view.findViewById(R.id.layout_list_homeworks);
		listExams = (LinearLayout) view.findViewById(R.id.layout_list_exams);

		ArrayList<Exam> exams = getExamsOfSubject();

		for (int i = 0; i < exams.size(); i++)
			addExamToList(exams.get(i));

		return view;
	}

	private ArrayList<Exam> getExamsOfSubject() {
		ArrayList<Exam> result = new ArrayList<Exam>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] argsExamsIds = subject.getExamsId().split(";");
		String[] projection = { DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME,
				DatabaseContract.Exams.COLUMN_NAME_END_DATE, DatabaseContract.Exams.COLUMN_NAME_NOTE };

		for (int i = 0; i < argsExamsIds.length; i++) {
			String[] args = { argsExamsIds[i] };
			Cursor cur = db.query(DatabaseContract.Exams.TABLE_NAME, projection, DatabaseContract.Exams._ID + "= ?",
					args, null, null, DatabaseContract.Exams._ID + " DESC");

			cur.moveToFirst();

			if (argsExamsIds[i].isEmpty())
				continue;

			int examId = Integer.parseInt(argsExamsIds[i]);
			String examName = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME));
			int subjectId = subject.getId();
			String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_END_DATE));
			float note = cur.getFloat(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_NOTE));

			result.add(new Exam(examId, subjectId, examName, endDate, note));
		}

		return result;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}

	@Override
	public void onClickXml(View view) {
		switch (view.getId()) {
		case R.id.detail_subject_new_exam:
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_EXAM);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewExamFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
			break;
		case R.id.detail_subject_new_homework:
			break;
		}
	}

	/* Get and set for subject */
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public void addExamToList(Exam exam) {
		int id = R.layout.subject_detail_exam_item;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		TextView text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_name);
		text.setText(exam.getName());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_date);
		text.setText(exam.getEnd());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_hour);
		text.setText(exam.getInitHour() + " - " + exam.getEndHour());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_note);
		text.setText("" + exam.getNote());
		listExams.addView(linearLayout);
	}
}
