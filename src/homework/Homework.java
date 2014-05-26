package homework;

public class Homework implements Comparable<Homework> {

	/** Id of the homework */
	private int id;
	/** Name of the homework */
	private String name;

	/**
	 * Date of the end of the homework in hh:mm-hh:mm-yyyy-MM-dd
	 * format(inithour, endhour, date)
	 */
	private String end;

	/** Description of the homework */
	private String description;

	/** Date of the initHour; */
	private String initHour;

	/** Date of the endHour; */
	private String endHour;

	/** Note of the homework */
	private float note;

	/** Subject of the homework */
	private int subjectId;

	/** Priority of the homework */
	private int priority;

	public Homework(int homeworkId, int subjectId, String description, String name, String end, float note, int priority) {
		this.setId(homeworkId);
		this.subjectId = subjectId;
		this.setDescription(description);
		String[] data = end.split("-");
		this.setInitHour(data[0]);
		this.setEndHour(data[1]);
		this.end = data[2];
		this.name = name;
		this.note = note;
		this.priority = priority;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	/* Get and set the priority */
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int compareTo(Homework another) {
		return this.priority - another.priority;
	}
}
