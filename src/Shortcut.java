import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Shortcut {
	 
	
	public static final String[] keywords = {	"addTask", "editTask","viewTask","deleteTask", 
										"clearAttr", "undoTask", "redoTask",
										"addShortcut", "viewShortcuts", "deleteShortcut",
										"resetShortcut", "addTemplate", "editTemplate", 
										"viewTemplates", "useTemplate", "deleteTemplate", 
										"resetTemplates", "help"}; 
	
	public static final String[][] defaultWordsSet = {{"add","addTask"}, {"edit","editTask"},
													{"view","viewTask"}, {"delete","deleteTask"},
													{"clear","clearAttr"}, {"undo","undoTask"}, 
													{"redo","redoTask"}, {"addShortcut","addShort"}, 
													{"viewShortcut","viewShort"}, {"deleteShortcut","deleteShort"}, 
													{"resetShortcut","resetShort"}, {"addTemplate","addTemp"}, 
													{"editTemplate","editTemp"}, {"viewTemplate","viewTemp"}, 
													{"useTemplate", "useTemp"}, {"deleteTemplate","deleteTemp"}, 
													{"resetTemplate", "resetTemp"}, {"help"}
													};
	
	private static final String[] reservedWords = {"at","location","from","datefrom","to","dateto","on",
													"before","by","detail","priority","name","title"};
	
	private ArrayList<ArrayList<String>> userShortcuts;
	private SystemHandler system;
	
	public Shortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < keywords.length; ++i) {
			userShortcuts.add(new ArrayList<String>());
		}
	}
	

	public void setSystemPath(SystemHandler system) {
		this.system = system;
	}
	
	
	public String[][] processShortcutCommand(String[] command) 
			throws NoSuchElementException, IllegalArgumentException {
		String[][] results = null;
		
		assert(command.length == 3);
		
		switch(matchCommand(command[0])) {
			case addShortcut:
				verifyCommand(command, 3);
				results = new String[1][];
				results[0] = insertShortcut(command[1],command[2]);
				writeOutToFile();
				break;
				
			case deleteShortcut:
				verifyCommand(command, 2);
				results = new String[1][];
				results[0] = removeShortcut(command[1]);
				writeOutToFile();
				break;
				
			case resetShortcut:
				verifyCommand(command, 1);
				resetShortcut();
				results = viewShortcuts();
				writeOutToFile();
				break;
				
			case viewShortcut:
				verifyCommand(command, 1);
				results = cloneShortcuts();
				break;
			case addShortcutInit:
				try {
					verifyCommand(command, 3);
					addShortcutInit(command);
					break;
				} catch (NumberFormatException e) {
					System.out.println(e);
				}
				
		}
		return results; 
	}
	
	private void writeOutToFile() {
		String[][] shortcuts = new String[keywords.length][];
		for(int i = 0; i < keywords.length; ++i) {
			shortcuts[i] = new String[userShortcuts.get(i).size()];
			userShortcuts.get(i).toArray(shortcuts[i]);
		}
		
		system = SystemHandler.getSystemHandler();
		
		//system.writeShortcutToFile(shortcuts);
		
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
		for(int i = 0; i < keywords.length; ++i) {
			userShortcuts.add(buildDefaultKeyword(i));
		}
	}
	
	private ArrayList<String> buildDefaultKeyword(int index) {
		ArrayList<String> customizedWord = new ArrayList<String>();
		String[] defaultwords = defaultWordsSet[index];
		for(int i = 0; i < defaultwords.length; ++i) {
			customizedWord.add(defaultwords[i]);
		}
		return customizedWord;
	}

	private String[][] viewShortcuts() {
		return cloneShortcuts();
	}
	
	private String[] removeShortcut(String shortcut) {
		
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			for(int j = 0; j < singleShortcut.size(); ++j) {
				
				if(shortcut.equals(singleShortcut.get(j))) {
					if(singleShortcut.size() < 2) {
						return null;
					}
					else {
						String removed = singleShortcut.remove(j);
						String[] result = {keywords[i], removed};
						return result;
					}
					
				}
			}
		}
		return null;
	}
	
	private String[] insertShortcut(String newShortcut, String originShortcut) {
		int belongTo = searchMatching(originShortcut);
		if(!isKeyWords(belongTo)) {
			return null;
		}
		else if(isExactKeyWords(newShortcut)) {
			return null;
		}
		else if (isReservedWord(newShortcut)) {
			return null;
		}
		else {
			
			ArrayList<String> toBeAddedInto = userShortcuts.get(belongTo);
			if(toBeAddedInto.size() > 5) {
				return null;
			}
			else {
				toBeAddedInto.add(newShortcut);
				String[] result = {keywords[belongTo], originShortcut, newShortcut};
				return result;	
			}
		}
	}
	
	private boolean isKeyWords(int index) {
		return (index > -1);
	}
	
	private boolean isKeyWords(String command) {
		int matchingIndex = searchMatching(command, false);
		return matchingIndex > -1;
	}
	
	private boolean isExactKeyWords(String command) {
		int matchingIndex = searchMatching(command, true);
		return matchingIndex > -1;
	}
	
	private int searchMatching(String command) {
		return searchMatching(command, false);
	}
	
	private int searchMatching (String command, boolean exactMatch) {
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			
			for(int j = 0; j < singleShortcut.size(); ++j) {
				if(exactMatch) {
					if(isExactMatchingWord(command, singleShortcut.get(j))) {
						return i;
					}
				} else if(isTheMatchingWord(command, singleShortcut.get(j))) {
					return i;
				}
				
			}
			
		}
		return -1;
	}
	
	private boolean isExactMatchingWord(String command, String matching) {
		if(command.equals(matching)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isTheMatchingWord(String command, String matching) {
		
		if(command.toLowerCase().charAt(command.length() - 1) == 's') {
			command = command.substring(0, command.length() - 1);
		}
		
		if(command.equalsIgnoreCase(matching)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private boolean isReservedWord(String word) {
		for(int i = 0; i < reservedWords.length; ++i) {
			if(word.equalsIgnoreCase(reservedWords[i])) {
				return true;
			}
		}
		return false;
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
