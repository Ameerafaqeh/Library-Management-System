
public class Teacher {
private int teacherID;
private String teacherName ;
private String subjecte;

public Teacher(int teacherID, String teacherName, String subjecte) {
	super();
	this.teacherID = teacherID;
	this.teacherName = teacherName;
	this.subjecte = subjecte;
}
public int getTeacherID() {
	return teacherID;
}
public void setTeacherID(int teacherID) {
	this.teacherID = teacherID;
}
public String getTeacherName() {
	return teacherName;
}
public void setTeacherName(String teacherName) {
	this.teacherName = teacherName;
}
public String getSubjecte() {
	return subjecte;
}
public void setSubjecte(String subjecte) {
	this.subjecte = subjecte;
}


}
