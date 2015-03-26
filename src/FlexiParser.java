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
    
    private static final int COMMAND_TYPE_INDEX = 0;
    private static final int TASK_ID_INDEX = 1;
    private static final int TASK_NAME_INDEX = 2;
    private static final int TASK_DATE_FROM_INDEX = 3;
    private static final int TASK_DATE_TO_INDEX = 4;
    private static final int TASK_DEADLINE_INDEX = 5;
    private static final int TASK_LOCATION_INDEX = 6;
    private static final int TASK_DETAILS_INDEX = 7;
    private static final int TASK_PRIORITY_INDEX = 8;
    
    private static final String ERROR_EXCEPTION = "Exception caught";
    private static final String DATE_FROM = "From";
    private static final String DATE_TO = "To";
    private static final String DATE_ON = "On";
    
    //use for template also?
    private static final String[] KEYWORDS_TASK = {null,"Title","From","To","On","At","Det","Pri"};
    private static final String[] KEYWORDS_SHORTCUT = {"Ori","New"};
   
    private static final String[] commandArray = {"addTask","editTask","deleteTask","viewTask","Block","SearchTask","addShortcut","deleteShortcut","viewShortcut","resetShortcut",
    														"addTemplate","deleteTemplate","viewTemplate","resetTemplate"};
    
    private static String[] inputArray;
	
    public static final int TASK_LENGTH = 9;
    public static final int SHORTCUT_LENGTH = 3;
    
    
    public FlexiParser() {
		
    	
    	
	}
	
    public String[] parseText(String userInput) {
    	
    	try {
		    
			inputArray = userInput.split("\\s+");
			
			
			String[] shortcutArray = new String[SHORTCUT_LENGTH];
			//String command = processCommand(inputArray[COMMAND_TYPE_INDEX]);
			//direct command must be correct;
			
			String command = inputArray[COMMAND_TYPE_INDEX];
			//Shortcut shortcut = Shortcut.getShortcut();
			//what does his one return
			//command = myshortcut.keywordMatching(command);
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
				//add task
			    case 0:
					
			    	//WARNING: NO CHECKING VALIDITY
			    	outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
			    	for(int i = 1; i < KEYWORDS_TASK.length; i++) {
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
					for(int i = 1; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, KEYWORDS_TASK[i],KEYWORDS_TASK);
			    		if(i == TASK_ID_INDEX && value.equals(TID_NOT_EXIST)) {
			    			//what should i return
			    			break;
			    			
			    		}
			    		
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
				//delete task
				case 2:
					//doesn't need the ID keyword
					//WARNING: NO CHECKING VALIDITY
					
					for(int i = 1; i < KEYWORDS_TASK.length; i++) {
			    		int j = i + 1;
			    		
			    		outputArray[j] = null;
			    		
					}
					
					
					break;
				//view task
				case 3:
					
					break;
				//block
				case 4:
					
					break;
				//search
				case 5:
					
					break;
				//add shortcut
				case 6:
					
					for(int i = 0; i < KEYWORDS_SHORTCUT.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, KEYWORDS_SHORTCUT[i], KEYWORDS_SHORTCUT);
			    		 
			    		if(value != null) {
			    			
			    			outputArray[j] = value.trim();
			    		
			    		}
			    		 
			    		 
			    	
			    	}
					
					
					break;
					
			}
			
			inputArray = outputArray;
			
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println(ERROR_EXCEPTION);
				
		}
    	return inputArray;
    	
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
    
    //extractor
	private String extractAttribute(String[] input, String keyWord,String[] keywords) {
			
			StringBuilder strb = new StringBuilder();
		
			String attribute = null;
		
			try {	
			for(int i = 0; i < input.length; i++) {
				
				if(input[i].equals(keyWord)) {
					
					int index = i+1;
					while(isNotKeyWord(input[index],keywords) && index < input.length) {
						
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
		}catch(Exception e) {
			
			System.out.println("error");
			
		}
		
		return attribute;
		
	}
	
	private boolean isDateTime(int index, String value) {
		
		if((KEYWORDS_TASK[index].equals(DATE_FROM) || KEYWORDS_TASK[index].equals(DATE_TO) || KEYWORDS_TASK[index].equals(DATE_ON)) && value != null) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	private void storeDateTime(String[] outputArr,String value,int index) {
		int j = index + 1;
		if((KEYWORDS_TASK[index].equals(DATE_FROM) || KEYWORDS_TASK[index].equals(DATE_TO) || KEYWORDS_TASK[index].equals(DATE_ON)) && value != null) {
			
			if(value.contains("/")) {
				
				DateFormat input = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH);
				DateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				
				    try {
						String temp = output.format(input.parse(value));
						Date now = output.parse(value);
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
	
	
	private boolean isNotKeyWord(String str,String[] keywords) {
		
		for(int i = 0; i < keywords.length; i++) {
			
			if(str.equals(keywords[i])) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
    public static void main(String[] args) {
    
    	
    	
    	FlexiParser test1 = new FlexiParser();
    	
    	
    	String[] temp = test1.parseText("addShortcut Ori addShortcut New addS");
    
    	
    	
    	
    		
    	for(int i=0;i<temp.length;i++) {
    		
    		System.out.println(temp[i]);
    		
    	}
    	//System.out.println();
    	//System.out.println();
    	

    

    	
    }
}

