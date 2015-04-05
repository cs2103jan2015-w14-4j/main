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


    private static final String MSG_ERR_NO_SUCH_ID = "ID does not exist";
    private static final String MSG_ERR_NO_SUCH_COMMAND = "System does not recognize this command";
    private static final String MSG_ERR_LENGTH = "%s has maximum length of 30";
    private static final String MSG_ERR_WRONG_DATE_NUMBER = "Wrong dates entered";
    private static final String MSG_ERR_WRONG_DATE_DURATION = "Start must be before end";
    private static final String MSG_ERR_EMPTY_TASK_NAME = "Task name cannot be empty";
    private static final String MSG_ERR_UNDO = "No operation to undo";
    private static final String MSG_ERR_REDO = "No operation to redo";
    private static final String MSG_ERR_SEARCH = "Search cannot be empty";
    private static final String MSG_ERR_NO_SUCH_STATUS = "System does not recognize this status";
    private static final String MSG_ERR_INVALID_CLEAR = "System cannot clear this";
    private static final String MSG_ERR_NO_SUCH_FILTER = "System does not recognize this filter option";
    private static final String MSG_ERR_NO_SUCH_ARRANGE = "System cannot arrange like this";


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
    private static final boolean IS_CLASH = true;
    private static final boolean IS_NOT_CLASH = false;
    private static final boolean DATE_IS_VALID = true;
    private static final boolean DATE_IS_INVALID = false;
    private static final boolean SEARCH_IS_FOUND = true;
    private static final boolean SEARCH_IS_NOT_FOUND = false;
    private static final int INDEX_ZERO = 0;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String ID_STRING = "id";
    private static final String TASK_NAME_STRING = "name";
    private static final String PRIORITY_STRING = "priority";
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

        checkTaskDetails(newTask.clone());


        addIDToTaskIDs(newTask.getTID());

        tasks.add(newTask);
        defaultSortTaskByCompleteAndDate();
    }

    //completed task will be put at the back
    //both categories of tasks will be sorted by date
    private void defaultSortTaskByCompleteAndDate() {
        Collections.sort(tasks, new ComparatorDateAndStatus());
    }

    private Task processInitializationWithID(String[] inputs) {
        return processAddWithID(inputs);
    }

    private void checkTaskDetails(Task task) {
        if(!isStringLengthLessThanThirty(task.getTaskName())) {
            throw new StringIndexOutOfBoundsException(String.format(MSG_ERR_LENGTH, 
                    TASK_TITLE_STRING));
        }

        if(!isStringLengthLessThanThirty(task.getLocation())) {
            throw new StringIndexOutOfBoundsException(String.format(MSG_ERR_LENGTH, 
                    LOCATION_STRING));
        }

        if(!isTaskDateNumberValid(task)) {
            throw new IllegalStateException(MSG_ERR_WRONG_DATE_NUMBER);
        }

        if(!isDateFromBeforeDateTo(task)) {
            throw new IllegalStateException(MSG_ERR_WRONG_DATE_DURATION);
        }

        if(isStringEmpty(task.getTaskName())) {
            throw new IllegalStateException(MSG_ERR_EMPTY_TASK_NAME);
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

    private Task processInitializationWithoutID(String[] inputs) {
        return processAddWithoutID(inputs);
    }
    //--------------------Initialization method ends--------------------



    public ArrayList<Task> processTM(String[] inputs) {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = null;

        switch(commandObtained) {

        case addTask:
            changeStatusToIntString(inputs);
            returningTasks = addATask(inputs);
            if(returningTasks != null) {
                updateUndoStackFromTask(returningTasks.get(INDEX_ZERO), inputs[COMMAND_TYPE]);
                saveTasksToFile();
            }
            break;

        case editTask: case clearAttr: case markTask:
            if(isAbleToEdit(inputs[TID])) {
                checkEditIsValid(inputs);
                inputs[COMMAND_TYPE] = changeCommandToEditTask();
                if(inputs[STATUS] != null) {
                    changeStatusToIntString(inputs);
                } 
                returningTasks = processEditCommand(inputs);
                saveTasksToFile();
            } else {
                throw new NoSuchElementException(MSG_ERR_NO_SUCH_ID);
            }
            break;

        case viewTask:
            returningTasks = viewTasks(inputs); 
            break;

        case deleteTask:
            if(isAbleToDelete(inputs[TID])) {
                returningTasks = processDeleteCommand(inputs);
                saveTasksToFile();
            } else {
                throw new NoSuchElementException(MSG_ERR_NO_SUCH_ID);
            }
            break;

        case searchTask:
            returningTasks = processSearchCommand(inputs);
            break;

        case undoTask:
            if(isStackEmpty(undoStack)) {
                throw new NoSuchElementException(MSG_ERR_UNDO);
            } else {
                returningTasks = undoAnOperation();
                saveTasksToFile();
            }
            break;

        case redoTask:
            if(isStackEmpty(redoStack)) {
                throw new NoSuchElementException(MSG_ERR_REDO);
            } else {
                returningTasks = redoAnOperation();
                saveTasksToFile();
            }
            break;

        case invalidTask:
            throw new NoSuchElementException(MSG_ERR_NO_SUCH_COMMAND);
        }

        return returningTasks;
    }

    private void checkEditIsValid(String[] inputs) {
        boolean isValid = false;
        for(int i = TASK_NAME; i < inputs.length; ++i) {
            if(inputs[i] != null) {
                isValid = true;
            }
        }

        if(!isValid) {
            throw new IllegalArgumentException("Action cannot be carried");
        }
    }

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

        checkTaskDetails(newTask.clone());

        assert newTask.getTID() >= INITIAL_TID;
        addIDToTaskIDs(newTask.getTID());

        tasks.add(newTask);
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        returningTasks.add(newTask.clone());

        //add clashed durational tasks to the return
        if(isTaskADurationalTask(newTask)) {
            addClashingTasksForReturning(newTask, returningTasks);
        }

        defaultSortTaskByCompleteAndDate();

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
                convertToIntType(inputs[STATUS]));

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
                convertToIntType(inputs[STATUS]));

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

    private String changeCommandToEditTask() {
        return COMMAND_TYPE_TASK_MANAGER.editTask.toString();
    }

    private ArrayList<Task> processEditCommand(String[] inputs) {
        Task taskToEdit = getTaskToEdit(inputs);
        //check at here to see whether need to throw exceptions or not
        Task taskAfterEditing = editATask(taskToEdit.clone(), inputs).get(0);
        //this lousy code need to change later!!!

        checkTaskDetails(taskAfterEditing);
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
    //----------Edit method ends----------



    //----------View method starts----------
    private ArrayList<Task> viewTasks(String[] inputs) {
        if(tasks.isEmpty()){
            return null;
        } else {
            ArrayList<Task> returningTasks = new ArrayList<Task>();

            if(isFilterOptionDefault(inputs)) {
                for(Task task: tasks) {
                    if(task.getStatus() != COMPLETE) {
                        returningTasks.add(task.clone());
                    }
                }
            } else {
                int filterType = getFileterOption(inputs);
                for(Task task: tasks) {
                    if(task.getStatus() == filterType)
                        returningTasks.add(task.clone());
                }
            }

            if(isViewOptionDefault(inputs)) {
                sortTasks(returningTasks, DATE_FROM);
                return returningTasks;
            } else {
                int viewType = getViewOption(inputs);
                sortTasks(returningTasks, viewType);
                return returningTasks;
            }
        }
    }

    private int getFileterOption(String[] inputs) {
        int filterType = getFilterTypeInt(inputs[FILTER_TYPE]);
        return filterType;
    }

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
    private ArrayList<Task> processSearchCommand(String[] inputs) {
        if(isStringEmpty(inputs[SEARCH_INDEX])) {
            throw new IllegalStateException(MSG_ERR_SEARCH);
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
            return DATE_IS_INVALID;
        } else {
            return DATE_IS_VALID;
        }
    }

    //this method is only for isSearchADateObject(String)
    private Date convertToDateWithoutPrintException(String str) {
        Date date = null;

        try {
            if(str != null && !str.equals(CLEAR_INFO_INDICATOR)) {
                DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                date = format.parse(str);
            }
        } catch (ParseException ex) {
        }

        return date;
    }

    private ArrayList<Task> searchTaskDateObject(Date searchDate) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        for(Task task: tasks) {
            if(isDurationalTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateFrom()) >= 0 && 
                        compareTwoDateOnly(searchDate, task.getDateTo()) <= 0) {
                    returningTasks.add(task);
                }
            } else if(isDeadlineTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDeadline()) == 0) {
                    returningTasks.add(task);
                }
            } else if(isForeverTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateFrom()) == 0) {
                    returningTasks.add(task);
                }
            } else if(isOnlyDateToTask(task)) {
                if(compareTwoDateOnly(searchDate, task.getDateTo()) == 0) {
                    returningTasks.add(task);
                }
            }
        }

        if(returningTasks.isEmpty()) {
            return null;
        } else {
            return returningTasks;
        }
    }

    private int compareTwoDateOnly(Date searchDate, Date dateInTask) {
        return DateTimeComparator.getDateOnlyInstance().compare(searchDate,
                dateInTask);
    }

    private boolean isDurationalTask(Task task) {
        return task.getDateFrom() != null && task.getDateTo() != null &&
                task.getDeadline() == null;
    }

    private boolean isFloatingTask(Task task) {
        return task.getDateFrom() == null && task.getDateTo() == null &&
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

    private ArrayList<Task> searchTaskNonDateObject(String search) {
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        for(Task task: tasks) {
            if(isSearchFound(task, search)) {
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

        strArray[STATUS] = convertToStringFromInt(task.getPriority());
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

    /**
     * This method is used by processEditCommand(), processDeleteCommand(), 
     * undoAnOperation(), redoAnOperation()
     * @see TaskManagerInterface#getTaskFromTID(int)
     */
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
     * This method is used by processAddWithID(), processAddWithoutID(), editTaskDateFrom(),
     *      editTaskDateTo() and editTaskDeadline();
     * @param dateString  assume dateString is in this format "dd/MM/yyyy HH:mm"
     * @return
     */
    private Date convertToDateObject(String dateString) {
        Date date = null;

        try {
            if(dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
                DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                date = format.parse(dateString);
            }
        } catch (ParseException ex) {
            //put a logger here
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
                    !isTaskComplete(existingTask) &&
                    isNewTaskClashedWithExistingTask(newTask, existingTask) && 
                    newTask.getTID() != existingTask.getTID()) {
                returningTasks.add(existingTask.clone());
            }
        }
    }

    private boolean isTaskComplete(Task task) {
        return task.getStatus() == COMPLETE;
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
        case DATE_FROM: Collections.sort(tasks, new ComparatorDate()); break;
        case LOCATION: Collections.sort(tasks, new ComparatorLocation()); break;
        case STATUS: Collections.sort(tasks, new ComparatorPriority()); break;
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