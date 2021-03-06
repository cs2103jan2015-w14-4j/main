import java.util.Comparator;

/**
 * This comparator compares task by their ID
 *
 */
//@author A0118892U
public class ComparatorID implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        if (task1.getTID() < task2.getTID()) {
            return -1;
        } else if (task1.getTID() > task2.getTID()) {
            return 1;
        } else {
            return 0;
        }
    }
}
