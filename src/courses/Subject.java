package courses;

/**
 * This class represent a container os subjects
 * 
 * @author Javier
 * 
 */
public class Subject {
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

	/** NUmber of exams in the subjects */
	private int numberOfExams;

	public Subject(String subjectName, String average, int numberExams, int numberHomework) {
		this.name = subjectName;
		this.note = Float.parseFloat(average);
		this.numberOfExams = numberExams;
		this.numberOfTasks = numberHomework;
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
}
