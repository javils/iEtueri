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
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
			if (argsHomeworkIds[i].trim().isEmpty())
				continue;

			String[] args = { argsHomeworkIds[i] };
			Cursor cur = db.query(DatabaseContract.Homework.TABLE_NAME, projection, DatabaseContract.Homework._ID
					+ "= ?", args, null, null, null);

			cur.moveToFirst();

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

		linearLayout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				dbHelper = new DatabaseHelper(getActivity());
				db = dbHelper.getWritableDatabase();
				TextView tIdExam = (TextView) v.findViewById(R.id.detail_subject_exams_examId);
				String idExam = tIdExam.getText().toString();
				db.delete(DatabaseContract.Exams.TABLE_NAME, DatabaseContract.Exams._ID + "=" + idExam, null);

				/** Quit the exam id on subject */
				String[] projection = { DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID };

				String[] argsId = { String.valueOf(subject.getId()) };
				Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, DatabaseContract.Subjects._ID
						+ "= ?", argsId, null, null, null);

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

				subject.setExamsId(result);
				subject.setNumberOfExams(subject.getNumberOfExams() - 1);
				/** Update with the new values */
				ContentValues values = new ContentValues();
				values.put(DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID, result);
				db.update(DatabaseContract.Subjects.TABLE_NAME, values,
						DatabaseContract.Subjects._ID + "=" + subject.getId(), null);

				if (v != null)
					v.setVisibility(View.GONE);
				return false;
			}
		});

		linearLayout.setOnClickListener(this);
	}

	public void addHomeworkToList(Homework homework) {
		int id = R.layout.subject_detail_homework_item;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null);
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

		linearLayout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				dbHelper = new DatabaseHelper(getActivity());
				db = dbHelper.getWritableDatabase();
				TextView tIdHomework = (TextView) v.findViewById(R.id.detail_subject_homework_homeworkId);
				String idHomework = tIdHomework.getText().toString();
				db.delete(DatabaseContract.Homework.TABLE_NAME, DatabaseContract.Homework._ID + "=" + idHomework, null);

				/** Quit the homework id on subject */
				String[] projection = { DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID };

				String[] argsId = { String.valueOf(subject.getId()) };
				Cursor cur = db.query(DatabaseContract.Subjects.TABLE_NAME, projection, DatabaseContract.Subjects._ID
						+ "= ?", argsId, null, null, null);

				cur.moveToFirst();
				String sHomeworkIds = cur.getString(cur
						.getColumnIndexOrThrow(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID));
				String[] homeworkIds = sHomeworkIds.split(";");
				String result = new String();
				for (int i = 0; i < homeworkIds.length; i++) {
					if (homeworkIds[i].isEmpty())
						continue;

					if (!homeworkIds[i].equals(idHomework))
						result += homeworkIds[i] + ";";
				}

				subject.setHomeworkId(result);
				subject.setNumberOfTasks(subject.getNumberOfTasks() - 1);
				/** Update with the new values */
				ContentValues values = new ContentValues();
				values.put(DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID, result);
				db.update(DatabaseContract.Subjects.TABLE_NAME, values,
						DatabaseContract.Subjects._ID + "=" + subject.getId(), null);

				if (v != null)
					v.setVisibility(View.GONE);
				return false;
			}
		});

		linearLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getVisibility() == View.GONE)
			return;
		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getReadableDatabase();
		FragmentManager fragmentManager = getFragmentManager();
		Fragment newFragment = new Fragment();

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

			newFragment = NavigationDrawerController.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_EXAM);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewExamFragment) newFragment).setSubject(subject);
			((NewExamFragment) newFragment).setExamToEdit(examToEdit);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();

			break;
		case R.layout.subject_detail_homework_item:
			TextView ttIdView = (TextView) v.findViewById(R.id.detail_subject_homework_homeworkId);
			String idHomework = ttIdView.getText().toString();
			String[] argsHomeworkId = { idHomework };
			String[] tprojection = { DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME,
					DatabaseContract.Homework.COLUMN_NAME_END_DATE, DatabaseContract.Homework.COLUMN_NAME_NOTE,
					DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION, DatabaseContract.Homework.COLUMN_NAME_PRIORITY };

			Cursor cur2 = db.query(DatabaseContract.Homework.TABLE_NAME, tprojection, DatabaseContract.Homework._ID
					+ "= ?", argsHomeworkId, null, null, DatabaseContract.Homework._ID + " DESC");

			cur2.moveToFirst();

			String homeworkName = cur2.getString(cur2
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME));
			int subjectIdHome = subject.getId();
			String endDateHome = cur2.getString(cur2
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_END_DATE));
			float noteHom = cur2.getFloat(cur2.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_NOTE));
			String description = cur2.getString(cur2
					.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION));
			int priority = cur2.getInt(cur2.getColumnIndexOrThrow(DatabaseContract.Homework.COLUMN_NAME_PRIORITY));
			Homework homeworkToEdit = new Homework(Integer.parseInt(idHomework), subjectIdHome, description,
					homeworkName, endDateHome, noteHom, priority);

			newFragment = NavigationDrawerController
					.newInstance(NavigationDrawerController.SECTION_NUMBER_NEW_HOMEWORK);
			if (newFragment instanceof OnClickButtonXml)
				MainActivity.setOnClickFragment(newFragment);
			MainActivity.setCurrentFragment(newFragment);
			((NewHomeworkFragment) newFragment).setSubject(subject);
			((NewHomeworkFragment) newFragment).setHomeworkToEdit(homeworkToEdit);
			fragmentManager.beginTransaction().replace(R.id.navigation_drawer_container, newFragment).commit();
			break;
		}

	}

}
