import java.util.Comparator;

public class ComparatorTaskName implements Comparator<Task> {
    
    public int compare(Task task1, Task task2) {
        String taskName1 = task1.getTaskName();
        String taskName2 = task2.getTaskName();
        
        if(taskName1 != null && taskName2 != null) {
            if(taskName1.compareTo(taskName2) == 0) {
                if(task1.getTID() < task2.getTID()) {
                    return -1;
                } else if(task1.getTID() > task2.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return taskName1.compareTo(taskName2);
            }
        } else if(taskName1 == null && taskName2 != null) {
            return 1;
        } else if(taskName1 != null && taskName2 == null) {
            return -1;
        } else {
            if(task1.getTID() < task2.getTID()) {
                return -1;
            } else if(task1.getTID() > task2.getTID()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
