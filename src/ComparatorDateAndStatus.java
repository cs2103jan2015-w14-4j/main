import java.util.Comparator;
import java.util.Date;

/**
 * Incomplete tasks will be put in front, while completed tasks will be put at the back
 * Both categories of tasks will be compared by date again.
 *
 */
//@author A0118892U
public class ComparatorDateAndStatus implements Comparator<Task> {
    private final int STATUS_COMPLETE = 6;

    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStatus() == STATUS_COMPLETE && task2.getStatus() == STATUS_COMPLETE) {
            Date date1 = null, date2 = null;

            //get date1 from task1
            if (isDurationalTask(task1) || isForeverTask(task1)) {
                date1 = task1.getDateFrom();
            } 

            if (isOnlyDateToTask(task1)) {
                date1 = task1.getDateTo();
            } 

            if (isDeadlineTask(task1)) {
                date1 = task1.getDeadline();
            }


            //get date2 from task2
            if (isDurationalTask(task2) || isForeverTask(task2)) {
                date2 = task2.getDateFrom();
            }

            if (isOnlyDateToTask(task2)) {
                date2 = task2.getDateTo();
            } 

            if (isDeadlineTask(task2)) {
                date2 = task2.getDeadline();
            }


            if (date1 != null && date2 != null) {
                if (date1.compareTo(date2) == 0) {
                    if (task1.getTID() < task2.getTID()) {
                        return -1;
                    } else if (task1.getTID() > task2.getTID()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return date1.compareTo(date2);
                }
            } else if (date1 == null && date2 != null) {
                return 1;
            } else if (date1 != null && date2 == null) {
                return -1;
            } else {
                if (task1.getTID() < task2.getTID()) {
                    return -1;
                } else if (task1.getTID() > task2.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (task1.getStatus() != STATUS_COMPLETE && task2.getStatus() == STATUS_COMPLETE) {
            return -1;
        } else if (task1.getStatus() == STATUS_COMPLETE && task2.getStatus() != STATUS_COMPLETE) {
            return 1;
        } else {
            Date date1 = null, date2 = null;

            //get date1 from task1
            if (isDurationalTask(task1) || isForeverTask(task1)) {
                date1 = task1.getDateFrom();
            }

            if (isOnlyDateToTask(task1)) {
                date1 = task1.getDateTo();
            }

            if (isDeadlineTask(task1)) {
                date1 = task1.getDeadline();
            }


            //get date2 from task2
            if (isDurationalTask(task2) || isForeverTask(task2)) {
                date2 = task2.getDateFrom();
            }

            if (isOnlyDateToTask(task2)) {
                date2 = task2.getDateTo();
            } 

            if (isDeadlineTask(task2)) {
                date2 = task2.getDeadline();
            }

            if (date1 != null && date2 != null) {
                if (date1.compareTo(date2) == 0) {
                    if (task1.getTID() < task2.getTID()) {
                        return -1;
                    } else if (task1.getTID() > task2.getTID()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return date1.compareTo(date2);
                }
            } else if (date1 == null && date2 != null) {
                return 1;
            } else if (date1 != null && date2 == null) {
                return -1;
            } else {
                if (task1.getTID() < task2.getTID()) {
                    return -1;
                } else if (task1.getTID() > task2.getTID()) {
                    return 1;
                } else {
                    return 0;
                }
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
