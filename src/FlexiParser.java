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
    
    private static final int SHORTCUT_KEY = 1;
    
    private static final String ERROR_EXCEPTION = "Exception caught";
    private static final String DATE_FROM = "from";
    private static final String DATE_TO = "to";
    private static final String DATE_ON = "on";
    
    private static final String EMPTY_STRING = "\"\"";
    //use for template also?
    private static final String[] KEYWORDS_TASK = {"ID","title","from","to","on","at","det","pri"};
    
    private static final String KEYWORD_SHORTCUT = "onto";
   
    private static final String[] commandArray = {"addTask","editTask","deleteTask","viewTask","Block","searchTask","undoTask","redoTask","addReminder","deleteReminder","addShortcut","deleteShortcut","viewShortcut","resetShortcut",
    														"addTemplate","deleteTemplate","viewTemplate","resetTemplate"};
    
    private static String[] inputArray;
	
    public static final int TASK_LENGTH = 9;
    public static final int SHORTCUT_LENGTH = 3;
    
    private Shortcut shortcut;
    
    public FlexiParser(Shortcut shortcut) {
		
    	this.shortcut = shortcut;
    	
	}
	
    public String[] parseText(String userInput) {
    	
    	try {
		    
			inputArray = userInput.split("\\s+");
			
			String[] shortcutArray = new String[SHORTCUT_LENGTH];
			//String command = processCommand(inputArray[COMMAND_TYPE_INDEX]);
			
			
			String command = inputArray[COMMAND_TYPE_INDEX];
			
			//what does his one return
			command = shortcut.keywordMatching(command);
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
			    	String title = extractText(inputArray,START_TITLE_INDEX);
			    	
			    	outputArray[TASK_NAME_INDEX] = title;
			    	//maybe change to index_ssd
			    	for(int i = 2; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, KEYWORDS_TASK[i],KEYWORDS_TASK);
			    		
			    		
			    		
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
					for(int i = 1; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, KEYWORDS_TASK[i],KEYWORDS_TASK);
			    		
			    		//null not
			    		
			    		
			    		if(EMPTY_STRING.equals(value)) {
			    			
			    			 outputArray[j] = EMPTY_STRING;
			    			 continue;
			    			
			    		}
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
					for(int i = 1; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		
			    		outputArray[j] = null;
			    		
					}
					
					
					break;
				//view task
				case 3:
					//WARNING: NO CHECKING VALIDITY
			    	outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
			    	outputArray[TASK_NAME_INDEX] = extractText(inputArray,START_TITLE_INDEX);
			    	//maybe change to index_ssd
			    	for(int i = 3; i < KEYWORDS_TASK.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}
					break;
				//block
				case 4:
					//not implemented
					break;
				//search
				case 5:
					//WARNING: NO CHECKING VALIDITY
					
			    	outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
			    	outputArray[TASK_NAME_INDEX] = extractText(inputArray,START_TITLE_INDEX);
			    	//maybe change to index_ssd
			    	for(int i = 3; i < KEYWORDS_TASK.length; i++) {
			    		
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
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					outputArray[TASK_NAME_INDEX] = null;
					for(int i = 2; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, KEYWORDS_TASK[i],KEYWORDS_TASK);
			    		
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
				//deletereminder
				case 9:
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					
					for(int i = 3; i < outputArray.length; i++) {
			    		
			    		outputArray[i] = null;
			    		
			    	}
					
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
				case 14:
					break;
			}
			
			inputArray = outputArray;
			
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println(ERROR_EXCEPTION);
				
		}
    	return inputArray;
    	
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
    
    private void extractShortcut(String[] input) {
    	
    	
    }
    
    private String extractText(String[] input,int index) {
    	String attribute = null;
    	StringBuilder strb = new StringBuilder();
    	int start = -1;
		int end = -1;
		int i = index;
		int j = 0;
    	try {
    		while(start<0) {
    			
				if(input[i].split("\'", -1).length > 2) {
					start = i;
					end = i;
					
				}
				if(input[i].contains("\'")) {

    				start = i;
    				
    			}
				i++;
    		}
    		j = start+1;
    		while(end<0){
    			
				if(input[j].contains("\'")) {
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
	private String extractAttribute(String[] input, String keyWord,String[] keywords) {
			String[] tempArr = keywords;
			StringBuilder strb = new StringBuilder();
			
			String attribute = null;
		
			try {	
				
				for(int i = 0; i < input.length; i++) {
				
				String checkWord = input[i].toLowerCase(); // allow user to type keyword in anyform
				
				
				if(checkWord.equals(keyWord) && checkWord!=null) {
					//not title
					int index = i+1;
					
					if(keyWord.equals("det") || keyWord.equals("at")  && input[index].contains("\'")) {
						attribute = extractText(input,index);
					}
				
					
					else{
					while(isNotKeyWord(input[index],tempArr/*,keyWord*/) && index < input.length ) { //tempArr = keyword
						
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
		
		if((KEYWORDS_TASK[index].equals(DATE_FROM) || KEYWORDS_TASK[index].equals(DATE_TO) || KEYWORDS_TASK[index].equals(DATE_ON)) && value != null && !value.equals(EMPTY_STRING)) {
			
			
			return true;
			
		}
		
		return false;
		
	}
	
	
	
	//method for taking in string marked by ' '
	private String extractFromSingleQuote(String preprocessed) {
		//is it?
		int start = 1;
		return preprocessed.substring(start,preprocessed.length()-2);
		
	}
	
	
	
	
	private void storeDateTime(String[] outputArr,String value,int index) {
		int j = index + 1;
		value = value.trim();
		if((KEYWORDS_TASK[index].equals(DATE_FROM) || KEYWORDS_TASK[index].equals(DATE_TO) || KEYWORDS_TASK[index].equals(DATE_ON)) && value != null) {
			DateFormat input;
			DateFormat output;
			DateFormat timeFormat;
			String temp;
			if(value.contains("/")) {
				if(value.contains(":")) {
					
					input = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				
				}
				else {
					
					input = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
					
				}
				 output = new SimpleDateFormat("dd/MM/yy HH:mm",Locale.ENGLISH);
				
				    try {
				    	temp = output.format(input.parse(value));
				    	System.out.println(temp);
				    	Date now = output.parse(temp);
						//System.out.println("It is: "+output.format(now));
						Calendar calendar = Calendar.getInstance();
				        calendar.setTime(now);
				       // System.out.println("When it came in: "+calendar.get(Calendar.DAY_OF_MONTH));
						
						
						//System.out.println("temp: "+ temp);
						//rem to put if valid
						//ArrayList<Date> dateList = useNatty(value);
						outputArr[j] = output.format(now).trim() ;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				

			}
			else {
				
			ArrayList<Date> dateList = useNatty(value);
			//System.out.println("The date is "+ dateList.get(0));
			outputArr[j] = dateConverter(dateList.get(0)).trim();
			} 
		 }
		
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
	
	
	private boolean isNotKeyWord(String str,String[] keywords/*,String ignoreWord*/) {
		
		for(int i = 0; i < keywords.length; i++) {
			str = str.toLowerCase();
			
			
			if(str.equals(keywords[i])/* && !str.equals(ignoreWord)*/) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
	
	
    public static void main(String[] args) {
    
    	
    	
    	FlexiParser test1 = new FlexiParser(new Shortcut());
    	
    	
    	String[] temp = test1.parseText("addTask 'help at homework from my house' at NUS from 11/04/2015 11:00");
    
    	
    	
    	
    		
    	for(int i=0;i<temp.length;i++) {
    		
    		System.out.print(temp[i]+"|");
    		
    	}
    	//System.out.println();
    	//System.out.println();
    	

    

    	
    }
}