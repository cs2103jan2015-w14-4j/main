import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Shortcut {
	 
	
	public static final String[] keywords = {	"addTask", "editTask","viewTasks","deleteTask", 
										"undoTask", "redoTask",
										"addShortcut", "viewShortcuts", "deleteShortcut",
										"resetShortcut", "addTemplate", "editTemplate", 
										"viewTemplates", "deleteTemplate", "resetTemplates", "help"}; 

	private ArrayList<ArrayList<String>> userShortcuts;
	private static Shortcut centralizedShortcut;
	
	private Shortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < keywords.length; ++i) {
			userShortcuts.add(new ArrayList<String>());
		}
	}
	
	public static Shortcut getShortcut() {
		if(centralizedShortcut == null) {
			centralizedShortcut = new Shortcut();
		}
		return centralizedShortcut;
	}
	
	public String[][] processShortcutCommand(String[] command) throws NoSuchElementException {
		String[][] results = null;
		
		assert(command.length == 3);
		
		switch(matchCommand(command[0])) {
			case addShortcut:
				verifyCommand(command, 3);
				results = new String[1][];
				results[0] = insertShortcut(command[1],command[2]);
				break;
				
			case deleteShortcut:
				verifyCommand(command, 2);
				results = new String[1][];
				results[0] = removeShortcut(command[1]);
				break;
				
			case resetShortcut:
				verifyCommand(command, 1);
				resetShortcut();
				results = viewShortcuts();
				break;
				
			case viewShortcut:
				verifyCommand(command, 1);
				results = cloneShortcuts();
				break;
			case addShortcutInit:
				verifyCommand(command, 3);
				addShortcutInit(command);
				break;
		}
		return results; 
	}
	
	private void addShortcutInit(String[] command) throws NumberFormatException {
		int row = Integer.parseInt(command[2]);
		
		assert(row < keywords.length);
		assert(row >= 0);
		assert(!isKeyWords(command[1]));
		
		ArrayList<String> toBeAddedInto = userShortcuts.get(row);
		
		toBeAddedInto.add(command[1]);
	}

	public String keywordMatching(String command) {
		int matchingIndex = searchMatching(command);
		if(matchingIndex > -1) {
			return keywords[matchingIndex];
		}
		else {
			return null;
		}
	}
	
	private COMMAND_TYPE_SHORTCUT matchCommand(String command) throws NoSuchElementException {
		switch(command) {
			case "addShortcut":
				return COMMAND_TYPE_SHORTCUT.addShortcut;
			case "viewShortcuts":
				return COMMAND_TYPE_SHORTCUT.viewShortcut;
			case "deleteShortcut":
				return COMMAND_TYPE_SHORTCUT.deleteShortcut;
			case "resetShortcut":
				return COMMAND_TYPE_SHORTCUT.resetShortcut;
			case "addShortcutInit":
				return COMMAND_TYPE_SHORTCUT.addShortcutInit;
			default:
				throw new NoSuchElementException("Wrong command received at Shortcut Manager.");
		}
		
	}
	
	private void resetShortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		userShortcuts.add(buildAddTask());
		userShortcuts.add(buildEditTask());
		userShortcuts.add(buildViewTask());
		userShortcuts.add(buildDeleteTask());
		userShortcuts.add(buildUndoTask());
		userShortcuts.add(buildRedoTask());
		userShortcuts.add(buildAddShortcut());
		userShortcuts.add(buildViewShortcut());
		userShortcuts.add(buildDeleteShortcut());
		userShortcuts.add(buildResetShortcut());
		userShortcuts.add(buildAddTemplate());
		userShortcuts.add(buildEditTemplate());
		userShortcuts.add(buildViewTemplates());
		userShortcuts.add(buildDeleteTemplate());
		userShortcuts.add(buildResetTemplates());
		userShortcuts.add(buildHelp());
	}
	
	

	private ArrayList<String> buildAddTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("add");
		defaults.add("addTask");
		
		return defaults;
	}
	private ArrayList<String> buildEditTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("edit");
		defaults.add("editTask");
		return defaults;
	}
	private ArrayList<String> buildViewTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("view");
		defaults.add("viewTask");
		defaults.add("viewTasks");
		return defaults;
	}
	private ArrayList<String> buildDeleteTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("delete");
		defaults.add("deleteTask");
		return defaults;
	}
	private ArrayList<String> buildUndoTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("undo");
		return defaults;
	}
	private ArrayList<String> buildRedoTask() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("redo");
		return defaults;
	}
	private ArrayList<String> buildAddShortcut() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("addShortcut");
		defaults.add("addShort");
		return defaults;
	}
	private ArrayList<String> buildDeleteShortcut() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("deleteShortcut");
		defaults.add("deleteShort");
		return defaults;
	}
	private ArrayList<String> buildViewShortcut() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("viewShortcuts");
		defaults.add("viewShortcut");
		defaults.add("viewShort");
		defaults.add("viewShorts");
		return defaults;
	}
	private ArrayList<String> buildResetShortcut() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("resetShortcuts");
		defaults.add("resetShortcut");
		return defaults;
	}
	private ArrayList<String> buildAddTemplate() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("addTemplate");
		defaults.add("addTemp");
		return defaults;
	}
	private ArrayList<String> buildEditTemplate() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("editTemplate");
		defaults.add("editTemp");
		return defaults;
	}
	private ArrayList<String> buildViewTemplates() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("viewTemplates");
		defaults.add("viewTemplate");
		defaults.add("viewTemp");
		defaults.add("viewTemps");
		return defaults;
	}
	private ArrayList<String> buildDeleteTemplate() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("deleteTemplate");
		defaults.add("deleteTemp");
		return defaults;
	}
	private ArrayList<String> buildResetTemplates() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("resetTemplates");
		defaults.add("resetTemplate");
		defaults.add("resetTemp");
		defaults.add("resetTemps");
		return defaults;
	}
	private ArrayList<String> buildHelp() {
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("help");
		return defaults;
	}
	
	
	
	private String[][] viewShortcuts() {
		return cloneShortcuts();
	}
	
	private String[] removeShortcut(String shortcut) {
		
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			for(int j = 0; j < singleShortcut.size(); ++j) {
				if(shortcut.equals(singleShortcut.get(j))) {
					String removed = singleShortcut.remove(j);
					String[] result = {keywords[i], removed};
					return result;
				}
			}
		}
		return null;
	}
	
	private String[] insertShortcut(String originShortcut, String newShortcut) {
		int belongTo = searchMatching(originShortcut);
		if(!isKeyWords(belongTo)) {
			return null;
		}
		else if(isKeyWords(searchMatching(newShortcut))) {
			return null;
		}
		else {
			ArrayList<String> toBeAddedInto = userShortcuts.get(belongTo);
			toBeAddedInto.add(newShortcut);
			String[] result = {keywords[belongTo], originShortcut, newShortcut};
			return result;
		}
	}
	
	private boolean isKeyWords(int index) {
		return (index > -1);
	}
	
	private boolean isKeyWords(String command) {
		int matchingIndex = searchMatching(command);
		return matchingIndex > -1;
	}
	
	private int searchMatching (String command) {
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			for(int j = 0; j < singleShortcut.size(); ++j) {
				if(command.equals(singleShortcut.get(j))) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private String[][] cloneShortcuts() {
		String[][] cloneList = new String[keywords.length][];
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			cloneList[i] = singleShortcut.toArray(new String[singleShortcut.size()]);

		}
		
		return cloneList;
	}
	
	private boolean verifyCommand(String[] command, int lengthToCheck) {
		assert(command.length == 3);
		for(int i = 0; i < lengthToCheck; ++i) {
			assert(command[i] != null);
		}
		return true;
	}
	
}
