import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Shortcut {
	
	
	public static final String[] keywords = {	"addTask", "editTask","viewTasks","deleteTask",
										"addShortcut", "viewShortcuts", "deleteShortcut",
										"resetShortcut", "addTemplate", "editTemplate", 
										"viewTemplates", "deleteTemplate", "help"}; 
	
	private ArrayList<ArrayList<String>> userShortcuts;
	private ArrayList<ArrayList<String>> defaultWords;
	
	public Shortcut() {
		resetShortcut();
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
		}
		return results; 
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
			default:
				throw new NoSuchElementException("Wrong command received at Shortcut Manager.");
		}
		
	}
	
	private void resetShortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < defaultWords.size(); ++i) {
			ArrayList<String> singleShortcut = new ArrayList<String>();
			ArrayList<String> matchList = defaultWords.get(i);
			for(int j = 0; j < matchList.size(); ++j) {
				singleShortcut.add(matchList.get(j));
			}
			userShortcuts.add(singleShortcut);
		}
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
		else if(isKeyWords(newShortcut)) {
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
		return index > -1;
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
			userShortcuts.add(singleShortcut);
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
