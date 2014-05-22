package utility;

import android.provider.BaseColumns;

public final class DatabaseContract {
	public static final String DB_NAME = "iEtueri.db";
	public static final int DB_VERSION = 1;

	/**
	 * Table name and columns of courses data.
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Courses implements BaseColumns {
		public static final String TABLE_NAME = "courses";
		public static final String COLUMN_NAME_COURSE_NAME = "courseName";
		public static final String COLUMN_NAME_NUMBER_OF_SUBJECTS = "numberOfSubjects";
		public static final String COLUMN_NAME_AVERAGE = "average";
		public static final String COLUMN_NAME_INIT_DATE = "dateInit";
		public static final String COLUMN_NAME_END_DATE = "dateEnd";
		public static final String COLUMN_NAME_SUBJECTS_IDS = "subjectsIds";
	}

	/**
	 * Table name and columns of subjects data.
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Subjects implements BaseColumns {
		public static final String TABLE_NAME = "subjects";
		public static final String COLUMN_NAME_COURSE_ID = "subjectId";
		public static final String COLUMN_NAME_SUBJECT_NAME = "subjectName";
		public static final String COLUMN_NAME_CREDITS = "credits";
		public static final String COLUMN_NAME_TEACHER = "teacher";
		public static final String COLUMN_NAME_NOTE = "note";
		public static final String COLUMN_NAME_NOTE_NECESSARY = "noteNecessary";
		public static final String COLUMN_NAME_AVERAGE = "average";
		public static final String COLUMN_NAME_HOMEWORK_ID = "homeworkIds";
		public static final String COLUMN_NAME_EXAMS_ID = "examsIds";
		public static final String COLUMN_NAME_PONDERATIONS_ID = "ponderationIds";
		public static final String COLUMN_NAME_SCHEDULES_ID = "schedulesIds";
	}

	/**
	 * Table name and columns of homework data.
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Homework implements BaseColumns {
		public static final String TABLE_NAME = "homework";
		public static final String COLUMN_NAME_HOMEWORK_NAME = "homeworkName";
		public static final String COLUMN_NAME_SUBJECT_ID = "subjectId";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_END_DATE = "endDate";
		public static final String COLUMN_NAME_PRIORITY = "priority";
		public static final String COLUMN_NAME_NOTE = "note";
		public static final String COLUMN_NAME_DONE = "done";
		public static final String COLUMN_NAME_PONDERATION = "ponderation";
	}

	/**
	 * Table name and columns of exam data.
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Exams implements BaseColumns {
		public static final String TABLE_NAME = "exams";
		public static final String COLUMN_NAME_EXAM_NAME = "examName";
		public static final String COLUMN_NAME_SUBJECT_ID = "subjectId";
		public static final String COLUMN_NAME_END_DATE = "endDate";
		public static final String COLUMN_NAME_NOTE = "note";
		public static final String COLUMN_NAME_NOTE_NECESSARY = "noteNecessary";
		public static final String COLUMN_NAME_DONE = "done";
		public static final String COLUMN_NAME_PONDERATION = "ponderation";
	}

	/**
	 * Table name and columns of ponderation data
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Ponderation implements BaseColumns {
		public static final String TABLE_NAME = "ponderations";
		public static final String COLUMN_NAME_SUBJECT_ID = "subjectId";
		public static final String COLUM_NAME_PONDERATION_NAME = "ponderationName";
		public static final String COLUMN_NAME_VALUE = "value";
	}

	/**
	 * Table name and columns of schedule data
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class Schedules implements BaseColumns {
		public static final String TABLE_NAME = "schedules";
		public static final String COLUMN_NAME_SUBJECT_ID = "subjectId";
		public static final String COLUMN_NAME_HOUR_INIT = "hourInit";
		public static final String COLUMN_NAME_HOUR_END = "hourEnd";
		public static final String COLUMN_NAME_DAYS_OF_CALENDAR = "daysOfCalendar";
	}

	/**
	 * Types of variables that we use in DB
	 * 
	 * @author Javier Luque Sanabria
	 * 
	 */
	public static class TypesVariables {
		public static final String TYPE_INT_10_AUTOINCREMENT = " integer PRIMARY KEY AUTOINCREMENT";
		public static final String TYPE_INT_10 = " integer";
		public static final String TYPE_VARCHAR_20 = " varchar(20) NOT NULL";
		public static final String TYPE_VARCHAR_40 = " varchar(40) NOT NULL";
		public static final String TYPE_FLOAT_10 = " float(10) NOT NULL";
		public static final String TYPE_DATE = " varchar(8) NOT NULL";
		public static final String TYPE_HOUR = " varchar(5) NOT NULL";
		public static final String TYPE_TEXT = " text";
		public static final String TYPE_BOOLEAN = " boolean NOT NULL";
	}

}
