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

	private static final int ARRAY_SIZE_SHORTCUT = 3;
	private static final int ADD_SHORTCUT_ARRAY_USED_SIZE = 3;
	private static final int ADD_SHORTCUT_INIT_ARRAY_USED_SIZE = 3;
	private static final int VIEW_SHORTCUT_ARRAY_USED_SIZE = 1;
	private static final int DELETE_SHORTCUT_ARRAY_USED_SIZE = 2;
	private static final int RESET_SHORTCUT_ARRAY_USED_SIZE = 1;
	 
	private static final int INDEX_DELETING_SHORTCUT = 1;
	private static final int INDEX_REFERED_SHORTCUT = 2;
	private static final int INDEX_NEW_SHORTCUT = 1;
	private static final int INDEX_INIT_REFERED_ROW = 2;
	
	private static final int MINIMUM_LENGTH = 3;
	private static final int MAXIMUM_LENGTH = 15;
	private static final int MINIMUM_CAPACITY = 1;
	private static final int MAXIMUM_CAPACITY = 10;

	private static final int INDEX_NOT_FOUND = -1;
	private static final int INDEX_COMMAND = 0;
	

	private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command in Shortcut Manager: %1$s";


	
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
		

		switch(matchCommand(command[INDEX_COMMAND])) {
			case addShortcut:
				verifyCommand(command, ADD_SHORTCUT_ARRAY_USED_SIZE);
				results = new String[1][];
				results[0] = insertShortcut(command[INDEX_NEW_SHORTCUT],
											command[INDEX_REFERED_SHORTCUT]);
				writeOutToFile();
				break;
				
			case deleteShortcut:
				verifyCommand(command, DELETE_SHORTCUT_ARRAY_USED_SIZE);
				results = new String[1][];
				results[0] = removeShortcut(command[INDEX_DELETING_SHORTCUT]);
				writeOutToFile();
				break;
				
			case resetShortcut:
				verifyCommand(command, RESET_SHORTCUT_ARRAY_USED_SIZE);
				resetShortcut();
				results = viewShortcuts();
				writeOutToFile();
				break;
				
			case viewShortcut:
				verifyCommand(command, VIEW_SHORTCUT_ARRAY_USED_SIZE);
				results = cloneShortcuts();
				break;
			case addShortcutInit:
				try {
					verifyCommand(command, ADD_SHORTCUT_INIT_ARRAY_USED_SIZE);
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
		int row = Integer.parseInt(command[INDEX_INIT_REFERED_ROW]);
		
		assert(row < keywords.length);
		assert(row >= 0);
		
		ArrayList<String> toBeAddedInto = userShortcuts.get(row);

		toBeAddedInto.add(command[INDEX_NEW_SHORTCUT]);
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
	
	
	
	private COMMAND_TYPE_SHORTCUT matchCommand(String command) 
			throws IllegalArgumentException {
        try{
        	return COMMAND_TYPE_SHORTCUT.valueOf(command);
        } catch (IllegalArgumentException e) {
        	throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
        			e.getMessage().substring(39)));
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
		int indexBelongTo = searchMatching(originShortcut);
		if(!isKeyWords(indexBelongTo)) {
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
		else if(isShortcutAtMaximumCapacity(indexBelongTo)) {
			return null;
		}
		else {
			addShortcut(newShortcut, indexBelongTo);
			String[] result = constructOutputString(indexBelongTo,originShortcut,newShortcut);
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
		return (index > INDEX_NOT_FOUND);
	}
	
	private boolean isKeyWords(String command) {
		int matchingIndex = searchMatching(command);
		return matchingIndex > INDEX_NOT_FOUND;
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
		return INDEX_NOT_FOUND;
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
		assert(command.length == ARRAY_SIZE_SHORTCUT);
		for(int i = 0; i < lengthToCheck; ++i) {
			assert(command[i] != null);
		}
		return true;
	}
	
}
