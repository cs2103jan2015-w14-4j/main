import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.awt.EventQueue;
import java.text.ParseException;

//@author A0108385B
public class SystemHandler {
	


	private static final String SAVE_LOCATION_DEFAULT = "default.txt";

	private static final String MSG_LOG_USER_COMMAND = "user enters: %s";
	
	private static final String MSG_LOG_PARSER = "Parser understood the command as the following: \"%s\"";
	private static final String MSG_ERR_INIT_TEMPLATE = "File has been corrupted, some templates might be lost.";
	private static final String MSG_ERR_INIT_SHORTCUT = "File has been corrupted, some shortcut data might be lost.";
	private static final String MSG_ERR_INIT_TASK = "File has been corrupted, some tasks might be lost.";
	private static final String MSG_ERR_ID_UNDEFINED = "This ID does not exist, please check again";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "SystemHandler does not recognize this command.";

	private static final String COMMAND_SAVE_TO = "saveTo";
	private static final String COMMAND_HELP = "help";
	private static final String[] COMMAND_GET_TASK_LIST = {"viewTask",null,null,null,null,null,null,null,null};
	private static final String[] COMMAND_RESET_SHORTCUT = {"resetShortcut", null, null};

	//Symbol used to construct string from parsed command to log message
	private static final String STRING_INIT_EMPTY = "";
	private static final String STRING_SEPERATOR = "|";
	private static final String STRING_EMPTY = "_";
	private static final String STRING_NULL = "null";

	public static final int LENGTH_COMMAND_TASK_MANAGER = 9;
	public static final int LENGTH_COMMAND_SHORTCUT = 3;
	public static final int LENGTH_COMMAND_TEMPLATE = 9;

	private static final int INDEX_SAVE_NEW_PATH = 1;
	
	private static final int INDEX_COMMAND_TASK_MANAGER = 0;
	private static final int INDEX_COMMAND_SHORTCUT = 1;
	private static final int INDEX_COMMAND_TEMPLATE = 2;
	private static final int INDEX_COMMAND_SAVE = 3;
	private static final int INDEX_COMMAND_HELP = 4;
	
	private static final int INDEX_EXECUTION_ERROR = 0;
	private static final int INDEX_EXECUTION_SUCCESS = 1;
	private static final int INDEX_EXECUTION_CLASH = 2;
	
	private static final int ERROR_INIT = 1;
	private static final boolean EXECUTION_SUCCESS = true;

	private static final int SIZE_ZERO = 0;


	

	
	private CentralizedLog 	logfile;
	private TaskManager 	myTaskList;
	private Template 		myTemplates;
	private Shortcut 		myShortcut;
	private String 			fileName;
	private FileStorage 	externalStorage;
	private UserInterface 	window;
	private FlexiParser 	parser;
	private DisplayProcessor displayProcessor;
	public static SystemHandler system;

	
	public static SystemHandler getSystemHandler() {
		return getSystemHandler(SAVE_LOCATION_DEFAULT);
	}
	
	public static SystemHandler getSystemHandler(String fileName) {
		if(system == null) {
			system = new SystemHandler();
			system.initializeSystem(fileName);
		}
		return system;
	}
	
	/**
	 * Booting the system and set the path to be called back.
	 * @param args	Parameter from input - not applicable
	 */
	public static void main(String[] args) {

		system = getSystemHandler();
		
		system.activateUI();
//		system.externalStorage.saveToAnotherLocation("../new/");
	}
	
	/**
	 * It activates user interface window by calling the Runnable user interface 
	 */
	private void activateUI() {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * This method is called by storage to set the shortcut manager to default 
	 * once it detects no shortcut is being initialized
	 */
	public void resetShortcutToDefault() {
		myShortcut.processShortcutCommand(COMMAND_RESET_SHORTCUT);
	}
	
	
	/**
	 * This method function as the communication line between different components to ensure
	 * intermediate results directed correctly. 
	 * The sequence is from UI -> parser -> logic -> displayProcessor 
	 * This is the place where error thrown from different component is responded to the User through user interface
	 * @param userInput
	 */
	public void rawUserInput(String userInput) {
		
		try {
			
			logfile.info(String.format(MSG_LOG_USER_COMMAND, userInput));
			
			String[] parsedCommand = parser.parseText(userInput);
			
			logfile.info(String.format(MSG_LOG_PARSER , parsedCommandtoString(parsedCommand)));
			
			int commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			
			validateParsedCommandLength(parsedCommand, commandGroupType);
			
			switch(commandGroupType) {
				case INDEX_COMMAND_TASK_MANAGER:
					executeTaskManager(parsedCommand);
					break;
					
				case INDEX_COMMAND_SHORTCUT:
					executeShortcutManager(parsedCommand);
					break;
					
				case INDEX_COMMAND_TEMPLATE:
					executeCustomizer(parsedCommand);
					break;
					
				case INDEX_COMMAND_HELP:
					displayProcessor.displayHelptoUser();
					break;
				
				case INDEX_COMMAND_SAVE:
					externalStorage.saveToAnotherLocation(parsedCommand[INDEX_SAVE_NEW_PATH]);
					break;
					
			}
			
		} catch(ParseException e) {
			displayProcessor.displayErrorToUI(e.getMessage());
			logfile.warning(String.format(e.getMessage()));
		} catch(NumberFormatException e) {
			displayProcessor.displayErrorToUI(MSG_ERR_ID_UNDEFINED);
			logfile.warning(MSG_ERR_ID_UNDEFINED);
		} catch(IllegalArgumentException e) {
			displayProcessor.displayErrorToUI(e.getMessage());
			logfile.warning(e.getMessage());
		} catch(NoSuchElementException e) {
			displayProcessor.displayErrorToUI(e.getMessage());
			logfile.warning(e.getMessage());
		} catch(IllegalStateException e) {
			displayProcessor.displayErrorToUI(e.getMessage());
			logfile.warning(e.getMessage());
		} 
		
	}
	
	
	/**
	 * This method is called to get the Task through the Task ID
	 * @param id		Task ID
	 * @return			A task object correspond to the ID requested
	 * @throws NoSuchElementException	Task ID requested is not found in Task Manager
	 */
	public Task requestTaskInformationfromTM(int id) throws NoSuchElementException {
		return myTaskList.getTaskFromTID(id);
	}
	
	/**
	 * @param fetchedTask A strings array that follows the format of TM strictly.
	 * Refer to Task Manager to find out the length and meaning of each string by checking the index constant.
	 */
	public void addTaskFromTemplate(String[] fetchedTask) {
		
		try {		
			executeTaskManager(fetchedTask);
		} catch (Exception e) {
			displayProcessor.displayErrorToUI(e.getMessage());
			logfile.warning(String.format(MSG_LOG_PARSER , e.getMessage()));
		}
	}
	
	/**
	 * This method calls storage to write out the data from task manager to storage
	 * @param taskList		ArrayList of tasks stored by task manager
	 */
	public void writeToFile(ArrayList<Task> taskList) {
		externalStorage.writeTaskToFile(taskList);
	}
	
	/**
	 * This method calls storage to write out the data from shortcut manager to storage
	 * @param shortcut		Array of strings arrays that represent the customized keywords 
	 */
	public void writeShortcutToFile(String[][] shortcut) {
		externalStorage.writeShortcutToFile(shortcut);
	}
	
	/**
	 * This method calls storage to write out the data from template manager to storage
	 * @param templates		ArrayList of task templates stored by template manager
	 * @param matchingName	ArrayList of string that match to the templates.
	 */
	public void writeTemplateToFile(ArrayList<Task> templates,ArrayList<String> matchingName) {
		externalStorage.writeTemplateToFile(templates, matchingName);
	}
	
	/**
	 * @param commandType	Command type
	 * @return				Index of the command belongs to. Task(1), Shortcut(2), Template(3)
	 * @throws IllegalArgumentException		The command is not defined in the system
	 */
	private static int getCommandGroupType(String commandType) throws IllegalArgumentException {
		if(commandType.equals(COMMAND_HELP)) {
			return INDEX_COMMAND_HELP;
		} else if(commandType.equals(COMMAND_SAVE_TO)) {
			return INDEX_COMMAND_SAVE;
		} else {
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
		
			
	}
	
	
	/**
	 * This method construct all the related classes required for the software
	 * @param fileName File location
	 */
	private void initializeSystem(String fileName) {
		
		myShortcut = new Shortcut();
		logfile = CentralizedLog.getLogger();
		myTemplates = new Template();
		myTaskList = new TaskManager();
		parser = new FlexiParser(myShortcut);
		externalStorage = new FileStorage(fileName);
		window = new UserInterface();
		displayProcessor = new DisplayProcessor(window);
		
		system = this;
		myTemplates.setSystemPath(system);
		myShortcut.setSystemPath(system);
		
		readDataFromFile();
		
		ArrayList<Task> fullList = myTaskList.processTM(COMMAND_GET_TASK_LIST);
		if(fullList != null && fullList.size() > 0) {
			displayProcessor.displayTaskfromLastLogin(fullList);
		}
	}

	/**
	 * It calls File Storage to load data into task manager, shortcut and template.
	 */
	private void readDataFromFile() {
		try{
			externalStorage.readTaskFromFile(myTaskList);
		} catch(Exception e) {
			window.displayMsg(MSG_ERR_INIT_TASK, ERROR_INIT);
			logfile.warning(MSG_ERR_INIT_TASK);
		}
		
		try{
			externalStorage.readShortcutFromFile(myShortcut);
		} catch(Exception e) {
			window.displayMsg(MSG_ERR_INIT_SHORTCUT, ERROR_INIT);
			logfile.warning(MSG_ERR_INIT_SHORTCUT);
		}
		
		try{
			externalStorage.readTemplateFromFile(myTemplates);
		} catch(Exception e) {
			window.displayMsg(MSG_ERR_INIT_TEMPLATE, ERROR_INIT);
			logfile.warning(MSG_ERR_INIT_TEMPLATE);
		}
	}
	
	/**
	 * @param parsedCommand		Strings array of command parsed by parser
	 * @return					String converted from a strings array to be logged.
	 */
	private String parsedCommandtoString(String[] parsedCommand) {
		String str = STRING_INIT_EMPTY;
		
    	for(int i = 0; i < parsedCommand.length; ++i) {
    		
    		if(parsedCommand[i] == null) {
    			str += STRING_NULL;
    			
    		} else if(parsedCommand[i].length() == SIZE_ZERO) {
    			str += STRING_EMPTY;
    			
    		} else {
    			str += parsedCommand[i];
    			
    		}
    		
    		str += STRING_SEPERATOR;
    	}
		return str;
	}

	
	/**
	 * @param parsedCommand		Strings array of command parsed by parser
	 * @param type				index of type of command - Task(1), Shortcut(2), Template(3)
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
				
			case INDEX_COMMAND_HELP:
				assert(parsedCommand.length == 1);
				break;
				
			case INDEX_COMMAND_SAVE:
				assert(parsedCommand.length == 2);
				break;
				
		}
		
	}
	
	/**
	 * @param command	String array in the format where shortcut manager understands. Refer to developer 
	 * 					manual under Shortcut for more information 
	 * @throws ParseException		INPUT FROM MA CONG
	 */
	private ArrayList<Task> executeTaskManager(String[] command) 
			throws ParseException {
		ArrayList<Task> result = myTaskList.processTM(command);
		
		ArrayList<Task> fullList = myTaskList.processTM(COMMAND_GET_TASK_LIST);
		if(result != null) {
			displayProcessor.displayTMResult(command, result, fullList);
		}
			
		return result;
	}

	/**
	 * @param command	String array in the format where shortcut manager understands. Refer to developer 
	 * 					manual under Shortcut for more information 
	 * @throws NoSuchElementException		Requested shortcut is not found in shortcut list
	 * 										or violation of restriction by shortcut manager
	 * @throws IllegalArgumentException		There are some error in format understood by shortcut manager
	 */
	private void executeShortcutManager(String[] command) 
			throws NoSuchElementException, IllegalArgumentException {
		
		String[][] result = myShortcut.processShortcutCommand(command);
		displayProcessor.displayShortcutResult(command, result);
		
	}

	/**
	 * @param command	String array in the format where template manager understands. Refer to developer 
	 * 					manual under Template for more information 
	 * @throws IllegalArgumentException		There are some error in format understood by template manager
	 * 										or violation of restriction by template manager
	 * @throws NoSuchElementException		Requested template is not found in template list
	 */
	private void executeCustomizer(String[] command) 
			throws IllegalArgumentException, NoSuchElementException {

		ArrayList<Task> result = myTemplates.processCustomizingCommand(command);
		if(result != null) {
			ArrayList<String> tempNames = myTemplates.getTemplateNames(result);		
			//window.displayTemplateTable(result, null, INDEX_EXECUTION_SUCCESS);
			displayProcessor.displayTemplateResult(command, tempNames, result);
		}
			
	}

}
