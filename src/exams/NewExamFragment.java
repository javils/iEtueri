package exams;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.DatePickerDialogFragment;
import utility.OnClickButtonXml;
import utility.TimePickerDialogFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javils.ietueri.R;

import courses.DetailSubjectFragment;
import courses.Subject;

public class NewExamFragment extends Fragment implements OnClickButtonXml {

	private EditText examName;
	private Button date;
	private Button fromHour;
	private Button toHour;
	private EditText note;

	private Subject subject;
	private Exam examToEdit;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_exam, container, false);

		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getWritableDatabase();

		/** Get the instances of the views */
		examName = (EditText) view.findViewById(R.id.newexam_exam_name);
		date = (Button) view.findViewById(R.id.newexam_end_date);
		fromHour = (Button) view.findViewById(R.id.newexam_from_hour);
		toHour = (Button) view.findViewById(R.id.newexam_to_hour);
		note = (EditText) view.findViewById(R.id.newexam_note);

		if (examToEdit != null) {
			examName.setText(examToEdit.getName());
			date.setHint(examToEdit.getEnd());
			fromHour.setHint(examToEdit.getInitHour());
			toHour.setHint(examToEdit.getEndHour());
			note.setText("" + examToEdit.getNote());
		}

		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		date.setHint(day + "/" + (month + 1) + "/" + year);
		fromHour.setHint(hour + ":" + minute);
		toHour.setHint((hour + 1) + ":" + minute);

		return view;
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
		case R.id.newexam_end_date:
			showDatePickerDialog(view);
			break;
		case R.id.newexam_from_hour:
		case R.id.newexam_to_hour:
			showTimePickerDialog(view);
			break;
		case R.id.newexam_cancel:
			cancelExam();
			break;
		case R.id.newexam_acept:
			if (examToEdit == null)
				addNewExam();
			else
				updateExam();
			break;
		}
	}

	public void cancelExam() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		((DetailSubjectFragment) newFragment).setSubject(subject);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
	}

	public void addNewExam() {
		if (examName.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de examen. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			/** Fill the database with the data */
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME, examName.getText().toString());
			date.setText(date.getText().toString().replaceAll("/", "-"));
			values.put(DatabaseContract.Exams.COLUMN_NAME_END_DATE, fromHour.getHint().toString() + "-"
					+ toHour.getHint().toString() + "-" + date.getHint().toString());
			float inote = 0;
			if (!note.getText().toString().isEmpty())
				inote = Float.valueOf(note.getText().toString());
			values.put(DatabaseContract.Exams.COLUMN_NAME_NOTE, inote);
			values.put(DatabaseContract.Exams.COLUMN_NAME_NOTE_NECESSARY, 0.0);
			values.put(DatabaseContract.Exams.COLUMN_NAME_DONE, false);
			values.put(DatabaseContract.Exams.COLUMN_NAME_PONDERATION, -1);
			values.put(DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID, subject.getId());

			db.insert(DatabaseContract.Exams.TABLE_NAME, null, values);

			values.clear();
			subject.setNumberOfExams(subject.getNumberOfExams() + 1);
			subject.setExamsId(subject.getExamsId() + ";"
					+ DatabaseHelper.getCountRow(db, DatabaseContract.Exams.TABLE_NAME));
			values.put(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, subject.getExamsId());
			String[] args = { String.valueOf(subject.getId()) };
			db.update(DatabaseContract.Subjects.TABLE_NAME, values, DatabaseContract.Subjects._ID + "=?", args);

			/** Back to Courses view */
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((DetailSubjectFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
		}
	}

	public void updateExam() {
		if (examName.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de examen. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			/** Fill the database with the data */
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME, examName.getText().toString());
			date.setText(date.getText().toString().replaceAll("/", "-"));
			values.put(DatabaseContract.Exams.COLUMN_NAME_END_DATE, fromHour.getHint().toString() + "-"
					+ toHour.getHint().toString() + "-" + date.getHint().toString());
			float inote = 0;
			if (!note.getText().toString().isEmpty())
				inote = Float.valueOf(note.getText().toString());
			values.put(DatabaseContract.Exams.COLUMN_NAME_NOTE, inote);
			values.put(DatabaseContract.Exams.COLUMN_NAME_NOTE_NECESSARY, 0.0);
			values.put(DatabaseContract.Exams.COLUMN_NAME_DONE, false);
			values.put(DatabaseContract.Exams.COLUMN_NAME_PONDERATION, -1);

			String[] id = { "" + examToEdit.getId() };
			db.update(DatabaseContract.Exams.TABLE_NAME, values, DatabaseContract.Exams._ID + "=?", id);

			/** Back to Courses view */
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((DetailSubjectFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
		}
	}

	public void showTimePickerDialog(View view) {
		TimePickerDialogFragment timeFragment = new TimePickerDialogFragment();
		timeFragment.setHandler((Button) view);
		timeFragment.show(getFragmentManager(), "newExamTimePicker");
	}

	public void showDatePickerDialog(View view) {
		DatePickerDialogFragment dateFragment = new DatePickerDialogFragment();
		dateFragment.setHandler((Button) view);
		dateFragment.show(getFragmentManager(), "newExamDatePicker");
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Exam getExamToEdit() {
		return examToEdit;
	}

	public void setExamToEdit(Exam examToEdit) {
		this.examToEdit = examToEdit;
	}
}
