import java.util.ArrayList;

//@author A0108385B
public class DisplayProcessor {

    private static final String CMD_TASK_VIEW = "viewTask";
    private static final String CMD_TASK_SEARCH = "searchTask";
    private static final String MSG_SAVE_SUCCESS = "The save path has been changed to \"%s\" successfully";
    private static final String MSG_TASK_STATUS = "The task:\"%s\" has been marked as %s";
    private static final String MSG_TASK_REDO = "The last task operation has been redone.";
    private static final String MSG_TASK_SEARCH = " %s task(s) have been found.";
    private static final String MSG_TASK_UNDO = "The last task operation has been undone.";
    private static final String MSG_TASK_DELETE = "The task:\"%s\" has been deleted.";
    private static final String MSG_TASK_CLASH_TASK = "%s (Task ID: %s),";
    private static final String MSG_TASK_CLASH = "The newly added task has clashed with the following task(s): ";
    private static final String MSG_TASK_UPDATE = "The task:\"%s\" (Task ID: %s), has been updated.";
    private static final String MSG_TASK_VIEW = "The tasks list has been retrieved.";

    private static final String MSG_TEMP_DELETE = "The template:\"%s\" has been deleted.";
    private static final String MSG_TEMP_UPDATE = "The template:\"%s\" has been updated. ";
    private static final String MSG_TEMP_VIEW = "The template list has been retrieved.";
    private static final String MSG_TEMP_NO_TEMPLATE = "There are no saved templates.";
    private static final String MSG_TEMP_RESET = "All Templates have been cleared.";

    private static final String MSG_KEYWORD_DELETED = "The keyword \"%s\" has been deleted.";
    private static final String MSG_KEYWORD_RESET = "All keywords have been reset to the list of defaults above.";

    private static final String MSG_KEYWORD_VIEW = "Displaying all Keywords";
    private static final String MSG_KEYWORD_ADDED_NEW = "New keyword \"%s\" has been added to the \"%s\" function.";

    private static final int SIZE_ZERO = 0;


    private static final int INDEX_EXECUTION_ERROR = 0;
    private static final int INDEX_EXECUTION_SUCCESS = 1;
    private static final int INDEX_EXECUTION_CLASH = 2;

    private static final int INDEX_COMMAND_TYPE = 0;

    private static final int INDEX_TEMP_NEW_NAME = 2;
    private static final int INDEX_TEMP_NAME = 1;

    private static final int NUM_TASK_SINGLE = 1;


    private static final int INDEX_KEYWORD_DELETE = 1;
    private static final int INDEX_KEYWORD_OLD = 2;
    private static final int INDEX_KEYWORD_NEW = 1;

    private static final boolean EXECUTION_SUCCESS = true;

    private static final int LENGTH_MESSAGE_DEFAULT = 1;
    private static final int MESSAGE_NUMBER_ONE = 0;
    private static final int MESSAGE_NUMBER_TWO = 1;
    private static final int MESSAGE_SIZE_ONE = 1;
    private static final int MESSAGE_SIZE_TWO = 2;
    private static final int RESULT_SIZE_ONE = 1;
    private static final int INDEX_TASK_AFFECTED = 0;

    private UserInterface window;


    public DisplayProcessor(UserInterface window) {
        this.window = window;
    }


    /**
     * This method calls UI to display the error message to user
     * @param message	Error message to be displayed to user
     */
    public void displayErrorToUI(String message) {
        window.displayMsg(message, INDEX_EXECUTION_ERROR);
    }


    /**
     * This method calls UI to display tasks list saved from last login
     * @param taskList Tasks list to be shown to user 
     */
    public void displayTaskfromLastLogin(ArrayList<Task> taskList) {
        window.displayTaskTable(taskList, taskList, INDEX_EXECUTION_SUCCESS);
    }


    /**
     * This method calls UI to display the result of changing save location
     */
    public void displayMoveFileResultToUI(String savePath) {
        window.displayMsg(String.format(MSG_SAVE_SUCCESS, savePath), INDEX_EXECUTION_SUCCESS);
    }


    /**
     * @param command		Command that has been executed
     * @param result		Affected tasks by the executed command
     * @param fullList		Full list of tasks stored in task manager
     */
    public void displayTMResult(String[] command, ArrayList<Task> result,
            ArrayList<Task> fullList) {
        String[] message = constructTMMessage(command, result);

        if (command[0].equals("deleteTask")) {
            window.displayTaskTable(null, fullList, INDEX_EXECUTION_SUCCESS);
        } else {
            window.displayTaskTable(result, fullList, INDEX_EXECUTION_SUCCESS);
        }

        if (hasMessage(message)) {
            window.displayMsg(message, getTaskManagerExecutionStatus(command,result));
        }

    }

    /**
     * 
     * @param command		Command that has been executed
     * @param result		Affected tasks by the executed command
     * @return				String array of messages to be displayed
     */
    private String[] constructTMMessage(String[] command, ArrayList<Task> result) {
        String[] message = null;

        switch (TaskManager.getCommand(command[INDEX_COMMAND_TYPE])) {
        case viewTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = MSG_TASK_VIEW;
            break;

        case addTask: 
        case editTask:
            if (result.size() > RESULT_SIZE_ONE) {
                message = new String[MESSAGE_SIZE_TWO];
                message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_UPDATE,
                        result.get(INDEX_TASK_AFFECTED).getTaskName(), result.get(INDEX_TASK_AFFECTED).getTID());
                message[MESSAGE_NUMBER_TWO] = MSG_TASK_CLASH;
                for(int i = 1; i < result.size(); ++i) {
                    message[MESSAGE_NUMBER_TWO] += String.format(MSG_TASK_CLASH_TASK, 
                            result.get(i).getTaskName(), result.get(i).getTID());
                }
                message[MESSAGE_NUMBER_TWO] = message[MESSAGE_SIZE_ONE].substring(0, message[MESSAGE_NUMBER_ONE].length() - 1) + ".";
            } else {
                message = new String[MESSAGE_SIZE_ONE];
                message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_UPDATE,
                        result.get(INDEX_TASK_AFFECTED).getTaskName(), result.get(INDEX_TASK_AFFECTED).getTID());
            }
            break;

        case deleteTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_DELETE,
                    result.get(INDEX_TASK_AFFECTED).getTaskName());
            break;

        case searchTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_SEARCH, result.size());
            break;

        case undoTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = MSG_TASK_UNDO;
            break;

        case redoTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = MSG_TASK_REDO;
            break;

        case clearAttr:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_UPDATE,
                    result.get(INDEX_TASK_AFFECTED).getTaskName(), result.get(INDEX_TASK_AFFECTED).getTID());
            break;

        case markTask:
            message = new String[MESSAGE_SIZE_ONE];
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TASK_STATUS, 
                    result.get(INDEX_TASK_AFFECTED).getTaskName(), result.get(INDEX_TASK_AFFECTED).getStatusString());
            break;
        }
        return message;
    }


    /**
     * @param command		Command that has been executed
     * @param result		Affected tasks by the executed command
     * @return				Index of execution status, success/error/clashed
     */
    private int getTaskManagerExecutionStatus(String[] command, ArrayList<Task> result) {
        if (command[INDEX_COMMAND_TYPE].equals(CMD_TASK_VIEW)) {
            if (result != null) {
                return INDEX_EXECUTION_SUCCESS;
            } else {
                return INDEX_EXECUTION_ERROR;
            }
        } else if (command[INDEX_COMMAND_TYPE].equals(CMD_TASK_SEARCH)) {
            if (result != null) {
                return INDEX_EXECUTION_SUCCESS;
            } else {
                return INDEX_EXECUTION_ERROR;
            }
        } else {	//For other task types that are not view or search
            if (result == null) {
                return INDEX_EXECUTION_ERROR;
            } else if (result.size() == NUM_TASK_SINGLE) {
                return INDEX_EXECUTION_SUCCESS;
            } else {
                return INDEX_EXECUTION_CLASH;
            }
        }
    }


    /**
     * @param command	Command that has been executed
     * @param result	Affected keywords by the executed command
     */
    public void displayKeywordResult(String[] command, String[][] result) {
        window.displayKeywords(result, EXECUTION_SUCCESS);
        String[] message = constructKeywordMessage(command, result);

        if (hasMessage(message)) {
            window.displayMsg(message, INDEX_EXECUTION_SUCCESS);
        }

    }


    /**
     * @param command	Command that has been executed
     * @param result	Affected keywords by the executed command
     * @return			String array of messages to be displayed, null if no messages to be displayed
     */
    private String[] constructKeywordMessage(String[] command,
            String[][] result) {
        String[] message = new String[LENGTH_MESSAGE_DEFAULT];

        switch (KeywordManager.getCommandType(command[INDEX_COMMAND_TYPE])) {
        case addKeyword:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_KEYWORD_ADDED_NEW, 
                    command[INDEX_KEYWORD_NEW], command[INDEX_KEYWORD_OLD]);
            break;

        case viewKeyword:
            message[MESSAGE_NUMBER_ONE] = MSG_KEYWORD_VIEW;
            break;

        case deleteKeyword:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_KEYWORD_DELETED, command[INDEX_KEYWORD_DELETE]);
            break;

        case resetKeyword:
            message[MESSAGE_NUMBER_ONE] = MSG_KEYWORD_RESET;
            break;

        case addKeywordInit:
            message = null;
        }
        return message;
    }


    /**
     * @param command	Command that has been executed
     * @param result	Affected templates by the executed command
     */
    public void displayTemplateResult(String[] command, ArrayList<String> names, ArrayList<Task> result) {

        window.displayTemplate(result, names, EXECUTION_SUCCESS);
        String[] message = constructTempMessage(command, result);

        if (hasMessage(message)) {
            window.displayMsg(message,INDEX_EXECUTION_SUCCESS);
        }
    }


    /**
     * @param message	Message to be displayed
     * @return			True if no message is null
     */
    private boolean hasMessage(String[] message) {
        return message != null;
    }


    /**
     * @param command	Command that has been executed
     * @param result	Affected templates by the executed command
     * @return			String array of messages to be displayed, null if no messages to be displayed
     */
    private String[] constructTempMessage(String[] command, ArrayList<Task> result) {
        String[] message = new String[LENGTH_MESSAGE_DEFAULT];

        switch (TemplateManager.getCommandType(command[INDEX_COMMAND_TYPE])) {
        case viewTemplate:
            if (noTemplateToView(result)) {
                message[MESSAGE_NUMBER_ONE] = MSG_TEMP_NO_TEMPLATE;
            } else {
                message[MESSAGE_NUMBER_ONE] = MSG_TEMP_VIEW;
            }
            break;

        case addTemplate:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TEMP_UPDATE, command[INDEX_TEMP_NEW_NAME]);
            break;

        case editTemplate:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TEMP_UPDATE, command[INDEX_TEMP_NAME]);
            break;

        case deleteTemplate:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TEMP_DELETE, command[INDEX_TEMP_NAME]);
            break;

        case resetTemplate:
            message[MESSAGE_NUMBER_ONE] = String.format(MSG_TEMP_RESET);
            break;

        case useTemplate: 
        case addTemplateInit: 
            message = null;

        }
        return message;
    }


    /**
     * @param result	ArrayList of templates to be viewed
     * @return			True if there are some templates to view
     */
    private boolean noTemplateToView(ArrayList<Task> result) {
        return result.size() == SIZE_ZERO;
    }


    /**
     * This method calls UI to display help list to user
     */
    public void displayHelptoUser() {
        window.displayText(UserInterface.HELP, true);
    }
}