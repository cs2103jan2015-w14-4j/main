import java.util.ArrayList;
import java.util.NoSuchElementException;

//@author A0108385B
public class KeywordManager {

    private static final String MSG_ERR_UNCHANGEABLE_KEYWORD = "This keyword cannot be edited.";
    private static final String MSG_ERR_CORRUPTED_SAVED_FILE = "Keyword saved file has been corrupted";
    private static final String MSG_ERR_MINIMUM_LENGTH = "\"%s\"is too short. Please choose a word that is at least 3 characters long to be used as a keyword.";
    private static final String MSG_ERR_MAX_CAPACITY = "\"%s\" cannot be added to this function as each function can only have a maximum of 10 keywords. Please delete one of the keywords if you want to add another one.";
    private static final String MSG_ERR_KEYWORD_EXIST = "\"%s\" is already being used as a keyword by Flexi Tracker.";
    private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command exists in Keyword Manager: %1$s";
    private static final String MSG_ERR_DEFAULT_KEYWORD_DELETE = "\"%s\" is a default system-defined keyword. It cannot be changed or deleted.";
    private static final String MSG_ERR_MINIMUM_KEYWORD_NUMBER = "Each function must have at least one keyword. \"%s\" cannot be removed.";
    private static final String MSG_ERR_KEYWORD_NOT_EXIST = "\"%s\" does not exist in the keyword list";

    //Command that are recognized by system 
    private static final String[] KEYWORDS = {	"addTask", 		"editTask",		"viewTask",
        "deleteTask", 	"clearAttr", 	"searchTask", 
        "undoTask", 	"redoTask", 	"markTask",
        "addKeyword", 	"viewKeyword", "deleteKeyword",
        "resetKeyword", "addTemplate", "editTemplate", 
        "viewTemplate", "useTemplate", 	"deleteTemplate", 
        "resetTemplate", "help", "saveTo"}; 

    //Default commands
    private static final String[] RESERVED_WORDS = {"at","location","from","datefrom","to","dateto","on",
        "before","by","detail","status","name","title"};

    private static final String[] DEFAULT_HELP = {"help"};
    private static final String[] DEFAULT_RESET_TEMP = {"resetTemplate", "resetTemp"};
    private static final String[] DEFAULT_DELETE_TEMP = {"deleteTemplate","deleteTemp"};
    private static final String[] DEFAULT_USE_TEMP = {"useTemplate", "useTemp"};
    private static final String[] DEFAULT_VIEW_TEMP = {"viewTemplate","viewTemp"};
    private static final String[] DEFAULT_EDIT_TEMP = {"editTemplate","editTemp"};
    private static final String[] DEFAULT_ADD_TEMP = {"addTemplate","addTemp"};
    private static final String[] DEFAULT_RESET_KEYWORD = {"resetShortcut","resetKeyword"};
    private static final String[] DEFAULT_DELETE_KEYWORD = {"deleteShortcut","deleteKeyword"};
    private static final String[] DEFAULT_VIEW_KEYWORD = {"viewShortcut","viewKeyword"};
    private static final String[] DEFAULT_ADD_KEYWORD = {"addShortcut","addKeyword"};
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
        DEFAULT_MARK_TASK, 		DEFAULT_ADD_KEYWORD, 
        DEFAULT_VIEW_KEYWORD, 	DEFAULT_DELETE_KEYWORD, 
        DEFAULT_RESET_KEYWORD, DEFAULT_ADD_TEMP, 
        DEFAULT_EDIT_TEMP, 		DEFAULT_VIEW_TEMP, 
        DEFAULT_USE_TEMP, 		DEFAULT_DELETE_TEMP, 
        DEFAULT_RESET_TEMP, 	DEFAULT_HELP,
        DEFAULT_SET_PATH
    };

    private static final int RESULT_FIRST = 0;
    private static final int RESULT_SIZE_DEFAULT = 1;

    private static final int INDEX_HELP = 19;
    private static final int INDEX_SET_PATH = 20;
    private static final int INDEX_RESET_KEYWORD = 12;

    private static final int[] UNCHANGEABLE_KEYWORD = { 	INDEX_HELP, 
        INDEX_RESET_KEYWORD, 
        INDEX_SET_PATH
    };

    private static final int ARRAY_SIZE_KEYWORD = 3;
    private static final int ADD_KEYWORD_ARRAY_USED_SIZE = 3;
    private static final int ADD_KEYWORD_INIT_ARRAY_USED_SIZE = 3;
    private static final int VIEW_KEYWORD_ARRAY_USED_SIZE = 1;
    private static final int DELETE_KEYWORD_ARRAY_USED_SIZE = 2;
    private static final int RESET_KEYWORD_ARRAY_USED_SIZE = 1;

    private static final int INDEX_DELETING_KEYWORD = 1;
    private static final int INDEX_REFERED_KEYWORD = 2;
    private static final int INDEX_NEW_KEYWORD = 1;
    private static final int INDEX_INIT_REFERED_ROW = 2;

    private static final int MINIMUM_LENGTH = 3;
    private static final int MAXIMUM_LENGTH = 15;
    private static final int MINIMUM_CAPACITY = 1;
    private static final int MAXIMUM_CAPACITY = 10;

    private static final int INDEX_NOT_FOUND = -1;
    private static final int INDEX_COMMAND = 0;


    private ArrayList<ArrayList<String>> userKeywords;
    private SystemHandler system;


    public KeywordManager() {
        userKeywords = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < KEYWORDS.length; ++i) {
            userKeywords.add(new ArrayList<String>());
        }
    }



    /**
     * It set the system path so that it can call the system Handler that governs it to 
     * fetch data from other components when required
     * @param system	The system handler that governs this keyword manager
     */
    public void setSystemPath(SystemHandler system) {
        this.system = system;
    }



    /**
     * This method receives commands and process them accordingly
     * @param command	The string array of command to be executed
     * @return			Arrays of string arrays that are demanded by command
     * @throws IllegalArgumentException		Invalid instruction has been demanded or command violates 
     * 										the constraints set by keyword manager
     * @throws NoSuchElementException		Demanded keyword from command does not exist
     */
    public String[][] processKeywordCommand(String[] command) 
            throws NoSuchElementException, IllegalArgumentException {
        String[][] results = new String[RESULT_SIZE_DEFAULT][];

        switch (getCommandType(command[INDEX_COMMAND])) {
        case addKeyword:
            verifyCommand(command, ADD_KEYWORD_ARRAY_USED_SIZE);
            results[RESULT_FIRST] = insertKeyword(command[INDEX_NEW_KEYWORD],
                    command[INDEX_REFERED_KEYWORD]);
            writeOutToFile();
            break;

        case deleteKeyword:
            verifyCommand(command, DELETE_KEYWORD_ARRAY_USED_SIZE);
            results[RESULT_FIRST] = removeKeyword(command[INDEX_DELETING_KEYWORD]);
            writeOutToFile();
            break;

        case resetKeyword:
            verifyCommand(command, RESET_KEYWORD_ARRAY_USED_SIZE);
            resetKeyword();
            results = viewKeywords();
            writeOutToFile();
            break;

        case viewKeyword:
            verifyCommand(command, VIEW_KEYWORD_ARRAY_USED_SIZE);
            results = cloneKeywords();
            break;

        case addKeywordInit:
            try {
                verifyCommand(command, ADD_KEYWORD_INIT_ARRAY_USED_SIZE);
                addKeywordInit(command);
                break;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(MSG_ERR_CORRUPTED_SAVED_FILE);
            }
        }

        return results; 
    }



    /**
     * 	This method calls system handler to initiate write out to storage. 
     *  It is called when there are changes made to keywords' data
     */
    private void writeOutToFile() {
        String[][] keywords = new String[KEYWORDS.length][];

        for (int i = 0; i < KEYWORDS.length; ++i) {
            keywords[i] = new String[userKeywords.get(i).size()];
            userKeywords.get(i).toArray(keywords[i]);
        }

        system.writeKeywordToFile(keywords);
    }


    /**
     * This method initialize keyword by executing commands issued by storage
     * @param command					The string array of command to be executed
     * @throws NumberFormatException 	The save file is corrupted and the index to be referred is not identified
     */
    private void addKeywordInit(String[] command) throws NumberFormatException {
        int row = Integer.parseInt(command[INDEX_INIT_REFERED_ROW]);

        assert(row < KEYWORDS.length);
        assert(row >= 0);

        ArrayList<String> toBeAddedInto = userKeywords.get(row);

        toBeAddedInto.add(command[INDEX_NEW_KEYWORD]);
    }


    /**
     * This method returns the system keyword that matches the input command
     * @param command	Customized Command to be matched with system recognized command
     * @return			The command that matches the customized command, null if not exist
     */
    public String keywordMatching(String command) {
        int matchingIndex = searchMatching(command);

        if (matchingIndex > -1) {
            return KEYWORDS[matchingIndex];
        } else {
            return null;
        }
    }


    /**
     * This method returns keyword command type that matches the string command 
     * @param command					String of Command type to be executed
     * @return							Type of command to be executed by keyword manager
     * @throws IllegalArgumentException	Type of command entered is not recognized by keyword manager
     */
    public static COMMAND_TYPE_KEYWORD getCommandType(String command) 
            throws IllegalArgumentException {
        try{
            return COMMAND_TYPE_KEYWORD.valueOf(command);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
                    e.getMessage().substring(39)));
        }   
    }


    /**
     * 	This method reset keyword list to default set
     */
    private void resetKeyword() {
        userKeywords = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < KEYWORDS.length; ++i) {
            userKeywords.add(buildDefaultKeyword(i));
        }
    }


    /**
     * This method reads keywords from default set and build them
     * @param index		Index of keyword default set
     * @return			Default Keywords that correspond to the index 
     */
    private ArrayList<String> buildDefaultKeyword(int index) {
        ArrayList<String> customizedWord = new ArrayList<String>();
        String[] defaultwords = DEFAULT_WORD_SET[index];

        for (int i = 0; i < defaultwords.length; ++i) {
            customizedWord.add(defaultwords[i]);
        }

        return customizedWord;
    }


    /**
     * @return	The cloned list of keywords
     */
    private String[][] viewKeywords() {
        return cloneKeywords();
    }


    /**
     * @param keyword					Keyword to be removed
     * @return							Keyword that function the same as the removed shortcut
     * @throws NoSuchElementException	The keyword to be deleted is not found	
     * @throws IllegalArgumentException	The keyword to be deleted is a default or the corresponded keyword is at minimum capacity
     */
    private String[] removeKeyword(String keyword) throws NoSuchElementException, IllegalArgumentException {
        int index = getShortcutMatchingIndex(keyword);

        if (index == INDEX_NOT_FOUND) {
            throw new NoSuchElementException(String.format(MSG_ERR_KEYWORD_NOT_EXIST, keyword));

        } else if (isKeywordAtMinimumCapacity(index)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_MINIMUM_KEYWORD_NUMBER, keyword));

        } else if (isDefaultKeyword(keyword)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_DEFAULT_KEYWORD_DELETE, keyword));
        } else {
            removeKeywordfromUserList(index, keyword);

            String[] result = constructOutputString(index, keyword);
            return result;
        }
    }


    /**
     * @param index		Index of system keyword where the keyword to be removed lies in
     * @param keyword	Keyword to be removed
     * @return			True if the keyword has been removed successfully
     */
    private boolean removeKeywordfromUserList(int index, String keyword) {
        ArrayList<String> userDefinedKeyword = userKeywords.get(index);

        for (int i = 0; i < userDefinedKeyword.size(); ++i) {
            if (userDefinedKeyword.get(i).equals(keyword)) {
                userDefinedKeyword.remove(i);
                return true;
            }
        }

        return false;
    }


    /**
     * @param keyword	Shortcut to be matched with keyword
     * @return			Index of keyword where the shortcut corresponds to, -1 if not found
     */
    private int getShortcutMatchingIndex(String keyword) {
        for (int i = 0; i < userKeywords.size(); ++i) {
            ArrayList<String> customizedKeyword = userKeywords.get(i);
            for (int j = 0; j < customizedKeyword.size(); ++j) {
                if (keyword.equals(customizedKeyword.get(j))) {
                    return i;
                }
            }
        }

        return INDEX_NOT_FOUND;
    }


    /**
     * @param newKeyword				New keyword to represent the keyword
     * @param originKeyword				Original keyword to let new keyword matches with the same keyword
     * @return							Output result after successfully added, null if fail to add
     * @throws NoSuchElementException	The keyword to match with is not found.
     * @throws IllegalArgumentException	There are some violations in constraints of keywords
     */
    private String[] insertKeyword(String newKeyword, String originKeyword) 
            throws NoSuchElementException, IllegalArgumentException {
        int indexBelongTo = searchMatching(originKeyword);

        verifyInputApprioriateness(newKeyword, originKeyword, indexBelongTo);

        addKeyword(newKeyword, indexBelongTo);
        String[] result = constructOutputString(indexBelongTo, originKeyword, newKeyword);

        return result;	
    }


    /**
     * This method verify all restrictions before allowing actions on the keyword
     * @param newKeyword				New keyword to represent the keyword
     * @param originKeyword				Original keyword to let new keyword matches with the same keyword
     * @param indexBelongTo				Index of keyword to be matched
     * @throws NoSuchElementException	There is no keywords where original keyword can match to
     * @throws IllegalArgumentException	There are some violations in constraints of keywords
     */
    private void verifyInputApprioriateness(String newKeyword, String originKeyword,
            int indexBelongTo) throws NoSuchElementException, IllegalArgumentException {
        if (!isKeyWords(indexBelongTo)) {
            throw new NoSuchElementException(String.format(MSG_ERR_KEYWORD_NOT_EXIST, originKeyword));
        } else if (isKeyWords(newKeyword)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_KEYWORD_EXIST, newKeyword));
        } else if (isReservedWord(newKeyword)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_DEFAULT_KEYWORD_DELETE, newKeyword));
        } else if (isUnchangeable(indexBelongTo)) {
            throw new IllegalArgumentException(MSG_ERR_UNCHANGEABLE_KEYWORD);
        } else if (isWordLengthInappropriate(newKeyword)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_MINIMUM_LENGTH, newKeyword));
        } else if (isKeywordAtMaximumCapacity(indexBelongTo)) {
            throw new IllegalArgumentException(String.format(MSG_ERR_MAX_CAPACITY, newKeyword));
        }
    }


    /**
     * This method construct the output as following: First index is the index of System keyword, 
     * second index is the deleted keyword
     * @param index		Index of keyword to be matched with
     * @param deleted	Deleted keyword
     * @return			Array of string that shows the index of deleted and deleted keyword 
     */
    private String[] constructOutputString(int index, String deleted) {
        String[] result = {Integer.toString(index), deleted};

        return result;
    }


    /**
     * This method construct the output string to be read by UI component.
     * The format is as follow: First index will be the index of System Keyword, followed by customized keyword
     * @param index			Index of keyword to be matched with
     * @param originalKey		Original Keyword that corresponds to the system keyword
     * @param newlyAddedKey	New keyword that corresponds to original
     * @return				Array of string that shows the index of keywords
     */
    private String[] constructOutputString(int index, String originalKey, String newlyAddedKey) {
        ArrayList<String> shortcuts = userKeywords.get(index);
        String[] result = new String[shortcuts.size() + 1];
        result[0] = Integer.toString(index);

        for (int i = 0 ; i < shortcuts.size(); ++i) {
            result[i + 1] = shortcuts.get(i);
        }

        return result;
    }


    private boolean isWordLengthInappropriate(String newKeyword) {
        return newKeyword.length() < MINIMUM_LENGTH || newKeyword.length() > MAXIMUM_LENGTH;
    }


    private boolean isKeywordAtMaximumCapacity(int index) {
        return userKeywords.get(index).size() > MAXIMUM_CAPACITY;
    }


    private boolean isKeywordAtMinimumCapacity(int index) {
        return userKeywords.get(index).size() < MINIMUM_CAPACITY;
    }


    /**
     * @param newKeyword		Keyword to be added
     * @param belongTo			Index of keyword where new keyword to be added to.
     */
    private void addKeyword(String newKeyword, int belongTo) {
        ArrayList<String> toBeAddedInto = userKeywords.get(belongTo);
        toBeAddedInto.add(newKeyword);
    }


    /**
     * @param index		Index that matches the keywords
     * @return			True if the index matches to a keyword
     */
    private boolean isKeyWords(int index) {
        return (index > INDEX_NOT_FOUND);
    }


    /**
     * @param keyword	Keyword to match the keyword
     * @return			True if the keyword matches a system keyword
     */
    private boolean isKeyWords(String keyword) {
        int matchingIndex = searchMatching(keyword);
        return matchingIndex > INDEX_NOT_FOUND;
    }


    /**
     * @param command	Keyword to match the default keyword
     * @return			True if the keyword matches a default keyword
     */
    private boolean isDefaultKeyword(String keyword) {
        for (int i = 0; i < DEFAULT_WORD_SET.length; ++i) {
            String[] defaultWords = DEFAULT_WORD_SET[i];

            for (int j = 0; j < defaultWords.length; ++j) {
                if (keyword.equalsIgnoreCase(defaultWords[j])) {
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
        for (int i = 0; i < UNCHANGEABLE_KEYWORD.length; ++i) {
            if (index == UNCHANGEABLE_KEYWORD[i]) {
                return true;
            }
        }

        return false;
    }


    /**
     * @param keyword	Shortcut to be matched with keyword
     * @return				Index of keywords where the shortcut matches with
     */
    private int searchMatching (String keyword) {
        for (int index = 0; index < userKeywords.size(); ++index) {
            ArrayList<String> keywordsList = userKeywords.get(index);

            for (int j = 0; j < keywordsList.size(); ++j) {
                if (isTheMatchingWord(keyword, keywordsList.get(j))) {
                    return index;
                }
            }
        }

        return INDEX_NOT_FOUND;
    }


    /**
     * @param keyword			Keyword to be matched to
     * @param matching			Keyword to be matched with
     * @return					True if both are the same keyword
     */
    private boolean isTheMatchingWord(String keyword, String matching) {
        return keyword.equalsIgnoreCase(matching);
    }


    /**
     * @param keyword			keyword to be checked
     * @return					True if the keyword is a reserved keyword
     */
    private boolean isReservedWord(String keyword) {
        for (int i = 0; i < RESERVED_WORDS.length; ++i) {
            if (keyword.equalsIgnoreCase(RESERVED_WORDS[i])) {
                return true;
            }
        }

        return false;
    }


    /**
     * @return	A clone of keyword list
     */
    private String[][] cloneKeywords() {
        String[][] cloneList = new String[KEYWORDS.length][];

        for (int i = 0; i < userKeywords.size(); ++i) {
            ArrayList<String> keywords = userKeywords.get(i);

            String[] clonedArray = new String[keywords.size() + 1];
            clonedArray[0] = Integer.toString(i);

            for (int j = 0; j < keywords.size(); ++j) {
                clonedArray[j + 1] = keywords.get(j);
            }
            cloneList[i] = clonedArray;
        }

        return cloneList;
    }


    /**
     * This method verify the command field by check any invalid fields used for particular type of command
     * @param command			Keyword command to be verified
     * @param lengthToCheck		Number of field to be checked
     */
    private void verifyCommand(String[] command, int lengthToCheck) {
        assert(command.length == ARRAY_SIZE_KEYWORD);

        for (int i = 0; i < lengthToCheck; ++i) {
            assert(command[i] != null);
        }
    }
}