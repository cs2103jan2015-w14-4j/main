import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.EventQueue;
import java.text.ParseException;
import java.util.Observable;


public class SystemHandler extends Observable {
	
	//dummy string acting like UI prompt
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	
	//Intended length of command array
	public static final int LENGTH_COMMAND = 9;
	
	private CentralizedLog 	logfile;
	private TaskManager 	myTaskList;
	private Template 		myTemplates;
	private Shortcut 		myShortcut;
	private String 			fileName;
	private FileStorage 	externalStorage;
	private static UserInterface 	window;
	private FlexiParser 	parser;
	
	private static ArrayList<Task> taskResult;
	
	public void outputTasklist(ArrayList<Task> result){
		taskResult = result;
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Task> getTaskResult(){
		return taskResult;
	}
	
	public void shortcutUpdate(String shortcuts){
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Return file location which the data saved at
	 * @return	File location which the data saved at
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * This constructor constructs System Handler object with fileName as the save location 
	 * @param fileName	File location which the data saved at
	 */
	public SystemHandler (String fileName) {
		this.fileName = fileName;
		initializeSystem(fileName);
	}
	
	/**
	 * 	This constructor constructs System Handler object with default.txt as the save location
	 */
	public SystemHandler () {
		String fileName = "default.txt";
		initializeSystem(fileName);
		
	}

	/**
	 * Booting the system 
	 * @param args	Parameter from input - not applicable
	 */
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String myFile = dummyUI(MSG_ASK_FILENAME, sc);
		SystemHandler mySystemControl = new SystemHandler(myFile);
		mySystemControl.activateUI();
		//mySystemControl.addObserver(window);
		sc.close();
	}
	
	/**
	 * It activates user interface window by calling the Runnable user interface 
	 */
	private void activateUI() {
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

	/**
	 * @param commandType	Command Type string extracted from first word of strings of user command
	 * @return	COMMAND_TYPE_GROUP to allocate the command to correct component
	 */
	private static COMMAND_TYPE_GROUP getCommandGroupType(String commandType) {
		switch(commandType) {
			case "init":	
			case "add":
			case "view":
			case "delete":
			case "edit":
			case "undo":
			case "redo":
				return COMMAND_TYPE_GROUP.TASK_MANAGER;
			//dummy command keyword
			case "addShort":
				return COMMAND_TYPE_GROUP.SHORTCUT_MANAGER;
			//dummy command keyword
			case "addCustomized":
				return COMMAND_TYPE_GROUP.CUSTOMIZED_MANAGER;
		}
		return null;
	}
	
	/**
	 * This is a dummy user interface to simulate user interaction
	 * @param msg	Message to show user
	 * @param sc	Scanner class to read user input
	 * @return		User input in a string
	 */
	private static String dummyUI(String msg, Scanner sc) {
		System.out.println(msg);
		String commandFromUser = sc.nextLine();
		return commandFromUser;
	}
	
	/**
	 * @param fileName	File location which the data saved at
	 * @return			True if the system is initialized properly
	 */
	private boolean initializeSystem(String fileName) {
		
		boolean isInitProperly = false;
		logfile = CentralizedLog.getLogger();
		parser = new FlexiParser();
		myTaskList = new TaskManager();
		externalStorage = new FileStorage(fileName);
		
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
//			String[] temp = parsedCommand;
//			
//	    	for(int i = 0; i < temp.length; ++i) {
//	    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
//	    		System.out.print("|");
//	    	}
			
			validateParsedCommand(parsedCommand);
			
			COMMAND_TYPE_GROUP commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			switch(commandGroupType) {
				case TASK_MANAGER:
					return executeTaskManager(parsedCommand);
				case SHORTCUT_MANAGER:
					executeShortcutManager(parsedCommand);
				case CUSTOMIZED_MANAGER:
					executeCustomizer(parsedCommand);
				default:
					assert(isAGroupCommand(getCommandGroupType(parsedCommand[0])));
			}
			
		} catch(ParseException e) {
			System.out.println(e);
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
		ArrayList<Task> result = myTaskList.processTM(command, externalStorage);
		outputTasklist(result);
		return result;
	}
	
	
	private void executeShortcutManager(String[] command) {
		//stub
		//Not implemented
		myShortcut.processShortcutCommand(command);

	}
	
	private void executeCustomizer(String[] command) {
		//stub
		//Not implemented
		myTemplates.processCustomizingCommand(command);
		
	}
	
	
}
