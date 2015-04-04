import java.util.Comparator;
import java.util.Date;

public class ComparatorDateFrom implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        Date date1 = null, date2 = null;
        
        //get date1 from task1
        if(isDurationalTask(task1) || isForeverTask(task1)) {
            date1 = task1.getDateFrom();
        }
        
        if(isOnlyDateToTask(task1)) {
            date1 = task1.getDateTo();
        }
        
        if(isDeadlineTask(task1)) {
            date1 = task1.getDeadline();
        }
        
        
        //get date2 from task2
        if(isDurationalTask(task2) || isForeverTask(task2)) {
            date2 = task2.getDateFrom();
        }
        
        if(isOnlyDateToTask(task2)) {
            date2 = task2.getDateTo();
        }
        
        if(isDeadlineTask(task2)) {
            date2 = task2.getDeadline();
        }
        
        if(date1 != null && date2 != null) {
            if(date1.compareTo(date2) == 0) {
                if(task1.getTID() < task2.getTID()) {
                    return -1;
                } else if(task1.getTID() > task1.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return date1.compareTo(date2);
            }
        } else if(date1 == null && date2 != null) {
            return 1;
        } else if(date1 != null && date2 == null) {
            return -1;
        } else {
            if(task1.getTID() < task2.getTID()) {
                return -1;
            } else if(task1.getTID() > task1.getTID()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    private boolean isDurationalTask(Task task) {
        return task.getDateFrom() != null && task.getDateTo() != null &&
                task.getDeadline() == null;
    }

    private boolean isDeadlineTask(Task task) {
        return task.getDateFrom() == null && task.getDateTo() == null &&
                task.getDeadline() != null; 
    }
    
    private boolean isForeverTask(Task task) {
        return task.getDateFrom() != null && task.getDateTo() == null &&
                task.getDeadline() == null;
    }
    
    private boolean isOnlyDateToTask(Task task) {
        return task.getDateFrom() == null && task.getDateTo() != null &&
                task.getDeadline() == null; 
    }
}
