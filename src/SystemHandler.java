import java.util.Scanner;

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
		mySystemControl.executeCommandUntilExit(sc);
		sc.close();
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
	
	private void executeCommandUntilExit(Scanner sc) {

		
		String inputFromUser;
		String[] parsedCommand;
		
		do {
			inputFromUser = SystemHandler.dummyUI(MSG_ASK_INPUT, sc);
			parsedCommand = Parser.parseString(inputFromUser);
			COMMAND_TYPE_GROUP commandGroupType = SystemHandler.getCommandGroupType(parsedCommand[0]);
			
			switch(commandGroupType) {
				case TASK_MANAGER:
					executeTaskManager(parsedCommand);
					break;
				case SHORTCUT_MANAGER:
					executeShortcutManager(parsedCommand);
					break;
				case CUSTOMIZED_MANAGER:
					executeCustomizer(parsedCommand);
					break;
			}
			
		} while(!inputFromUser.equals("exit"));
	}
	
	private void executeTaskManager(String[] command) {
		String[] result = myTaskList.processTaskCommand(command);
		outputResultToUser(result);
	}
	
	private void executeShortcutManager(String[] command) {
		String[] result = myShortcut.processShortcutCommand(command);
		outputResultToUser(result);
	}
	
	private void executeCustomizer(String[] command) {
		String[] result = myCustomizedList.processCustomizingCommand(command);
		outputResultToUser(result);
	}
	
	private void outputResultToUser(String[] result) {
		//dummy
		System.out.println("DUMMY OUTPUT");
	}

	
}