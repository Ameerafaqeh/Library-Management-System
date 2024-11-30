
public class Member {
	private int memberID;
	private String memberName;
	private String membertype;
	public Member(int memberID, String memberName, String membertyoe) {
		super();
		this.memberID = memberID;
		this.memberName = memberName;
		this.membertype = membertyoe;
	}
	/**
	 * @return the memberID
	 */
	public int getMemberID() {
		return memberID;
	}
	/**
	 * @param memberID the memberID to set
	 */
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}
	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return the membertyoe
	 */
	public String getMembertype() {
		return membertype;
	}
	/**
	 * @param membertyoe the membertyoe to set
	 */
	public void setMembertype(String membertyoe) {
		this.membertype = membertyoe;
	}
	
	
	

}
