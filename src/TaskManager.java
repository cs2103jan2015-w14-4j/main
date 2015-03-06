import java.util.ArrayList;

public class TaskManager {
    //what format of time do you provide and
    //I suggest this format 31/05/2011 14:00
    //                      DD/MM/YYYY HH:mm

    //can I assume all commands are valid, no invalid commands?
    //almost valid, 
    //

    //null indicate nonexistence
    private static final int COMMAND_TYPE = 0;
    private static final int TID = 1;
    private static final int NAME = 2;
    private static final int DATEFROM = 3;
    private static final int DATETO = 4;
    private static final int DEADLINE = 5;
    private static final int LOCATION = 6;
    private static final int DEATAIL = 7;
    private static final boolean TID_IS_NOT_FOUND = false;
    private static final boolean TID_IS_FOUND = true;
    private static final int INITIAL_TID = 1000;

    private enum COMMAND_TYPE_TASK_MANAGER {
        add, edit, view, delete, init, undo, redo, invalid
    }

    private ArrayList<Task> _tasks;
    private ArrayList<String[]> _handledTasks = new ArrayList<String[]>();

    public TaskManager() {
        this._tasks = new ArrayList<Task>();
    }

    @SuppressWarnings("incomplete-switch")
    public ArrayList<Task> processTM(String[] input) {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(input[COMMAND_TYPE]);
        ArrayList<Task> returingTasks = new ArrayList<Task>();

        switch(commandObtained) {
        case add: break;
        case edit: break;
        case view: returingTasks = viewTasks(); break;
        case delete: 
            //0 is a dummy
            if(isAbleToDeleteTask(0)) {

            }
            break;
        }

        return returingTasks;
    }

    private COMMAND_TYPE_TASK_MANAGER obtainCommand (String command) {
        COMMAND_TYPE_TASK_MANAGER commandObtained;
        try {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.valueOf(command);
        } catch (IllegalArgumentException ex) {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.invalid;
        }
        return commandObtained;
    }

    private int getNewTID() {
        int newTID;

        newTID = _tasks.get(_tasks.size()).getTID() + 1;

        return newTID;
    }

    //dummy
    private boolean isAbleToAddTask(String[] input) {
        //if ID is not a number, then add to _handledTasks
        //if ID is a valid number, then do not add to _handledTasks
        return false;
    }

    //dummy
    private boolean isAbleToEditTask(String[] input) {
        return false;
    }

    private ArrayList<Task> viewTasks() {
        return _tasks;
    }

    private boolean isAbleToDeleteTask(int TID) {
        boolean isTIDFound = TID_IS_NOT_FOUND;
        for(Task task : _tasks) {
            if(task.getTID() == TID) {
                isTIDFound = TID_IS_FOUND;
            }
        }
        return isTIDFound;
    }

    //+undoAddTask(): boolean
    //+undoDeleteTask(): boolean

}
