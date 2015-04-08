import java.util.ArrayList;

//@author A0108385B
public class DisplayProcessor {
	
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
	
	private static final String MSG_SHORTCUT_DELETED = "The keyword \"%s\" has been deleted.";
	private static final String MSG_SHORTCUT_RESET = "All keywords have been reset to the list of defaults above.";

	private static final String MSG_SHORTCUT_VIEW = "Displaying all Keywords";
	private static final String MSG_SHORTCUT_ADDED_NEW = "New keyword \"%s\" has been added to the \"%s\" function.";

	private static final int SIZE_ZERO = 0;
	
	
	private static final int INDEX_EXECUTION_ERROR = 0;
	private static final int INDEX_EXECUTION_SUCCESS = 1;
	private static final int INDEX_EXECUTION_CLASH = 2;
	
	private static final int INDEX_COMMAND_TYPE = 0;
	
	private static final int INDEX_TEMP_NEW_NAME = 2;
	private static final int INDEX_TEMP_NAME = 1;
	
	private static final int NUM_TASK_SINGLE = 1;
	
	
	private static final int INDEX_SHORTCUT_DELETE = 1;
	private static final int INDEX_SHORTCUT_OLD = 2;
	private static final int INDEX_SHORTCUT_NEW = 1;
	
	private static final boolean EXECUTION_SUCCESS = true;

	private static final int LENGTH_MESSAGE_DEFAULT = 1;
	private static final int MESSAGE_NUMBER_ONE = 0;
	
	
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
	
	public void displayTaskfromLastLogin(ArrayList<Task> taskList) {
		window.displayTaskTable(taskList, taskList, INDEX_EXECUTION_SUCCESS);
	}
	
	/**
	 * @param command		Command that has been executed
	 * @param result		Affected tasks by the executed command
	 * @param fullList		Full list of tasks stored in task manager
	 */
	public void displayTMResult(String[] command, ArrayList<Task> result,
			ArrayList<Task> fullList) {
		String[] message = constructTMMessage(command, result);
		
		if(command[0].equals("deleteTask")) {
			window.displayTaskTable(null, fullList, INDEX_EXECUTION_SUCCESS);
		} else {
			window.displayTaskTable(result, fullList, INDEX_EXECUTION_SUCCESS);
		}
		
		if(hasMessage(message)) {
			window.displayMsg(message, getTaskManagerExecutionStatus(command,result));
		}
			
	}
	
	/**
	 * MA CONG: turn your task manager get command function to public static, this method need to use.
	 * @param command		Command that has been executed
	 * @param result		Affected tasks by the executed command
	 * @return				String array of messages to be displayed
	 */
	private String[] constructTMMessage(String[] command, ArrayList<Task> result) {
		String[] message = null;
		switch(command[0]) {
		case "viewTask":
			message = new String[1];
			message[0] = MSG_TASK_VIEW;
			break;
		case "addTask":
		case "editTask":
			if(result.size() > 1) {
				message = new String[2];
				message[0] = String.format(MSG_TASK_UPDATE,result.get(0).getTaskName(),result.get(0).getTID());
				message[1] = MSG_TASK_CLASH;
				for(int i = 1; i < result.size(); ++i) {
					message[1] += String.format(MSG_TASK_CLASH_TASK, result.get(i).getTaskName(), result.get(i).getTID());
				}
				message[1] = message[1].substring(0, message[1].length() - 1) + ".";
			} else {
				message = new String[1];
				message[0] = String.format(MSG_TASK_UPDATE,result.get(0).getTaskName(),result.get(0).getTID());
				
			}
			break;
				
		case "deleteTask":
			message = new String[1];
			message[0] = String.format(MSG_TASK_DELETE,result.get(0).getTaskName());
			break;
			
		case "searchTask":
			message = new String[1];
			message[0] = String.format(MSG_TASK_SEARCH, result.size());
			break;
			
		case "undoTask":
			message = new String[1];
			message[0] = MSG_TASK_UNDO;
			break;
			
		case "redoTask":
			message = new String[1];
			message[0] = MSG_TASK_REDO;
			break;
			
		case "clearAttr":
			message = new String[1];
			message[0] = String.format(MSG_TASK_UPDATE,result.get(0).getTaskName(),result.get(0).getTID());
			break;
			
		case "markTask":
			message = new String[2];
			message[0] = String.format(MSG_TASK_STATUS, result.get(0).getTaskName(), result.get(0).getStatusString());
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
		if(command[0] == "viewTask") {
			if(result != null) {
				return INDEX_EXECUTION_SUCCESS;
			} else {
				return INDEX_EXECUTION_ERROR;
			}
		} else if(command[0] == "searchTask") {
			if(result != null) {
				return INDEX_EXECUTION_SUCCESS;
			} else {
				return INDEX_EXECUTION_ERROR;
			}
		} else {	//For other task types that are not view or search
			if(result == null) {
				return INDEX_EXECUTION_ERROR;
			} else if(result.size() == NUM_TASK_SINGLE) {
				return INDEX_EXECUTION_SUCCESS;
			} else {
				return INDEX_EXECUTION_CLASH;
			}
		}
	}
	
	/**
	 * @param command	Command that has been executed
	 * @param result	Affected shortcuts by the executed command
	 */
	public void displayShortcutResult(String[] command, String[][] result) {
		window.displayShortcuts(result, EXECUTION_SUCCESS);
		String[] message = constructShortcutMessage(command, result);
		
		if(hasMessage(message)) {
			window.displayMsg(message, INDEX_EXECUTION_SUCCESS);
		}
			
	}
	
	/**
	 * @param command	Command that has been executed
	 * @param result	Affected shortcuts by the executed command
	 * @return			String array of messages to be displayed, null if no messages to be displayed
	 */
	private String[] constructShortcutMessage(String[] command,
			String[][] result) {
		String[] message = new String[LENGTH_MESSAGE_DEFAULT];
		switch(Shortcut.getCommandType(command[INDEX_COMMAND_TYPE])) {
			case addShortcut:
				message[MESSAGE_NUMBER_ONE] = String.format(MSG_SHORTCUT_ADDED_NEW, command[INDEX_SHORTCUT_NEW], command[INDEX_SHORTCUT_OLD]);
				break;
			case viewShortcut:
				message[MESSAGE_NUMBER_ONE] = MSG_SHORTCUT_VIEW;
				break;
			case deleteShortcut:
				message[MESSAGE_NUMBER_ONE] = String.format(MSG_SHORTCUT_DELETED, command[INDEX_SHORTCUT_DELETE]);
				break;
			case resetShortcut:
				message[MESSAGE_NUMBER_ONE] = MSG_SHORTCUT_RESET;
				break;
			case addShortcutInit:
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
		
		if(hasMessage(message)) {
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
		
		switch(Template.getCommandType(command[INDEX_COMMAND_TYPE])) {
			case viewTemplate:
				if(noTemplateToView(result)) {
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

	public void displayHelptoUser() {
		window.displayText(window.HELP, true);
		
	}
}