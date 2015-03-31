import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskManager implements TaskManagerInterface {
    public static final int COMMAND_TYPE = 0;
    public static final int TID = 1;
    public static final int TASK_NAME = 2;
    public static final int DATE_FROM = 3;
    public static final int DATE_TO = 4;
    public static final int DEADLINE = 5;
    public static final int LOCATION = 6;
    public static final int DETAILS = 7;
    public static final int PRIORITY = 8;
    public static final int DEFAULT_STRING_SIZE = 9;
    public static final int VIEW_TYPE = 2;
    public static final int REMINDER = 3;

    private static final int INITIAL_TID = 10;
    private static final int NUM_ATTRIBUTE_FOR_DATE_OBJECT = 5;
    private static final int DAY_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    private static final int HOUR_INDEX = 3;
    private static final int MINUTE_INDEX = 4;
    private static final int SEARCH_INDEX = 2;
    private static final String CLEAR_INFO_INDICATOR = "";
    private static final boolean IS_CLASH = true;
    private static final boolean IS_NOT_CLASH = false;
    private static final boolean DATE_IS_VALID = true;
    private static final boolean DATE_IS_INVALID = false;
    private static final boolean SEARCH_IS_FOUND = true;
    private static final boolean SEARCH_IS_NOT_FOUND = false;
    private static final int INDEX_ZERO = 0;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String ID_STRING = "id";
    private static final String TASK_NAME_STRING = "task name";
    private static final String PRIORITY_STRING = "priority";
    private static final String DATE_FROM_STRING = "date from";
    private static final String DEADLINE_STRING = "deadline";
    private static final String LOCATION_STRING = "location";


    private ArrayList<Task> tasks;
    private int IDCounter;
    private Stack<String[]> undoStack = new Stack<String[]>();
    private Stack<String[]> redoStack = new Stack<String[]>();
    private HashSet<Integer> TaskIDs = new HashSet<Integer>();



    //--------------------constructor-----------------
    public TaskManager() {
        tasks = new ArrayList<Task>();
        IDCounter = INITIAL_TID;
    }



    //--------------------getter----------------------
    //This method is for testing purpose
    protected ArrayList<Task> getTasks() {
        return tasks;
    }

    //This method is for testing purpose
    protected Stack<String[]> getUndoStack() {
        return undoStack;
    }

    //This method is for testing purpose
    protected Stack<String[]> getRedoStack() {
        return redoStack;
    }



    //--------------------other methods-----------------------------------
    //--------------------Initialization method starts--------------------
    public void processInitialization(String[] inputs) {
        Task newTask;
        if(isInputsHavingTID(inputs)){
            newTask = processInitializationWithID(inputs);
        } else {  
            newTask = processInitializationWithoutID(inputs);
        }

        addIDToTaskIDs(newTask.getTID());
        assertTaskDatesAreValid(newTask);

        sortTasks(tasks, TID);
        tasks.add(newTask);
    }

    private Task processInitializationWithID(String[] inputs) {
        return processAddWithID(inputs);
    }

    private Task processInitializationWithoutID(String[] inputs) {
        return processAddWithoutID(inputs);
    }
    //--------------------Initialization method ends--------------------



    public ArrayList<Task> processTM(String[] inputs) {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = null;

        switch(commandObtained) {

        case addTask:
            returningTasks = addATask(inputs);
            if(returningTasks != null) {
                updateUndoStackFromTask(returningTasks.get(INDEX_ZERO), inputs[COMMAND_TYPE]);
                saveTasksToFile();
            }
            break;

        case editTask:
            if(isAbleToEdit(inputs[TID])) {
                returningTasks = processEditCommand(inputs);
                saveTasksToFile();
            }
            break;

        case viewTask:
            returningTasks = viewTasks(inputs); 
            break;

        case deleteTask:
            if(isAbleToDelete(inputs[TID])) {
                returningTasks = processDeleteCommand(inputs);
                saveTasksToFile();
            }
            break;

        case searchTask:
            returningTasks = searchTask(inputs);
            break;

        case undoTask:
            returningTasks = undoAnOperation();
            saveTasksToFile();
            break;

        case redoTask:
            returningTasks = redoAnOperation();
            saveTasksToFile();
            break;

        case invalidTask:
            //what do I do if command is invalid
            break;
        }

        return returningTasks;
    }



    /**
     * if the command does not exist, returns a invalidTask
     * @param command  a String received from FlexiParser
     * @return         a COMMAND_TYPE_TASK_MANAGER type of the String command
     */
    private COMMAND_TYPE_TASK_MANAGER obtainCommand (String command) {
        COMMAND_TYPE_TASK_MANAGER commandObtained;
        try {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.valueOf(command);
        } catch (IllegalArgumentException ex) {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.invalidTask;
        }

        return commandObtained;
    }



    //--------------------Add method starts--------------------
    private ArrayList<Task> addATask(String[] inputs) {
        Task newTask;
        if(isInputsHavingTID(inputs)){
            newTask = processAddWithID(inputs);       
        } else {
            newTask = processAddWithoutID(inputs);
        }

        assertTaskDatesAreValid(newTask);
        assert newTask.getTID() >= INITIAL_TID;
        addIDToTaskIDs(newTask.getTID());

        tasks.add(newTask);
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        returningTasks.add(newTask.clone());

        //add clashed durational tasks to the return
        if(isTaskADurationalTask(newTask)) {
            addClashingTasksForReturning(newTask, returningTasks);
        }

        sortTasks(tasks, TID);

        return returningTasks;
    }

    private Task processAddWithID(String[] inputs) {
        if(isIDClashing(inputs[TID])) {
            inputs[TID] = convertToStringFromInt(getNewTID());
        }

        if(isIDLessThanTen(inputs[TID])) {
            inputs[TID] = convertToStringFromInt(getNewTID());
        }

        Task newTask = new Task(convertToIntType(inputs[TID]), inputs[TASK_NAME], 
                convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                convertToIntType(inputs[PRIORITY]));

        updateIDCounter(inputs[TID]);

        return newTask;
    }

    private boolean isIDLessThanTen(String TID) {
        return convertToIntType(TID) < INITIAL_TID;
    }

    private Task processAddWithoutID(String[] inputs) {
        Task newTask = new Task(getNewTID(), inputs[TASK_NAME], 
                convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                convertToIntType(inputs[PRIORITY]));

        return newTask;
    }

    private void addIDToTaskIDs(int TID) {
        TaskIDs.add(TID);
    }

    private boolean isNewTaskClashedWithExistingTask(Task newTask, Task existing) {
        if(existing.getDateTo().compareTo(newTask.getDateFrom()) <= 0 || 
                existing.getDateFrom().compareTo(newTask.getDateTo()) >= 0) {
            return IS_NOT_CLASH;
        }

        return IS_CLASH;
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

    private boolean isInputsHavingTID(String[] inputs) {
        return inputs[TID] != null;
    }

    private void updateIDCounter(String currentID) {
        if(IDCounter < convertToIntType(currentID)){
            IDCounter = convertToIntType(currentID);
        }
    }
    //--------------------Add method ends--------------------



    //--------------------Edit method starts-----------------
    //if there is a ID clash, means TID is found
    private boolean isAbleToEdit(String TaskID) {
        return isIDClashing(TaskID);
    }

    private ArrayList<Task> processEditCommand(String[] inputs) {
        Task taskToEdit = getTaskToEdit(inputs);
        updateStackForEdit(taskToEdit, inputs, undoStack);

        return editATask(taskToEdit, inputs);
    }

    private Task getTaskToEdit(String[] inputs) {
        return getTaskFromTIDString(inputs);
    }

    private ArrayList<Task> editATask(Task taskToEdit, String[] inputs) {
        for(int i = TASK_NAME; i < inputs.length; ++i) {
            if(isTaskInfoChanged(inputs, i)) {
                editTaskInfo(inputs, taskToEdit, i);
            }

            if(isTaskInfoToClear(inputs, i)) {
                clearTaskInfo(taskToEdit, i);
            }
        }
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        returningTasks.add(taskToEdit.clone());

        assertTaskDatesAreValid(taskToEdit);

        //add clashed durational tasks to the return
        if(isTaskADurationalTask(taskToEdit)) {
            addClashingTasksForReturning(taskToEdit, returningTasks);
        }

        return returningTasks;
    }

    private boolean isTaskInfoChanged(String[] inputs, int i) {
        return inputs[i] != null;
    }

    private void editTaskInfo(String[] inputs, Task task, int i) {
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

    private void editTaskDeadline(String[] inputs, Task task) {
        Date newDeadline = convertToDateObject(inputs[DEADLINE]);
        task.setDeadline(newDeadline);
    }

    private void editTaskDateTo(String[] inputs, Task task) {
        Date newDateTo = convertToDateObject(inputs[DATE_TO]);
        task.setDateTo(newDateTo);
    }

    private void editTaskDateFrom(String[] inputs, Task task) {
        Date newDateFrom = convertToDateObject(inputs[DATE_FROM]);
        task.setDateFrom(newDateFrom);
    }

    private void editTaskName(String[] inputs, Task task) {
        task.setTaskName(inputs[TASK_NAME]);
    }

    private boolean isTaskInfoToClear(String[] inputs, int i) {
        return inputs[i] != null && inputs[i].equals(CLEAR_INFO_INDICATOR);
    }

    private void clearTaskInfo(Task task, int i) {
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
    //----------Edit method ends----------



    //----------View method starts----------
    private ArrayList<Task> viewTasks(String[] inputs) {
        if(tasks.isEmpty()){
            return null;
        } else {
            ArrayList<Task> returningTasks = new ArrayList<Task>();
            for(Task task: tasks) {
                returningTasks.add(task.clone());
            }
            if(isViewOptionDefault(inputs)) {
                return returningTasks;
            } else {
                int viewType = getViewOption(inputs);
                sortTasks(returningTasks, viewType);
                return returningTasks;
            }
        }
    }

    private boolean isViewOptionDefault(String[] inputs) {
        return inputs[VIEW_TYPE] == null;
    }

    private int getViewOption(String[] inputs) {
        int viewType = getViewTypeInt(inputs[VIEW_TYPE]);

        return viewType;
    }

    private int getViewTypeInt(String viewType) {
        viewType = viewType.toLowerCase();
        switch (viewType) {
        case ID_STRING: 
            return TID;
        case TASK_NAME_STRING: 
            return TASK_NAME;
        case DATE_FROM_STRING:
            return DATE_FROM;
        case DEADLINE_STRING:
            return DEADLINE;
        case LOCATION_STRING:
            return LOCATION;
        case PRIORITY_STRING:
            return PRIORITY;
        default:
            //need to change to throw exception later
            return TID;
        }
    }
    //--------------------View method ends--------------------



    //--------------------Delete method starts----------------
    private boolean isAbleToDelete(String TaskID) {
        return isIDClashing(TaskID);
    }

    private ArrayList<Task> processDeleteCommand(String[] inputs) {
        int TIDToDelete = getTaskTID(inputs);
        Task taskToDelete = getTaskToDelete(inputs);
        updateUndoStackFromTask(taskToDelete, inputs[COMMAND_TYPE]);

        return deleteATask(TIDToDelete);
    }

    private Task getTaskToDelete(String[] inputs) {
        return getTaskFromTIDString(inputs);
    }

    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = null;
        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task nextTask = (Task) iterator.next();
            if(TID == nextTask.getTID()) {
                returningTasks = new ArrayList<Task>();
                returningTasks.add(nextTask.clone());
                iterator.remove();
            }
        }
        removeIDFromTaskIDs(TID);

        return returningTasks;
    }

    private void removeIDFromTaskIDs(int TID) {
        TaskIDs.remove(TID);
    }
    //--------------------Delete method ends--------------------



    //--------------------Search method starts--------------------
    private ArrayList<Task> searchTask(String[] inputs) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        assert inputs[SEARCH_INDEX].trim() != "";

        for(Task task: tasks) {
            if(isSearchFound(task, inputs[SEARCH_INDEX])) {
                returningTasks.add(task.clone());
            }
        }

        if(returningTasks.isEmpty()) {
            return null;
        } else {
            return returningTasks;
        }
    }

    private boolean isSearchFound(Task task, String search) {
        boolean isSearchFound = SEARCH_IS_NOT_FOUND;
        String[] strForSearch = new String[DEFAULT_STRING_SIZE];
        strForSearch[COMMAND_TYPE] = null;

        getStringArrayFromTask(task, strForSearch);

        for(String str: strForSearch) {
            if(str != null && str.toLowerCase().contains(search.toLowerCase())) {
                isSearchFound = SEARCH_IS_FOUND;
                break;
            }
        }

        return isSearchFound;
    }
    //--------------------Search method ends--------------------



    //--------------------Undo method starts--------------------
    private ArrayList<Task> undoAnOperation() {
        ArrayList<Task> returningTasks = null;

        if(!undoStack.isEmpty()) {
            String[] undoOperation = undoStack.peek();
            COMMAND_TYPE_TASK_MANAGER commandUndo = obtainCommand(undoOperation[COMMAND_TYPE]);

            switch(commandUndo) {
            case addTask: 
                int TIDToDelete = getTaskTID(undoOperation);
                returningTasks = deleteATask(TIDToDelete);
                break;
            case deleteTask:
                returningTasks = addATask(undoOperation);
                break;
            case editTask:
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

    private ArrayList<Task> editATaskForUndo(Task taskToEdit, String[] inputs) {
        ArrayList<Task> returningTasks = null;

        updateStackForEditUnderUndoRedo(taskToEdit, inputs, undoStack);
        returningTasks = editATask(taskToEdit, inputs);

        return returningTasks;
    }

    private void updateRedoStack() {
        redoStack.push(undoStack.pop());
    }
    //--------------------Undo method ends----------------------



    //--------------------Redo method starts--------------------
    private ArrayList<Task> redoAnOperation() {
        ArrayList<Task> returningTasks = null;

        if(!redoStack.isEmpty()) {
            String[] redoOperation = redoStack.peek();
            COMMAND_TYPE_TASK_MANAGER commandUndo = obtainCommand(redoOperation[COMMAND_TYPE]);

            switch(commandUndo) {
            case addTask:
                returningTasks = addATask(redoOperation);
                break;
            case deleteTask:
                int TIDToDelete = getTaskTID(redoOperation);
                returningTasks = deleteATask(TIDToDelete);
                break;
            case editTask:
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

    private ArrayList<Task> editATaskForRedo(Task taskToEdit, String[] inputs) {
        ArrayList<Task> returningTasks = null;

        updateStackForEditUnderUndoRedo(taskToEdit, inputs, redoStack);
        returningTasks = editATask(taskToEdit, inputs);

        return returningTasks;
    }

    private void updateUndoStackFromRedoOperation() {
        undoStack.push(redoStack.pop());
    }
    //--------------------Redo method ends--------------------



    //--------------------Methods used more than once start----------------------
    /**
     * This method is used by updateStackForEditUnderUndoRedo() and processEditCommand()
     * @param taskToEdit
     * @param inputs
     * @param stack
     */
    private void updateStackForEdit(Task taskToEdit, String[] inputs, 
            Stack<String[]> stack) {
        String[] strForStack = new String[DEFAULT_STRING_SIZE];

        strForStack[COMMAND_TYPE] = inputs[COMMAND_TYPE];
        getStringArrayFromTask(taskToEdit, strForStack);

        for(int i = TASK_NAME; i < DEFAULT_STRING_SIZE; ++i) {
            if(strForStack[i] == null && inputs[i] != null) {
                strForStack[i] = CLEAR_INFO_INDICATOR;
            }
        }

        stack.push(strForStack);
    }

    /**
     * This method is used by editATaskForUndo() and editATaskForRedo()
     * @param taskToEdit
     * @param inputs
     * @param stack
     */
    private void updateStackForEditUnderUndoRedo(Task taskToEdit, String[] inputs, 
            Stack<String[]> stack) {
        stack.pop();
        updateStackForEdit(taskToEdit, inputs, stack);
    }

    /**
     * This method is used by updateUndoStackFromTask(), isSearchFound() and updateStackForEdit()
     * @param task
     * @param strArray
     */
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

    /**
     * This method is used by processTM(), undoAnOperation() and redoAnOperation()
     * @param inputs
     * @return
     */
    private int getTaskTID(String[] inputs) {
        int TaskTID = convertToIntType(inputs[TID]);
        return TaskTID;
    }

    /**
     * This method is used by addTask and deleteTask case in processTM()
     * @param task
     * @param commandType
     */
    private void updateUndoStackFromTask(Task task, String commandType) {
        String[] strForUndoStack = new String[DEFAULT_STRING_SIZE];

        strForUndoStack[COMMAND_TYPE] = commandType;
        getStringArrayFromTask(task, strForUndoStack);

        undoStack.push(strForUndoStack);
    }


    private Task getTaskFromTIDString(String[] inputs) {
        int TID = getTaskTID(inputs);
        return getTaskFromTID(TID);
    }

    /**
     * This method is used by processEditCommand(), processDeleteCommand(), 
     * undoAnOperation(), redoAnOperation()
     * @see TaskManagerInterface#getTaskFromTID(int)
     */
    public Task getTaskFromTID(int TID) {
        Task taskFound = null;

        for(Task task : tasks) {
            if(task.getTID() == TID) {
                taskFound = task;
                break;
            }
        }

        return taskFound;
    }

    /**
     * This method is used by processAddWithID(), processAddWithoutID(), editTaskDateFrom(),
     *      editTaskDateTo() and editTaskDeadline();
     * @param dateString  assume dateString is in this format "dd/MM/yyyy HH:mm"
     * @return
     */
    private Date convertToDateObject(String dateString) {
        Date date = null;

        try {
            if(dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
                assert isDateValid(dateString);
                DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                date = format.parse(dateString);
            }
        } catch (ParseException ex) {
            System.out.println(ex);
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

    /**
     * This method is used by processIDWithAdd(), isAbleToEdit(), isAbleToDelete()
     * @param TID
     * @return
     */
    private boolean isIDClashing(String TID) {
        return TaskIDs.contains(convertToIntType(TID));
    }

    /**
     * This method is used by addATask() and editATask()
     * @param task
     * @return
     */
    private boolean isTaskADurationalTask(Task task) {
        return task.getDateFrom() != null && task.getDateTo() != null;
    }

    /**
     * This method is used by addATask() and editATask()
     * @param newTask
     * @param returningTasks
     */
    private void addClashingTasksForReturning(Task newTask, 
            ArrayList<Task> returningTasks) {
        for(Task existingTask : tasks) {
            if(isTaskADurationalTask(existingTask) &&
                    isNewTaskClashedWithExistingTask(newTask, existingTask) && 
                    newTask.getTID() != existingTask.getTID()) {
                returningTasks.add(existingTask.clone());
            }
        }
    }

    /**
     * This method is used by processInitialization(), addATask(), viewTasks()
     * @param tasks
     * @param type
     */
    private void sortTasks(ArrayList<Task> tasks, int type) {
        switch(type) {
        case TID: Collections.sort(tasks, new ComparatorID()); break;
        case TASK_NAME: Collections.sort(tasks, new ComparatorTaskName()); break;
        case DATE_FROM: Collections.sort(tasks, new ComparatorDateFrom()); break;
        case DEADLINE: Collections.sort(tasks, new ComparatorDeadline()); break;
        case LOCATION: Collections.sort(tasks, new ComparatorLocation()); break;
        case PRIORITY: Collections.sort(tasks, new ComparatorPriority()); break;
        }
    }

    private void saveTasksToFile() {
        SystemHandler handler = SystemHandler.getSystemHandler();
        handler.writeToFile(tasks);
    }
    //--------------------Methods used more than once end----------------------


    //--------------------Assertion methods start----------------------
    public boolean isDateValid(String date) {
        boolean isDateValid = DATE_IS_VALID;

        Matcher m = Pattern.compile("\\d+").matcher(date);
        int[] numbers = new int[NUM_ATTRIBUTE_FOR_DATE_OBJECT];
        int i = 0;

        //assume date objects always have five attributes
        while(m.find()) {
            numbers[i] = Integer.parseInt(m.group());
            if(numbers[i] < 0)
                isDateValid = DATE_IS_INVALID;
            ++i;
        }

        if(isDateValid && numbers[HOUR_INDEX] > 23) {
            isDateValid = DATE_IS_INVALID;
        }

        if(isDateValid && numbers[MINUTE_INDEX] > 59) {
            isDateValid = DATE_IS_INVALID;
        }

        if(isDateValid) {
            switch(numbers[MONTH_INDEX]) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                if(numbers[DAY_INDEX] == 0 || numbers[DAY_INDEX] > 31) {
                    isDateValid = DATE_IS_INVALID;
                }
                break;
            case 4: case 6: case 9: case 11:
                if(numbers[DAY_INDEX] == 0 || numbers[DAY_INDEX] > 30) {
                    isDateValid = DATE_IS_INVALID;
                }
                break;
            case 2:
                if(isLeapYear(numbers[YEAR_INDEX])) {
                    if(numbers[DAY_INDEX] == 0 || numbers[DAY_INDEX] > 29) {
                        isDateValid = DATE_IS_INVALID;
                    }
                } else {
                    if(numbers[DAY_INDEX] == 0 || numbers[DAY_INDEX] > 28) {
                        isDateValid = DATE_IS_INVALID;
                    }
                }
                break;
            default:
                isDateValid = DATE_IS_INVALID;
                break;
            }
        }

        return isDateValid;
    }

    private boolean isLeapYear(int year) {
        boolean isLeapYear = true;
        if(year % 100 == 0) {
            if(year % 400 != 0) {
                isLeapYear = false;
            }
        } else {
            if(year % 4 != 0) {
                isLeapYear = false;
            }
        }

        return isLeapYear;
    }

    protected boolean isDateFromSmallerThanDateTo(Date dateFrom, Date dateTo) {
        if(dateFrom.compareTo(dateTo) < 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isDeadlineAfterCurrentTime(Date deadline) {
        Date date = new Date();
        if(deadline.compareTo(date) > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isTaskDateNumberValid(Task task) {
        //durational task
        if(task.getDateFrom() != null && task.getDateTo() != null && 
                task.getDeadline() == null) {
            return true;
        }

        //deadline task
        if(task.getDateFrom() == null && task.getDateTo() == null && 
                task.getDeadline() != null) {
            return true;
        }

        //floating task
        if(task.getDateFrom() == null && task.getDateTo() == null && 
                task.getDeadline() == null) {
            return true;
        }

        return false;
    }

    private void assertTaskDatesAreValid(Task newTask) {
        assert isTaskDateNumberValid(newTask);
        if(isTaskADurationalTask(newTask)) {
            assert newTask.getDateTo() != null;
            assert isDateFromSmallerThanDateTo(newTask.getDateFrom(), 
                    newTask.getDateTo());
        }
    }
    //--------------------Assertion methods ends----------------------
}