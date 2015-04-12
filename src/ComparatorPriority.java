import java.util.Comparator;

//@author A0118892U
public class ComparatorPriority implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        if(task1.getStatus() < task2.getStatus()) {
            return -1;
        } else if(task1.getStatus() > task2.getStatus()) {
            return 1;
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
