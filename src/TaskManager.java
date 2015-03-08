import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

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
    private static final int INITIAL_TID = 1000;
    private static final int DEFAULT_SIZE = 9;
    private static final String EMPTY_INPUT = "null";
    private static final String CLEAR_INFO_INDICATOR = "";
    private static final String INVALID_COMMAND_MESSAGE = "The command is invalid.\n";
    private static final boolean TID_IS_NOT_FOUND = false;
    private static final boolean TID_IS_FOUND = true;
    private static final int INDEX_OF_ONLY_TASK = 0;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private enum COMMAND_TYPE_TASK_MANAGER {
        add, edit, view, delete, init, undo, redo, invalid
    }

    private static ArrayList<Task> _tasks;
    private static int _IDCounter;
    private Stack<String[]> _undoStack = new Stack<String[]>();
    private Stack<String[]> _redoStack = new Stack<String[]>();

    //------------constructor-------
    public TaskManager() {
        TaskManager._tasks = new ArrayList<Task>();
    }

    //------------getter------------
    public ArrayList<Task> getTasks() {
        return _tasks;
    }

    //------------other methods------------
    public ArrayList<Task> processTM(String[] inputs) throws ParseException {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = null;

        switch(commandObtained) {
        case add:
            returningTasks = addATask(inputs);
            if(returningTasks != null) {
                updateUndoStackForAdd(returningTasks, inputs[COMMAND_TYPE]);
            }
            break;
        case edit:
            if(isAbleToEdit(inputs)) {
                int TIDToEdit = getTaskTID(inputs);
                Task taskToEdit = getTaskFromTID(TIDToEdit);
                updateStackForEdit(taskToEdit, inputs, _undoStack);
                returningTasks = editATask(taskToEdit, inputs);
            }
            break;
        case view:
            returningTasks = viewTasks(); 
            break;
        case delete:
            if(isAbleToDelete(inputs)) {
                int TIDToDelete = getTaskTID(inputs);
                Task taskToDelete = getTaskFromTID(TIDToDelete);
                updateUndoStackFromTask(taskToDelete, inputs[COMMAND_TYPE]);
                returningTasks = deleteATask(TIDToDelete);
            }
            break;
        case init:
            initializeTasks();
            returningTasks = viewTasks();
            break;
        case undo:
            undoAnOperation();
            break;
        case redo:
            break;
        case invalid:
            System.out.print(INVALID_COMMAND_MESSAGE);
            break;
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


    private ArrayList<Task> addATask(String[] inputs) throws ParseException {
        ArrayList<Task> returningTasks = null;
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
            returningTasks = new ArrayList<Task>();
            returningTasks.add(newTask);
        }
        return returningTasks;
    }   

    private int getNewTID() {
        int newTID;
        if(_tasks.isEmpty()) {
            newTID = INITIAL_TID;
        } else {
            ++_IDCounter;
            newTID = _IDCounter;
        }
        return newTID;
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

    private boolean isAbleToEdit(String[] inputs) {
        int TaskTID = getTaskTID(inputs);
        return isTIDFound(TaskTID);
    }

    private int getTaskTID(String[] inputs) {
        int TaskTID = convertToIntType(inputs[TID]);
        return TaskTID;
    }

    private boolean isTIDFound(int TID) {
        boolean isTIDFound = TID_IS_NOT_FOUND;
        for(Task task : _tasks) {
            if(task.getTID() == TID) {
                isTIDFound = TID_IS_FOUND;
                break;
            }
        }
        return isTIDFound;
    }

    private Task getTaskFromTID(int TID) {
        Task taskFound = null;
        for(Task task : _tasks) {
            if(task.getTID() == TID) {
                taskFound = task;
                break;
            }
        }
        return taskFound;
    }

    private ArrayList<Task> editATask(Task taskToEdit, String[] inputs) 
            throws ParseException {
        ArrayList<Task> returningTasks = null;

        for(int i = TASK_NAME; i < inputs.length; ++i) {
            if(!isInputEmpty(inputs, i)) {
                editTaskInfo(inputs, taskToEdit, i);
            }

            if(isContentToBeClear(inputs, i)) {
                clearTaskInfo(taskToEdit, i);
            }
        }
        returningTasks = new ArrayList<Task>();
        returningTasks.add(taskToEdit);

        return returningTasks;
    }

    private boolean isInputEmpty(String[] inputs, int i) {
        return inputs[i].equals(EMPTY_INPUT);
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

    private boolean isContentToBeClear(String[] inputs, int i) {
        return inputs[i].equals(CLEAR_INFO_INDICATOR);
    }

    private void clearTaskInfo(Task task, int i) throws ParseException {
        switch(i) {
        case TASK_NAME: clearTaskName(task); break;
        case DATE_FROM: clearTaskDateFrom(task); break;
        case DATE_TO: clearTaskDateTo(task); break;
        case DEADLINE: clearTaskDeadline(task); break;
        case LOCATION: clearTaskLocation(task); break;
        case DETAILS: clearTaskDetails(task); break;
        case PRIORITY: clearTaskPriority(task); break;
        }
    }

    private void clearTaskName(Task task) {
        task.setTaskName(EMPTY_INPUT);
    }

    private void clearTaskDateFrom(Task task) {
        task.setTaskName(null);
    }

    private void clearTaskDateTo(Task task) {
        task.setDateTo(null);
    }

    private void clearTaskDeadline(Task task) {
        task.setDeadline(null);
    }

    private void clearTaskLocation(Task task) {
        task.setLocation(EMPTY_INPUT);
    }

    private void clearTaskDetails(Task task) {
        task.setDetails(EMPTY_INPUT);
    }

    private void clearTaskPriority(Task task) {
        task.setPriority(0);
    }

    private ArrayList<Task> viewTasks() {
        return _tasks;
    }


    private boolean isAbleToDelete(String[] inputs) {
        int TaskTID = getTaskTID(inputs);
        return isTIDFound(TaskTID);
    }

    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = null;
        Iterator<Task> iterator = _tasks.iterator();
        while (iterator.hasNext()) {
            Task nextTask = (Task) iterator.next();
            if(TID == nextTask.getTID()) {
                returningTasks = new ArrayList<Task>();
                returningTasks.add(nextTask);
                iterator.remove();
            }
        }
        return returningTasks;
    }


    //@warning: incomplete method()
    private void initializeTasks() throws ParseException {
        //basically call Wei Quan and do his readFromFile() method
        //because I do not know how he named the method, so I cannot write now

        //suggested method
        //FileStorage fileStorage = new FileStorage();
        //fileStorage.retriveDataFromFile();
    }


    private ArrayList<Task> undoAnOperation() throws ParseException {
        ArrayList<Task> returningTasks = null;
        if(!_undoStack.isEmpty()) {
            String[] undoOperation = _undoStack.peek();
            COMMAND_TYPE_TASK_MANAGER commandUndo = obtainCommand(undoOperation[COMMAND_TYPE]);
            switch(commandUndo) {
            case add: 
                int TIDToDelete = getTaskTID(undoOperation);
                returningTasks = deleteATask(TIDToDelete);
                break;
            case delete:
                returningTasks = addATask(undoOperation);
                break;
            case edit:
                int TIDToEdit = getTaskTID(undoOperation);
                Task taskToEdit = getTaskFromTID(TIDToEdit);
                returningTasks = editATaskFromUndo(taskToEdit, undoOperation);
                break;
            default:
                break;
            }
            //updateRedoStack();
        }
        return returningTasks;
    }
    
    private void updateUndoStackForAdd(ArrayList<Task> tasks, String commandType) {
        Task task = tasks.get(INDEX_OF_ONLY_TASK);
        updateUndoStackFromTask(task, commandType); 
    }
    
    private void updateUndoStackFromTask(Task task, String commandType) {
        String[] strForUndoStack = new String[DEFAULT_SIZE];

        strForUndoStack[COMMAND_TYPE] = commandType;
        getStringArrayFromTask(task, strForUndoStack);

        _undoStack.push(strForUndoStack);
    }
    
    private void getStringArrayFromTask(Task task, String[] strArray) {
        strArray[TID] = convertToStringFromInt(task.getTID());
        strArray[TASK_NAME] = task.getTaskName();

        if(task.getDateFrom() != null) {
            strArray[DATE_FROM] = convertToStringFromDate(task.getDateFrom());
        } else {
            strArray[DATE_FROM] = EMPTY_INPUT;
        }

        if(task.getDateTo() != null) {
            strArray[DATE_TO] = convertToStringFromDate(task.getDateTo());
        } else {
            strArray[DATE_TO] = EMPTY_INPUT;
        }

        if(task.getDeadline() != null) {
            strArray[DEADLINE] = convertToStringFromDate(task.getDeadline());
        } else {
            strArray[DEADLINE] = EMPTY_INPUT;
        }

        strArray[LOCATION] = task.getLocation();
        strArray[DETAILS] = task.getDetails();
        strArray[PRIORITY] = convertToStringFromInt(task.getPriority());
    }
}
