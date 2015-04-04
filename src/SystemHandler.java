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
	
	//dummy string acting like UI prompt
	private static final String MSG_ERR_NO_SUCH_COMMAND = "System does not recognize this command";
	private static final String MSG_LOG_USER_COMMAND = "user enters: $1%s";
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
//	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
//	private static final String CLEAR_INFO_INDICATOR = "";
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
	private static final int ERROR_INIT = 1;
	private static final int INDEX_EXECUTION_SUCCESS = 1;
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
		externalStorage.writeToFile(taskList);
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
			externalStorage.readFromFile(myTaskList);
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
		} catch(IllegalArgumentException e) {
			window.displayMsg(e.getMessage(), INDEX_EXECUTION_ERROR);
		} catch(NoSuchElementException e) {
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
		ArrayList<Task> fullList = myTaskList.processTM(COMMAND_GET_TASK_LIST);
		window.displayTaskTable(result, fullList, INDEX_EXECUTION_SUCCESS);
		return result;
	}
	
	
	private String[][] executeShortcutManager(String[] command) 
			throws NoSuchElementException, IllegalArgumentException {
		
		String[][] result = myShortcut.processShortcutCommand(command);
		window.displayShortcuts(result, EXECUTION_SUCCESS);
		return result;
	}
	
	private void executeCustomizer(String[] command) 
			throws IllegalArgumentException {

		ArrayList<Task> result = myTemplates.processCustomizingCommand(command);
//		ArrayList<Task> fullList = myTemplates.processCustomizingCommand(CMD_GET_TEMPLATE);
		//window.displayTaskTable(result, true);
	
	}
	
	
}
