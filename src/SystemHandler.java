import java.util.Scanner;
import java.util.ArrayList;
import java.awt.EventQueue;
import java.text.ParseException;

public class SystemHandler {
	
	//dummy string acting like UI prompt
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	
	public static final int LENGTH_COMMAND = 9;
	
	private TaskManager myTaskList;
	private Customize myCustomizedList;
	private Shortcut myShortcut;
	private String fileName;
	private FileStorage externalStorage;
	private UserInterface window;
	
	public String getFileName() {
		return fileName;
	}
	
	public SystemHandler (String fileName) {
		this.fileName = fileName;
		initializeSystem(fileName);
		System.out.println("INITED with ("+fileName+")...");
	}
	
	public SystemHandler () {
		String fileName = "default.txt";
		initializeSystem(fileName);
		
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String myFile = dummyUI(MSG_ASK_FILENAME, sc);
		SystemHandler mySystemControl = new SystemHandler(myFile);
		mySystemControl.activateUI();
		sc.close();
	}
	
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
	
	
	
	public ArrayList<Task> rawUserInput(String userInput) {
		System.out.println("Executing '"+ userInput+"'");
		return processUserInput(userInput);
	}

	private static COMMAND_TYPE_GROUP getCommandGroupType(String commandType) {
		switch(commandType) {
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
	
	private static String dummyUI(String msg, Scanner sc) {
		System.out.println(msg);
		String commandFromUser = sc.nextLine();
		return commandFromUser;
	}
	
	private boolean initializeSystem(String fileName) {
		
		boolean isInitProperly = false;
		myTaskList = new TaskManager();
		externalStorage = new FileStorage(fileName);
		if(isInitProperly) {
			return true;	
		}
		else {
			return false;
		}
	}
	
	private ArrayList<Task> processUserInput(String inputFromUser) {
		try {
			//Parse command
			FlexiParser parser = new FlexiParser(inputFromUser);
			String[] parsedCommand = parser.getStringArray();
			
			//For checking purposes
			String[] temp = parsedCommand;
			
	    	for(int i = 0; i < temp.length; ++i) {
	    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
	    		System.out.print("|");
	    	}
			
			
			if(parsedCommand.length != LENGTH_COMMAND)
				throw new ParseException("Invalid length of parsed command", 
						parsedCommand.length - LENGTH_COMMAND);
			
			
			COMMAND_TYPE_GROUP commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			switch(commandGroupType) {
				case TASK_MANAGER:
					return executeTaskManager(parsedCommand);
				case SHORTCUT_MANAGER:
					executeShortcutManager(parsedCommand);
				case CUSTOMIZED_MANAGER:
					executeCustomizer(parsedCommand);
			}
			
		} catch(ParseException e) {
			System.out.println(e);
		}
		return null;
		
	}
	
	private ArrayList<Task> executeTaskManager(String[] command) throws ParseException {
		ArrayList<Task> result = myTaskList.processTM(command, externalStorage);
		return result;
	}
	
	private void executeShortcutManager(String[] command) {
		myShortcut.processShortcutCommand(command);

	}
	
	private void executeCustomizer(String[] command) {
		myCustomizedList.processCustomizingCommand(command);
		
	}
	
	
}
