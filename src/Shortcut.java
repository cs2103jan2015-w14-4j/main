import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Shortcut {
	 
	public static final String[] keywords = {	"addTask", "editTask","viewTask","deleteTask", 
										"clearAttr", "searchTask", "undoTask", "redoTask", "markTask",
										"addShortcut", "viewShortcuts", "deleteShortcut",
										"resetShortcut", "addTemplate", "editTemplate", 
										"viewTemplates", "useTemplate", "deleteTemplate", 
										"resetTemplates", "help"}; 
	
	public static final String[][] defaultWordsSet = {{"add","addTask"}, {"edit","editTask"},
													{"view","viewTask"}, {"delete","deleteTask"},
													{"clear","clearAttr"}, {"search", "searchTask"}, 
													{"undo","undoTask"}, {"redo","redoTask"},
													{"mark","markTask"}, {"addShortcut","addKeyword"}, 
													{"viewShortcut","viewKeyword"}, {"deleteShortcut","deleteKeyword"}, 
													{"resetShortcut","resetKeyword"}, {"addTemplate","addTemp"}, 
													{"editTemplate","editTemp"}, {"viewTemplate","viewTemp"}, 
													{"useTemplate", "useTemp"}, {"deleteTemplate","deleteTemp"}, 
													{"resetTemplate", "resetTemp"}, {"help"}
													};
	
	private static final String[] reservedWords = {"at","location","from","datefrom","to","dateto","on",
													"before","by","detail","priority","name","title"};

	private static final int MINIMUM_LENGTH = 3;
	private static final int MAXIMUM_LENGTH = 15;
	private static final int MINIMUM_CAPACITY = 1;
	private static final int MAXIMUM_CAPACITY = 10;

	private static final int INDEX_NOT_FOUND = -1;
	
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
		
		system.writeShortcutToFile(shortcuts);
		
	}
	
	private void addShortcutInit(String[] command) throws NumberFormatException {
		int row = Integer.parseInt(command[2]);
		
		assert(row < keywords.length);
		assert(row >= 0);
		
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
		
		int index = getShortcutMatchingIndex(shortcut);
		if(index == INDEX_NOT_FOUND) {
			return null;
			
		} else if(isShortcutKeyAtMinimumCapacity(index)){
			return null;
			
		} else if(isDefaultShortcut(shortcut)) {
			return null;
			
		} else {
			removeShortcutfromUserList(index, shortcut);
			
			String[] result = constructOutputString(index, shortcut);
			return result;
		}
		
	}
	
	private boolean isDefaultShortcut(String shortcut) {
		for(int i = 0; i < defaultWordsSet.length; ++i) {
			String[] defaultWords = defaultWordsSet[i];
			for(int j = 0; j < defaultWords.length; ++j) {
				if(shortcut.equalsIgnoreCase(defaultWords[j])) {
					return true;
				}
			}
		}
		return false;
	}


	private boolean removeShortcutfromUserList(int index, String shortcut) {
		ArrayList<String> userDefinedShortcut = userShortcuts.get(index);
		for(int i = 0; i < userDefinedShortcut.size(); ++i) {
			if(userDefinedShortcut.get(i).equals(shortcut)) {
				userDefinedShortcut.remove(i);
				return true;
			}
		}
		return false;
	}
	
	private boolean isShortcutKeyAtMinimumCapacity(int index) {
		return userShortcuts.get(index).size() < MINIMUM_CAPACITY;
	}
	private int getShortcutMatchingIndex(String shortcut) {
		for(int i = 0; i < userShortcuts.size(); ++i) {
			ArrayList<String> singleShortcut = userShortcuts.get(i);
			for(int j = 0; j < singleShortcut.size(); ++j) {
				if(shortcut.equals(singleShortcut.get(j))) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}
	
	private String[] insertShortcut(String newShortcut, String originShortcut) {
		int belongTo = searchMatching(originShortcut);
		if(!isKeyWords(belongTo)) {
			return null;
		}
		else if(isKeyWords(newShortcut)) {
			return null;
		}
		else if (isReservedWord(newShortcut)) {
			return null;
		}
		else if (isWordLengthInappropriate(newShortcut)) {
			return null;
		}
		else if(isShortcutAtMaximumCapacity(belongTo)) {
			return null;
		}
		else {
			addShortcut(newShortcut, belongTo);
			String[] result = constructOutputString(belongTo,originShortcut,newShortcut);
			return result;	
			
		}
	}
	
	private String[] constructOutputString(int index, String deleted) {
		String[] result = {keywords[index], deleted};
		return result;
	}
	
	private String[] constructOutputString(int index, String original, String newlyAdded) {
		String[] result = {keywords[index], original, newlyAdded};
		return result;
	}

	private boolean isWordLengthInappropriate(String newShortcut) {
		return newShortcut.length() < MINIMUM_LENGTH || newShortcut.length() > MAXIMUM_LENGTH;
	}


	private boolean isShortcutAtMaximumCapacity(int index) {
		return userShortcuts.get(index).size() > MAXIMUM_CAPACITY;
	}


	/**
	 * @param newShortcut
	 * @param belongTo
	 */
	private void addShortcut(String newShortcut, int belongTo) {
		ArrayList<String> toBeAddedInto = userShortcuts.get(belongTo);
		toBeAddedInto.add(newShortcut);
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
				if(isTheMatchingWord(command, singleShortcut.get(j))) {
					return i;
				}
				
			}
			
		}
		return -1;
	}
	
	private boolean isTheMatchingWord(String command, String matching) {
		
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
