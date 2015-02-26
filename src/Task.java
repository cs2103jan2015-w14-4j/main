import java.util.ArrayList;
import java.util.Date;

public class Task {
	
	private static final int COMPLETED_TASK 	= 1;
	private static final int INCOMPLETE_TASK 	= 2;
	private static final int DISCARDED_TASK 	= 3;
	
	private int TID;
	private String taskName;
	private Date dateFrom;
	private Date dateTo;
	private Date deadline;
	private String location;
	private String details;
	private int priority; //Create first for future
	private ArrayList<Date> reminders; 
	private int status;
	

	//Constructor 
	public Task(int TID, String taskName, Date dateFrom, 
			Date dateTo, Date deadline, String location, String details, int priority) {
		this.TID = TID;
		this.taskName = taskName;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.deadline = deadline;
		this.location = location;
		this.details = details;
		this.priority = priority;
		this.reminders = new ArrayList<Date>();
		this.status = INCOMPLETE_TASK;
	}
	
	
	//Setter ********************************
	public void setTID(int TID) {
		this.TID = TID;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setReminders(ArrayList<Date> reminders) {
		this.reminders = reminders;
	}

	//Reminders operation
	public boolean addReminders(Date dateToBeReminded) {
		reminders.add(dateToBeReminded);
		return true;
	}
	public boolean deleteReminders(Date dateToBeDeleted) {
		for(int i = 0; i < reminders.size(); ++i) {
			if(reminders.get(i).compareTo(dateToBeDeleted) == 0) {
				reminders.remove(i);
				return true;
			}
		}
		return false;
	}
	
	//Update Status ************************
	public void setComplete() {
		this.status = COMPLETED_TASK;
	}
	
	public void setIncomplete() {
		this.status = INCOMPLETE_TASK;
	}
	
	public void setDiscard() {
		this.status = DISCARDED_TASK;
	}
		
	//GETTER ***********************************
	public int getTID() {
		return TID;
	}

	public String getTaskName() {
		return taskName;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public Date getDeadline() {
		return deadline;
	}

	public String getLocation() {
		return location;
	}

	public String getDetails() {
		return details;
	}

	public int getPriority() {
		return priority;
	}

	public ArrayList<Date> getReminders() {
		return reminders;
	}
	
	public int getStatus() {
		return status;
	}

}
