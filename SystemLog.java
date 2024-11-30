import java.util.Date;

public class SystemLog {
	private int logID;
	private Date timestamp;
	private String eventType;

	public SystemLog(int logID, Date timestamp, String eventType) {
		super();
		this.logID = logID;
		this.timestamp = timestamp;
		this.eventType = eventType;
	}

	public int getLogID() {
		return logID;
	}

	public void setLogID(int logID) {
		this.logID = logID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "SystemLog [logID=" + logID + ", timestamp=" + timestamp + ", eventType=" + eventType + "]";
	}

}