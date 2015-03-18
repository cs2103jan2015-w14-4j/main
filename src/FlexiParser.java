//once constructed can keep parsing
public class FlexiParser {
	
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_VIEW = "view";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_EDIT = "edit";
	
    private static final String TID_NOT_EXIST = null;
    
    public static final int LENGTH_COMMAND = 9;
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
    
    private static String[] inputArray;
	
    public FlexiParser() {
		
		
	}
	
    public String[] parseText(String userInput) {
    	
    	try {
		    
			inputArray = userInput.split("\\s*,\\s*");
			
			
			String[] outputArray = new String[LENGTH_COMMAND];
			outputArray[COMMAND_TYPE_INDEX] = inputArray[COMMAND_TYPE_INDEX];
			
			
			
			
			switch(inputArray[COMMAND_TYPE_INDEX]) {
				
			    case COMMAND_ADD:
					
			    	//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_NAME_INDEX] = inputArray[1];
					outputArray[TASK_ID_INDEX] = TID_NOT_EXIST;
					addCommand(inputArray, outputArray);
					
					break;
				case COMMAND_EDIT:
					
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_ID_INDEX] = inputArray[TASK_ID_INDEX];
					editCommand(inputArray, outputArray);
					
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
    
	private void addCommand(String[] input,String[] outputArray) {
		String timeF = null;
		String dateF = null;
		String timeT = null;
		String dateT = null;
		for(int i = 2; i < input.length; ++i) {
			
			if(inputArray[i].equals("from")) {
				
				//WARNING: No checking for time input format
				timeF = inputArray[++i];
			
			}
			
			else if(inputArray[i].equals("to")) {
				
				//WARNING: No checking for time input format
				timeT = inputArray[++i];
			
			}
			else if(inputArray[i].equals("on")) {
				
				//WARNING: No checking for date input format
				//ASSUMPTION: No across midnight task 
				dateF = dateT = inputArray[++i];
			
			}
			else if(inputArray[i].equals("at")) {
				
				outputArray[TASK_LOCATION_INDEX] = inputArray[++i];
			
			}
		}
		
		if(dateF != null && timeF != null) {
			
			outputArray[TASK_DATE_FROM_INDEX] = dateF + " " + timeF;
		}
		
		if(dateT != null && timeT != null) {
			
			outputArray[TASK_DATE_TO_INDEX] = dateT + " " + timeT;
		}	
		
	}
	
	private void editCommand(String[] input,String[] outputArray) {
		
		String timeF = null;
		String dateF = null;
		String timeT = null;
		String dateT = null;
		for(int i = 1; i < input.length; ++i) {
			
			if(inputArray[i].equals("from")) {
				
				//WARNING: No checking for time input format
				timeF = inputArray[++i];
			
			}
			
			else if(inputArray[i].equals("name")) {
				
				//WARNING: No checking for time input format
				outputArray[TASK_NAME_INDEX] = inputArray[++i];
			
			}
			
			else if(inputArray[i].equals("to")) {
				
				//WARNING: No checking for time input format
				timeT = inputArray[++i];
			}
			
			else if(inputArray[i].equals("on")) {
				
				//WARNING: No checking for date input format
				//ASSUMPTION: No across midnight task 
				dateF = dateT = inputArray[++i];
			}
			
			else if(inputArray[i].equals("at")) {
			
				outputArray[TASK_LOCATION_INDEX] = inputArray[++i];
			
			}
		
		}
		
		if(dateF != null && timeF != null) {
			
			outputArray[TASK_DATE_FROM_INDEX] = dateF + " " + timeF;
			
		}
		
		if(dateT != null && timeT != null) {
			
			outputArray[TASK_DATE_TO_INDEX] = dateT + " " + timeT;
			
		}
	
	}
	
    //getter
    public String[] getStringArray() {
    	
    	return inputArray;
    	
    }
    
    public static void main(String[] args) {
    	
    	FlexiParser test1 = new FlexiParser();
    	
    	String[] temp = test1.parseText("add,meeting,on,24/03/2015,from,14:00,to,16:00");
    	for(int i = 0; i < temp.length; ++i) {
    	
    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
    		System.out.print("| ");
    	
    	}
    	
    	System.out.println();
    	System.out.println();
    	
    

    	
    }
}