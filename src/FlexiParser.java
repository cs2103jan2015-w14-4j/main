//not sure natty loop for what
//next time put inside get attribute
//for edit id? how to check
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;




//add Task behind all the command


public class FlexiParser {
	
    private static final String COMMAND_ADD = "addTask";
    private static final String COMMAND_VIEW = "viewTask";
    private static final String COMMAND_DELETE = "deleteTask";
    private static final String COMMAND_EDIT = "editTask";
	
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
    
    private static final String[] keyWords = {"ID","Title","From","To","On","At","Det","Pri"};
    
    private static String[] inputArray;
	
    public static final int TASK_LENGTH = 9;
    
    
    
    public FlexiParser() {
		
    	
    	
	}
	
    public String[] parseText(String userInput) {
    	
    	try {
		    
			inputArray = userInput.split("\\s+");
			
			String[] outputArray = new String[TASK_LENGTH];
			
			//String command = processCommand(inputArray[COMMAND_TYPE_INDEX]);
			//direct command must be correct;
			
			String command = inputArray[COMMAND_TYPE_INDEX];
			outputArray[COMMAND_TYPE_INDEX] = command;
	
			switch(command) {
				
			    case COMMAND_ADD:
					
			    	//WARNING: NO CHECKING VALIDITY
			    	
			    	for(int i = 0; i < keyWords.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, keyWords[i]);
			    		if((keyWords[i].equals(DATE_FROM) || keyWords[i].equals(DATE_TO) || keyWords[i].equals(DATE_ON)) && value != null) {
			    			 
			    			ArrayList<Date> dateList = useNatty(value);
			    			outputArray[j] = dateConverter(dateList.get(0));
			    			 
			    		 }
			    		 
			    		else {
			    			 	 
			    			 outputArray[j] = value;
			    		 
			    		 }
			    	
			    	}
			    		//outputArray[TASK_ID_INDEX] = extractAttribute(inputArray,keyWords[0]);
			    		//outputArray[TASK_NAME_INDEX] = extractAttribute(inputArray,keyWords[1]);
			    		//outputArray[TASK_LOCATION_INDEX] = extractAttribute(inputArray,keyWords[5]);
			    		//outputArray[TASK_DETAILS_INDEX] = extractAttribute(inputArray,keyWords[6]);
			    		//outputArray[TASK_PRIORITY_INDEX] = extractAttribute(inputArray,keyWords[7]);
			    	
			    	//addCommand(inputArray, outputArray);
					
					break;
				case COMMAND_EDIT:
					
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					for(int i = 1; i < keyWords.length; i++) {
			    		int j = i + 1;
			    		String value = extractAttribute(inputArray, keyWords[i]);
			    		if((keyWords[i].equals(DATE_FROM) || keyWords[i].equals(DATE_TO) || keyWords[i].equals(DATE_ON)) && value != null) {
			    			 
			    			ArrayList<Date> dateList = useNatty(value);
			    			outputArray[j] = dateConverter(dateList.get(0));
			    			 
			    		 }
			    		 
			    		else {
			    			 	 
			    			 outputArray[j] = value;
			    		 
			    		 }
			    	
			    	}
					
					break;
				case COMMAND_DELETE:
					
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					
					break;
				case COMMAND_VIEW:
					
					break;
					
			}
			
			inputArray = outputArray;
			
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println(ERROR_EXCEPTION);
				
		}
    	return inputArray;
    	
    }
    
    private String processCommand(String command) {
    	
    	String processedCommand = command.toLowerCase();
    	
    	return processedCommand;
    	
    }
    
    //extractor
	private String extractAttribute(String[] input, String keyWord) {
			
			StringBuilder strb = new StringBuilder();
		
			String attribute = null;
		
			try {	
			for(int i = 0; i < input.length; i++) {
				
				if(input[i].equals(keyWord)) {
					
					int index = i+1;
					while(isNotKeyWord(input[index]) && index < input.length) {
						
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
	
	
	private boolean isNotKeyWord(String str) {
		
		for(int i = 0; i < keyWords.length; i++) {
			
			if(str.equals(keyWords[i])) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
    public static void main(String[] args) {
    
    	
    	
    	FlexiParser test1 = new FlexiParser();
    	
    	
    	String[] temp = test1.parseText("editTask ID 1001 Title meeting with huehue On June 20th 20:00 To 16:00");
    
    	//test1.useNatty("to March 24 2015");
    	
    	
    		
    	for(int i=0;i<temp.length;i++) {
    		
    		System.out.println(temp[i]);
    		
    	}
    	//System.out.println();
    	//System.out.println();
    	

    

    	
    }
}