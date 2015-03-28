import java.util.Comparator;

public class ComparatorPriority implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        if(task1.getPriority() < task2.getPriority()) {
            return -1;
        } else if(task1.getPriority() > task1.getPriority()) {
            return 1;
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
