package homework;

import java.util.Calendar;

import navigationdrawer.MainActivity;
import navigationdrawer.NavigationDrawerController;
import subject.DetailSubjectFragment;
import subject.Subject;
import utility.DatabaseContract;
import utility.DatabaseHelper;
import utility.DatePickerDialogFragment;
import utility.OnBackPressed;
import utility.OnClickButtonXml;
import utility.PriorityDialogFragment;
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

public class NewHomeworkFragment extends Fragment implements OnClickButtonXml, OnBackPressed {

	private EditText homeworkName;
	private Button date;
	private Button fromHour;
	private Button toHour;
	private Button priority;
	private EditText note;
	private EditText description;

	private Subject subject;

	private Homework homeworkToEdit;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	/** Code for the priority */
	public static final int PRIORITY_CODE = 2;

	public static final int HIGH_PRIORITY = 0;
	public static final int NORMAL_PRIORITY = 1;
	public static final int LOW_PRIORITY = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_homework, container, false);

		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getWritableDatabase();

		/** Get the instances of the views */
		homeworkName = (EditText) view.findViewById(R.id.newhomework_homework_name);
		date = (Button) view.findViewById(R.id.newhomework_end_date);
		fromHour = (Button) view.findViewById(R.id.newhomework_from_hour);
		toHour = (Button) view.findViewById(R.id.newhomework_to_hour);
		note = (EditText) view.findViewById(R.id.newhomework_note);
		description = (EditText) view.findViewById(R.id.newhomework_description);
		priority = (Button) view.findViewById(R.id.newhomework_priority);

		if (homeworkToEdit != null) {
			homeworkName.setText(homeworkToEdit.getName());
			date.setHint(homeworkToEdit.getEnd());
			fromHour.setHint(homeworkToEdit.getInitHour());
			toHour.setHint(homeworkToEdit.getEndHour());
			note.setText("" + homeworkToEdit.getNote());
			description.setText(homeworkToEdit.getDescription());
			String sPriority = getString(R.string.normal_priority);
			switch (homeworkToEdit.getPriority()) {
			case HIGH_PRIORITY:
				sPriority = getString(R.string.high_priority);
				break;
			case LOW_PRIORITY:
				sPriority = getString(R.string.low_priority);
				break;
			}
			priority.setHint(sPriority);
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
		case R.id.newhomework_end_date:
			showDatePickerDialog(view);
			break;
		case R.id.newhomework_from_hour:
		case R.id.newhomework_to_hour:
			showTimePickerDialog(view);
			break;
		case R.id.newhomework_priority:
			showPriorityDialog(view);
			break;
		case R.id.newhomework_cancel:
			cancelHomework();
			break;
		case R.id.newhomework_acept:
			if (homeworkToEdit == null)
				addNewHomework();
			else
				updateHomework();
			break;
		}
	}

	public void showPriorityDialog(View view) {
		PriorityDialogFragment priorityFragment = new PriorityDialogFragment();
		priorityFragment.setHandler((Button) view);
		priorityFragment.setTargetFragment(this, PRIORITY_CODE);
		priorityFragment.show(getFragmentManager(), "newPriorityDialog");
	}

	public void cancelHomework() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = NavigationDrawerController
				.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);
		if (newFragment instanceof OnClickButtonXml)
			MainActivity.setOnClickFragment(newFragment);
		MainActivity.setCurrentFragment(newFragment);
		((DetailSubjectFragment) newFragment).setSubject(subject);
		fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

		/** Close the database */
		db.close();
		dbHelper.close();
	}

	public int getPrority() {
		int result = NORMAL_PRIORITY;
		if (priority.getHint().toString().equals(getString(R.string.high_priority)))
			result = HIGH_PRIORITY;
		else if (priority.getHint().toString().equals(getString(R.string.low_priority)))
			result = LOW_PRIORITY;

		return result;
	}

	public void addNewHomework() {
		if (homeworkName.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de tarea. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			/** Fill the database with the data */
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME, homeworkName.getText().toString());
			date.setText(date.getText().toString().replaceAll("/", "-"));
			values.put(DatabaseContract.Homework.COLUMN_NAME_END_DATE, fromHour.getHint().toString() + "-"
					+ toHour.getHint().toString() + "-" + date.getHint().toString());
			float inote = 0;
			if (!note.getText().toString().isEmpty())
				inote = Float.valueOf(note.getText().toString());
			values.put(DatabaseContract.Homework.COLUMN_NAME_NOTE, inote);
			values.put(DatabaseContract.Homework.COLUMN_NAME_PRIORITY, getPrority());
			values.put(DatabaseContract.Homework.COLUMN_NAME_DONE, false);
			values.put(DatabaseContract.Homework.COLUMN_NAME_PONDERATION, " ");
			values.put(DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID, subject.getId());
			values.put(DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION, description.getText().toString());

			db.insert(DatabaseContract.Homework.TABLE_NAME, null, values);

			values.clear();
			subject.setNumberOfTasks(subject.getNumberOfTasks() + 1);
			subject.setHomeworkId(subject.getHomeworkId()
					+ DatabaseHelper.getNextId(db, DatabaseContract.Homework.TABLE_NAME) + ";");
			values.put(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID, subject.getHomeworkId());
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

			/** Close the database */
			db.close();
			dbHelper.close();
		}
	}

	public void updateHomework() {
		if (homeworkName.getText().toString().isEmpty())
			Toast.makeText(getActivity(), "No hay nombre de tarea. Por favor rellenalo.", Toast.LENGTH_SHORT).show();
		else {
			/** Fill the database with the data */
			ContentValues values = new ContentValues();
			values.put(DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME, homeworkName.getText().toString());
			date.setText(date.getText().toString().replaceAll("/", "-"));
			values.put(DatabaseContract.Homework.COLUMN_NAME_END_DATE, fromHour.getHint().toString() + "-"
					+ toHour.getHint().toString() + "-" + date.getHint().toString());
			float inote = 0;
			if (!note.getText().toString().isEmpty())
				inote = Float.valueOf(note.getText().toString());
			values.put(DatabaseContract.Homework.COLUMN_NAME_NOTE, inote);
			values.put(DatabaseContract.Homework.COLUMN_NAME_PRIORITY, getPrority());
			values.put(DatabaseContract.Homework.COLUMN_NAME_DONE, false);
			values.put(DatabaseContract.Homework.COLUMN_NAME_PONDERATION, " ");
			values.put(DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID, subject.getId());
			values.put(DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION, description.getText().toString());

			String[] id = { "" + homeworkToEdit.getId() };
			db.update(DatabaseContract.Homework.TABLE_NAME, values, DatabaseContract.Homework._ID + "=?", id);

			/** Back to Courses view */
			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_DETAIL_SUBJECT);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((DetailSubjectFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

			/** Close the database */
			db.close();
			dbHelper.close();
		}
	}

	public void showTimePickerDialog(View view) {
		TimePickerDialogFragment timeFragment = new TimePickerDialogFragment();
		timeFragment.setHandler((Button) view);
		timeFragment.show(getFragmentManager(), "newHomeworkTimePicker");
	}

	public void showDatePickerDialog(View view) {
		DatePickerDialogFragment dateFragment = new DatePickerDialogFragment();
		dateFragment.setHandler((Button) view);
		dateFragment.show(getFragmentManager(), "newHomeworkDatePicker");
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Homework getHomeworkToEdit() {
		return homeworkToEdit;
	}

	public void setHomeworkToEdit(Homework homeworkToEdit) {
		this.homeworkToEdit = homeworkToEdit;
	}

	@Override
	public void onBackPressed() {
		cancelHomework();
	}
}
