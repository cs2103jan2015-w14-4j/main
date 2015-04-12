import java.util.Comparator;

/**
 * This comparator compares tasks by their location in terms of alphabetical order.
 * When there is a tie, tasks will be compared by their IDs.
 *
 */
//@author A0118892U
public class ComparatorLocation implements Comparator<Task>{

    public int compare(Task task1, Task task2) {
        String location1 = task1.getLocation();
        String location2 = task2.getLocation();
        
        if(location1 != null && location2 != null) {
            if(location1.compareTo(location2) == 0) {
                if(task1.getTID() < task2.getTID()) {
                    return -1;
                } else if(task1.getTID() > task2.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return location1.compareTo(location2);
            }
        } else if(location1 == null && location2 != null) {
            return 1;
        } else if(location1 != null && location2 == null) {
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