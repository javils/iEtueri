package courses;

import homework.Homework;
import homework.NewHomeworkFragment;

import java.util.ArrayList;
import java.util.Collections;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javils.ietueri.R;

import exams.Exam;
import exams.NewExamFragment;

public class DetailSubjectFragment extends Fragment implements OnClickButtonXml, OnClickListener {

	private ViewGroup listHomework;
	private ViewGroup listExams;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private Subject subject;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_subjects, container, false);

		/** Get the instances of the views */
		listHomework = (LinearLayout) view.findViewById(R.id.layout_list_homeworks);
		listExams = (LinearLayout) view.findViewById(R.id.layout_list_exams);

		ArrayList<Exam> exams = getExamsOfSubject();

		for (int i = 0; i < exams.size(); i++)
			addExamToList(exams.get(i));

		ArrayList<Homework> homework = getHomeworkOfSubject();

		for (int i = 0; i < homework.size(); i++)
			addHomeworkToList(homework.get(i));

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

	private ArrayList<Homework> getHomeworkOfSubject() {
		ArrayList<Homework> result = new ArrayList<Homework>();
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		String[] argsHomeworkIds = subject.getHomeworkId().split(";");
		String[] projection = { DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME,
				DatabaseContract.Homework.COLUMN_NAME_END_DATE, DatabaseContract.Homework.COLUMN_NAME_NOTE,
				DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION, DatabaseContract.Homework.COLUMN_NAME_PRIORITY };

		for (int i = 0; i < argsHomeworkIds.length; i++) {
			String[] args = { argsHomeworkIds[i] };
			Cursor cur = db.query(DatabaseContract.Homework.TABLE_NAME, projection, DatabaseContract.Homework._ID
					+ "= ?", args, null, null, null);

			cur.moveToFirst();

			if (argsHomeworkIds[i].isEmpty())
				continue;

			int homeworkId = Integer.parseInt(argsHomeworkIds[i]);
			String homeworkName = cur.getString(cur
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME));
			int subjectId = subject.getId();
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

	@Override
	public void onClickXml(View view) {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = new Fragment();
		switch (view.getId()) {
		case R.id.detail_subject_new_exam:

			newFragment = NavigationDrawerController.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_EXAM);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewExamFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
			break;
		case R.id.detail_subject_new_homework:
			newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_HOMEWORK);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewHomeworkFragment) newFragment).setSubject(subject);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
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
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		TextView text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_name);
		text.setText(exam.getName());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_date);
		text.setText(exam.getEnd());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_hour);
		text.setText(exam.getInitHour() + " - " + exam.getEndHour());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_note);
		if (exam.getNote() != 0)
			text.setText("" + exam.getNote());

		/** Get the id of the exam on the view */
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_exams_examId);
		text.setText("" + exam.getId());
		linearLayout.setId(R.layout.subject_detail_exam_item);

		listExams.addView(linearLayout);

		linearLayout.setOnClickListener(this);
	}

	public void addHomeworkToList(Homework homework) {
		int id = R.layout.subject_detail_homework_item;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
		TextView text = (TextView) linearLayout.findViewById(R.id.detail_subject_homework_name);
		text.setText(homework.getName());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_homework_enddate);
		text.setText(homework.getEnd());
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_homework_note);
		text.setText("" + homework.getNote());
		linearLayout.setId(R.layout.subject_detail_homework_item);
		listHomework.addView(linearLayout);

		/** Get the id of the homework in the view */
		text = (TextView) linearLayout.findViewById(R.id.detail_subject_homework_homeworkId);
		text.setText("" + homework.getId());

		linearLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();

		switch (v.getId()) {
		case R.layout.subject_detail_exam_item:
			TextView tIdView = (TextView) v.findViewById(R.id.detail_subject_exams_examId);
			String idExam = tIdView.getText().toString();
			String[] argsExamId = { idExam };
			String[] projection = { DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME,
					DatabaseContract.Exams.COLUMN_NAME_END_DATE, DatabaseContract.Exams.COLUMN_NAME_NOTE };

			Cursor cur = db.query(DatabaseContract.Exams.TABLE_NAME, projection, DatabaseContract.Exams._ID + "= ?",
					argsExamId, null, null, DatabaseContract.Exams._ID + " DESC");

			cur.moveToFirst();

			int examId = Integer.parseInt(argsExamId[0]);
			String examName = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME));
			int subjectId = subject.getId();
			String endDate = cur.getString(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_END_DATE));
			float note = cur.getFloat(cur.getColumnIndexOrThrow(DatabaseContract.Exams.COLUMN_NAME_NOTE));

			Exam examToEdit = new Exam(examId, subjectId, examName, endDate, note);

			FragmentManager fragmentManager = getFragmentManager();
			Fragment newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_EXAM);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewExamFragment) newFragment).setSubject(subject);
			((NewExamFragment) newFragment).setExamToEdit(examToEdit);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

			break;
		case R.layout.subject_detail_homework_item:
			break;
		}

	}
}
