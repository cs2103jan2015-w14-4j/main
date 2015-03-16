import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

public class TaskManager {
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
    private static final String CLEAR_INFO_INDICATOR = "";
    private static final String INVALID_COMMAND_MESSAGE = "The command is invalid.\n";
    private static final boolean TID_IS_NOT_FOUND = false;
    private static final boolean TID_IS_FOUND = true;
    private static final int INDEX_OF_ONLY_TASK = 0;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private ArrayList<Task> tasks;
    private int IDCounter;
    private Stack<String[]> undoStack = new Stack<String[]>();
    private Stack<String[]> redoStack = new Stack<String[]>();

    //------------constructor-------
    public TaskManager() {
        tasks = new ArrayList<Task>();
        IDCounter = INITIAL_TID;
    }

    //------------getter------------
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    //------------other methods------------
    public void processAddForInitialization(String[] inputs) throws ParseException {
        addATask(inputs);
    }

    public ArrayList<Task> processTM(String[] inputs, FileStorage externalStorage) throws ParseException {
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
                updateStackForEdit(taskToEdit, inputs, undoStack);
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
            initializeTasks(externalStorage);
            returningTasks = viewTasks();
            break;
        case undo:
            returningTasks = undoAnOperation();
            break;
        case redo:
            returningTasks = redoAnOperation();
            break;
        case invalid:
            System.out.print(INVALID_COMMAND_MESSAGE);
            break;
        }

        externalStorage.writeToFile(tasks);

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
            tasks.add(newTask);
            returningTasks = new ArrayList<Task>();
            returningTasks.add(newTask);
        } else {
            Task newTask = new Task(convertToIntType(inputs[TID]), inputs[TASK_NAME], 
                    convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                    convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                    convertToIntType(inputs[PRIORITY]));
            updateIDCounter(inputs[TID]);
            tasks.add(newTask);
            returningTasks = new ArrayList<Task>();
            returningTasks.add(newTask);
        }
        return returningTasks;
    }   

    private int getNewTID() {
        int newTID;
        if(tasks.isEmpty()) {
            newTID = INITIAL_TID;
        } else {
            ++IDCounter;
            newTID = IDCounter;
        }
        return newTID;
    }

    private boolean hasTID(String[] inputs) {
        return inputs[TID] == null;
    }

    private void updateIDCounter(String currentID) {
        if(IDCounter < convertToIntType(currentID)){
            IDCounter = convertToIntType(currentID);
        }
    }

    //assume dateString is as this format "dd/MM/yyyy HH:mm"
    private Date convertToDateObject(String dateString) throws ParseException {
        Date date = null;
        if(dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
            DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            date = format.parse(dateString);
        }
        return date;
    }

    private int convertToIntType(String intString) {
        int intType = 0;
        if(intString != null) {
            intType = Integer.parseInt(intString);
        }
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
        for(Task task : tasks) {
            if(task.getTID() == TID) {
                isTIDFound = TID_IS_FOUND;
                break;
            }
        }
        return isTIDFound;
    }

    private Task getTaskFromTID(int TID) {
        Task taskFound = null;
        for(Task task : tasks) {
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

            if(inputs[i] != null && isContentToClear(inputs, i)) {
                clearTaskInfo(taskToEdit, i);
            }
        }
        returningTasks = new ArrayList<Task>();
        returningTasks.add(taskToEdit);

        return returningTasks;
    }

    private boolean isInputEmpty(String[] inputs, int i) {
        return inputs[i] == null;
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

    private boolean isContentToClear(String[] inputs, int i) {
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
        task.setTaskName(null);
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
        task.setLocation(null);
    }

    private void clearTaskDetails(Task task) {
        task.setDetails(null);
    }

    private void clearTaskPriority(Task task) {
        task.setPriority(0);
    }


    private ArrayList<Task> viewTasks() {
        if(tasks.isEmpty()){
            return null;
        } else {
            return tasks;
        }
    }


    private boolean isAbleToDelete(String[] inputs) {
        int TaskTID = getTaskTID(inputs);
        return isTIDFound(TaskTID);
    }

    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = null;
        Iterator<Task> iterator = tasks.iterator();
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


    private void initializeTasks(FileStorage externalStorage) throws ParseException {
        externalStorage.readFromFile(this);
    }


    //to tell the stack there is an edit operation, 
    //but the corresponding information for undo is wrong
    private void updateStackForEdit(Task taskToEdit, String[] inputs, 
            Stack<String[]> stack) {
        String[] strForStack = new String[DEFAULT_SIZE];

        strForStack[COMMAND_TYPE] = inputs[COMMAND_TYPE];
        getStringArrayFromTask(taskToEdit, strForStack);

        for(int i = TASK_NAME; i < DEFAULT_SIZE; ++i) {
            if(strForStack[i] == null && inputs[i] != null) {
                strForStack[i] = CLEAR_INFO_INDICATOR;
            }
        }

        stack.push(strForStack);
    }

    private ArrayList<Task> undoAnOperation() throws ParseException {
        ArrayList<Task> returningTasks = null;
        if(!undoStack.isEmpty()) {
            String[] undoOperation = undoStack.peek();
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
                returningTasks = editATaskForUndo(taskToEdit, undoOperation);
                break;
            default:
                break;
            }
            updateRedoStack();
        }
        return returningTasks;
    }

    private ArrayList<Task> editATaskForUndo(Task taskToEdit, String[] inputs) 
            throws ParseException {
        ArrayList<Task> returningTasks = null;
        updateStackForEditUnderUndoRedo(taskToEdit, inputs, undoStack);
        returningTasks = editATask(taskToEdit, inputs);
        return returningTasks;
    }
    
    //correct the corresponding information for undo operation
    private void updateStackForEditUnderUndoRedo(Task taskToEdit, String[] inputs, 
            Stack<String[]> stack) {
        stack.pop();
        updateStackForEdit(taskToEdit, inputs, stack);
    }

    //This ArrayList contains only one item
    private void updateUndoStackForAdd(ArrayList<Task> tasks, String commandType) {
        Task task = tasks.get(INDEX_OF_ONLY_TASK);
        updateUndoStackFromTask(task, commandType);
    }

    private void updateUndoStackFromTask(Task task, String commandType) {
        String[] strForUndoStack = new String[DEFAULT_SIZE];

        strForUndoStack[COMMAND_TYPE] = commandType;
        getStringArrayFromTask(task, strForUndoStack);

        undoStack.push(strForUndoStack);
    }

    private void getStringArrayFromTask(Task task, String[] strArray) {
        strArray[TID] = convertToStringFromInt(task.getTID());
        strArray[TASK_NAME] = task.getTaskName();

        if(task.getDateFrom() != null) {
            strArray[DATE_FROM] = convertToStringFromDate(task.getDateFrom());
        } else {
            strArray[DATE_FROM] = null;
        }

        if(task.getDateTo() != null) {
            strArray[DATE_TO] = convertToStringFromDate(task.getDateTo());
        } else {
            strArray[DATE_TO] = null;
        }

        if(task.getDeadline() != null) {
            strArray[DEADLINE] = convertToStringFromDate(task.getDeadline());
        } else {
            strArray[DEADLINE] = null;
        }

        if(task.getLocation() != null) {
            strArray[LOCATION] = task.getLocation();
        } else {
            strArray[LOCATION] = null;
        }

        if(task.getDetails() != null) {
            strArray[DETAILS] = task.getDetails();
        } else {
            strArray[DETAILS] = null;
        }

        strArray[PRIORITY] = convertToStringFromInt(task.getPriority());
    }

    private String convertToStringFromDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dateString = dateFormat.format(dateObject);
        return dateString;
    }

    private String convertToStringFromInt(int intType) {
        String intString = Integer.toString(intType);
        return intString;
    }

    private void updateRedoStack() {
        redoStack.push(undoStack.pop());
    }


    private ArrayList<Task> redoAnOperation() throws ParseException {
        ArrayList<Task> returningTasks = null;
        if(!redoStack.isEmpty()) {
            String[] redoOperation = redoStack.peek();
            COMMAND_TYPE_TASK_MANAGER commandUndo = obtainCommand(redoOperation[COMMAND_TYPE]);
            switch(commandUndo) {
            case add:
                returningTasks = addATask(redoOperation);
                break;
            case delete:
                int TIDToDelete = getTaskTID(redoOperation);
                returningTasks = deleteATask(TIDToDelete);
                break;
            case edit:
                int TIDToEdit = getTaskTID(redoOperation);
                Task taskToEdit = getTaskFromTID(TIDToEdit);
                returningTasks = editATaskForRedo(taskToEdit, redoOperation);
                break;
            default:
                break;
            }
            updateUndoStackFromRedoOperation();
        }
        return returningTasks;
    }

    private ArrayList<Task> editATaskForRedo(Task taskToEdit, String[] inputs) 
            throws ParseException {
        ArrayList<Task> returningTasks = null;
        updateStackForEditUnderUndoRedo(taskToEdit, inputs, redoStack);
        returningTasks = editATask(taskToEdit, inputs);
        return returningTasks;
    }

    private void updateUndoStackFromRedoOperation() {
        undoStack.push(redoStack.pop());
    }
}
