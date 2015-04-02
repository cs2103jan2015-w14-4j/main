import java.util.Date;
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
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
//	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
//	private static final String CLEAR_INFO_INDICATOR = "";
	private static final String[] CMD_GET_TEMPLATE = {"viewTask",null,null,null,null,null,null,null,null};
	private static final String[] CMD_GET_TASK_LIST = {"viewTemplates",null,null,null,null,null,null,null,null};
	
	//Intended length of command array
	public static final int LENGTH_COMMAND = 9;
	private static final int ERROR = 0;
	private static final int ERROR_INIT = 1;
	
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
	
	public Task requestTask(int id) {
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
			window.displayMsg(e.getMessage(), ERROR);
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
	private static COMMAND_TYPE_GROUP getCommandGroupType(String commandType) {
		switch(commandType) {
			case "addTask":
			case "viewTask":
			case "deleteTask":
			case "editTask":
			case "undoTask":
			case "redoTask":
			case "searchTask":
			case "init":
				return COMMAND_TYPE_GROUP.TASK_MANAGER;
			//dummy command keyword
			case "addShortcut":
			case "viewShortcuts":
			case "deleteShortcut":
			case "resetShortcuts":
				return COMMAND_TYPE_GROUP.SHORTCUT_MANAGER;
			//dummy command keyword
			case "addTemplate":
			case "editTemplate":
			case "viewTemplates":
			case "deleteTemplate":
			case "useTemplate":
			case "resetTemplates":
				return COMMAND_TYPE_GROUP.CUSTOMIZED_MANAGER;
			default:
		}
		return null;
	}
	
	
	/**
	 * @param fileName	File location which the data saved at
	 * @return			True if the system is initialized properly
	 */
	private boolean initializeSystem(String fileName) {
		
		boolean isInitProperly = false;
		myShortcut = new Shortcut();
		String[] cmd = {"resetShortcut",null,null};
		myShortcut.processShortcutCommand(cmd);
		
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
			logfile.log(Level.CONFIG,"user enters: "+inputFromUser);
			
			//Parse command
			
			String[] parsedCommand = parser.parseText(inputFromUser);
			
			//For checking purposes
			String[] temp = parsedCommand;
			
	    	for(int i = 0; i < temp.length; ++i) {
	    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
	    		System.out.print("|");
	    	}
			
			//validateParsedCommand(parsedCommand);
			
			COMMAND_TYPE_GROUP commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			switch(commandGroupType) {
				case TASK_MANAGER:
					return executeTaskManager(parsedCommand);
					
				case SHORTCUT_MANAGER:
					String[][] displayS = executeShortcutManager(parsedCommand);
					break;
				case CUSTOMIZED_MANAGER:
					executeCustomizer(parsedCommand);
					break;
				default:
					assert(isAGroupCommand(getCommandGroupType(parsedCommand[0])));
			}
			
		} catch(ParseException e) {
			window.displayMsg(e.getMessage(), ERROR);
		} 
		return null;
		
	}

	private boolean isAGroupCommand(COMMAND_TYPE_GROUP command) {
		return command != null;
	}
	
	/**
	 * @param parsedCommand		A parsed command received from parser 
	 * @throws ParseException	The length of parsed command array is not the wanted length
	 */
	private boolean validateParsedCommand(String[] parsedCommand)
			throws ParseException {
		assert(parsedCommand.length == LENGTH_COMMAND);
		if(parsedCommand.length != LENGTH_COMMAND) {
			throw new ParseException("Invalid length of parsed command", 
					parsedCommand.length - LENGTH_COMMAND);
		}
		return true;
	}
	
	/**
	 * @param command	A parsed command received from parser
	 * @return			An ArrayList of task objects that are affected by the command
	 * @throws ParseException	The date format does not match the wanted format
	 */
	private ArrayList<Task> executeTaskManager(String[] command) throws ParseException {
		ArrayList<Task> result = myTaskList.processTM(command);
		ArrayList<Task> fullList = myTaskList.processTM(CMD_GET_TASK_LIST);
		window.displayTaskTable(result, true);
		return result;
	}
	
	
	private String[][] executeShortcutManager(String[] command) {
		String[][] result = myShortcut.processShortcutCommand(command);
		window.displayShortcuts(result, true);
		return result;
	}
	
	private void executeCustomizer(String[] command) {
		try {
			ArrayList<Task> result = myTemplates.processCustomizingCommand(command);
			ArrayList<Task> fullList = myTemplates.processCustomizingCommand(CMD_GET_TEMPLATE);
			window.displayTaskTable(result, true);
		} catch (Exception e) {
			
		}
			
		
	}
	
	
}
