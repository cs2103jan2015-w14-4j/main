import java.util.ArrayList;


public class DisplayProcessor {
	
	private static final String MSG_TASK_STATUS = "The task:\"%s\" has been marked as %s";
	private static final String MSG_TASK_REDO = "The last task operation has been redone.";
	private static final String MSG_TASK_SEARCH = " %s task(s) have been found.";
	private static final String MSG_TASK_UNDO = "The last task operation has been undone.";
	private static final String MSG_TASK_DELETE = "The task:\"%s\" has been deleted.";
	private static final String MSG_TASK_CLASH_TASK = "%s(%s),";
	private static final String MSG_TASK_CLASH = "The newly added task has clashed with the following task(ID): ";
	private static final String MSG_TASK_UPDATE = "The task:\"%s\" has been updated from the Flexi Tracker under ID number %s.";
	private static final String MSG_TASK_VIEW = "The tasks list has been retrieved from the Flexi Tracker.";
	
	private static final String MSG_TEMP_DELETE = "The template:\"%s\" has been deleted from the Flexi Tracker.";
	private static final String MSG_TEMP_UPDATE = "The template:\"%s\" has been updated from the Flexi Tracker ";
	private static final String MSG_TEMP_VIEW = "The template list has been retrieved from the Flexi Tracker.";
	private static final String MSG_TEMP_NO_TEMPLATE = "No templates found in Flexi Tracker";
	private static final String MSG_TEMP_RESET = "Templates have been reset, add your templates now.";
	

	private static final String MSG_SHORTCUT_DELETED = "Keyword \"%s\" has been deleted.";
	private static final String MSG_SHORTCUT_RESET = "All keywords have been reset to the list of defaults above.";
	private static final String MSG_SHORTCUT_VIEW = "All keywords have been retrieved";

	private static final String MSG_SHORTCUT_ADDED_NEW = "New keyword \"%s\" has been added. It will function the same as \"%s\"";
	
	private static final int INDEX_EXECUTION_ERROR = 0;
	private static final int INDEX_EXECUTION_SUCCESS = 1;
	private static final int INDEX_EXECUTION_CLASH = 2;
	private static final boolean EXECUTION_SUCCESS = true;
	
	
	private UserInterface window;
	
	public DisplayProcessor(UserInterface window) {
		this.window = window;
	}
	
	public void displayErrorToUI(String message) {
		window.displayMsg(message, INDEX_EXECUTION_ERROR);
	}
	
	/**
	 * @param command
	 * @param result
	 * @param fullList
	 */
	public void displayTMResult(String[] command, ArrayList<Task> result,
			ArrayList<Task> fullList) {
		String[] message = constructTMMessage(command, result);
		window.displayTaskTable(result, fullList, INDEX_EXECUTION_SUCCESS);
		window.displayMsg(message, getTaskManagerExecutionStatus(command,result));
	}
	
	/**
	 * @param command
	 * @param result
	 * @return
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
			message = new String[2];
			message[0] = String.format(MSG_TASK_UPDATE,result.get(0).getTaskName(),result.get(0).getTID());
			if(result.size() > 1) {
				message[1] = MSG_TASK_CLASH;
				for(int i = 1; i < result.size(); ++i) {
					message[1] += String.format(MSG_TASK_CLASH_TASK, result.get(i).getTaskName(), result.get(i).getTID());
				}
				message[1] = message[1].substring(0, message[1].length() - 1) + ".";
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
	 * @param command
	 * @param result
	 * @return
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
		} else {
			if(result == null) {
				return INDEX_EXECUTION_ERROR;
			} else if(result.size() == 1) {
				return INDEX_EXECUTION_SUCCESS;
			} else {
				return INDEX_EXECUTION_CLASH;
			}
		}
	}
	
	/**
	 * @param command
	 * @param result
	 */
	public void displayShortcutResult(String[] command, String[][] result) {
		window.displayShortcuts(result, EXECUTION_SUCCESS);
		String[] message = constructShortcutMessage(command, result);
		window.displayMsg(message, INDEX_EXECUTION_SUCCESS);
	}
	
	/**
	 * @param command
	 * @param result
	 * @return
	 */
	private String[] constructShortcutMessage(String[] command,
			String[][] result) {
		String[] message = new String[1];
		switch(command[0]) {
			case "addShortcut":
				message[0] = String.format(MSG_SHORTCUT_ADDED_NEW, command[1], command[2]);
				break;
			case "viewShortcut":
				message[0] = MSG_SHORTCUT_VIEW;
				break;
			case "deleteShortcut":
				message[0] = String.format(MSG_SHORTCUT_DELETED, command[1]);
				break;
			case "resetShortcut":
				message[0] = MSG_SHORTCUT_RESET;
				break;
		}
		return message;
	}
	
	/**
	 * @param command
	 * @param result
	 */
	public void displayTemplateResult(String[] command, ArrayList<String> names, ArrayList<Task> result) {
		
		window.displayTemplate(result, names, EXECUTION_SUCCESS);
		String[] message = constructTempMessage(command, result);
		window.displayMsg(message,INDEX_EXECUTION_SUCCESS);
	}
	
	/**
	 * @param command
	 * @param result
	 * @return
	 */
	private String[] constructTempMessage(String[] command, ArrayList<Task> result) {
		String[] message = null;
		switch(command[0]) {
		case "viewTemplate":
			message = new String[1];
			if(result.size() == 0) {
				message[0] = MSG_TEMP_NO_TEMPLATE;
			} else {
				message[0] = MSG_TEMP_VIEW;
			}
			break;
			
		case "addTemplate":
		case "editTemplate":
			message = new String[1];
			message[0] = String.format(MSG_TEMP_UPDATE, command[2]);
			break;
				
		case "deleteTemplate":
			message = new String[1];
			message[0] = String.format(MSG_TEMP_DELETE, command[1]);
			break;
			
		case "resetTemplate":
			message = new String[1];
			message[0] = String.format(MSG_TEMP_RESET, command[1]);
			break;
		}
		return message;
	}
}