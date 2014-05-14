package utility;

import java.io.File;

import utility.DatabaseContract.TypesVariables;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	/** Courses table */
	private static final String SQL_CREATE_TABLE_COURSES = "CREATE TABLE " + DatabaseContract.Courses.TABLE_NAME + " ("
			+ DatabaseContract.Courses._ID + DatabaseContract.TypesVariables.TYPE_INT_10_AUTOINCREMENT + ","
			+ DatabaseContract.Courses.COLUMN_NAME_COURSE_NAME + DatabaseContract.TypesVariables.TYPE_VARCHAR_20 + ","
			+ DatabaseContract.Courses.COLUMN_NAME_NUMBER_OF_SUBJECTS + DatabaseContract.TypesVariables.TYPE_INT_10
			+ "," + DatabaseContract.Courses.COLUMN_NAME_AVERAGE + DatabaseContract.TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Courses.COLUMN_NAME_INIT_DATE + DatabaseContract.TypesVariables.TYPE_DATE + ","
			+ DatabaseContract.Courses.COLUMN_NAME_END_DATE + DatabaseContract.TypesVariables.TYPE_DATE + ","
			+ DatabaseContract.Courses.COLUMN_NAME_SUBJECTS_IDS + DatabaseContract.TypesVariables.TYPE_TEXT + "); ";

	/** Subjects table */
	private static final String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + DatabaseContract.Subjects.TABLE_NAME
			+ " (" + DatabaseContract.Subjects._ID + DatabaseContract.TypesVariables.TYPE_INT_10_AUTOINCREMENT + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_COURSE_ID + DatabaseContract.TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_SUBJECT_NAME + DatabaseContract.TypesVariables.TYPE_VARCHAR_20
			+ "," + DatabaseContract.Subjects.COLUMN_NAME_CREDITS + DatabaseContract.TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_AVERAGE + DatabaseContract.TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_TEACHER + DatabaseContract.TypesVariables.TYPE_VARCHAR_20 + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_NOTE + DatabaseContract.TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_NOTE_NECESSARY + DatabaseContract.TypesVariables.TYPE_FLOAT_10
			+ "," + DatabaseContract.Subjects.COLUMN_NAME_HOMEWORK_ID + DatabaseContract.TypesVariables.TYPE_TEXT + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_EXAMS_ID + DatabaseContract.TypesVariables.TYPE_TEXT + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_PONDERATIONS_ID + DatabaseContract.TypesVariables.TYPE_TEXT + ","
			+ DatabaseContract.Subjects.COLUMN_NAME_SCHEDULES_ID + DatabaseContract.TypesVariables.TYPE_TEXT + "); ";

	private static final String SQL_CREATE_TABLE_HOMEWORK = "CREATE TABLE " + DatabaseContract.Homework.TABLE_NAME
			+ " (" + DatabaseContract.Homework._ID + DatabaseContract.TypesVariables.TYPE_INT_10_AUTOINCREMENT + ","
			+ DatabaseContract.Homework.COLUMN_NAME_SUBJECT_ID + "," + TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Homework.COLUMN_NAME_HOMEWORK_NAME + TypesVariables.TYPE_VARCHAR_20 + ","
			+ DatabaseContract.Homework.COLUMN_NAME_DESCRIPTION + TypesVariables.TYPE_VARCHAR_40 + ","
			+ DatabaseContract.Homework.COLUMN_NAME_END_DATE + TypesVariables.TYPE_DATE + ","
			+ DatabaseContract.Homework.COLUMN_NAME_PRIORITY + TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Homework.COLUMN_NAME_NOTE + TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Homework.COLUMN_NAME_DONE + TypesVariables.TYPE_BOOLEAN + ","
			+ DatabaseContract.Homework.COLUMN_NAME_PONDERATION + TypesVariables.TYPE_INT_10 + "); ";

	private static final String SQL_CREATE_TABLE_EXAMS = "CREATE TABLE " + DatabaseContract.Exams.TABLE_NAME + " ("
			+ DatabaseContract.Exams._ID + DatabaseContract.TypesVariables.TYPE_INT_10_AUTOINCREMENT + ","
			+ DatabaseContract.Exams.COLUMN_NAME_SUBJECT_ID + "," + TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Exams.COLUMN_NAME_EXAM_NAME + TypesVariables.TYPE_VARCHAR_20 + ","
			+ DatabaseContract.Exams.COLUMN_NAME_END_DATE + TypesVariables.TYPE_DATE + ","
			+ DatabaseContract.Exams.COLUMN_NAME_NOTE + TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Exams.COLUMN_NAME_NOTE_NECESSARY + TypesVariables.TYPE_FLOAT_10 + ","
			+ DatabaseContract.Exams.COLUMN_NAME_DONE + TypesVariables.TYPE_BOOLEAN + ","
			+ DatabaseContract.Exams.COLUMN_NAME_PONDERATION + TypesVariables.TYPE_INT_10 + "); ";

	private static final String SQL_CREATE_TABLE_PONDERATIONS = "CREATE TABLE "
			+ DatabaseContract.Ponderation.TABLE_NAME + " (" + DatabaseContract.Ponderation._ID
			+ TypesVariables.TYPE_INT_10_AUTOINCREMENT + "," + DatabaseContract.Ponderation.COLUM_NAME_PONDERATION_NAME
			+ TypesVariables.TYPE_VARCHAR_20 + "," + DatabaseContract.Ponderation.COLUMN_NAME_SUBJECT_ID
			+ TypesVariables.TYPE_INT_10 + "," + DatabaseContract.Ponderation.COLUMN_NAME_VALUE
			+ TypesVariables.TYPE_INT_10 + "); ";

	private static final String SQL_CREATE_TABLE_SCHEDULES = "CREATE TABLE " + DatabaseContract.Schedules.TABLE_NAME
			+ " (" + DatabaseContract.Schedules._ID + TypesVariables.TYPE_INT_10_AUTOINCREMENT + ","
			+ DatabaseContract.Schedules.COLUMN_NAME_SUBJECT_ID + TypesVariables.TYPE_INT_10 + ","
			+ DatabaseContract.Schedules.COLUMN_NAME_HOUR_INIT + TypesVariables.TYPE_HOUR + ","
			+ DatabaseContract.Schedules.COLUMN_NAME_HOUR_END + TypesVariables.TYPE_HOUR + ","
			+ DatabaseContract.Schedules.COLUMN_NAME_DAYS_OF_CALENDAR + TypesVariables.TYPE_TEXT + "); ";

	public DatabaseHelper(Context context) {
		super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_COURSES);
		db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
		db.execSQL(SQL_CREATE_TABLE_EXAMS);
		db.execSQL(SQL_CREATE_TABLE_HOMEWORK);
		db.execSQL(SQL_CREATE_TABLE_PONDERATIONS);
		db.execSQL(SQL_CREATE_TABLE_SCHEDULES);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		SQLiteDatabase.deleteDatabase(new File(DatabaseContract.DB_NAME));
		onCreate(db);
	}
}
