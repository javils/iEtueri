package homework;

public class Homework {

	/** Id of the homework */
	private int id;
	/** Name of the homewokr */
	private String name;

	/**
	 * Date of the end of the homework in hh:mm-hh:mm-yyyy-MM-dd
	 * format(inithour, endhour, date)
	 */
	private String end;

	/** Date of the initHour; */
	private String initHour;

	/** Date of the endHour; */
	private String endHour;

	/** Note of the exam */
	private float note;

	/** Subject of the exam */
	private int subjectId;

	public Homework(int homeworkId, int subjectId, String name, String end, float note) {
		this.setId(homeworkId);
		this.subjectId = subjectId;
		String[] data = end.split("-");
		this.setInitHour(data[0]);
		this.setEndHour(data[1]);
		this.end = data[2];
		this.name = name;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/* Get and set the exam name */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* Get the end date of the homework */
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getInitHour() {
		return initHour;
	}

	public void setInitHour(String initHour) {
		this.initHour = initHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	/* Get and set the note */
	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	/* Get and set the subject Id */
	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

}
