package homework;

import java.util.ArrayList;
import java.util.Collections;

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

public class HomeworkFragment extends Fragment {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ListView listHomework;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homework, container, false);

		/** Get the instances of the views */
		listHomework = (ListView) view.findViewById(R.id.list_homework);

		ArrayList<Homework> homework = getAllHomework();
		HomeworkAdapter adapter = new HomeworkAdapter(getActivity(), R.layout.subject_detail_homework_item, homework);
		listHomework.setAdapter(adapter);

		return view;
	}

	private ArrayList<Homework> getAllHomework() {
		ArrayList<Homework> result = new ArrayList<Homework>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] projection = { DatabaseContract.Homework._ID, DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID,
				DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME, DatabaseContract.Homework.COLUMN_NAME_END_DATE,
				DatabaseContract.Homework.COLUMN_NAME_NOTE, DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION,
				DatabaseContract.Homework.COLUMN_NAME_PRIORITY };

		Cursor cur = db.query(DatabaseContract.Homework.TABLE_NAME, projection, null, null, null, null, null);

		cur.moveToFirst();
		for (int i = 0; i < cur.getCount(); i++) {

			int homeworkId = Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Homework._ID)));
			String homeworkName = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME));
			int subjectId = Integer.parseInt(cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID)));
			String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_END_DATE));
			float note = cur.getFloat(cur.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_NOTE));
			String description = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION));
			int priority = cur.getInt(cur.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_PRIORITY));
			result.add(new Homework(homeworkId, subjectId, description, homeworkName, endDate, note, priority));
		}

		Collections.sort(result);
		return result;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments()
				.getInt(NavigationDrawerController.ARG_SECTION_NUMBER));
	}
}
