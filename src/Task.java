import java.text.SimpleDateFormat;
import java.util.Date;

//@author A0108385B
public class Task {

    public static final int MINIMUM_LENGTH_TASK_NAME = 1;
    public static final int MAXIMUM_LENGTH_TASK_NAME = 30;
    public static final int MAXIMUM_LENGTH_LOCATION = 30;

    public static final String STRING_STATUS_URGENT = "Urgent";
    public static final String STRING_STATUS_MAJOR = "Major";
    public static final String STRING_STATUS_NORMAL = "Normal";
    public static final String STRING_STATUS_MINOR = "Minor";
    public static final String STRING_STATUS_CASUAL = "Casual";
    public static final String STRING_STATUS_COMPLETE 	= "Complete";
    public static final String STRING_STATUS_OVERDUE 	= "Overdue";

    public static final int INDEX_STATUS_URGENT = 1;
    public static final int INDEX_STATUS_MAJOR = 2;
    public static final int INDEX_STATUS_NORMAL = 3;
    public static final int INDEX_STATUS_MINOR = 4;
    public static final int INDEX_STATUS_CASUAL = 5;
    public static final int INDEX_STATUS_COMPLETE 	= 6;
    public static final int INDEX_STATUS_OVERDUE 	= 7;

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    public static final int INDEX_TASK_ID = 0;
    public static final int INDEX_TASK_NAME = 1;
    public static final int INDEX_DATE_FROM = 2;
    public static final int INDEX_DATE_TO = 3;
    public static final int INDEX_LOCATION = 4;
    public static final int INDEX_DETAILS = 5;
    public static final int INDEX_STATUS = 6;
    public static final int DEFAULT_STRING_SIZE = 7;
    private static final String ZERO_TIME = " 00:00";

    private int TID;
    private String taskName;
    private Date dateFrom;
    private Date dateTo;
    private Date deadline;
    private String location;
    private String details;
    private int status;


    //Constructor 
    public Task(int TID, String taskName, Date dateFrom, 
            Date dateTo, Date deadline, String location, String details, int status) {
        this.TID = TID;
        this.taskName = taskName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.deadline = deadline;
        this.location = location;
        this.details = details;
        this.status = status;
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

    public void setStatus(int status) {
        this.status = status;
    }

    //Update Status ************************
    //might need to remove later
    public void setUrgent() {
        this.status = INDEX_STATUS_URGENT;
    }


    public void setMajor() {
        this.status = INDEX_STATUS_MAJOR;
    }


    public void setNormal() {
        this.status = INDEX_STATUS_NORMAL;
    }


    public void setOverdue() {
        this.status = INDEX_STATUS_OVERDUE;
    }


    public void setComplate() {
        this.status = INDEX_STATUS_COMPLETE;
    }


    public void setMinor() {
        this.status = INDEX_STATUS_MINOR;
    }


    public void setCasual() {
        this.status = INDEX_STATUS_CASUAL;
    }
    //might need to remove later
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
        if (dateFrom != null) {
            return convertToStringFromDate(dateFrom);
        } else {
            return null;
        }

    }


    public String getDateToString() {
        if (dateTo != null) {
            return convertToStringFromDate(dateTo);
        } else {
            return null;
        }
    }


    public String getDeadlineString() {
        if (deadline != null) {
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


    public int getStatus() {
        return status;
    }


    public String getStatusString() {
        switch (status) {
        case INDEX_STATUS_URGENT:
            return STRING_STATUS_URGENT;

        case INDEX_STATUS_MAJOR:
            return STRING_STATUS_MAJOR;

        case INDEX_STATUS_NORMAL:
            return STRING_STATUS_NORMAL;

        case INDEX_STATUS_MINOR:
            return STRING_STATUS_MINOR;

        case INDEX_STATUS_CASUAL:
            return STRING_STATUS_CASUAL;

        case INDEX_STATUS_COMPLETE:
            return STRING_STATUS_COMPLETE;

        case INDEX_STATUS_OVERDUE:
            return STRING_STATUS_OVERDUE;

        default:
            return STRING_STATUS_NORMAL;
        }
    }



    /**
     * This method provide equality comparison of task object.
     * @param task Task to be compared with
     * @return	True if both task are equal in all fields, false otherwise.
     */
    public boolean isEqual(Task task) {
        if (task == null) {
            return false;
        }
        if (!dateEqual(task.getDateFrom(), dateFrom)) {
            return false;
        }
        if (!dateEqual(task.getDateTo(), dateTo)) {
            return false;
        }
        if (!dateEqual(task.getDeadline(), deadline)) {
            return false;
        }
        if (!stringEqual(task.getTaskName(), taskName)) {
            return false;
        }
        if (task.getTID() != TID) {
            return false;
        }
        if (!stringEqual(task.getLocation(), location)) {
            return false;
        }
        if (!stringEqual(task.getDetails(), details)) {
            return false;
        }
        if (task.getStatus() != status) {
            return false;
        }
        return true;
    }


    /**
     * This method compares equality of date and allows compared date object to be null.
     * If both date are null, they are considered as equal.
     * @param date1		Date 1 to be compared
     * @param date2 	Date 1 to be compared
     * @return			True if both date are equal, false otherwise.
     */
    private static boolean dateEqual(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        } else if (date1 != null && date2 == null) {
            return false;
        } else if (date1 == null && date2 != null) {
            return false;
        } else {
            return date1.equals(date2);
        }
    }


    /**
     * This method compares equality of string and allows compared string object to be null.
     * If both string are null, they are considered as equal.
     * @param str1	String 1 to be compared
     * @param str2	String 2 to be compared
     * @return		True if both string are equal, false otherwise.
     */
    private static boolean stringEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null && str2 == null) {
            return false;
        } else if (str1 == null && str2 != null) {
            return false;
        } else {
            return str1.equals(str2);
        }
    }

    public Task clone() {
        return new Task(TID,taskName, cloneDate(dateFrom), cloneDate(dateTo), 
                cloneDate(deadline), location, details, status);
    }

    //@author A0118892U
    private Date cloneDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Date(date.getTime());
        }
    }

    //@author A0118892U
    public String[] toStringArray() {
        String[] taskStringArray = new String[DEFAULT_STRING_SIZE];

        taskStringArray[INDEX_TASK_ID] = Integer.toString(TID);

        if (taskName != null) {
            taskStringArray[INDEX_TASK_NAME] = taskName;
        } else {
            taskStringArray[INDEX_TASK_NAME] = null;
        }

        if (isDurationalTask()) {
            taskStringArray[INDEX_DATE_FROM] = removeTimePartFromDate(convertToStringFromDate(dateFrom));
            taskStringArray[INDEX_DATE_TO] = removeTimePartFromDate(convertToStringFromDate(dateTo));
        }

        if (isDeadlineTask()) {
            taskStringArray[INDEX_DATE_FROM] = null;
            if (dateTo != null) {
                taskStringArray[INDEX_DATE_TO] = removeTimePartFromDate(convertToStringFromDate(dateTo));
            }
            if (deadline != null) {
                taskStringArray[INDEX_DATE_TO] = removeTimePartFromDate(convertToStringFromDate(deadline));
            }
        }

        if (isFloatingTask()) {
            taskStringArray[INDEX_DATE_FROM] = null;
            taskStringArray[INDEX_DATE_TO] = null;
        }

        if (isForeverTask()) {
            taskStringArray[INDEX_DATE_FROM] = removeTimePartFromDate(convertToStringFromDate(dateFrom));
            taskStringArray[INDEX_DATE_TO] = null;
        }

        if (location != null) {
            taskStringArray[INDEX_LOCATION] = location;
        } else {
            taskStringArray[INDEX_LOCATION] = null;
        }

        if (details != null) {
            taskStringArray[INDEX_DETAILS] = details;
        } else {
            taskStringArray[INDEX_DETAILS] = null;
        }

        taskStringArray[INDEX_STATUS] = getStatusString();

        return taskStringArray;
    }

    //@author A0118892U
    private boolean isDurationalTask() {
        return getDateFrom() != null && getDateTo() != null &&
                getDeadline() == null;
    }

    //@author A0118892U
    private boolean isFloatingTask() {
        return getDateFrom() == null && getDateTo() == null &&
                getDeadline() == null;
    }

    //@author A0118892U
    private boolean isDeadlineTask() {
        return (getDateFrom() == null && getDateTo() == null &&
                getDeadline() != null) || 
                (getDateFrom() == null && getDateTo() != null &&
                getDeadline() == null); 
    }

    //@author A0118892U
    //Same as floating task
    private boolean isForeverTask() {
        return getDateFrom() != null && getDateTo() == null &&
                getDeadline() == null;
    }

    //@author A0118892U
    private String removeTimePartFromDate(String dateString) {
        return dateString.replace(ZERO_TIME, "");
    }

    //@author A0118892U
    private String convertToStringFromDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dateString = dateFormat.format(dateObject);
        return dateString;
    }
}
