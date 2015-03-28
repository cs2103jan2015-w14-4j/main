import java.util.Comparator;
import java.util.Date;

public class ComparatorDateFrom implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        Date dateFrom1 = task1.getDateFrom();
        Date dateFrom2 = task2.getDateFrom();
        
        if(dateFrom1 != null && dateFrom2 != null) {
            if(dateFrom1.compareTo(dateFrom2) == 0) {
                if(task1.getTID() < task2.getTID()) {
                    return -1;
                } else if(task1.getTID() > task1.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return dateFrom1.compareTo(dateFrom2);
            }
        } else if(dateFrom1 == null && dateFrom2 != null) {
            return 1;
        } else if(dateFrom1 != null && dateFrom2 == null) {
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

}
