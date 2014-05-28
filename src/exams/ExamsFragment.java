package exams;

import java.util.ArrayList;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.javils.ietueri.R;

public class ExamsFragment extends Fragment {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ListView listExams;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homework, container, false);

		/** Get the instances of the views */
		listExams = (ListView) view.findViewById(R.id.list_homework);

		ArrayList<Exam> exams = getAllExams();
		final ExamsAdapter adapter = new ExamsAdapter(getActivity(), R.layout.subject_detail_exam_item, exams);
		listExams.setAdapter(adapter);

		listExams.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tIdExam = (TextView) view.findViewById(R.id.detail_subject_exams_examId);
				String idExam = tIdExam.getText().toString();

				String[] projection = { DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID };

				String[] argsExamId = { idExam };
				Cursor cur = db.query(DatabaseContract.Exams.TABLE_NAME, projection,
						DatabaseContract.Exams._ID + "= ?", argsExamId, null, null, null);

				cur.moveToFirst();

				String subjectId = cur.getString(cur
						.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID));

				db.delete(DatabaseContract.Exams.TABLE_NAME, DatabaseContract.Exams._ID + "=" + idExam, null);

				cur.close();

				/** Quit the exam id on subject */
				projection = new String[] { DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID };

				String[] argsId = { String.valueOf(subjectId) };
				cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, DatabaseContract.Subjects._ID + "= ?",
						argsId, null, null, null);

				cur.moveToFirst();
				String sExamsIds = cur.getString(cur
						.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID));
				String[] examIds = sExamsIds.split(";");
				String result = new String();
				for (int i = 0; i < examIds.length; i++) {
					if (examIds[i].isEmpty())
						continue;

					if (!examIds[i].equals(idExam))
						result += examIds[i] + ";";
				}

				/** Update with the new values */
				ContentValues values = new ContentValues();
				values.put(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, result);
				db.update(DatabaseContract.Subjects.TABLE_NAME, values,
						DatabaseContract.Subjects._ID + "=" + subjectId, null);

				adapter.remove(adapter.getItem(position));

				adapter.notifyDataSetChanged();
				return true;
			}
		});

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
