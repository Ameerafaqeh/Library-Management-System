
public class Student {
	private int studentID;
	private String studentName;
	private int Classvalue;

	public Student(int studentID, String studentName, int class1) {
		super();
		this.studentID = studentID;
		this.studentName = studentName;
		Classvalue = class1;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Override
	public String toString() {
		return "Student [studentID=" + studentID + ", studentName=" + studentName + ", Class=" + Classvalue + "]";
	}

	public int getClassvalue() {
		return Classvalue;
	}

	public void setClass(int class1) {
		Classvalue = class1;
	}

}
