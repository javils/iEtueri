package exams;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javils.ietueri.R;

public class ExamsFragment extends Fragment {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ListView listHomework;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homework, container, false);

		/** Get the instances of the views */
		listHomework = (ListView) view.findViewById(R.id.list_homework);

		ArrayList<Exam> exams = getAllExams();
		ExamsAdapter adapter = new ExamsAdapter(getActivity(), R.layout.subject_detail_exam_item, exams);
		listHomework.setAdapter(adapter);

		return view;
	}

	private ArrayList<Exam> getAllExams() {
		ArrayList<Exam> result = new ArrayList<Exam>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Exams._ID, DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID,
				DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME, DatabaseContract.Exams.COLUMN_NAME_END_DATE,
				DatabaseContract.Exams.COLUMN_NAME_NOTE };

		Cursor cur = db.query(DatabaseContract.Exams.TABLE_NAME, projection, null, null, null, null, null);
		cur.moveToFirst();
		for (int i = 0; i < cur.getCount(); i++) {

			int examId = Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams._ID)));
			String examName = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME));
			int subjectId = Integer.parseInt(cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID)));
			String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_END_DATE));
			float note = cur.getFloat(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_NOTE));

			result.add(new Exam(examId, subjectId, examName, endDate, note));

			cur.moveToNext();
		}

		return result;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
