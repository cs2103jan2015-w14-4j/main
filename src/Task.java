import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
	
	private static final int COMPLETED_TASK 	= 1;
	private static final int INCOMPLETE_TASK 	= 2;
	private static final int DISCARDED_TASK 	= 3;
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final int TASK_ID = 0;
    private static final int TASK_NAME = 1;
    private static final int DATE_FROM = 2;
    private static final int DATE_TO = 3;
    private static final int DEADLINE = 4;
    private static final int LOCATION = 5;
    private static final int DETAILS = 6;
    private static final int PRIORITY = 7;
    private static final int DEFAULT_STRING_SIZE = 8;
	
	private int TID;
	private String taskName;
	private Date dateFrom;
	private Date dateTo;
	private Date deadline;
	private String location;
	private String details;
	private int priority; //Create first for future
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
	
	public String getDateFromString() {
		if(dateFrom != null) {
			return convertToStringFromDate(dateFrom);
		} else {
			return null;
		}
		
	}

	public String getDateToString() {
		if(dateTo != null) {
			return convertToStringFromDate(dateTo);
		} else {
			return null;
		}
	}

	public String getDeadlineString() {
		if(deadline != null) {
			return convertToStringFromDate(deadline);
		} else {
			return null;
		}
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

	
	public int getStatus() {
		return status;
	}
	
	
	public boolean isEqual(Task task) {
		if( !dateEqual(task.getDateFrom(), dateFrom)) {
			return false;
		}
		if( !dateEqual(task.getDateTo(), dateTo)) {
			return false;
		}
		if( !dateEqual(task.getDeadline(), deadline)) {
			return false;
		}
		if(!stringEqual(task.getTaskName(), taskName)) {
			return false;
		}
		if(task.getTID() != TID) {
			return false;
		}
		if(task.getPriority() != priority) {
			return false;
		}
		if(!stringEqual(task.getLocation(), location)) {
			return false;
		}
		if(!stringEqual(task.getDetails(), details)) {
			return false;
		}
		if(status != task.getStatus()) {
			return false;
		}
		return true;
	}
	
	private static boolean dateEqual(Date date1, Date date2) {
		if(date1 == null && date2 == null) {
			return true;
		}
		else if(date1 != null && date2 == null) {
			return false;
		}
		else if(date1 == null && date2 != null) {
			return false;
		}
		else {
			return date1.equals(date2);
		}
	}
	
	private static boolean stringEqual(String str1, String str2) {
		if(str1 == null && str2 == null) {
			return true;
		}
		else if(str1 != null && str2 == null) {
			return false;
		}
		else if(str1 == null && str2 != null) {
			return false;
		}
		else {
			return str1.equals(str2);
		}
	}
	
	public Task clone() {
		Date newDateFrom = null;
		Date newDateTo = null;
		Date newDeadline = null;
		if(dateFrom != null) {
			newDateFrom = new Date(dateFrom.getTime());
		}
		if(dateTo != null) {
			newDateTo = new Date(dateTo.getTime());
		}
		if(deadline != null) {
			newDeadline = new Date(deadline.getTime());
		}
		return new Task(TID,taskName, newDateFrom, newDateTo, 
				newDeadline, location, details, priority);
	}
	
    public String[] toStringArray() {
        String[] taskStringArray = new String[DEFAULT_STRING_SIZE];
        
	    taskStringArray[TASK_ID] = "" + TID;
	    
        if(taskName != null) {
            taskStringArray[TASK_NAME] = taskName;
        } else {
            taskStringArray[TASK_NAME] = null;
        }
	    
        if(dateFrom != null) {
            taskStringArray[DATE_FROM] = convertToStringFromDate(dateFrom);
        } else {
            taskStringArray[DATE_FROM] = null;
        }

        if(dateTo != null) {
            taskStringArray[DATE_TO] = convertToStringFromDate(dateTo);
        } else {
            taskStringArray[DATE_TO] = null;
        }

        if(deadline != null) {
            taskStringArray[DEADLINE] = convertToStringFromDate(deadline);
        } else {
            taskStringArray[DEADLINE] = null;
        }

        if(location != null) {
            taskStringArray[LOCATION] = location;
        } else {
            taskStringArray[LOCATION] = null;
        }

        if(details != null) {
            taskStringArray[DETAILS] = details;
        } else {
            taskStringArray[DETAILS] = null;
        }

        taskStringArray[PRIORITY] = "" + priority;
        
	    return taskStringArray;
	}
    
    private String convertToStringFromDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dateString = dateFormat.format(dateObject);
        return dateString;
    }
}
