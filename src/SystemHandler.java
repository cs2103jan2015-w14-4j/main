import java.util.Scanner;
import java.util.ArrayList;
import java.text.ParseException;

public class SystemHandler {
	
	//dummy string acting like UI prompt
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	
	private TaskManager myTaskList;
	private Customize myCustomizedList;
	private Shortcut myShortcut;
	private String fileName;
	
	
	public String getFileName() {
		return fileName;
	}
	
	public SystemHandler (String fileName) {
		this.fileName = fileName;
		initializeSystem(fileName);
	}
	

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String myFile = dummyUI(MSG_ASK_FILENAME, sc);
		SystemHandler mySystemControl = new SystemHandler(myFile);
		sc.close();
	}
	
	public ArrayList<Task> rawUserInput(String userInput) {
		return processUserInput(userInput);
	}

	private static COMMAND_TYPE_GROUP getCommandGroupType(String commandType) {
		switch(commandType) {
			case "add":
			case "view":
			case "delete":
			case "edit":
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
		if(isInitProperly) {
			return true;	
		}
		else {
			return false;
		}
	}
	
	private ArrayList<Task> processUserInput(String inputFromUser) {
		
		 String[] parsedCommand = Parser.parseString(inputFromUser);
		COMMAND_TYPE_GROUP commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
		
		switch(commandGroupType) {
			case TASK_MANAGER:
				return executeTaskManager(parsedCommand);
			case SHORTCUT_MANAGER:
				executeShortcutManager(parsedCommand);
			case CUSTOMIZED_MANAGER:
				executeCustomizer(parsedCommand);
		}
		
		return null;
		
	}
	
	private ArrayList<Task> executeTaskManager(String[] command) {
		try {
			ArrayList<Task> result = myTaskList.processTM(command);
			return result;
		} catch (ParseException e){
			System.out.println(e);
		}
		
		return null;
	}
	
	private void executeShortcutManager(String[] command) {
		myShortcut.processShortcutCommand(command);

	}
	
	private void executeCustomizer(String[] command) {
		myCustomizedList.processCustomizingCommand(command);
		
	}
	
	
}
