import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskManager {
    //------------solved--------------
    //what format of time do you provide and
    //I suggest this format 31/05/2011 14:00
    //                      DD/MM/YYYY HH:mm

    //can I assume all commands are valid, no invalid commands?
    //almost valid, 
    //

    //for edit task, are you gonna send me what is being edited in the String[]
    //yes
    
    //what should I return if I can't carry the action. 
    //Eg edit 1134, delete 1135; but there are no 1134 1135?
    //return null

    //if there is a detail associated with a task previously, now you wanna remove the detail
    //how to pass me the edit array String?
    //pass me a ""

    //-------------unsolved-----------

    private static final int COMMAND_TYPE = 0;
    private static final int TID = 1;
    private static final int TASK_NAME = 2;
    private static final int DATE_FROM = 3;
    private static final int DATE_TO = 4;
    private static final int DEADLINE = 5;
    private static final int LOCATION = 6;
    private static final int DETAILS = 7;
    private static final int PRIORITY = 8;
    private static final boolean TID_IS_NOT_FOUND = false;
    private static final boolean TID_IS_FOUND = true;
    private static final int INITIAL_TID = 1000;
    private static final String EMPTY_INPUT = "null";
    private static final String INVALID_COMMAND_MESSAGE = "The command is invalid.\n";
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private enum COMMAND_TYPE_TASK_MANAGER {
        add, edit, view, delete, init, undo, redo, invalid
    }
    
    private ArrayList<Task> _tasks;
    //private ArrayList<String[]> _handledTasks = new ArrayList<String[]>();
    private int _IDCounter = INITIAL_TID;

    //------------constructor-------
    public TaskManager() {
        this._tasks = new ArrayList<Task>();
    }

    //------------getter------------
    public ArrayList<Task> getTasks() {return _tasks;}

    //------------other methods------------
    public ArrayList<Task> processTM(String[] inputs) throws ParseException {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = null;

        switch(commandObtained) {
        case add: 
            returningTasks = addATask(inputs);
            break;
        case edit: 
            returningTasks = editATask(convertToIntType(inputs[TID]), inputs);
            break;
        case view: 
            returningTasks = viewTasks(); 
            break;
        case delete: 
            returningTasks = deleteATask(convertToIntType(inputs[TID]));
            break;
        case init:
            //call Wei Quan first
            //then he calls me back
            //then I add one by one and view it at the end
            break;
        case undo:
            break;
        case redo:
            break;
        case invalid:
            System.out.print(INVALID_COMMAND_MESSAGE);
            break;
        }

        return returningTasks;
    }

    private ArrayList<Task> addATask(String[] inputs) throws ParseException {
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        if(hasTID(inputs)){
            Task newTask = new Task(getNewTID(), inputs[TASK_NAME], 
                    convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                    convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                    convertToIntType(inputs[PRIORITY]));
            _tasks.add(newTask);
            returningTasks = new ArrayList<Task>();
            returningTasks.add(newTask);
        } else {
            Task newTask = new Task(convertToIntType(inputs[TID]), inputs[TASK_NAME], 
                    convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                    convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                    convertToIntType(inputs[PRIORITY]));
            updateIDCounter(inputs[TID]);
            _tasks.add(newTask);
            returningTasks.add(newTask);
        }
        return returningTasks;
    }
    
    private boolean hasTID(String[] inputs) {
        return inputs[TID].equals(EMPTY_INPUT);
    }
    
    private void updateIDCounter(String currentID) {
        if(_IDCounter < convertToIntType(currentID)){
            _IDCounter = convertToIntType(currentID);
        }
    }
    
    //assume dateString is as this format "dd/MM/yyyy HH:mm"
    private Date convertToDateObject(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = format.parse(dateString);
        return date;
    }

    private int convertToIntType(String intString) {
        int intType = Integer.parseInt(intString);
        return intType;
    }

    private ArrayList<Task> editATask(int TID, String[] inputs) throws ParseException {
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        for(Task task : _tasks) {
            if(TID == task.getTID()) {
                for(int i = TASK_NAME; i < inputs.length; i++) {
                    if(!isInputEmpty(inputs, i)) {
                        editTaskInfo(inputs, task, i);
                    }
                }
                returningTasks.add(task);
            }
        }
        return returningTasks;
    }

    private void editTaskInfo(String[] inputs, Task task, int i)
            throws ParseException {
        switch(i) {
        case TASK_NAME: editTaskName(inputs, task); break;
        case DATE_FROM: editTaskDateFrom(inputs, task); break;
        case DATE_TO: editTaskDateTo(inputs, task); break;
        case DEADLINE: editTaskDeadline(inputs, task); break;
        case LOCATION: editTaskLocation(inputs, task); break;
        case DETAILS: editTaskDetails(inputs, task); break;
        case PRIORITY: editTaskPriority(inputs, task); break;
        }
    }

    private void editTaskPriority(String[] inputs, Task task) {
        int newPriority = convertToIntType(inputs[PRIORITY]);
        task.setPriority(newPriority);
    }

    private void editTaskDetails(String[] inputs, Task task) {
        task.setDetails(inputs[DETAILS]);
    }

    private void editTaskLocation(String[] inputs, Task task) {
        task.setLocation(inputs[LOCATION]);
    }

    private void editTaskDeadline(String[] inputs, Task task)
            throws ParseException {
        Date newDeadline = convertToDateObject(inputs[DEADLINE]);
        task.setDeadline(newDeadline);
    }

    private void editTaskDateTo(String[] inputs, Task task)
            throws ParseException {
        Date newDateTo = convertToDateObject(inputs[DATE_TO]);
        task.setDateTo(newDateTo);
    }

    private void editTaskDateFrom(String[] inputs, Task task)
            throws ParseException {
        Date newDateFrom = convertToDateObject(inputs[DATE_FROM]);
        task.setDateFrom(newDateFrom);
    }

    private void editTaskName(String[] inputs, Task task) {
        task.setTaskName(inputs[TASK_NAME]);
    }

    private boolean isInputEmpty(String[] inputs, int i) {
        return inputs[i].equals(EMPTY_INPUT);
    }

    /*private String getStringFromInt() {

    }

    private String getStringFromDate() {

    }*/



    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = new ArrayList<Task>(viewTasks());
        for(Task task : _tasks) {
            if(TID == task.getTID()) {

                returningTasks.add(task);
            }
        }
        return returningTasks;
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
        if(_tasks.isEmpty()) {
            newTID = INITIAL_TID + 1;
        } else {
            newTID = _IDCounter + 1;
            increaseIDCounterByOne();
        }

        return newTID;
    }
    
    private void increaseIDCounterByOne() {
        ++_IDCounter;
    }

    private ArrayList<Task> viewTasks() {
        return _tasks;
    }

    //+undoAddTask(): boolean
    //+undoDeleteTask(): boolean

}
