import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTimeComparator;

//@author A0118892U
public class TaskManager implements TaskManagerInterface {
    public static final int COMMAND_TYPE = 0;
    public static final int TID = 1;
    public static final int TASK_NAME = 2;
    public static final int DATE_FROM = 3;
    public static final int DATE_TO = 4;
    public static final int DEADLINE = 5;
    public static final int LOCATION = 6;
    public static final int DETAILS = 7;
    public static final int STATUS = 8;
    public static final int DEFAULT_STRING_SIZE = 9;
    public static final int VIEW_TYPE = 2;
    public static final int FILTER_TYPE = 8;

    private static final String MSG_ERR_NO_SUCH_ID = "The Task ID does not exist";
    private static final String MSG_ERR_NO_SUCH_COMMAND = "Flexi Tracker does not recognize this command.";
    private static final String MSG_ERR_LENGTH = "%s allows a maximum length of 30.";
    private static final String MSG_ERR_WRONG_DATE_NUMBER = "The date entered is invalid.";
    private static final String MSG_ERR_WRONG_DATE_DURATION = "The duration entered is invalid. Start Date must be before End Date";
    private static final String MSG_ERR_EMPTY_TASK_NAME = "Task name cannot be empty.";
    private static final String MSG_ERR_UNDO = "There is no operation to undo.";
    private static final String MSG_ERR_REDO = "There is no operation to redo.";
    private static final String MSG_ERR_SEARCH = "Search query cannot be empty.";
    private static final String MSG_ERR_NO_SUCH_STATUS = "Flexi Tracker does not recognize this status.";
    private static final String MSG_ERR_INVALID_CLEAR = "The clear command was invalid. Nothing has been cleared.";
    private static final String MSG_ERR_NO_SUCH_FILTER = "Tasks cannot be filtered in the requested manner.";
    private static final String MSG_ERR_NO_SUCH_ARRANGE = "The tasks cannot be sorted in this order.";
    private static final String MSG_ERR_FAIL_TO_EDIT = "Failed to edit the task";
    
    private static final int URGENT = 1;
    private static final int MAJOR = 2;
    private static final int NORMAL = 3;
    private static final int MINOR = 4;
    private static final int CASUAL = 5;
    private static final int COMPLETE = 6;
    private static final int OVERDUE = 7;

    private static final String URGENT_STRING = "urgent";
    private static final String MAJOR_STRING = "major";
    private static final String NORMAL_STRING = "normal";
    private static final String MINOR_STRING = "minor";
    private static final String CASUAL_STRING = "casual";
    private static final String COMPLETE_STRING = "complete";
    private static final String OVERDUE_STRING = "overdue";

    private static final int INITIAL_TID = 10;
    private static final int NUM_ATTRIBUTE_FOR_DATE_OBJECT = 5;
    private static final int DAY_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    private static final int HOUR_INDEX = 3;
    private static final int MINUTE_INDEX = 4;
    private static final int SEARCH_INDEX = 2;
    private static final String CLEAR_INFO_INDICATOR = "";
    private static final boolean DATE_IS_VALID = true;
    private static final boolean DATE_IS_INVALID = false;
    private static final boolean SEARCH_IS_FOUND = true;
    private static final boolean SEARCH_IS_NOT_FOUND = false;
    private static final int INDEX_AFFECTED_TASK = 0;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String ID_STRING = "id";
    private static final String TASK_NAME_STRING = "title";
    private static final String PRIORITY_STRING = "status";
    private static final String DATE_STRING = "date";
    private static final String DEADLINE_STRING = "deadline";
    private static final String LOCATION_STRING = "location";
    private static final String TASK_TITLE_STRING = "task title";


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

        assertTaskIDIsBiggerThanTen(newTask);
        assertDurationalTaskIsValid(newTask);
        assertTaskDateNumberIsvalid(newTask);
        
        updateTaskIDs(newTask.getTID());
        tasks.add(newTask);
        defaultSortTaskByCompleteAndDate();
    }

    private Task processInitializationWithID(String[] inputs) {
        return processAddWithID(inputs);
    }

    private Task processInitializationWithoutID(String[] inputs) {
        return processAddWithoutID(inputs);
    }
    //--------------------Initialization method ends--------------------



    public ArrayList<Task> processTM(String[] inputs) 
            throws NoSuchElementException, IllegalArgumentException {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        switch(commandObtained) {
        case addTask:
            returningTasks = addATask(inputs);
            updateUndoStackFromTask(returningTasks.get(INDEX_AFFECTED_TASK), inputs[COMMAND_TYPE]);
            saveTasksToFile();
            break;

        case editTask: case clearAttr: case markTask:
            returningTasks = processEditCommand(inputs);
            saveTasksToFile();
            break;

        case viewTask:
            returningTasks = viewTasks(inputs); 
            saveTasksToFile();
            break;

        case deleteTask:
            returningTasks = processDeleteCommand(inputs);
            saveTasksToFile();
            break;

        case searchTask:
            returningTasks = processSearchCommand(inputs);
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
            throw new NoSuchElementException(MSG_ERR_NO_SUCH_COMMAND);
        }

        return returningTasks;
    }

    /**
     * This method converts command to COMMAND_TYPE_TASK_MANAGER. 
     * If the command does not exist, returns an invalidTask command
     * @param command  a String received from FlexiParser
     * @return         a COMMAND_TYPE_TASK_MANAGER type of the String command
     */
    private COMMAND_TYPE_TASK_MANAGER obtainCommand (String command) {
        COMMAND_TYPE_TASK_MANAGER commandObtained;
        try {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.invalidTask;
        }

        return commandObtained;
    }



    //--------------------Add method starts--------------------
    /**
     * This method adds a task from the inputs received from SystemHandler.
     * It also updates taskIDs to prevent clashing ID.
     * @param inputs  a parsed command received from SystemHandler
     * @return        affected tasks in an ArrayList with clashing tasks
     */
    private ArrayList<Task> addATask(String[] inputs) {
        Task newTask;

        changeStatusToIntString(inputs);

        if(isInputsHavingTID(inputs)){
            newTask = processAddWithID(inputs);       
        } else {
            newTask = processAddWithoutID(inputs);
        }

        checkTaskDetails(newTask.clone());

        if(isTaskOverdue(newTask)) {
            newTask.setPriority(OVERDUE);
        }
        assertDurationalTaskIsValid(newTask);
        assertTaskDateNumberIsvalid(newTask);

        
        updateTaskIDs(newTask.getTID());
        tasks.add(newTask);
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        returningTasks.add(newTask.clone());
        addClashingDurationalTask(newTask, returningTasks);
        defaultSortTaskByCompleteAndDate();

        return returningTasks;
    }

    private boolean isInputsHavingTID(String[] inputs) {
        return inputs[TID] != null;
    }

    /**
     * This method deals with adding task with an ID. If there is a ID clash, it will
     * get a new task ID.
     * @param inputs  a parsed command received from SystemHandler with
     *                status changed to an int type
     * @return        the newly added task
     */
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
                convertToIntType(inputs[STATUS]));

        updateIDCounter(inputs[TID]);
        
        return newTask;
    }

    /**
     * This method updates IDCounter. It makes sure IDCounter is always equal to
     * the largest ID for the purpose of getting a new ID.
     * @param currentID
     */
    private void updateIDCounter(String currentID) {
        if(IDCounter < convertToIntType(currentID)){
            IDCounter = convertToIntType(currentID);
        }
    }

    private boolean isIDLessThanTen(String TID) {
        return convertToIntType(TID) < INITIAL_TID;
    }

    /**
     * This method deals with adding task without an ID.
     * @param inputs  a parsed command received from SystemHandler with
     *                status changed to an int type
     * @return        the newly added task
     */
    private Task processAddWithoutID(String[] inputs) {
        Task newTask = new Task(getNewTID(), inputs[TASK_NAME], 
                convertToDateObject(inputs[DATE_FROM]), convertToDateObject(inputs[DATE_TO]), 
                convertToDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                convertToIntType(inputs[STATUS]));

        if(inputs[DATE_FROM] != null) {
            assertDateObjectIsValid(newTask.getDateFrom());
        }
        if(inputs[DATE_TO] != null) {
            assertDateObjectIsValid(newTask.getDateTo());
        }
        if(inputs[DEADLINE] != null) {
            assertDateObjectIsValid(newTask.getDeadline());
        }
        
        return newTask;
    }

    /**
     * This method update TaskIDs HashSet to for checking clashing IDs
     * @param TID  Task ID from a task
     */
    private void updateTaskIDs(int TID) {
        TaskIDs.add(TID);
    }

    /**
     * This method generates a new Task ID for a task
     * @return  a new task ID
     */
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
    //--------------------Add method ends--------------------


    //--------------------Edit method starts-----------------
    /**
     * This method edits a task. It throws error if edit is unable to be performed.
     * @param inputs  a parsed command received from SystemHandler
     * @return        affected tasks in an ArrayList with clashing tasks
     */
    private ArrayList<Task> processEditCommand(String[] inputs) {
        if(!isAbleToEdit(inputs[TID])) {
            throw new NoSuchElementException(MSG_ERR_NO_SUCH_ID);
        }

        checkEditIsValid(inputs);

        processInformationBeforeEdit(inputs);

        Task taskToEdit = getTaskToEdit(inputs);
        Task taskAfterEditing = editATask(taskToEdit.clone(), inputs).get(INDEX_AFFECTED_TASK);
        checkTaskDetails(taskAfterEditing);

        //update undo stack before performing edit, so it is possible to recover it
        updateStackForEdit(taskToEdit, inputs, undoStack);
        return editATask(taskToEdit, inputs);
    }

    /**
     * If there is a ID clash, it means TaskID is found
     * @param TaskID
     * @return        true if able to edit; else false
     */
    private boolean isAbleToEdit(String TaskID) {
        return isIDClashing(TaskID);
    }

    /**
     * This methods checks the there is at least one field of the data to be edited
     * @param inputs  parsed command received from SystemHandler
     */
    private void checkEditIsValid(String[] inputs) {
        boolean isValid = false;
        for(int i = TASK_NAME; i < inputs.length; ++i) {
            if(inputs[i] != null) {
                isValid = true;
            }
        }

        if(!isValid) {
            throw new IllegalArgumentException(MSG_ERR_FAIL_TO_EDIT);
        }
    }

    /**
     * This method does the necessary changes before process editing a task
     * @param inputs  a parsed command received from SystemHandler
     */
    private void processInformationBeforeEdit(String[] inputs) {
        inputs[COMMAND_TYPE] = changeCommandToEditTask();
        if(inputs[STATUS] != null) {
            changeStatusToIntString(inputs);
        }
    }

    /**
     * This method changes clearAttr and markTask command to editTask
     * @return  String type of editTask
     */
    private String changeCommandToEditTask() {
        return COMMAND_TYPE_TASK_MANAGER.editTask.toString();
    }

    private Task getTaskToEdit(String[] inputs) {
        return getTaskFromTIDString(inputs);
    }

    /**
     * This method edit a task, clear attributes of a task or mark status of a task.
     * @param taskToEdit  the task to be edited
     * @param inputs      a String array indicates the changes. null means there is 
     *                    no change to this attribute, "" means to clear that attribute,
     *                    else means to update it with the new information. 
     * @return            affected task with clashing duration task
     */
    private ArrayList<Task> editATask(Task taskToEdit, String[] inputs) {
        for(int i = TASK_NAME; i < inputs.length; ++i) {
            if(isTaskInfoChanged(inputs, i)) {
                editTaskInfo(inputs, taskToEdit, i);
            }

            if(isTaskInfoToClear(inputs, i)) {
                clearTaskInfo(taskToEdit, i);
            }
        }

        //sets previously overdue tasks back to normal priority
        if(isTaskStatusOverdue(taskToEdit) && !isTaskOverdue(taskToEdit)) {
            taskToEdit.setPriority(NORMAL);
        }
        
        if(inputs[DATE_FROM] != null) {
            assertDateObjectIsValid(taskToEdit.getDateFrom());
        }
        if(inputs[DATE_TO] != null) {
            assertDateObjectIsValid(taskToEdit.getDateTo());
        }
        if(inputs[DEADLINE] != null) {
            assertDateObjectIsValid(taskToEdit.getDeadline());
        }

        ArrayList<Task> returningTasks = new ArrayList<Task>();
        returningTasks.add(taskToEdit.clone());
        addClashingDurationalTask(taskToEdit, returningTasks);

        return returningTasks;
    }

    private boolean isTaskInfoChanged(String[] inputs, int i) {
        return inputs[i] != null;
    }

    /**
     * This method edits the task information
     * @param inputs  parsed String array
     * @param task    the task to be edited
     * @param i       index in the String array to be changed in the task to be edited
     */
    private void editTaskInfo(String[] inputs, Task task, int i) {
        switch(i) {
        case TASK_NAME: editTaskName(inputs, task); break;
        case DATE_FROM: editTaskDateFrom(inputs, task); break;
        case DATE_TO: editTaskDateTo(inputs, task); break;
        case DEADLINE: editTaskDeadline(inputs, task); break;
        case LOCATION: editTaskLocation(inputs, task); break;
        case DETAILS: editTaskDetails(inputs, task); break;
        case STATUS: editTaskPriority(inputs, task); break;
        }
    }

    private void editTaskPriority(String[] inputs, Task task) {
        int newPriority = convertToIntType(inputs[STATUS]);
        task.setPriority(newPriority);
    }

    private void editTaskDetails(String[] inputs, Task task) {
        task.setDetails(inputs[DETAILS]);
    }

    private void editTaskLocation(String[] inputs, Task task) {
        task.setLocation(inputs[LOCATION]);

    }

    /**
     * Due to UI combines dateTo and deadline, more checks are needed to ensure 
     * correct data information is edited.
     * @param inputs
     * @param task
     */
    private void editTaskDeadline(String[] inputs, Task task) {
        if(task.getDeadline() != null) {
            Date newDeadline = convertToDateObject(inputs[DEADLINE]);
            task.setDeadline(newDeadline);
        }
        if(task.getDeadline() == null && task.getDateTo() != null) {
            Date newDateTo = convertToDateObject(inputs[DEADLINE]);
            task.setDeadline(newDateTo);
        }
        if(task.getDeadline() == null && task.getDateTo() == null) {
            Date newDeadline = convertToDateObject(inputs[DEADLINE]);
            task.setDeadline(newDeadline);
        }
    }

    /**
     * Due to UI combines dateTo and deadline, more checks are needed to ensure 
     * correct data information is edited.
     * @param inputs
     * @param task
     */
    private void editTaskDateTo(String[] inputs, Task task) {
        if(task.getDateTo() != null) {
            Date newDateTo = convertToDateObject(inputs[DATE_TO]);
            task.setDateTo(newDateTo);
        }
        if(task.getDateTo() == null && task.getDeadline() != null) {
            Date newDeadline = convertToDateObject(inputs[DATE_TO]);
            task.setDeadline(newDeadline);
        }
        if(task.getDateTo() == null && task.getDeadline() == null) {
            Date newDateTo = convertToDateObject(inputs[DATE_TO]);
            task.setDateTo(newDateTo);
        }
    }

    private void editTaskDateFrom(String[] inputs, Task task) {
        Date newDateFrom = convertToDateObject(inputs[DATE_FROM]);
        task.setDateFrom(newDateFrom);
    }

    private void editTaskName(String[] inputs, Task task) {
        if(inputs[TASK_NAME].trim() == "") {
            throw new IllegalArgumentException(MSG_ERR_EMPTY_TASK_NAME);
        }
        task.setTaskName(inputs[TASK_NAME]);
    }

    private boolean isTaskInfoToClear(String[] inputs, int i) {
        return inputs[i] != null && inputs[i].equals(CLEAR_INFO_INDICATOR);
    }

    /**
     * This method clears the task information
     * @param task  task to clear its information
     * @param i     the index tells which information to clear
     */
    private void clearTaskInfo(Task task, int i) {
        switch(i) {
        case DATE_FROM: clearTaskDateFrom(task); break;
        case DATE_TO: clearTaskDateTo(task); break;
        case DEADLINE: clearTaskDeadline(task); break;
        case LOCATION: clearTaskLocation(task); break;
        case DETAILS: clearTaskDetails(task); break;
        case STATUS: clearTaskPriority(task); break;
        default: throw new NoSuchElementException(MSG_ERR_INVALID_CLEAR);
        }
    }

    private void clearTaskDateFrom(Task task) {
        task.setDateFrom(null);
    }

    /**
     * Due to UI combines dateTo and deadline, more checks are needed to ensure 
     * correct information is cleared.
     * @param task
     */
    private void clearTaskDateTo(Task task) {
        if(task.getDateTo() != null) {
            task.setDateTo(null);
        }
        if(task.getDateTo() == null && task.getDeadline() != null) {
            task.setDeadline(null);
        }
        if(task.getDateTo() == null && task.getDeadline() == null) {
            task.setDateTo(null);
        }
    }

    /**
     * Due to UI combines dateTo and deadline, more checks are needed to ensure 
     * correct information is cleared.
     * @param task
     */
    private void clearTaskDeadline(Task task) {
        if(task.getDeadline() != null) {
            task.setDeadline(null);
        }
        if(task.getDeadline() == null && task.getDateTo() != null) {
            task.setDateTo(null);
        }
        if(task.getDeadline() == null && task.getDateTo() == null) {
            task.setDeadline(null);
        }
    }

    private void clearTaskLocation(Task task) {
        task.setLocation(null);
    }

    private void clearTaskDetails(Task task) {
        task.setDetails(null);
    }

    private void clearTaskPriority(Task task) {
        task.setPriority(NORMAL);
    }

    private boolean isTaskStatusOverdue(Task task) {
        return task.getPriority() == OVERDUE;
    }
    //----------Edit method ends----------



    //----------View method starts----------
    /**
     * This method returns the task in the filter and sort option specified
     * @param inputs  parsed command indicates filter and sort option
     * @return        filtered sorted tasks
     */
    private ArrayList<Task> viewTasks(String[] inputs) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        changeIncompleteTaskStatusToOverdue();
        performFilterOnTasks(inputs, returningTasks);

        return performViewOptionOnTasks(inputs, returningTasks);
    }

    /**
     * This method compares incomplete tasks with current time and change their status
     */
    private void changeIncompleteTaskStatusToOverdue() {
        for(Task task: tasks) {
            if(isTaskOverdue(task) && !isTaskComplete(task)) {
                task.setPriority(OVERDUE);
            }
        }
    }

    /**
     * This method performs filter on the tasks to be displayed, the default filter
     * is everything.
     * @param inputs          parsed command with index 8 being the filter
     * @param returningTasks  selected tasks based on the filter type
     */
    public void performFilterOnTasks(String[] inputs,
            ArrayList<Task> returningTasks) {
        if(isFilterOptionDefault(inputs)) {
            for(Task task: tasks) {
                if(!isTaskComplete(task.clone())) {
                    returningTasks.add(task.clone());
                }
            }
        } else {
            int filterType = getFileterOption(inputs);
            for(Task task: tasks) {
                if(task.getPriority() == filterType)
                    returningTasks.add(task.clone());
            }
        }
    }

    private int getFileterOption(String[] inputs) {
        int filterType = getFilterTypeInt(inputs[FILTER_TYPE]);
        return filterType;
    }

    /**
     * This methods get the int type of the filter type from its String type
     * @param filterType  String type of filter type
     * @return            int type of filter type
     */
    private int getFilterTypeInt(String filterType) {
        filterType = filterType.toLowerCase();
        switch(filterType) {
        case URGENT_STRING: return URGENT;
        case MAJOR_STRING: return MAJOR;
        case NORMAL_STRING: return NORMAL;
        case MINOR_STRING: return MINOR;
        case CASUAL_STRING: return CASUAL;
        case COMPLETE_STRING: return COMPLETE;
        case OVERDUE_STRING: return OVERDUE;
        default: throw new NoSuchElementException(MSG_ERR_NO_SUCH_FILTER);
        }
    }

    private boolean isFilterOptionDefault(String[] inputs) {
        return inputs[FILTER_TYPE] == null;
    }

    /**
     * This methods perform sorting on the tasks to be displayed. Default sorting
     * is by date
     * @param inputs
     * @param returningTasks
     * @return
     */
    public ArrayList<Task> performViewOptionOnTasks(String[] inputs,
            ArrayList<Task> returningTasks) {
        if(isViewOptionDefault(inputs)) {
            sortTasks(returningTasks, DATE_FROM);
            return returningTasks;
        } else {
            int viewType = getViewOption(inputs);
            sortTasks(returningTasks, viewType);
            return returningTasks;
        }
    }

    private boolean isViewOptionDefault(String[] inputs) {
        return inputs[VIEW_TYPE] == null;
    }

    private int getViewOption(String[] inputs) {
        int viewType = getViewTypeInt(inputs[VIEW_TYPE]);
        return viewType;
    }

    /**
     * This methods get the int type of the view option from its String type
     * @param viewType  String type of view option
     * @return          int type of view option
     */
    private int getViewTypeInt(String viewType) {
        viewType = viewType.toLowerCase();
        switch (viewType) {
        case ID_STRING: return TID;
        case TASK_NAME_STRING: return TASK_NAME;
        case DATE_STRING: return DATE_FROM;
        case DEADLINE_STRING: return DEADLINE;
        case LOCATION_STRING: return LOCATION;
        case PRIORITY_STRING: return STATUS;
        default: throw new NoSuchElementException(MSG_ERR_NO_SUCH_ARRANGE);
        }
    }
    //--------------------View method ends--------------------



    //--------------------Delete method starts----------------
    /**
     * This method deletes the task
     * @param inputs  parsed command with index 1 being the ID to be deleted
     * @return
     */
    private ArrayList<Task> processDeleteCommand(String[] inputs) {
        if(!isAbleToDelete(inputs[TID])) {
            throw new NoSuchElementException(MSG_ERR_NO_SUCH_ID);
        }

        int TIDToDelete = getTaskTID(inputs);
        Task taskToDelete = getTaskToDelete(inputs);
        updateUndoStackFromTask(taskToDelete, inputs[COMMAND_TYPE]);

        return deleteATask(TIDToDelete);
    }

    private boolean isAbleToDelete(String TaskID) {
        return isIDClashing(TaskID);
    }

    private Task getTaskToDelete(String[] inputs) {
        return getTaskFromTIDString(inputs);
    }

    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task nextTask = (Task) iterator.next();
            if(TID == nextTask.getTID()) {
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
    /**
     * This method searches tasks from the user input
     * @param inputs  parsed command with index 2 being the thing to search
     * @return
     */
    private ArrayList<Task> processSearchCommand(String[] inputs) {
        if(isStringEmpty(inputs[SEARCH_INDEX])) {
            throw new IllegalArgumentException(MSG_ERR_SEARCH);
        }

        if(isSearchADateObject(inputs[SEARCH_INDEX])) {
            Date searchDate = convertToDateWithoutPrintException(inputs[SEARCH_INDEX]);
            return searchTaskDateObject(searchDate);
        } else {
            return searchTaskNonDateObject(inputs[SEARCH_INDEX]);
        }
    }

    private boolean isSearchADateObject(String search) {
        if(convertToDateWithoutPrintException(search) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method is only for isSearchADateObject(String)
     * @param str  item to search
     * @return     date object if the item to search is a date, else null
     */
    private Date convertToDateWithoutPrintException(String str) {
        Date date = null;

        try {
            if(str != null && !str.equals(CLEAR_INFO_INDICATOR)) {
                DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                date = format.parse(str);
            }
        } catch (ParseException ex) {
            assert(true);
        }

        return date;
    }

    /**
     * This method adds tasks with date to search in its duration. For tasks with only
     * one date, it adds tasks with date is same as searchDate
     * @param searchDate  date to search
     * @return            tasks matches the search criteria
     */
    private ArrayList<Task> searchTaskDateObject(Date searchDate) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        for(Task task: tasks) {
            if(isDurationalTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateFrom()) >= 0 && 
                        compareTwoDateOnly(searchDate, task.getDateTo()) <= 0) {
                    returningTasks.add(task.clone());
                }
            } else if(isDeadlineTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDeadline()) == 0) {
                    returningTasks.add(task.clone());
                }
            } else if(isForeverTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateFrom()) == 0) {
                    returningTasks.add(task.clone());
                }
            } else if(isOnlyDateToTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateTo()) == 0) {
                    returningTasks.add(task.clone());
                }
            }
        }

        return returningTasks;
    }

    /**
     * This method compares only the date part of two date object
     * @param searchDate  date to search
     * @param dateInTask  date in the task
     * @return            -1 if searchDate < dateInTask; 0 if equals; 
     *                    and 1 if searchDate > dateInTask
     */
    private int compareTwoDateOnly(Date searchDate, Date dateInTask) {
        return DateTimeComparator.getDateOnlyInstance().compare(searchDate,
                dateInTask);
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

    /**
     * This method searches in all tasks
     * @param search  thing to search
     * @return        tasks matches with the search
     */
    private ArrayList<Task> searchTaskNonDateObject(String search) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        for(Task task: tasks) {
            if(isSearchFound(task, search)) {
                returningTasks.add(task.clone());
            }
        }

        return returningTasks;
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
    /**
     * This operation undos an operation. (addTask, deleteTask, editTask, markTask and clearAttr)
     * @return  affected tasks
     */
    private ArrayList<Task> undoAnOperation() {
        if(isStackEmpty(undoStack)) {
            throw new NoSuchElementException(MSG_ERR_UNDO);
        }

        ArrayList<Task> returningTasks = new ArrayList<Task>();

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
                Task taskToEdit = getNonCloneTaskFromTID(TIDToEdit);
                returningTasks = editATaskForUndo(taskToEdit, undoOperation);
                break;
            default:
                break;
            }
            updateRedoStack();
        }

        return returningTasks;
    }

    /**
     * This method updates the undo stack with the task information before proceeding
     * for the undo action. So that further redo is possible to recover it.
     * @param taskToEdit  task to be edited
     * @param inputs      undo operation from undo stack
     * @return            affected tasks
     */
    private ArrayList<Task> editATaskForUndo(Task taskToEdit, String[] inputs) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        updateStackForEditUnderUndoRedo(taskToEdit, inputs, undoStack);
        returningTasks = editATask(taskToEdit, inputs);

        return returningTasks;
    }

    private void updateRedoStack() {
        redoStack.push(undoStack.pop());
    }
    //--------------------Undo method ends----------------------



    //--------------------Redo method starts--------------------
    /**
     * This operation redos an undo operation.
     * @return  affected tasks
     */
    private ArrayList<Task> redoAnOperation() {
        if(isStackEmpty(redoStack)) {
            throw new NoSuchElementException(MSG_ERR_REDO);
        }

        ArrayList<Task> returningTasks = new ArrayList<Task>();

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
                Task taskToEdit = getNonCloneTaskFromTID(TIDToEdit);
                returningTasks = editATaskForRedo(taskToEdit, redoOperation);
                break;
            default:
                break;
            }
            updateUndoStackFromRedoOperation();
        }

        return returningTasks;
    }

    /**
     * This method updates the redo stack with the task information before proceeding
     * for the redo action. So that further undo is possible to recover it.
     * @param taskToEdit  task to be edited
     * @param inputs      redo operation from redo stack
     * @return            affected tasks
     */
    private ArrayList<Task> editATaskForRedo(Task taskToEdit, String[] inputs) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

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
     * This method updates undo or redo stack before performing edit operation
     * This method is used by updateStackForEditUnderUndoRedo() and processEditCommand()
     * @param taskToEdit  task to be edited
     * @param inputs      inputs String array from the stack
     * @param stack       stack to be updated
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
     * This methods removes the edit entry from the stack before performing edit operation 
     * This method is used by editATaskForUndo() and editATaskForRedo()
     * @param taskToEdit  task to be edited
     * @param inputs      inputs array string from the stack
     * @param stack       the stack to update
     */
    private void updateStackForEditUnderUndoRedo(Task taskToEdit, String[] inputs, 
            Stack<String[]> stack) {
        stack.pop();
        updateStackForEdit(taskToEdit, inputs, stack);
    }

    /**
     * This method converts task to a string array
     * This method is used by updateUndoStackFromTask(), isSearchFound() and updateStackForEdit()
     * @param task      task to be converted
     * @param strArray  String array generated from task
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

        strArray[STATUS] = convertToStringFromInt(task.getPriority());
    }

    /**
     * This method converts date object to a default date format string ("dd/MM/yyyy HH:mm")
     * @param dateObject  date object to be converted
     * @return            String type of that date object
     */
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
     * This method gets task ID from the String array
     * This method is used by getTaskFromTIDString(), processDeleteCommand(), 
     * undoAnOperation() and redoAnOperation()
     * @param inputs
     * @return
     */
    private int getTaskTID(String[] inputs) {
        int TaskTID = convertToIntType(inputs[TID]);
        return TaskTID;
    }

    /**
     * This method updates undo stack from task with command type
     * This method is used by addTask case in processTM() and processDeleteCommand()
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
        return getNonCloneTaskFromTID(TID);
    }

    private Task getNonCloneTaskFromTID(int TID) {
        Task taskFound = null;

        for(Task task : tasks) {
            if(task.getTID() == TID) {
                taskFound = task;
                break;
            }
        }

        return taskFound;
    }
    
    public Task getTaskFromTID(int TID) {
        Task taskFound = null;

        for(Task task : tasks) {
            if(task.getTID() == TID) {
                taskFound = task.clone();
                break;
            }
        }

        return taskFound;
    }

    /**
     * This method converts a dateString in default format ("dd/MM/yyyy HH:mm") to a date object
     * @param dateString  assume dateString is in this format "dd/MM/yyyy HH:mm"
     * @return            date object converted
     */
    private Date convertToDateObject(String dateString) {
        Date date = null;

        try {
            if(dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
                DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                date = format.parse(dateString);
            }
        } catch (ParseException ex) {
            assert(true);            
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
     * This method checks whether ID is clashing or not.
     * @param TID  task ID in string type
     * @return     true if task ID is found, else false
     */
    private boolean isIDClashing(String TID) {
        return TaskIDs.contains(convertToIntType(TID));
    }

    private void addClashingDurationalTask(Task newTask,
            ArrayList<Task> returningTasks) {
        if(isDurationalTask(newTask)) {
            addClashingTasksForReturning(newTask, returningTasks);
        }
    }
    
    private void addClashingTasksForReturning(Task newTask, 
            ArrayList<Task> returningTasks) {
        for(Task existingTask : tasks) {
            if(isDurationalTask(existingTask) &&
                    !isTaskComplete(existingTask) &&
                    isNewTaskClashedWithExistingTask(newTask, existingTask) && 
                    newTask.getTID() != existingTask.getTID()) {
                returningTasks.add(existingTask.clone());
            }
        }
    }

    private boolean isNewTaskClashedWithExistingTask(Task newTask, Task existing) {
        if(existing.getDateTo().compareTo(newTask.getDateFrom()) <= 0 || 
                existing.getDateFrom().compareTo(newTask.getDateTo()) >= 0) {
            return false;
        }

        return true;
    }


    private boolean isTaskComplete(Task task) {
        return task.getPriority() == COMPLETE;
    }

    /**
     * This method sorts tasks based on the type
     * @param tasks  tasks ArrayList to be sorted
     * @param type   how to sort the task
     */
    private void sortTasks(ArrayList<Task> tasks, int type) {
        switch(type) {
        case TID: Collections.sort(tasks, new ComparatorID()); break;
        case TASK_NAME: Collections.sort(tasks, new ComparatorTaskName()); break;
        case DATE_FROM: Collections.sort(tasks, new ComparatorDate()); break;
        case LOCATION: Collections.sort(tasks, new ComparatorLocation()); break;
        case STATUS: Collections.sort(tasks, new ComparatorPriority()); break;
        }
    }
    
    /**
     * This method calls SystemHandler to save tasks to the file
     */
    private void saveTasksToFile() {
        SystemHandler handler = SystemHandler.getSystemHandler();
        handler.writeToFile(tasks);
    }
    
    /**
     * This method checks whether the task is a valid task
     * @param task  task to be checked
     */
    private void checkTaskDetails(Task task) {
        if(!isStringLengthLessThanThirty(task.getTaskName())) {
            throw new IllegalArgumentException(String.format(MSG_ERR_LENGTH, 
                    TASK_TITLE_STRING));
        }

        if(!isStringLengthLessThanThirty(task.getLocation())) {
            throw new IllegalArgumentException(String.format(MSG_ERR_LENGTH, 
                    LOCATION_STRING));
        }

        if(!isTaskDateNumberValid(task)) {
            throw new IllegalArgumentException(MSG_ERR_WRONG_DATE_NUMBER);
        }

        if(!isDateFromBeforeDateTo(task)) {
            throw new IllegalArgumentException(MSG_ERR_WRONG_DATE_DURATION);
        }

        if(isStringEmpty(task.getTaskName())) {
            throw new IllegalArgumentException(MSG_ERR_EMPTY_TASK_NAME);
        }
    }

    private boolean isStringEmpty(String str) {
        if(str == null) {
            return true;
        }
        str = str.trim();
        return str.isEmpty();
    }

    private boolean isStringLengthLessThanThirty(String str) {
        if(str != null) {
            return str.length() <= 30;
        } else {
            return true;
        }
    }

    /**
     * This method sorts the incomplete tasks in front of complete tasks,
     * both categories of tasks will be sorted by date.
     */
    private void defaultSortTaskByCompleteAndDate() {
        Collections.sort(tasks, new ComparatorDateAndStatus());
    }

    private boolean isTaskOverdue(Task task) {
        boolean isOverdue = false;
        Date current = new Date();

        if(isOnlyDateToTask(task) || isDurationalTask(task)) {
            if(task.getDateTo().compareTo(current) == -1) {
                isOverdue = true;
            }
        }

        if(isDeadlineTask(task)) {
            if(task.getDeadline().compareTo(current) == -1) {
                isOverdue = true;
            } 
        }

        return isOverdue;
    }
    
    /**
     * This method changes the status to be represented by number
     * @param inputs  parsed command containing status to be changed
     */
    private void changeStatusToIntString(String[] inputs) {
        if(inputs[STATUS] == null) {
            inputs[STATUS] = convertToStringFromInt(NORMAL);
        } else {
            String temp = inputs[STATUS].toLowerCase();
            switch(temp) {
            case URGENT_STRING: inputs[STATUS] = convertToStringFromInt(URGENT); break;
            case MAJOR_STRING: inputs[STATUS] = convertToStringFromInt(MAJOR); break;
            case NORMAL_STRING: inputs[STATUS] = convertToStringFromInt(NORMAL); break;
            case MINOR_STRING: inputs[STATUS] = convertToStringFromInt(MINOR); break;
            case CASUAL_STRING: inputs[STATUS] = convertToStringFromInt(CASUAL); break;
            case COMPLETE_STRING: inputs[STATUS] = convertToStringFromInt(COMPLETE); break;
            case OVERDUE_STRING: inputs[STATUS] = convertToStringFromInt(OVERDUE); break;
            default: throw new NoSuchElementException(MSG_ERR_NO_SUCH_STATUS);
            }
        }
    }

    private boolean isStackEmpty(Stack<String[]> stack) {
        return stack.isEmpty();
    }
    //--------------------Methods used more than once end----------------------

    

    //--------------------Assertion methods start----------------------
    private void assertTaskIDIsBiggerThanTen(Task task) {
        assert task.getTID() >= INITIAL_TID;
    }
    
    private void assertDateObjectIsValid(Date dateObject) {
        String dateString = convertToStringFromDate(dateObject);
        assert isDateValid(dateString);
    }
    
    protected boolean isDateValid(String date) {
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

    private void assertDurationalTaskIsValid(Task task) {
        assert isDateFromBeforeDateTo(task);
    }
    
    protected boolean isDateFromBeforeDateTo(Task task) {
        Date dateFrom = task.getDateFrom();
        Date dateTo = task.getDateTo();

        if(dateFrom == null || dateTo == null) {
            return true;
        }

        if(dateFrom.compareTo(dateTo) < 0) {
            return true;
        } else {
            return false;
        }
    }

    private void assertTaskDateNumberIsvalid(Task task) {
        assert isTaskDateNumberValid(task);
    }
    
    protected boolean isTaskDateNumberValid(Task task) {
        //durational task
        if(task.getDateFrom() != null && task.getDateTo() != null && 
                task.getDeadline() == null) {
            return true;
        }

        //forever task
        if(task.getDateFrom() != null && task.getDateTo() == null && 
                task.getDeadline() == null) {
            return true;
        }

        //deadline task
        if(task.getDateFrom() == null && task.getDateTo() == null && 
                task.getDeadline() != null) {
            return true;
        }

        //another form of deadline task
        if(task.getDateFrom() == null && task.getDateTo() != null && 
                task.getDeadline() == null) {
            return true;
        }

        //floating task
        if(task.getDateFrom() == null && task.getDateTo() == null && 
                task.getDeadline() == null) {
            return true;
        }

        return false;
    }
    //--------------------Assertion methods ends----------------------
}