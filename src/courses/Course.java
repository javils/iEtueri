package courses;

/**
 * This class is a container of course
 * 
 * @author Javier Luque Sanabria
 */
public class Course {
	/** Name of the course */
	private String courseName;

	/** Number of subjects in the course */
	private int numberOfSubjects;

	/** Average of all subjects in the course */
	private double average;

	/** Date of the course */
	private String date;

	/** Constructor */
	public Course(String courseName, String date) {
		this.courseName = courseName;
		this.date = date;
		this.numberOfSubjects = 0;
		this.average = 0.0f;
	}

	public Course(String courseName, int numberOfSubjects, double average) {
		this.courseName = courseName;
		this.numberOfSubjects = numberOfSubjects;
		this.average = average;
	}

	public Course(String courseName, int numberOfSubjects, double average, String date) {
		this.courseName = courseName;
		this.date = date;
		this.numberOfSubjects = numberOfSubjects;
		this.average = average;
	}

	/* Get and set courseName methods */
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/* Get and set number of subjects */
	public int getNumberOfSubjects() {
		return numberOfSubjects;
	}

	public void setNumberOfSubjects(int numberOfSubjects) {
		this.numberOfSubjects = numberOfSubjects;
	}

	/* Get and set the average */
	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	/* Get and set the date of the course */
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
