public class LibraryStaff {
	private int staffID;
	private String Name;
	private String jodTitel;
	private int userID;

	public LibraryStaff(int staffID, String name, String jodTitel, int userID) {
		super();
		this.staffID = staffID;
		Name = name;
		this.jodTitel = jodTitel;
		this.userID = userID;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getJodTitel() {
		return jodTitel;
	}

	public void setJodTitel(String jodTitel) {
		this.jodTitel = jodTitel;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "LibraryStaff [staffID=" + staffID + ", Name=" + Name + ", jodTitel=" + jodTitel + ", userID=" + userID
				+ "]";
	}
}
