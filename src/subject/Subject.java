package subject;

/**
 * This class represent a container os subjects
 * 
 * @author Javier
 * 
 */
public class Subject {
	/** Id of the subject */
	private int id;

	/** Id of the course */
	private int courseId;

	/** Name of the subject */
	private String name;

	/** Credits of the subject */
	private float credits;

	/** Name of the teacher */
	private String teacher;

	/** Current note of the subject */
	private float note;

	/** Note necessary for pass the subject */
	private float noteNecessary;

	/** Number of task in the subject */
	private int numberOfTasks;

	/** Number of exams in the subjects */
	private int numberOfExams;

	/** Homework ids */
	private String homeworkId;

	/** Exam ids */
	private String examsId;

	public Subject(int id, int courseId, String subjectName, String homeworkId, String examsId) {
		this.id = id;
		this.courseId = courseId;
		this.name = subjectName;
		this.setHomeworkId(homeworkId);
		this.setExamsId(examsId);
	}

	public Subject(String subjectName, String average, int numberExams, int numberHomework) {
		this.name = subjectName;
		this.note = Float.parseFloat(average);
		this.numberOfExams = numberExams;
		this.numberOfTasks = numberHomework;
	}

	/* Get and set of the id of the subject */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/* Get and set of subject's course id */
	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	/* Get and set of the subject name */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* Get and set the credits */
	public float getCredits() {
		return credits;
	}

	public void setCredits(float credits) {
		this.credits = credits;
	}

	/* Get and set teacher name */
	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	/* Get and set of the note */
	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	/* Get and set the note necessary */
	public float getNoteNecessary() {
		return noteNecessary;
	}

	public void setNoteNecessary(float noteNecessary) {
		this.noteNecessary = noteNecessary;
	}

	/* Get and set the number of tasks */
	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	/* Get and set the number of exams */
	public int getNumberOfExams() {
		return numberOfExams;
	}

	public void setNumberOfExams(int numberOfExams) {
		this.numberOfExams = numberOfExams;
	}

	/* Get and set homeworkid */
	public String getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(String homeworkId) {
		this.homeworkId = homeworkId;
	}

	/* Get and set examsid */
	public String getExamsId() {
		return examsId;
	}

	public void setExamsId(String examsId) {
		this.examsId = examsId;
	}
}
