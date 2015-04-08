import java.util.Comparator;

//@author A0118892U
/**
 * @author Ma Cong (A0118892U)
 *
 */
public class ComparatorPriority implements Comparator<Task> {

    public int compare(Task task1, Task task2) {
        if(task1.getPriority() < task2.getPriority()) {
            return -1;
        } else if(task1.getPriority() > task2.getPriority()) {
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
