import java.util.ArrayList;
import java.util.NoSuchElementException;

//@author A0108385B
public class Shortcut {

	private static final String MSG_ERR_UNCHANGEABLE_KEYWORD = "This keyword cannot be edited.";
	private static final String MSG_ERR_CORRUPTED_SAVED_FILE = "Shortcut saved file has been corrupted";
	private static final int RESULT_FIRST = 0;
	private static final int RESULT_SIZE_DEFAULT = 1;
	private final String MSG_ERR_MINIMUM_LENGTH = "\"%s\"is too short. Please choose a word that is at least 3 characters long to be used as a keyword.";
	private static final String MSG_ERR_MAX_CAPACITY = "\"%s\" cannot be added to this function as each function can only have a maximum of 10 keywords. Please delete one of the keywords if you want to add another one.";
	private static final String MSG_ERR_KEYWORD_EXIST = "\"%s\" is already being used as a keyword by Flexi Tracker.";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command exists in Shortcut Manager: %1$s";
	private static final String MSG_ERR_DEFAULT_KEYWORD_DELETE = "\"%s\" is a default system-defined keyword. It cannot be changed or deleted.";
	private static final String MSG_ERR_MINIMUM_SHORTCUT_NUMBER = "Each function must have at least one keyword. \"%s\" cannot be removed.";

	private static final String MSG_ERR_SHORTCUT_NOT_EXIST = "\"%s\" does not exist in the keyword list";

	//Command that are recognized by system 
	private static final String[] KEYWORDS = {	"addTask", 		"editTask",		"viewTask",
												"deleteTask", 	"clearAttr", 	"searchTask", 
												"undoTask", 	"redoTask", 	"markTask",
												"addShortcut", 	"viewShortcut", "deleteShortcut",
												"resetShortcut", "addTemplate", "editTemplate", 
												"viewTemplate", "useTemplate", 	"deleteTemplate", 
												"resetTemplate", "help", "saveTo"}; 
	
	
	//Default commands
	private static final String[] DEFAULT_HELP = {"help"};
	private static final String[] DEFAULT_RESET_TEMP = {"resetTemplate", "resetTemp"};
	private static final String[] DEFAULT_DELETE_TEMP = {"deleteTemplate","deleteTemp"};
	private static final String[] DEFAULT_USE_TEMP = {"useTemplate", "useTemp"};
	private static final String[] DEFAULT_VIEW_TEMP = {"viewTemplate","viewTemp"};
	private static final String[] DEFAULT_EDIT_TEMP = {"editTemplate","editTemp"};
	private static final String[] DEFAULT_ADD_TEMP = {"addTemplate","addTemp"};
	private static final String[] DEFAULT_RESET_SHORTCUT = {"resetShortcut","resetKeyword"};
	private static final String[] DEFAULT_DELETE_SHORTCUT = {"deleteShortcut","deleteKeyword"};
	private static final String[] DEFAULT_VIEW_SHORTCUT = {"viewShortcut","viewKeyword"};
	private static final String[] DEFAULT_ADD_SHORTCUT = {"addShortcut","addKeyword"};
	private static final String[] DEFAULT_MARK_TASK = {"mark","markTask"};
	private static final String[] DEFAULT_REDO_TASK = {"redo","redoTask"};
	private static final String[] DEFAULT_UNDO_TASK = {"undo","undoTask"};
	private static final String[] DEFAULT_SEARCH_TASK = {"search", "searchTask"};
	private static final String[] DEFAULT_CLEAR_ATTR = {"clear","clearAttr"};
	private static final String[] DEFAULT_DELETE_TASK = {"delete","deleteTask"};
	private static final String[] DEFAULT_VIEW_TASK = {"view","viewTask"};
	private static final String[] DEFAULT_EDIT_TASK = {"edit","editTask"};
	private static final String[] DEFAULT_ADD_TASK = {"add","addTask"};
	public static final String[] DEFAULT_SET_PATH = {"saveTo"};
	
	private static final String[][] DEFAULT_WORD_SET = {	DEFAULT_ADD_TASK, 		DEFAULT_EDIT_TASK,
														DEFAULT_VIEW_TASK, 		DEFAULT_DELETE_TASK,
														DEFAULT_CLEAR_ATTR,		DEFAULT_SEARCH_TASK, 
														DEFAULT_UNDO_TASK, 		DEFAULT_REDO_TASK,
														DEFAULT_MARK_TASK, 		DEFAULT_ADD_SHORTCUT, 
														DEFAULT_VIEW_SHORTCUT, 	DEFAULT_DELETE_SHORTCUT, 
														DEFAULT_RESET_SHORTCUT, DEFAULT_ADD_TEMP, 
														DEFAULT_EDIT_TEMP, 		DEFAULT_VIEW_TEMP, 
														DEFAULT_USE_TEMP, 		DEFAULT_DELETE_TEMP, 
														DEFAULT_RESET_TEMP, 	DEFAULT_HELP,
														DEFAULT_SET_PATH
													};
	
	private static final int INDEX_HELP 				= 19;
	private static final int INDEX_SET_PATH 			= 20;
	private static final int INDEX_RESET_SHORTCUT 	= 12;
	
	private static final int[] UNCHANGEABLE_KEYWORD = { 	INDEX_HELP, 
															INDEX_RESET_SHORTCUT, 
															INDEX_SET_PATH
													};
	
	private static final String[] RESERVED_WORDS = {"at","location","from","datefrom","to","dateto","on",
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
	
	
	private ArrayList<ArrayList<String>> userShortcuts;
	private SystemHandler system;
	
	public Shortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < KEYWORDS.length; ++i) {
			userShortcuts.add(new ArrayList<String>());
		}
	}
	

	/**
	 * It set the system path so that it can call the system Handler that governs it to 
	 * fetch data from other components when required
	 * @param system	The system handler that governs this shortcut manager
	 */
	public void setSystemPath(SystemHandler system) {
		this.system = system;
	}
	
	
	/**
	 * @param command	The string array of command to be executed
	 * @return			Arrays of string arrays that are demanded by command
	 * @throws IllegalArgumentException		Invalid instruction has been demanded or command violates 
	 * 										the constraints set by shortcut manager
	 * @throws NoSuchElementException		Demanded shortcut from command does not exist
	 */
	public String[][] processShortcutCommand(String[] command) 
			throws NoSuchElementException, IllegalArgumentException {
		
		String[][] results = new String[RESULT_SIZE_DEFAULT][];

		switch(getCommandType(command[INDEX_COMMAND])) {
			case addShortcut:
				verifyCommand(command, ADD_SHORTCUT_ARRAY_USED_SIZE);
				results[RESULT_FIRST] = insertShortcut(command[INDEX_NEW_SHORTCUT],
											command[INDEX_REFERED_SHORTCUT]);
				writeOutToFile();
				break;
				
			case deleteShortcut:
				verifyCommand(command, DELETE_SHORTCUT_ARRAY_USED_SIZE);
				results[RESULT_FIRST] = removeShortcut(command[INDEX_DELETING_SHORTCUT]);
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
					throw new IllegalArgumentException(MSG_ERR_CORRUPTED_SAVED_FILE);
				}
				
		}
		return results; 
	}
	
	
	/**
	 * 	This method calls system handler to initiate write out to storage. 
	 *  It is called when there are changes made to shortcuts' data
	 */
	private void writeOutToFile() {
		String[][] shortcuts = new String[KEYWORDS.length][];
		for(int i = 0; i < KEYWORDS.length; ++i) {
			shortcuts[i] = new String[userShortcuts.get(i).size()];
			userShortcuts.get(i).toArray(shortcuts[i]);
		}
		
		system.writeShortcutToFile(shortcuts);
		
	}
	
	/**
	 * This method initialize shortcut by executing commands issued by storage
	 * @param command					The string array of command to be executed
	 * @throws NumberFormatException 	The save file is corrupted and the index to be referred is not identified
	 */
	private void addShortcutInit(String[] command) throws NumberFormatException {
		int row = Integer.parseInt(command[INDEX_INIT_REFERED_ROW]);
		
		assert(row < KEYWORDS.length);
		assert(row >= 0);
		
		ArrayList<String> toBeAddedInto = userShortcuts.get(row);

		toBeAddedInto.add(command[INDEX_NEW_SHORTCUT]);
	}

	
	/**
	 * @param command	Customized Command to be matched with system recognized command
	 * @return			The command that matches the customized command, null if not exist
	 */
	public String keywordMatching(String command) {
		int matchingIndex = searchMatching(command);
		if(matchingIndex > -1) {
			return KEYWORDS[matchingIndex];
		}
		else {
			return null;
		}
	}
	
	
	
	/**
	 * @param command					String of Command type to be executed
	 * @return							Type of command to be executed by shortcut manager
	 * @throws IllegalArgumentException	Type of command entered is not recognized by shortcut manager
	 */
	public static COMMAND_TYPE_SHORTCUT getCommandType(String command) 
			throws IllegalArgumentException {
        try{
        	return COMMAND_TYPE_SHORTCUT.valueOf(command);
        } catch (IllegalArgumentException e) {
        	throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
        			e.getMessage().substring(39)));
        }
        
	}
	
	/**
	 * 	This method reset shortcut list to default set
	 */
	private void resetShortcut() {
		userShortcuts = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < KEYWORDS.length; ++i) {
			userShortcuts.add(buildDefaultKeyword(i));
		}
	}
	
	/**
	 * This method reads shortcuts from default set and build them
	 * @param index		Index of shortcut default set
	 * @return			Default Shortcuts that correspond to the index 
	 */
	private ArrayList<String> buildDefaultKeyword(int index) {
		ArrayList<String> customizedWord = new ArrayList<String>();
		String[] defaultwords = DEFAULT_WORD_SET[index];
		for(int i = 0; i < defaultwords.length; ++i) {
			customizedWord.add(defaultwords[i]);
		}
		return customizedWord;
	}

	/**
	 * @return	The cloned list of shortcuts
	 */
	private String[][] viewShortcuts() {
		return cloneShortcuts();
	}
	
	/**
	 * @param shortcut					Shortcut to be removed
	 * @return							Shortcut that function the same as the removed shortcut
	 * @throws NoSuchElementException	The shortcut to be deleted is not found	
	 * @throws IllegalArgumentException	The shortcut to be deleted is a default or the corresponded keyword is at minimum capacity
	 */
	private String[] removeShortcut(String shortcut) throws NoSuchElementException, IllegalArgumentException {
		
		int index = getShortcutMatchingIndex(shortcut);
		if(index == INDEX_NOT_FOUND) {
			throw new NoSuchElementException(String.format(MSG_ERR_SHORTCUT_NOT_EXIST, shortcut));
			
		} else if(isShortcutKeyAtMinimumCapacity(index)){
			//Wrong type
			throw new IllegalArgumentException(String.format(MSG_ERR_MINIMUM_SHORTCUT_NUMBER, shortcut));
			
		} else if(isDefaultShortcut(shortcut)) {
			//Wrong type
			throw new IllegalArgumentException(String.format(MSG_ERR_DEFAULT_KEYWORD_DELETE, shortcut));
			
		} else {
			removeShortcutfromUserList(index, shortcut);
			
			String[] result = constructOutputString(index, shortcut);
			return result;
		}
		
	}

	/**
	 * @param index		Index of keyword where the shortcut to be removed lies in
	 * @param shortcut	Shortcut word to be removed
	 * @return			True if the shortcut has been removed successfully
	 */
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
	
	
	/**
	 * @param shortcut	Shortcut to be matched with keyword
	 * @return			Index of keyword where the shortcut corresponds to, -1 if not found
	 */
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
	
	/**
	 * @param newShortcut				New shortcut to represent the keyword
	 * @param originShortcut			Original shortcut to let new shortcut matches with the same keyword
	 * @return							Output result after successfully added, null if fail to add
	 * @throws NoSuchElementException	The keyword to match with is not found.
	 * @throws IllegalArgumentException	There are some violations in constraints of shortcuts
	 */
	private String[] insertShortcut(String newShortcut, String originShortcut) 
			throws NoSuchElementException, IllegalArgumentException {
		int indexBelongTo = searchMatching(originShortcut);
		
		verifyInputApprioriateness(newShortcut, originShortcut, indexBelongTo);
		
		addShortcut(newShortcut, indexBelongTo);
		String[] result = constructOutputString(indexBelongTo, originShortcut, newShortcut);
		return result;	
		
	}


	/**
	 * @param newShortcut				New shortcut to represent the keyword
	 * @param originShortcut			Original shortcut to let new shortcut matches with the same keyword
	 * @param indexBelongTo				Index of keyword to be matched
	 * @throws NoSuchElementException	There is no keywords where original Shortcut can match to
	 * @throws IllegalArgumentException	There are some violations in constraints of shortcuts
	 */
	private void verifyInputApprioriateness(String newShortcut, String originShortcut,
			int indexBelongTo) throws NoSuchElementException, IllegalArgumentException {
		
		if(!isKeyWords(indexBelongTo)) {
			throw new NoSuchElementException(String.format(MSG_ERR_SHORTCUT_NOT_EXIST, originShortcut));
			
		} else if(isKeyWords(newShortcut)) {
			throw new IllegalArgumentException(String.format(MSG_ERR_KEYWORD_EXIST, newShortcut));
			
		} else if (isReservedWord(newShortcut)) {
			throw new IllegalArgumentException(String.format(MSG_ERR_DEFAULT_KEYWORD_DELETE, newShortcut));
			
		} else if(isUnchangeable(indexBelongTo)) {
			throw new IllegalArgumentException(MSG_ERR_UNCHANGEABLE_KEYWORD);
			
		} else if (isWordLengthInappropriate(newShortcut)) {
			throw new IllegalArgumentException(String.format(MSG_ERR_MINIMUM_LENGTH, newShortcut));
			
		} else if(isShortcutAtMaximumCapacity(indexBelongTo)) {
			throw new IllegalArgumentException(String.format(MSG_ERR_MAX_CAPACITY, newShortcut));
			
		}
		
	}
	
	/**
	 * @param index		Index of keyword to be matched with
	 * @param deleted	Deleted shortcut
	 * @return			Array of string that shows the index of deleted and deleted shortcut 
	 */
	private String[] constructOutputString(int index, String deleted) {
		String[] result = {Integer.toString(index), deleted};
		return result;
	}
	
	/**
	 * @param index			Index of keyword to be matched with
	 * @param original		Shortcut that corresponds to the keyword
	 * @param newlyAdded	New shortcut that corresponds to the keyword
	 * @return				Array of string that shows the index of keywords
	 */
	private String[] constructOutputString(int index, String original, String newlyAdded) {
		ArrayList<String> shortcuts = userShortcuts.get(index);
		String[] result = new String[shortcuts.size() + 1];
		result[0] = Integer.toString(index);
		for(int i = 0 ; i < shortcuts.size(); ++i) {
			result[i + 1] = shortcuts.get(i);
		}
		return result;
	}

	private boolean isWordLengthInappropriate(String newShortcut) {
		return newShortcut.length() < MINIMUM_LENGTH || newShortcut.length() > MAXIMUM_LENGTH;
	}


	private boolean isShortcutAtMaximumCapacity(int index) {
		return userShortcuts.get(index).size() > MAXIMUM_CAPACITY;
	}

	private boolean isShortcutKeyAtMinimumCapacity(int index) {
		return userShortcuts.get(index).size() < MINIMUM_CAPACITY;
	}

	/**
	 * @param newShortcut		Shortcut to be added
	 * @param belongTo			Index of keyword where new shortcut to be added to.
	 */
	private void addShortcut(String newShortcut, int belongTo) {
		ArrayList<String> toBeAddedInto = userShortcuts.get(belongTo);
		toBeAddedInto.add(newShortcut);
		
	}
	
	/**
	 * @param index		Index that matches the keywords
	 * @return			True if the index matches to a keyword
	 */
	private boolean isKeyWords(int index) {
		return (index > INDEX_NOT_FOUND);
	}
	
	/**
	 * @param shortcutWord	Shortcut to match the keyword
	 * @return			True if the shortcut matches a keyword
	 */
	private boolean isKeyWords(String shortcutWord) {
		int matchingIndex = searchMatching(shortcutWord);
		return matchingIndex > INDEX_NOT_FOUND;
	}

	/**
	 * @param command	Shortcut to match the default shortcut
	 * @return			True if the shortcut matches a default shortcut
	 */
	private boolean isDefaultShortcut(String shortcut) {
		for(int i = 0; i < DEFAULT_WORD_SET.length; ++i) {
			String[] defaultWords = DEFAULT_WORD_SET[i];
			
			for(int j = 0; j < defaultWords.length; ++j) {
				
				if(shortcut.equalsIgnoreCase(defaultWords[j])) {
					return true;
				}
				
			}
			
		}
		return false;
	}
	
	/**
	 * @param index			Index of keyword to be manipulated 
	 * @return				True if it is a unchangeable keyword
	 */
	private boolean isUnchangeable(int index) {
		for(int i = 0; i < UNCHANGEABLE_KEYWORD.length; ++i) {
			if(index == UNCHANGEABLE_KEYWORD[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param shortcutWord	Shortcut to be matched with keyword
	 * @return				Index of keywords where the shortcut matches with
	 */
	private int searchMatching (String shortcutWord) {
		for(int index = 0; index < userShortcuts.size(); ++index) {
			ArrayList<String> shortcutsList = userShortcuts.get(index);
			
			for(int j = 0; j < shortcutsList.size(); ++j) {
				if(isTheMatchingWord(shortcutWord, shortcutsList.get(j))) {
					return index;
				}
				
			}
			
		}
		return INDEX_NOT_FOUND;
	}
	
	/**
	 * @param shortcutWord		Shortcut to be matched to
	 * @param matching			Shortcut to be matched with
	 * @return					True if both are the same shortcut
	 */
	private boolean isTheMatchingWord(String shortcutWord, String matching) {
		return shortcutWord.equalsIgnoreCase(matching);
	}
	
	/**
	 * @param shortcutWord		Shortcut to be checked
	 * @return					True if the shortcut is a default shortcut
	 */
	private boolean isReservedWord(String shortcutWord) {
		for(int i = 0; i < RESERVED_WORDS.length; ++i) {
			if(shortcutWord.equalsIgnoreCase(RESERVED_WORDS[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return	A clone of shortcuts list
	 */
	private String[][] cloneShortcuts() {
		String[][] cloneList = new String[KEYWORDS.length][];
		
		for(int i = 0; i < userShortcuts.size(); ++i) {
			
			ArrayList<String> shortcuts = userShortcuts.get(i);
			
			String[] clonedArray = new String[shortcuts.size() + 1];
			clonedArray[0] = Integer.toString(i);
			
			for(int j = 0; j < shortcuts.size(); ++j) {
				clonedArray[j + 1] = shortcuts.get(j);
			}
			
			cloneList[i] = clonedArray;
		}
		
		return cloneList;
	}

	/**
	 * This method verify the command field by check any invalid fields used for particular type of command
	 * @param command			Shortcut command to be verified
	 * @param lengthToCheck		Number of field to be checked
	 */
	private void verifyCommand(String[] command, int lengthToCheck) {
		assert(command.length == ARRAY_SIZE_SHORTCUT);
		
		for(int i = 0; i < lengthToCheck; ++i) {
			assert(command[i] != null);
		}
		
	}
	
}