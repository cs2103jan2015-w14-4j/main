import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class SystemHandler {
	
	private static final String MSG_ERR_ID_UNDEFINED = "Something is wrong with the ID, please check again";
	private static final String MSG_TASK_STATUS = "The task:\"%s\" has been marked as %s";
	private static final String MSG_TASK_REDO = "A task operation has been redo.";
	private static final String MSG_TASK_SEARCH = "There are %s task(s) fulfilling the searching requirement.";
	private static final String MSG_TASK_UNDO = "A task operation has been undo.";
	private static final String MSG_TASK_DELETE = "The task:\"%s\" has been deleted from the Flexi Tracker.";
	private static final String MSG_TASK_CLASH_TASK = "%s(%s),";
	private static final String MSG_TASK_CLASH = "The newly added task has clashed with the following task(ID): ";
	private static final String MSG_TASK_UPDATE = "The task:\"%s\" has been updated from the Flexi Tracker under ID number %s.";
	private static final String MSG_TASK_VIEW = "The tasks list has been retrieved from the Flexi Tracker.";
	private static final String MSG_TEMP_DELETE = "The template:\"%s\" has been deleted from the Flexi Tracker.";
	private static final String MSG_TEMP_UPDATE = "The template:\"%s\" has been updated from the Flexi Tracker ";
	private static final String MSG_TEMP_VIEW = "The template list has been retrieved from the Flexi Tracker.";
	private static final String MSG_TEMP_NO_TEMPLATE = "No templates found in Flexi Tracker";
	private static final String MSG_SHORTCUT_DELETED = "Keyword \"%s\" has been deleted.";
	private static final String MSG_SHORTCUT_RESET = "All keywords have been reset to the list above";
	private static final String MSG_SHORTCUT_VIEW = "All keywords have been retrieved";
	private static final String MSG_SHORTCUT_ADDED_NEW = "New keyword \"%s\" has been added. It will function the same as \"%s\"";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "SystemHandler does not recognize this command";
	private static final String MSG_LOG_USER_COMMAND = "user enters: %s";
	
	private static final String[] COMMAND_GET_TEMPLATE = {"viewTemplate",null,null,null,null,null,null,null,null};
	private static final String[] COMMAND_GET_TASK_LIST = {"viewTask",null,null,null,null,null,null,null,null};
	private static final String[] COMMAND_RESET_SHORTCUT = {"resetShortcut", null, null};

	//Intended length of command array
	public static final int LENGTH_COMMAND_TASK_MANAGER = 9;
	public static final int LENGTH_COMMAND_SHORTCUT = 3;
	public static final int LENGTH_COMMAND_TEMPLATE = 9;
	private static final int INDEX_COMMAND_TASK_MANAGER = 0;
	private static final int INDEX_COMMAND_SHORTCUT = 1;
	private static final int INDEX_COMMAND_TEMPLATE = 2;
	private static final int INDEX_EXECUTION_ERROR = 0;
	private static final int INDEX_EXECUTION_SUCCESS = 1;
	private static final int INDEX_EXECUTION_CLASH = 2;
	private static final int ERROR_INIT = 1;
	private static final boolean EXECUTION_SUCCESS = true;
	
	
	private CentralizedLog 	logfile;
	private TaskManager 	myTaskList;
	private Template 		myTemplates;
	private Shortcut 		myShortcut;
	private String 			fileName;
	private FileStorage 	externalStorage;
	private UserInterface 	window;
	private FlexiParser 	parser;
	
	public static SystemHandler system;
	
	/**
	 * Return file location which the data saved at
	 * @return	File location which the data saved at
	 */
	public String getFileName() {
		return fileName;
	}
	
//	/**
//	 * This constructor constructs System Handler object with fileName as the save location 
//	 * @param fileName	File location which the data saved at
//	 */
//	public SystemHandler (String fileName) {
//		this.fileName = fileName;
//	}
	
//	/**
//	 * 	This constructor constructs System Handler object with default.txt as the save location
//	 */
//	private SystemHandler () {
//		new SystemHandler("default.txt");
//	}
	
	public static SystemHandler getSystemHandler() {
		return getSystemHandler("default.txt");
	}
	
	public static SystemHandler getSystemHandler(String fileName) {
		if(system == null) {
			system = new SystemHandler();
			system.initializeSystem(fileName);
		}
		return system;
	}
	
	/**
	 * Booting the system 
	 * @param args	Parameter from input - not applicable
	 */
	public static void main(String[] args) {

		system = getSystemHandler();
		system.myTemplates.setSystemPath(system);
		system.myShortcut.setSystemPath(system);
		
		system.activateUI();
	}
	
	/**
	 * It activates user interface window by calling the Runnable user interface 
	 */
	private void activateUI() {
		window = new UserInterface();
		System.out.println("Activated");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new UserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void initialize(String name) {
		fileName = name;
		
	}
	
	public void resetShortcutToDefault() {
		
		myShortcut.processShortcutCommand(COMMAND_RESET_SHORTCUT);
	}
	
	/**
	 * It reads in user command and process it.
	 * After the process, it returns the affected tasks in an ArrayList
	 * @param userInput	
	 * @return	An ArrayList of tasks related to command executed
	 */
	public ArrayList<Task> rawUserInput(String userInput) {
		System.out.println("Executing '"+ userInput+"'");
		return processUserInput(userInput);
	}
	
	public Task requestTask(int id) throws NoSuchElementException {
		//stub
//		return new Task(1000, "NEW",
//				convertToDateObject("12/09/2015 10:00"),
//				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0);
		Task gotten = myTaskList.getTaskFromTID(id);
		return myTaskList.getTaskFromTID(id);
		
	}
	
	public void addTaskFromTemplate(String[] fetchedTask) {
		try {
			executeTaskManager(fetchedTask);
		} catch (ParseException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		}
		
	}
	
	public boolean writeToFile(ArrayList<Task> taskList) {
		externalStorage.writeTaskToFile(taskList);
		return true;
	}
	
	public boolean writeShortcutToFile(String[][] shortcut) {
		externalStorage.writeShortcutToFile(shortcut);
		return true;
	}
	
	public boolean writeTemplateToFile(ArrayList<Task> templates,ArrayList<String> matchingName) {
		externalStorage.writeTemplateToFile(templates, matchingName);
		return true;
	}
	

//	private Date convertToDateObject(String dateString) {
//		try {
//			Date date = null;
//			if (dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
//				DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
//				date = format.parse(dateString);
//			}
//			return date;
//		} catch (ParseException e) {
//			System.out.println(e);
//			return null;
//		}
//	}
	
	/**
	 * @param commandType	Command Type string extracted from first word of strings of user command
	 * @return	COMMAND_TYPE_GROUP to allocate the command to correct component
	 */
	private static int getCommandGroupType(String commandType) {
		for(COMMAND_TYPE_TASK_MANAGER command : COMMAND_TYPE_TASK_MANAGER.values()) {
			if(command.name().equals(commandType)) {
				System.out.println("CHECK "+ command.name() +" with "+ commandType);
				return INDEX_COMMAND_TASK_MANAGER;
			}
		}
		for(COMMAND_TYPE_SHORTCUT command : COMMAND_TYPE_SHORTCUT.values()) {
			if(command.name().equals(commandType)) {
				return INDEX_COMMAND_SHORTCUT;
			}
		}
		for(COMMAND_TYPE_TEMPLATE command : COMMAND_TYPE_TEMPLATE.values()) {
			if(command.name().equals(commandType)) {
				return INDEX_COMMAND_TEMPLATE;
			}
		}
		throw new IllegalArgumentException(MSG_ERR_NO_SUCH_COMMAND);
	}
	
	
	/**
	 * @param fileName	File location which the data saved at
	 * @return			True if the system is initialized properly
	 */
	private boolean initializeSystem(String fileName) {
		
		boolean isInitProperly = false;
		myShortcut = new Shortcut();
//		String[] cmd = {"resetShortcut",null,null};
//		myShortcut.processShortcutCommand(cmd);
		
		logfile = CentralizedLog.getLogger();
		myTemplates = new Template();
		myTaskList = new TaskManager();
		parser = new FlexiParser(myShortcut);
		externalStorage = new FileStorage(fileName);
		system = this;
		try{
			externalStorage.readTaskFromFile(myTaskList);
			externalStorage.readShortcutFromFile(myShortcut);
			externalStorage.readTemplateFromFile(myTemplates);
		} catch(ParseException e) {
			window.displayMsg(e.getMessage(), ERROR_INIT);
		}
		
		if(isInitProperly) {
			return true;	
		}
		else {
			return false;
		}
	}
	
	/**
	 * This method functions as a communication line between components and 
	 * calls the correct components according to the command from user and fetch the correct data 
	 * @param inputFromUser		A command string received from user
	 * @return					An ArrayList of task objects that are affected by the command
	 */
	private ArrayList<Task> processUserInput(String inputFromUser) {
		try {
			
			logfile.log(Level.CONFIG, String.format(MSG_LOG_USER_COMMAND, inputFromUser));
			
			//Parse command
			
			String[] parsedCommand = parser.parseText(inputFromUser);
			
			//For checking purposes
			String[] temp = parsedCommand;
			
	    	for(int i = 0; i < temp.length; ++i) {
	    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
	    		System.out.print("|");
	    	}
			
			int commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			validateParsedCommandLength(parsedCommand, commandGroupType);
			switch(commandGroupType) {
				case INDEX_COMMAND_TASK_MANAGER:
					return executeTaskManager(parsedCommand);
					
				case INDEX_COMMAND_SHORTCUT:
					String[][] displayS = executeShortcutManager(parsedCommand);
					break;
					
				case INDEX_COMMAND_TEMPLATE:
					executeCustomizer(parsedCommand);
					break;
			}
			
		} catch(ParseException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		} catch(NumberFormatException e) {
			window.displayMsg(MSG_ERR_ID_UNDEFINED, INDEX_EXECUTION_ERROR);
		} catch(IllegalArgumentException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		} catch(NoSuchElementException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		} catch(IllegalStateException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		} 
		return null;
		
	}

	/**
	 * @param parsedCommand		A parsed command received from parser 
	 * @throws ParseException	The length of parsed command array is not the wanted length
	 */
	private void validateParsedCommandLength(String[] parsedCommand, int type) {
		switch(type) {
			case INDEX_COMMAND_TASK_MANAGER:
				assert(parsedCommand.length == LENGTH_COMMAND_TASK_MANAGER);
				break;
			case INDEX_COMMAND_SHORTCUT:
				assert(parsedCommand.length == LENGTH_COMMAND_SHORTCUT);
				break;
			case INDEX_COMMAND_TEMPLATE:
				assert(parsedCommand.length == LENGTH_COMMAND_TEMPLATE);
				break;
		}
		
	}
	
	/**
	 * @param command	A parsed command received from parser
	 * @return			An ArrayList of task objects that are affected by the command
	 * @throws ParseException	The date format does not match the wanted format
	 */
	private ArrayList<Task> executeTaskManager(String[] command) 
			throws ParseException {
		ArrayList<Task> result = myTaskList.processTM(command);
		//ArrayList<Task> fullList = myTaskList.processTM(COMMAND_GET_TASK_LIST);
		displayTMResultToUI(command, result, new ArrayList<Task>());
		return result;
	}

	/**
	 * @param command
	 * @param result
	 * @param fullList
	 */
	private void displayTMResultToUI(String[] command, ArrayList<Task> result,
			ArrayList<Task> fullList) {
		String[] message = constructTMMessage(command, result);
		window.displayTaskTable(result, fullList, INDEX_EXECUTION_SUCCESS);
		window.displayMsg(message, getTaskManagerExecutionStatus(command,result));
	}
	
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
	
	private String[][] executeShortcutManager(String[] command) 
			throws NoSuchElementException, IllegalArgumentException {
		
		String[][] result = myShortcut.processShortcutCommand(command);
		displayShortcutResultToUI(command, result);
		return result;
	}

	/**
	 * @param command
	 * @param result
	 */
	private void displayShortcutResultToUI(String[] command, String[][] result) {
		window.displayShortcuts(result, EXECUTION_SUCCESS);
		String[] message = constructShortcutMessage(command, result);
		window.displayMsg(message, INDEX_EXECUTION_SUCCESS);
	}
	
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
	
	private void executeCustomizer(String[] command) 
			throws IllegalArgumentException {

		ArrayList<Task> result = myTemplates.processCustomizingCommand(command);
		ArrayList<String> tempNames = myTemplates.getTemplateNames(result);		window.displayTaskTable(result, result, INDEX_EXECUTION_SUCCESS);
		displayTemplateResulttoUI(command, tempNames, result);
	}

	/**
	 * @param command
	 * @param result
	 */
	private void displayTemplateResulttoUI(String[] command, ArrayList<String> names, ArrayList<Task> result) {
		window.displayTemplate(result, names, EXECUTION_SUCCESS);
		String[] message = constructTempMessage(command, result);
		window.displayMsg(message,INDEX_EXECUTION_SUCCESS);
	}
	
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
			
		}
		return message;
	}
	
	
}
