import java.util.Comparator;
import java.util.Date;

public class ComparatorDeadline implements Comparator<Task> {
    
    public int compare(Task task1, Task task2) {
        Date deadline1 = task1.getDeadline();
        Date deadline2 = task2.getDeadline();
        
        if(deadline1 != null && deadline2 != null) {
            if(deadline1.compareTo(deadline2) == 0) {
                if(task1.getTID() < task2.getTID()) {
                    return -1;
                } else if(task1.getTID() > task1.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return deadline1.compareTo(deadline2);
            }
        } else if(deadline1 == null && deadline2 != null) {
            return 1;
        } else if(deadline1 != null && deadline2 == null) {
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
