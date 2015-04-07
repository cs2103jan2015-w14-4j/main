//not sure natty loop for what
//next time put inside get attribute
//for edit id? how to check
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;




//add Task behind all the command
//add escape if n

public class FlexiParser {
	private static final String MSG_ERR_INTERNAL_PARSER_IMPLEMENTATION = "\"%s\" is not implemented in parser yet.";
	private static final String MSG_ERR_UNRECOGNIZED_COMMAND = "Flexi Tracker does not recognise the command \"%s\". Type \"viewKeyword\" to see all recognised keywords.";
	private static final String COMMAND_ADD = "addTask";
    
    private static final String COMMAND_VIEW = "viewTask";
    private static final String COMMAND_DELETE = "deleteTask";
    private static final String COMMAND_EDIT = "editTask";
    
    private static final String COMMAND_ADD_SHORTCUT = "addTask";
    private static final String COMMAND_VIEW_SHORTCUT = "viewTasks";
    private static final String COMMAND_DELETE_SHORTCUT = "deleteTask";
    private static final String COMMAND_EDIT_SHORTCUT = "editTask";
    
	
    private static final String TID_NOT_EXIST = null;
    private static final String NOT_EXIST = null;
    
    private static final int COMMAND_TYPE_INDEX = 0;
    private static final int TASK_ID_INDEX = 1;
    private static final int TASK_NAME_INDEX = 2;
    private static final int TASK_DATE_FROM_INDEX = 3;
    private static final int TASK_DATE_TO_INDEX = 4;
    private static final int TASK_DEADLINE_INDEX = 5;
    private static final int TASK_LOCATION_INDEX = 6;
    private static final int TASK_DETAILS_INDEX = 7;
    private static final int TASK_PRIORITY_INDEX = 8;
    
    private static final int START_TITLE_INDEX = 1;
    private static final int START_PRIORITY_INDEX = 7;
    private static final int TO_ADD_INDEX = 1;
    
    private static final int SHORTCUT_KEY = 1;
    
    private static final int KEY_NOT_PRESENT = -1;
    
    private static final String ERROR_EXCEPTION = "Exception caught";
    private static final String DATE_FROM = "from";
    private static final String DATE_TO = "to";
    private static final String DATE_ON = "on";
    private static final String VIEW_BY = "by";
    private static final String TEMPLATE_NAME_INDEX = "as";
    
    private static final String EMPTY_STRING = "";
    private static final String DATE_FORMAT_STRING = "/";
   
    private static final String[] KEYWORDS_ONE_TASK = {"ID","by","from","to","on","at","det","as"};
    private static final String[] KEYWORDS_TWO_TASK = {"ID","taskname","datefrom","dateto","before","location","details","priority"};
    
    
    private static final String KEYWORD_SHORTCUT = "onto";
   
    private static final String[] commandArray = {"addTask","editTask","deleteTask","viewTask","Block","searchTask","undoTask","redoTask","addReminder","deleteReminder","addShortcut","deleteShortcut","viewShortcut","resetShortcut",
    														"addTemplate","deleteTemplate","viewTemplate","resetTemplate","editTemplate","useTemplate","clearAttr","markTask"};
    
    private static String[] inputArray;
	
    public static final int TASK_LENGTH = 9;
    public static final int SHORTCUT_LENGTH = 3;
    
    private Shortcut shortcut;
    
    public FlexiParser(Shortcut shortcut) {
		
    	this.shortcut = shortcut;
    	
	}
	
    public String[] parseText(String userInput) throws IllegalArgumentException {
    	
    	
		    
			inputArray = userInput.split("\\s+");
			flipDate(inputArray);
			
			
			String command = inputArray[COMMAND_TYPE_INDEX];
			System.out.println("|"+command+"|");
			
			command = shortcut.keywordMatching(command);
			System.out.println("|"+command+"|");
			if(command == null) {
				throw new IllegalArgumentException(String.format(MSG_ERR_UNRECOGNIZED_COMMAND, inputArray[COMMAND_TYPE_INDEX]));
			}
			String[] outputArray;
			if(!command.contains("Shortcut")) {
				
				outputArray = new String[TASK_LENGTH];
				
			}
			else {
				
				outputArray = new String[SHORTCUT_LENGTH];
				
			}
			
			outputArray[COMMAND_TYPE_INDEX] = command;
			
			
			
			int commandIndex = getCommandIndex(command);
			
			switch(commandIndex) {
				//add task need to ignore title also
			    case 0:
			    	
			    	//WARNING: NO CHECKING VALIDITY
			    	outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
			    	String title = EMPTY_STRING;
			    	//surround with try catch for when there is no title?
			    	if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
			    		title = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    		
			    	}
			    	else {
			    	
			    		title = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	outputArray[TASK_NAME_INDEX] = title;
			    	//maybe change to index_ssd
			    	for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    		
			    		
			    		
			    		if(isDateTime(i,value)) {
			    			
			    			storeDateTime(outputArray,value,i);
			    			
			    		}
			    		 
			    		else {
			    			if(value != null) {
			    			 outputArray[j] = value.trim();
			    			}
			    		 
			    		}
			    	
			    	}
			    		
			    		
			    	
					
					break;
				//edit task
				case 1:
					
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					//error catching 
					if(inputArray[TASK_ID_INDEX].equals(TID_NOT_EXIST)) {
		    			//what should i return
		    		
						System.out.println("not exist?");
						
		    			
		    		}
					outputArray[TASK_NAME_INDEX] = null;
					for(int i = 1; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    		
			    		//null not
			   
			    		if(isDateTime(i,value) && !isClearAttribute(value)) {
			    			
			    			storeDateTime(outputArray,value,i);
			    			
			    		}
			    		 
			    		else {
			    			 	 
			    			if(value != null) {
				    			 outputArray[j] = value.trim();
				    			}
			    		 
			    		 }
			    	
			    	}
					
					break;
				//delete task
				case 2:
					
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_ID_INDEX] = inputArray[1];
					for(int i = 1; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		
			    		outputArray[j] = null;
			    		
					}
					
					
					break;
				//view task
				case 3:
					//WARNING: NO CHECKING VALIDITY
			    	String viewTitle = NOT_EXIST;
					outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
			    	if(inputArray.length <= 1 || inputArray[START_TITLE_INDEX].equals(VIEW_BY) || inputArray.length <= 1) {
			    		
			    		
			    		
			    	}
			    	else if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
			    		viewTitle = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    			
			    	}
			    	else {
			    	
			    		viewTitle = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	outputArray[TASK_PRIORITY_INDEX] = viewTitle;
			    	String value =extractAttribute(inputArray,START_TITLE_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	
			    	if(value!=(NOT_EXIST)) {
			    		outputArray[TASK_NAME_INDEX] = value.trim();
			    	}
			    	else {
			    		
			    		outputArray[TASK_NAME_INDEX] = value;
			    		
			    	}
			    	//maybe change to index_ssd
			    	
					break;
				//block
				case 4:
					//not implemented
					break;
				//search
				case 5:
					//WARNING: NO CHECKING VALIDITY
					flipDate(inputArray);
			    	outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
			    	
			    	String searchTerm = EMPTY_STRING;
			    	//surround with try catch for when there is no title?
			    	if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
			    		searchTerm = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    		
			    	}
			    	else {
			    	
			    		searchTerm = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	/*if(searchTerm.contains(DATE_FORMAT_STRING)) {
		    			
		    			storeDateTime(outputArray,value,i);
		    			
		    		}
		    		 
		    		else {
		    			if(value != null) {
		    			 outputArray[TASK_NAME_INDEX] = value.trim();
		    			}
		    		 
		    		}*/
			    	
			    	
			    	outputArray[TASK_NAME_INDEX] = searchTerm;
			   
			    	//maybe change to index_ssd
			    	for(int i = 3; i < KEYWORDS_ONE_TASK.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}
					break;
				//undo
				case 6:
					
			    	for(int i = 1; i < outputArray.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}
				
					break;
				//redo
				case 7:
					
					for(int i = 1; i < outputArray.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}
					
					break;
				//addreminder
				case 8:
				/*	outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					outputArray[TASK_NAME_INDEX] = null;
					for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray,i,KEYWORDS_ONE_TASK);
			    		
			    		if(isDateTime(i,value)) {
			    			
			    			storeDateTime(outputArray,value,i);
			    			
			    		}
			    		 
			    		else {
			    			if(value != null) {
			    			 outputArray[j] = value.trim();
			    			}
			    		 
			    		}
			    	
			    	}*/
					
					break;
				//deletereminder
				case 9:
					/*outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					
					for(int i = 3; i < outputArray.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}*/
					
					break;
				//addShortcut
				case 10:
					
					for(int i = 0; i < inputArray.length;i++) {
						
						if(inputArray[i].equals(KEYWORD_SHORTCUT) && inputArray.length>3){
							outputArray[1] = inputArray[i-1];
							outputArray[2] = inputArray[i+1];
						}
						
						
					}

					break;
				//"deleteShortcut"
				case 11:
					
					outputArray[SHORTCUT_KEY] = inputArray[SHORTCUT_KEY];
					
					break;
				//"viewShortcut"
				case 12:
					for(int i = 1; i < outputArray.length;i++) {
						
						outputArray[i]= null;
						
					}
					
					
					break;
				//"resetShortcut"
				case 13:
					for(int i = 1; i < outputArray.length;i++) {
						
						outputArray[i]= null;
						
					}
					break;
				//addTemplate
				case 14:
					//WARNING: NO CHECKING VALIDITY
			    	
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
			    	
			    	//surround with try catch for when there is no title?
			    	
			    	String extractedValue = extractAttribute(inputArray,START_PRIORITY_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    			
			    	if(extractedValue != null) {
			    		outputArray[TASK_NAME_INDEX] = extractedValue.trim();
			    			
			    		 
			    	}
			    	
					break;
				//deleteTemplate
				case 15:
					String templateName =  EMPTY_STRING;
			    	if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
			    		templateName = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    		
			    	}
			    	else {
			    	
			    		templateName = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	outputArray[TASK_ID_INDEX] = templateName;
					
					break;
				//viewTemplate
				case 16:
					//doesn't need anything
					break;
				//resetTemplate
				case 17:
					//also doesn't need anything
					break;
				//editTemplate
				case 18:
					
					//WARNING: NO CHECKING VALIDITY
					String templateTitle = EMPTY_STRING;
					if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
						templateTitle = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    		
			    	}
			    	else {
			    	
			    		templateTitle = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	outputArray[TASK_ID_INDEX] = templateTitle;	
					outputArray[TASK_NAME_INDEX] = null;
					//so that it doesn't get to title
					for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		String value1 = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    		
			    		//null not
			   
			    		if(isDateTime(i,value1) && !isClearAttribute(value1)) {
			    			
			    			storeDateTime(outputArray,value1,i);
			    			
			    		}
			    		 
			    		else {
			    			 	 
			    			if(value1 != null) {
				    			 outputArray[j] = value1.trim();
				    			}
			    		 
			    		 }
			    	
			    	}
					
					
					break;
				//useTemplate
				case 19:
					//WARNING: NO CHECKING VALIDITY
					String templName = EMPTY_STRING;
					if(inputArray[START_TITLE_INDEX].contains("\"")) {
			    		
						templName = extractTextWithQuotes(inputArray,START_TITLE_INDEX);
			    		
			    	}
			    	else {
			    	
			    		templName = extractText(inputArray,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	}
			    	
			    	outputArray[TASK_ID_INDEX] = templName;	
					outputArray[TASK_NAME_INDEX] = null;
					//so that it doesn't get to title
					for(int i = 2; i < KEYWORDS_ONE_TASK.length; i++) {
			    		int j = i + 1;
			    		String value1 = extractAttribute(inputArray, i,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    		
			    		//null not
			   
			    		if(isDateTime(i,value1) && !isClearAttribute(value1)) {
			    			
			    			storeDateTime(outputArray,value1,i);
			    			
			    		}
			    		 
			    		else {
			    			 	 
			    			if(value1 != null) {
				    			 outputArray[j] = value1.trim();
				    			}
			    		 
			    		 }
			    	
			    	}
					
					break;
				//clearAttr
				case 20:
					
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					for(int i = 2;i < KEYWORDS_TWO_TASK.length;i++) {
						for(int j = 0;j < inputArray.length;j++) {
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
					break;
				//markTask
				case 21:
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					String prior =extractAttribute(inputArray,START_PRIORITY_INDEX,KEYWORDS_ONE_TASK,KEYWORDS_TWO_TASK);
			    	
			    	if(prior!=(NOT_EXIST)) {
			    		outputArray[TASK_PRIORITY_INDEX] = prior.trim();
			    	}
			    	else {
			    		
			    		outputArray[TASK_PRIORITY_INDEX] = prior;
			    		
			    	}
					break;
				default:
					throw new IllegalArgumentException(String.format(MSG_ERR_INTERNAL_PARSER_IMPLEMENTATION, command));
			}
			
			inputArray = outputArray;
			
		
    	return inputArray;
    	
    }
    
    private int indexOfKey(String[] keyWords,String input,String matched) {
    	for(int i = 0; i < keyWords.length; i++) {
    		
    		if(matched.equals(input.toLowerCase()) && keyWords[i].equals(matched)) {
    		
    			return i;
    			
    		}
    		
    	}
    	return -1;
    }
    
    private boolean isClearAttribute(String value) {
    	
    	if(value != NOT_EXIST) {
    		value = value.trim();
    		if(value.equals(EMPTY_STRING)) {
    		return true;
    		}
    		
    	}
    	return false;
    }
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
    
    
    //not used as command must be exact?
    private String processCommand(String command) {
    	
    	String processedCommand = command.toLowerCase();
    	
    	return processedCommand;
    	
    }
    
  
    private String extractText(String[] inputArr,String[] keyArrOne,String[] keyArrTwo) {
    	StringBuilder strb = new StringBuilder();
    	
    	
    	for(int i=1;i<inputArr.length;i++) {
    		
    		if(!isKeyWord(inputArr[i],keyArrOne,keyArrTwo) && i < inputArr.length ) { //tempArr = keyword
    			
    			strb.append(inputArr[i]);
    			strb.append(" ");
    			
    			if(i==inputArr.length) {
				
    				break;
			
    			}
		
    		}
    		else {
    			
    			break;
    			
    		}
    	}
    	
    	return strb.toString().trim() ;
    	
    }
    private String extractTextWithQuotes(String[] input,int index) {
    	String attribute = null;
    	StringBuilder strb = new StringBuilder();
    	int start = -1;
		int end = -1;
		int i = index;
		int j = 0;
    	try {
    		while(start<0) {
    			
				if(input[i].split("\"", -1).length > 2) {
					start = i;
					end = i;
					
				}
				if(input[i].contains("\"")) {

    				start = i;
    				
    			}
				i++;
    		}
    		j = start+1;
    		while(end<0){
    			
				if(input[j].contains("\"")) {
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
				
    	}catch(Exception e) {
			
			System.out.println("error1");
			
		}
    	
    	return attribute;	
    	
    }
    //extractor
	private String extractAttribute(String[] input, int keywordIndex,String[] keywordsOne,String[] keywordsTwo) {
			
			StringBuilder strb = new StringBuilder();
			String keyword1 = KEYWORDS_ONE_TASK[keywordIndex];
			String keyword2 = KEYWORDS_TWO_TASK[keywordIndex];
			
			String attribute = null;
		
			try {	
				
				for(int i = 0; i < input.length; i++) {
				
				String checkWord = input[i].toLowerCase(); // allow user to type keyword in anyform
				
				
				if(checkWord.equals(keyword1) || checkWord.equals(keyword2) && checkWord!=null) {
					//System.out.println(checkWord);

					int index = i+1;
					//change2
					if((checkWord.equals("by") || checkWord.equals("taskname") || checkWord.equals("det") || checkWord.equals("at") || checkWord.equals("details") || checkWord.equals("location")) && input[index].contains("\"")) {
						
						attribute = extractTextWithQuotes(input,index);
					}
				
					
					else{
					while(!isKeyWord(input[index],keywordsOne,keywordsTwo) && index < input.length ) { //tempArr = keyword
						
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
		}catch(Exception e) {
			
			System.out.println("ERROR");
			
		}
		
		return attribute;
			
	}
	
	private boolean isDateTime(int index, String value) {
		
		if((KEYWORDS_ONE_TASK[index].equals(DATE_FROM) || KEYWORDS_ONE_TASK[index].equals(DATE_TO) || KEYWORDS_ONE_TASK[index].equals(DATE_ON)) && value != null && !value.equals(EMPTY_STRING)) {
			
			
			return true;
			
		}
		
		return false;
		
	}
	
	
	
	//method for taking in string marked by ' '
	private String extractFromSingleQuote(String preprocessed) {
		//is it?
		int start = 1;
		//do we wanna trim?
		return preprocessed.substring(start,preprocessed.length()-2).trim();
		
	}
	
	
	
	
	private void storeDateTime(String[] outputArr,String value,int index) {
		int j = index + 1;
		value = value.trim();
		if((KEYWORDS_ONE_TASK[index].equals(DATE_FROM) || KEYWORDS_ONE_TASK[index].equals(DATE_TO) || KEYWORDS_ONE_TASK[index].equals(DATE_ON)) && value != null) {
			DateFormat input;
			DateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
			DateFormat timeFormat;
			String temp;
				
			ArrayList<Date> dateList = useNatty(value);
			//System.out.println("The date is "+ dateList.get(0));
			
			outputArr[j] = dateConverter(dateList.get(0)).trim();
		}
	}
		
	
	private String[] flipDate(String[] inputArr) {
		String[] tempArr;
		String changedDate;
		for(int i = 0;i < inputArr.length;i++) {
			if(inputArr[i].contains("/")) {
				/*int index = inputArr[i].indexOf("/");
				int dayStart = index-2;
				//exclusive
				int dayEnd =  index;
				
				int monthStart = index+1;
				int monthEnd = index+3;*/
				tempArr = inputArr[i].split("/");
				//index of month
				String temp = tempArr[1].concat("/");
				temp = temp.concat(tempArr[0]);
				temp = temp.concat("/");
				changedDate = temp.concat(tempArr[2]);
				inputArr[i] = changedDate;
			}
			
		}
		/*for(int i =0; i<inputArr.length;i++) {
			
			System.out.print(inputArr[i]);
			
		}*/
		
		return inputArr;
		
	}
	
	private ArrayList<Date> useNatty(String dateInput) {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(dateInput);
		ArrayList<Date> dateList = new ArrayList<Date>();
		
		for(DateGroup group:groups)  {
			//this part loops through groups
			Date dates = group.getDates().get(0);   
			dateList.add(dates);
			//System.out.println(dates.toLocaleString());        

		}
		return dateList;
		
	}
	
	private String dateConverter(Date date) {
		SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy HH:mm");
		
		return ft.format(date);
		
	}
	
	
	private boolean isKeyWord(String str,String[] keywordsOne,String[] keywordsTwo/*,String ignoreWord*/) {
			
		for(int i = 0; i < keywordsOne.length; i++) {
			str = str.toLowerCase();
			
			
			if(str.equals(keywordsOne[i]) || str.equals(keywordsTwo[i])/* && !str.equals(ignoreWord)*/) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	
	
    public static void main(String[] args) {
    
    	
    	Shortcut sc = new Shortcut();
    	FlexiParser test1 = new FlexiParser(sc);
    	
    	
    String[] temp = test1.parseText("addTask ss");
    
    for(int i=0;i<temp.length;i++) {
		
		System.out.print(temp[i]+"|");
		
	}
    	
    
    	
    
    }
}