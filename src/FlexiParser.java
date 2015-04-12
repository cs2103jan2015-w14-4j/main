import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

//@author A0116514N
public class FlexiParser {
    private static final String MSG_ERR_INTERNAL_PARSER_IMPLEMENTATION = "\"%s\" is not implemented in parser yet.";
    private static final String MSG_ERR_UNRECOGNIZED_COMMAND = "\"%s\" is not a command recognized by Flexi Tracker. Type \"viewKeyword\" to see all the keywords.";
    private static final String MSG_ERR_NOT_DATE = "The date/time you have entered using the keyword representing date \"%s\" is invalid, please enter a new one.";
    private static final String MSG_ERR_TITLE_EMPTY = "Task cannot be created due to the lack of a title, please enter the task again.";
    private static final String MSG_ERR_ID_EDIT = "Task cannot be edited as task ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_EDIT_TEMPLATE = "Template cannot be edited as task ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_USE_TEMPLATE = "Template cannot be utilized as task ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_DELETE_TASK = "Task cannot be deleted as ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_DELETE_KEYWORD = "Keyword cannot be deleted as ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_DELETE_TEMPLATE = "Template cannot be deleted as ID was not specified, please try again while entering a correct ID.";
    private static final String MSG_ERR_ID_MARK_TASK = "Task cannot be marked as ID is not specified";
    private static final String MSG_ERR_ID_ADD_KEYWORD = "To add a keyword please enter one old and one new keyword: [new keyword] onto [old keyword].";
    private static final String MSG_ERR_ID_CLEAR_ATTR = "Attributes cannot be cleared as ID not specified.";
    private static final String MSG_ERR_ID_SEARCH = "Please specify a search term." ;
    private static final String MSG_ERR_QUOTE = "The misuse of quotation marks is found, please enter your task again.";
    private static final String MSG_ERR_DATE_ENTERED = "The date that you have specified is not accepted please enter the task again.";
    private static final String MSG_ERR_ADD_TEMPLATE = "There is insufficient arguments needed to add the task.";

    private static final String COMMAND_KEYWORD = "Keyword";
    private static final String COMMAND_SAVE = "save";
    private static final String COMMAND_HELP = "help";

    private static final String TID_NOT_EXIST = null;
    private static final String NOT_EXIST = null;
    private static final String[] ARRAY_NOT_EXIST = null;

    private static final int COMMAND_TYPE_INDEX = 0;
    private static final int TASK_ID_INDEX = 1;
    private static final int TASK_NAME_INDEX = 2;
    private static final int TASK_DATE_FROM_INDEX = 3;
    private static final int TASK_DATE_TO_INDEX = 4;
    private static final int TASK_DEADLINE_INDEX = 5;
    private static final int TASK_LOCATION_INDEX = 6;
    private static final int TASK_DETAILS_INDEX = 7;
    private static final int TASK_STATUS_INDEX = 8;

    private static final int START_TITLE_INDEX = 1;
    private static final int START_STATUS_INDEX = 7;
    private static final int TO_ADD_INDEX = 1;
    private static final int DATE_INDEX = 0;
    private static final int KEYWORD_KEY = 1;
    private static final int STORE_SAVE_INDEX = 1;

    private static final int KEY_NOT_PRESENT = -1;

    private static final int DAY_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int YEAR_INDEX = 2;

    private static final int NEW_KEYWORD_INDEX = 1;
    private static final int OLD_KEYWORD_INDEX = 2;
    private static final int CORRECT_LENGTH_DELETE_KEYWORD = 2;
    private static final int CORRECT_LENGTH_DELETE_TEMPLATE = 2;
    private static final int CORRECT_LENGTH_EDIT_TEMPLATE = 2;
    private static final int CORRECT_LENGTH_USE_TEMPLATE = 2;
    private static final int CORRECT_LENGTH_CLEAR_ATTR = 2;
    private static final int CORRECT_LENGTH_MARK_TASK = 2;
    private static final int CORRECT_LENGTH_ADD_TEMPLATE = 4;

    private static final String DATE_FROM = "from";
    private static final String DATE_TO = "to";
    private static final String DATE_ON = "on";
    private static final String VIEW_BY = "by";
    private static final String TEMPLATE_NAME_INDEX = "as";

    private static final String EMPTY_STRING = "";
    private static final String DATE_FORMAT_STRING = "/";
    private static final String DOUBLE_QUOTE_STRING = "\"";

    private static final String[] KEYWORDS_ONE_TASK = {"ID","by","from","to","on","at","det","as"};
    private static final String[] KEYWORDS_TWO_TASK = {"ID","name","datefrom","dateto","before","location","remarks","status"};


    private static final String KEYWORD_KEYWORD = "onto";

    private static final String[] commandArray = {"addTask","editTask","deleteTask","viewTask","searchTask","undoTask","redoTask","addReminder","deleteReminder","addKeyword","deleteKeyword","viewKeyword","resetKeyword",
        "addTemplate","deleteTemplate","viewTemplate","resetTemplate","editTemplate","useTemplate","clearAttr","markTask","saveTo","help"};

    //Index of keywords for switch statements
    private static final int  ADD_TASK_INDEX = 0;
    private static final int  EDIT_TASK_INDEX = 1;
    private static final int  DELETE_TASK_INDEX = 2;
    private static final int  VIEW_TASK_INDEX = 3;
    private static final int  SEARCH_TASK_INDEX = 4;
    private static final int  UNDO_TASK_INDEX = 5;
    private static final int  REDO_TASK_INDEX = 6;
    private static final int  ADD_REMINDER_INDEX = 7;
    private static final int  DELETE_REMINDER_INDEX = 8;
    private static final int  ADD_KEYWORD_INDEX = 9;
    private static final int  DELETE_KEYWORD_INDEX = 10;
    private static final int  VIEW_KEYWORD_INDEX = 11;
    private static final int  RESET_KEYWORD_INDEX = 12;
    private static final int  ADD_TEMPLATE_INDEX = 13;
    private static final int  DELETE_TEMPLATE_INDEX = 14;
    private static final int  VIEW_TEMPLATE_INDEX = 15;
    private static final int  RESET_TEMPLATE_INDEX = 16;
    private static final int  EDIT_TEMPLATE_INDEX = 17;
    private static final int  USE_TEMPLATE_INDEX = 18;
    private static final int  CLEAR_ATTRIBUTE_INDEX = 19;
    private static final int  MARK_TASK_INDEX = 20;
    private static final int  SAVE_FILE_INDEX = 21;
    private static final int  HELP_COMMAND_INDEX = 22;

    private static String[] inputArray;

    public static final int TASK_LENGTH = 9;
    public static final int KEYWORD_LENGTH = 3;
    public static final int CHANGE_LOCATION_LENGTH = 2;
    public static final int HELP_LENGTH = 1;
    private KeywordManager keywords;

    public FlexiParser(KeywordManager keywords) {
        this.keywords = keywords;
    }

    //@author A0116514N
    public String[] parseText(String userInput) throws IllegalArgumentException {
        inputArray = userInput.split("\\s+");
        String command = inputArray[COMMAND_TYPE_INDEX];
        command = keywords.keywordMatching(command);

        if(command == NOT_EXIST) {
            throw new IllegalArgumentException(String.format(MSG_ERR_UNRECOGNIZED_COMMAND, inputArray[COMMAND_TYPE_INDEX]));
        }

        String[] outputArray;
        if(command.contains(COMMAND_SAVE)) {
            outputArray = new String[CHANGE_LOCATION_LENGTH];
        }
        else if(command.contains(COMMAND_HELP)) {
            outputArray = new String[HELP_LENGTH];
        }
        else if(command.contains(COMMAND_KEYWORD)) {
            outputArray = new String[KEYWORD_LENGTH];
        }
        else {		
            outputArray = new String[TASK_LENGTH];
        }
        outputArray[COMMAND_TYPE_INDEX] = command;

        int commandIndex = getCommandIndex(command);

        switch(commandIndex) {
        case ADD_TASK_INDEX:
            addTask(outputArray);
            break;
        case EDIT_TASK_INDEX:
            editTask(outputArray);
            break;
        case DELETE_TASK_INDEX:
            deleteTask(outputArray);
            break;
        case VIEW_TASK_INDEX:
            viewTask(outputArray);
            break;
        case SEARCH_TASK_INDEX:
            searchTask(outputArray);
            break;
        case UNDO_TASK_INDEX:
            break;
        case REDO_TASK_INDEX:
            break;
        case ADD_REMINDER_INDEX:
            break;
        case DELETE_REMINDER_INDEX:
            break;
        case ADD_KEYWORD_INDEX:
            addKeyword(outputArray);
            break;
        case DELETE_KEYWORD_INDEX:
            deleteKeyword(outputArray);
            break;
        case VIEW_KEYWORD_INDEX:
            break;
        case RESET_KEYWORD_INDEX:
            break;
        case ADD_TEMPLATE_INDEX:
            addTemplate(outputArray);
            break;
        case DELETE_TEMPLATE_INDEX:
            deleteTemplate(outputArray);
            break;
        case VIEW_TEMPLATE_INDEX:
            break;
        case RESET_TEMPLATE_INDEX:
            break;
        case EDIT_TEMPLATE_INDEX:
            editTemplate(outputArray);
            break;
        case USE_TEMPLATE_INDEX:
            useTemplate(outputArray);
            break;
        case CLEAR_ATTRIBUTE_INDEX:
            clearAttribute(outputArray);
            break;
        case MARK_TASK_INDEX:
            markTask(outputArray);
            break;
        case SAVE_FILE_INDEX:	
            outputArray[STORE_SAVE_INDEX]= inputArray[STORE_SAVE_INDEX];
            break;
        case HELP_COMMAND_INDEX:
            break;
        default:
            throw new IllegalArgumentException(String.format(MSG_ERR_INTERNAL_PARSER_IMPLEMENTATION, command));
        }

        inputArray = outputArray;

        return inputArray;
    }

    /**
     * This function is called to parse user input if the user wishes to change status
     * @param outputArray
     * @throws IllegalArgumentException	throws when not enough arguments are giving through user parsing
     */
    //@author A0116514N
    private void markTask(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_MARK_TASK) {
            throw new IllegalArgumentException(MSG_ERR_ID_MARK_TASK);
        }

        outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
        String prior = extractAttribute(inputArray,START_STATUS_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);

        if(prior != (NOT_EXIST)) {
            outputArray[TASK_STATUS_INDEX] = prior.trim();
        } else {
            outputArray[TASK_STATUS_INDEX] = prior;
        }
    }

    /**
     * Used to parse command for when user wishes to clear only some attributes of a task
     * @param outputArray
     * @throws IllegalArgumentException	throws when user doesn't enter ID
     */
    //@author A0116514N
    private void clearAttribute(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_CLEAR_ATTR) {
            throw new IllegalArgumentException(MSG_ERR_ID_CLEAR_ATTR);
        }

        outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];

        for(int i = 2; i < KEYWORDS_TWO_TASK.length; i++) {
            for(int j = 0; j < inputArray.length; j++) {
                int indexOfOne = indexOfKey(KEYWORDS_ONE_TASK,inputArray[j],KEYWORDS_ONE_TASK[i]);
                int indexOfTwo = indexOfKey(KEYWORDS_TWO_TASK,inputArray[j],KEYWORDS_TWO_TASK[i]);

                if(indexOfOne != KEY_NOT_PRESENT) {
                    outputArray[indexOfOne+TO_ADD_INDEX] = EMPTY_STRING;
                }
                if(indexOfTwo != KEY_NOT_PRESENT) {
                    outputArray[indexOfTwo+TO_ADD_INDEX] = EMPTY_STRING;
                }
            }
        }
    }

    /**
     * This function is called when user wishes to use a template 
     * @param outputArray
     * @throws IllegalArgumentException	throws when Id not specified
     */
    //@author A0116514N
    private void useTemplate(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_USE_TEMPLATE) {
            throw new IllegalArgumentException(MSG_ERR_ID_USE_TEMPLATE);
        }

        String tempName = EMPTY_STRING;
        if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            tempName = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            tempName = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }

        outputArray[TASK_ID_INDEX] = tempName;	
        outputArray[TASK_NAME_INDEX] = NOT_EXIST;
        for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
            int j = i + 1;
            String valueTemplate = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
            if(isDateTime(i,valueTemplate) && !isClearAttribute(valueTemplate)) {
                valueTemplate = flipDate(valueTemplate);
                storeDateTime(outputArray,valueTemplate,i);
            } else if (valueTemplate != NOT_EXIST) {
                outputArray[j] = valueTemplate.trim();
            }
        }
    }

    /**
     * Parses user input to allow editing of templates
     * @param outputArray
     * @throws IllegalArgumentException	throws when no template ID specified
     */
    //@author A0116514N
    private void editTemplate(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_EDIT_TEMPLATE) {
            throw new IllegalArgumentException(MSG_ERR_ID_EDIT_TEMPLATE);
        }

        String templateTitle = EMPTY_STRING;
        if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            templateTitle = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            templateTitle = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }

        outputArray[TASK_ID_INDEX] = templateTitle;	
        outputArray[TASK_NAME_INDEX] = NOT_EXIST;
        for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
            int j = i + 1;
            String valueTemplate = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
            if(isDateTime(i,valueTemplate) && !isClearAttribute(valueTemplate)) {
                valueTemplate = flipDate(valueTemplate);
                storeDateTime(outputArray,valueTemplate,i);
            } else if (valueTemplate != NOT_EXIST) {
                outputArray[j] = valueTemplate.trim();
            }
        }
    }

    /**
     * Function parses user input for deletion of template.
     * @param outputArray
     * @throws IllegalArgumentException	throws when no ID given
     */
    //@author A0116514N
    private void deleteTemplate(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_DELETE_TEMPLATE) {
            throw new IllegalArgumentException(MSG_ERR_ID_DELETE_TEMPLATE);
        }

        String templateName =  EMPTY_STRING;
        if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            templateName = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            templateName = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }
        outputArray[TASK_ID_INDEX] = templateName;
    }

    /**
     * Function parses user input for adding of template.
     * @param outputArray
     * @throws IllegalArgumentException	thrown if name not given
     */
    //@author A0116514N
    private void addTemplate(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length != CORRECT_LENGTH_ADD_TEMPLATE) {
            throw new IllegalArgumentException(MSG_ERR_ADD_TEMPLATE);
        }

        outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
        String extractedValue = extractAttribute(inputArray,START_STATUS_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);	
        if(extractedValue != NOT_EXIST) {
            outputArray[TASK_NAME_INDEX] = extractedValue.trim();
        }
    }

    /**
     * Parses in correct format for deleting of a keyword
     * @param outputArray
     * @throws IllegalArgumentException	thrown if no argument to delete
     */
    //@author A0116514N
    private void deleteKeyword(String[] outputArray) throws IllegalArgumentException {
        if(inputArray.length < CORRECT_LENGTH_DELETE_KEYWORD) {
            throw new IllegalArgumentException(MSG_ERR_ID_DELETE_KEYWORD);
        }

        outputArray[KEYWORD_KEY] = inputArray[KEYWORD_KEY];
    }

    /**
     * This functions takes two string from user that signifies a desired pairing of keywords.
     * @param outputArray
     * @throws IllegalArgumentException	throws when two keywords are not given
     */
    //@author A0116514N
    private void addKeyword(String[] outputArray) throws IllegalArgumentException {
        for(int i = 0; i < inputArray.length;i++) {
            if(inputArray[i].equals(KEYWORD_KEYWORD) && inputArray.length>3){
                outputArray[NEW_KEYWORD_INDEX] = inputArray[i-1];
                outputArray[OLD_KEYWORD_INDEX] = inputArray[i+1];
            }
        }

        //Ma Cong what is 1 and 2
        if(outputArray[1] == NOT_EXIST && outputArray[2] == NOT_EXIST ) {
            throw new IllegalArgumentException(MSG_ERR_ID_ADD_KEYWORD);
        }
    }

    /**
     * Function to parse user input requesting to search available tasks, in desired format
     * @param outputArray
     */
    //@author A0116514N
    private void searchTask(String[] outputArray) throws IllegalArgumentException {
        outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
        if(inputArray.length < 2) {
            throw new IllegalArgumentException(MSG_ERR_ID_SEARCH);
        }

        String searchTerm = EMPTY_STRING;
        //surround with try catch for when there is no title?
        if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            searchTerm = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            searchTerm = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }

        searchTerm = flipDate(searchTerm);
        ArrayList<Date> dateList = useNatty(searchTerm);
        if(dateList.isEmpty()) {
            outputArray[TASK_NAME_INDEX] = searchTerm; 
        } else {
            Date tempDate = dateList.get(DATE_INDEX);
            searchTerm = dateConverter(tempDate);
            outputArray[TASK_NAME_INDEX] = searchTerm;	
        }

        for(int i = 3; i < KEYWORDS_ONE_TASK.length; i++) {
            outputArray[i] = NOT_EXIST;
        }
    }

    /**
     * Function to parse user input requesting to view tasks, in desired format
     * @param outputArray
     */
    //@author A0116514N
    private void viewTask(String[] outputArray) {
        //WARNING: NO CHECKING VALIDITY
        String viewTitle = NOT_EXIST;
        outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
        if(inputArray.length <= 1 || inputArray[START_TITLE_INDEX].equals(VIEW_BY) || inputArray.length <= 1) {
            //MA CONG  what is this if doing here. it has no body
        } else if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            viewTitle = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            viewTitle = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }

        outputArray[TASK_STATUS_INDEX] = viewTitle;
        String value =extractAttribute(inputArray,START_TITLE_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        if(value!=(NOT_EXIST)) {
            outputArray[TASK_NAME_INDEX] = value.trim();
        } else {
            outputArray[TASK_NAME_INDEX] = value;
        }
    }

    /**
     * Function to parse user input requesting to delete a task, in desired format
     * @param outputArray	
     * @throws IllegalArgumentException	if no ID is specified
     */
    //@author A0116514N
    private void deleteTask(String[] outputArray) throws IllegalArgumentException {
        //WARNING: NO CHECKING VALIDITY
        if(inputArray.length<2) {
            throw new IllegalArgumentException(MSG_ERR_ID_DELETE_TASK);
        }

        outputArray[TASK_ID_INDEX] = inputArray[1];
        for(int i = 1; i < KEYWORDS_ONE_TASK.length; i++) {
            int j = i + 1;
            outputArray[j] = NOT_EXIST;
        }
    }

    /**
     * Function to parse user input requesting to edit a task, in desired format
     * @param outputArray
     * @throws NumberFormatException	thrown when task ID given if not parsable to Integer
     */
    //@author A0116514N
    private void editTask(String[] outputArray) throws NumberFormatException {
        if(!isParsable(inputArray[TASK_ID_INDEX])) {
            throw new NumberFormatException(MSG_ERR_ID_EDIT);
        }

        outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
        outputArray[TASK_NAME_INDEX] = NOT_EXIST;
        for(int i = 1; i < KEYWORDS_ONE_TASK.length; i++) {
            int j = i + 1;
            String value = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);

            if(isDateTime(i,value) && !isClearAttribute(value)) {
                value = flipDate(value);
                storeDateTime(outputArray,value,i);
            } else if(value != null) {
                outputArray[j] = value.trim();     
            }
        }
    }

    /**
     * Function to parse user input requesting to add a task, in desired format
     * @param outputArray
     * @throws IllegalArgumentException	thrown when a title is not specified by user
     */
    //@author A0116514N
    private void addTask(String[] outputArray) throws IllegalArgumentException {
        outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
        String title = NOT_EXIST;

        if(inputArray[START_TITLE_INDEX].contains(DOUBLE_QUOTE_STRING)) {
            title = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
        } else {
            title = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
        }

        if(title.equals(EMPTY_STRING)) {
            throw new IllegalArgumentException(MSG_ERR_TITLE_EMPTY);
        }

        outputArray[TASK_NAME_INDEX] = title;

        for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
            int j = i + 1;
            String value = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);

            if(isDateTime(i,value)) {
                value = flipDate(value);
                storeDateTime(outputArray,value,i);
            } else if(value != NOT_EXIST) {
                outputArray[j] = value.trim();
            }
        }
    }

    //@author A0116514N
    private int indexOfKey(String[] keyWords,String input,String matched) {
        for(int i = 0; i < keyWords.length; i++) {
            if(matched.equals(input.toLowerCase()) && keyWords[i].equals(matched)) {
                return i;
            }
        }//Ma Cong what is this -1??
        return -1;
    }

    //@author A0116514N
    private boolean isClearAttribute(String value) {
        if(value != NOT_EXIST) {
            value = value.trim();
            if(value.equals(EMPTY_STRING)) {
                return true;
            }
        }
        return false;
    }

    //@author A0116514N
    private int getCommandIndex(String command) {
        int index = -1;;
        for(int i = 0; i < commandArray.length; i++) {
            if(command.equals(commandArray[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    //@author A0116514N
    private boolean isParsable(String input) {
        boolean parsable = true;
        
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        
        return parsable;
    }

    //@author A0116514N
    private String extractText(String[] inputArr,String[] keyArrOne,String[] keyArrTwo) {
        StringBuilder strb = new StringBuilder();
        
        for(int i=1;i<inputArr.length;i++) {
            if(!isKeyWord(inputArr[i],keyArrOne,keyArrTwo) && i < inputArr.length ) { //tempArr = keyword
                strb.append(inputArr[i]);
                strb.append(" ");
                if(i==inputArr.length) {
                    break;
                }
            } else {
                break;
            }
        }
        
        return strb.toString().trim() ;   	
    }

    //@author A0116514N
    private String extractTextWithQuotes(String[] input,int index) {
        String attribute = NOT_EXIST;
        StringBuilder strb = new StringBuilder();
        int start = -1;
        int end = -1;
        int i = index;
        int j = 0;
        
        while(start < 0) {
            if(input[i].split(DOUBLE_QUOTE_STRING, -1).length > 2) {
                start = i;
                end = i;
            }
            if(input[i].contains(DOUBLE_QUOTE_STRING)) {
                start = i;
            }
            i++;
        }
        
        j = start+1;
        while(end < 0) {
            if(input[j].contains(DOUBLE_QUOTE_STRING)) {
                end = j;
            }
            j++;
        }
        
        for(int k = start; k <= end; k++) {
            strb.append(input[k]);
            strb.append(" ");

        }
        
        for(int m = start; m <= end; m++) {
            inputArray[m] = "";
        }

        attribute = extractFromSingleQuote(strb.toString());
        
        return attribute;	
    }
    
    /**
     * Extracts values that are identified by a keyword before it
     * @param input	the whole user input in the form of String[]
     * @param keywordIndex	int representing the current keyword we are looking for
     * @param keywordsOne	String[] of possible keywords
     * @param keywordsTwo	String[] of possible keywords
     * @return	attribute which is a string that will be stored in the output array
     */
    //@author A0116514N
    private String extractAttribute(String[] input, int keywordIndex,String[] keywordsOne,String[] keywordsTwo) throws IllegalArgumentException {	
        StringBuilder strb = new StringBuilder();
        String keyword1 = KEYWORDS_ONE_TASK[keywordIndex];
        String keyword2 = KEYWORDS_TWO_TASK[keywordIndex];	
        String attribute = NOT_EXIST;
        boolean openQuote = false;
        
        for(int i = 0; i < input.length; i++) {
            String checkWord = input[i].toLowerCase(); 
            if(input[i].contains(DOUBLE_QUOTE_STRING)) {
                //if quote is already open(true) close it
                if(openQuote) {
                    openQuote = false;
                } else {
                    openQuote = true;
                }
            }
            
            if(openQuote) {
                continue;
            }

            if(checkWord.equals(keyword1) || checkWord.equals(keyword2) && checkWord!= NOT_EXIST) {
                int index = i+1;

                if((checkWord.equals("by") || checkWord.equals("taskname") || checkWord.equals("det") || checkWord.equals("at") || checkWord.equals("details") || checkWord.equals("location")) && input[index].contains(DOUBLE_QUOTE_STRING)) {

                    attribute = extractTextWithQuotes(input,index);
                } else {
                    while(!isKeyWord(input[index],keywordsOne,keywordsTwo) && index < input.length ) { 
                        strb.append(input[index]);
                        strb.append(" ");
                        index++;
                        
                        if(index==input.length) {
                            break;
                        }
                    }
                    attribute = strb.toString();
                }
            }
        }
        
        if(openQuote){
            throw new IllegalArgumentException(MSG_ERR_QUOTE);
        }

        return attribute;	
    }

    /**
     * Checks if value is a possible date value
     * @param index	of date related keyword
     * @param value	String representing user input that cannot be null
     * @return true if the keyword is a keyword related to date
     */
    //@author A0116514N
    private boolean isDateTime(int index, String value) {
        if((KEYWORDS_ONE_TASK[index].equals(DATE_FROM) || KEYWORDS_ONE_TASK[index].equals(DATE_TO) || KEYWORDS_ONE_TASK[index].equals(DATE_ON)) && value != NOT_EXIST && !value.equals(EMPTY_STRING)) {
            return true;
        }
        
        return false;
    }

    /**
     * trims quotation marks
     * @param preprocessed	a string that has quotes which needs extracting
     * @return preprocessed without quotes
     */
    //@author A0116514N
    private String extractFromSingleQuote(String preprocessed) {
        int start = 1;
        return preprocessed.substring(start,preprocessed.length()-2).trim();
    }

    /**
     * Stores date values in outputArr since date has to be handled differently by passing to Natty
     * @param outputArr	the array that is output form parseText()
     * @param value	the string value that is to be converted to date format
     * @param index	of keyword in inputArray()
     * @throws NullPointerException
     */
    //@author A0116514N
    private void storeDateTime(String[] outputArr,String value,int index) {
        int j = index + 1;
        String keyword = EMPTY_STRING;
        
        if((KEYWORDS_ONE_TASK[index].equals(DATE_FROM) || KEYWORDS_ONE_TASK[index].equals(DATE_TO) || KEYWORDS_ONE_TASK[index].equals(DATE_ON)) && value != NOT_EXIST) {
            keyword = KEYWORDS_ONE_TASK[index];
            value = value.trim();
            ArrayList<Date> dateList = useNatty(value);
            if(dateList.isEmpty()) {
                throw new  NullPointerException(String.format(MSG_ERR_NOT_DATE,keyword));
            }

            outputArr[j] = dateConverter(dateList.get(0)).trim();
        } 
    }

    /**
     * Switches day and month the user enters in a format similar to dd/mm/yyyy 
     * since Natty only allows US date format
     * @param value	String value representing date
     * @return	String changedDate which has values swapped
     */
    //@author A0116514N
    private String flipDate(String value) {
        String[] tempArr = ARRAY_NOT_EXIST;
        String changedDate = NOT_EXIST;
        if(value.contains(DATE_FORMAT_STRING)) {
            tempArr = value.split(DATE_FORMAT_STRING);

            String day = tempArr[DAY_INDEX];
            String month = tempArr[MONTH_INDEX];
            String year = tempArr[YEAR_INDEX];

            changedDate = month.concat(DATE_FORMAT_STRING);
            changedDate = changedDate.concat(day);
            changedDate = changedDate.concat(DATE_FORMAT_STRING);
            changedDate = changedDate.concat(year);
            return changedDate;
        }

        return value;
    }

    /**
     * Takes in natural language and converts it to Date attempts to correct user input
     * @param dateInput	String to be converted to Date
     * @return	dateList an ArrayList of dates
     */
    //@author A0116514N
    private ArrayList<Date> useNatty(String dateInput)/* throw IllegalArgumentException */{
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(dateInput);
        ArrayList<Date> dateList = new ArrayList<Date>();

        for(DateGroup group:groups)  {
            Date dates = group.getDates().get(0);   
            dateList.add(dates);        
        }
        
        return dateList;	
    }

    /**
     * Takes in a Date object and converts it to desired format
     * @param date	date object to be checked
     * @return	date object in format ft
     */
    //@author A0116514N
    private String dateConverter(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy HH:mm");

        return ft.format(date);
    }

    /**
     * This method takes in a string and checks if it is a keyword
     * @param str	string to be checked
     * @param keywordsOne	array of keywords
     * @param keywordsTwo	array of keywords
     * @return	true if string is a keyword, else false
     */
    //@author A0116514N
    private boolean isKeyWord(String str,String[] keywordsOne,String[] keywordsTwo) {
        for(int i = 0; i < keywordsOne.length; i++) {
            str = str.toLowerCase();
            if(str.equals(keywordsOne[i]) || str.equals(keywordsTwo[i])) {
                return true;
            }
        }
        
        return false;
    }
}