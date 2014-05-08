package courses;

/**
 * This class is a container of course
 * 
 * @author Javier Luque Sanabria
 */
public class Course {
	/** Id of course */
	private int id;

	/** Name of the course */
	private String courseName;

	/** Number of subjects in the course */
	private int numberOfSubjects;

	/** Average of all subjects in the course */
	private double average;

	/** Init Date of the course */
	private String initDate;

	/** End date of the course */
	private String endDate;

	/** subjects ids */
	private String subjectsIds;

	/** Constructor */
	public Course(String courseName, String initDate, String endDate) {
		this.courseName = courseName;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberOfSubjects = 0;
		this.average = 0.0f;
	}

	public Course(String courseName, int numberOfSubjects, double average) {
		this.courseName = courseName;
		this.numberOfSubjects = numberOfSubjects;
		this.average = average;
	}

	public Course(String courseName, int numberOfSubjects, double average, String initDate, String endDate) {
		this.courseName = courseName;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberOfSubjects = numberOfSubjects;
		this.average = average;
	}

	public Course(int id, String courseName, int numberOfSubjects, double average, String initDate, String endDate,
			String subjectsIds) {
		this.setId(id);
		this.courseName = courseName;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberOfSubjects = numberOfSubjects;
		this.average = average;
		this.setSubjectsIds(subjectsIds);
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

	/* Get and set the init date of the course */
	public String getInitDate() {
		return initDate;
	}

	public void setInitDate(String initDate) {
		this.initDate = initDate;
	}

	/* Get and set the end date of the course */
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/* Get and set the sbjects ids */
	public String getSubjectsIds() {
		return subjectsIds;
	}

	public void setSubjectsIds(String subjectsIds) {
		this.subjectsIds = subjectsIds;
	}

	/* Get and set the id of the course */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
