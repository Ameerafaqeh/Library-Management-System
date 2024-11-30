import java.sql.Date;

public class Loads {
    private int loadId;
    private Date loadDate;
    private int userId;
    private Date dueDate;
    private Date returnDate;
    private String loanStatus;
    private int bookBorrowed;
    private int staffId;

    public Loads(int loadId, Date loadDate, int userId, Date dueDate, Date returnDate, String loanStatus, int bookBorrowed, int staffId) {
        this.loadId = loadId;
        this.loadDate = loadDate;
        this.userId = userId;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.loanStatus = loanStatus;
        this.bookBorrowed = bookBorrowed;
        this.staffId = staffId;
    }

    public int getLoadId() {
        return loadId;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getBookBorrowed() {
        return bookBorrowed;
    }

    public void setBookBorrowed(int bookBorrowed) {
        this.bookBorrowed = bookBorrowed;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "Loads [loadId=" + loadId + ", loadDate=" + loadDate + ", userId=" + userId + ", dueDate=" + dueDate
                + ", returnDate=" + returnDate + ", loanStatus=" + loanStatus + ", bookBorrowed=" + bookBorrowed
                + ", staffId=" + staffId + "]";
    }
}
