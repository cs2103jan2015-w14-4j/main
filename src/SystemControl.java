import java.util.Scanner;

public class SystemControl {
	
	//dummy
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	
	private TaskList myTaskList;
	private Customize myCustomizedList;
	private Shortcut myShortcut;
	private String fileName;
	
	
	public String getFileName() {
		return fileName;
	}

	enum COMMAND_TYPE {
		TASK_MANAGER, SHORTCUT_MANAGER, CUSTOMIZED_MANAGER
	}
	
	
	
	public SystemControl (String fileName) {
		this.fileName = fileName;
		initializeSystem(fileName);
	}
	

	public static void main(String[] args) {
		
		String myFile = dummyUI(MSG_ASK_FILENAME);
		SystemControl mySystemControl = new SystemControl(myFile);
		mySystemControl.executeCommandUntilExit();
	}


	private static COMMAND_TYPE getCommandGroupType(String commandType) {
		switch(commandType) {
			case "add":
			case "view":
			case "delete":
			case "edit":
				return COMMAND_TYPE.TASK_MANAGER;
			//dummy command keyword
			case "addShort":
				return COMMAND_TYPE.SHORTCUT_MANAGER;
			//dummy command keyword
			case "addCustomized":
				return COMMAND_TYPE.CUSTOMIZED_MANAGER;
		}
		return null;
	}
	
	private static String dummyUI(String msg) {
		System.out.println(msg);
		Scanner sc = new Scanner(System.in);
		String commandFromUser = sc.nextLine();
		sc.close();
		return commandFromUser;
	}
	
	public boolean initializeSystem(String fileName) {
		
		boolean isInitProperly = false;
		myTaskList = new TaskList();
		if(isInitProperly) {
			return true;	
		}
		else {
			return false;
		}
	}
	
	private void executeCommandUntilExit() {
		
		String inputFromUser = SystemControl.dummyUI(MSG_ASK_INPUT);
		String[] parsedCommand;
		
		do {
			
			parsedCommand = Parser.parseString(inputFromUser);
			COMMAND_TYPE commandGroupType = SystemControl.getCommandGroupType(parsedCommand[0]);
			
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
	}

	
}
