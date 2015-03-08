public class FlexiParser {
	private static String[] inputArray;
	//private static final int regexForSplit = 2; 

	public static final int LENGTH_COMMAND = 8;


    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_VIEW = "view";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_EDIT = "edit";
	
    private static final String TID_NOT_EXIST = null;
    
    private static final int COMMAND_TYPE = 0;
    private static final int TID = 1;
    private static final int TASK_NAME = 2;
    private static final int DATE_FROM = 3;
    private static final int DATE_TO = 4;
    private static final int DEADLINE = 5;
    private static final int LOCATION = 6;
    private static final int DETAILS = 7;
    private static final int PRIORITY = 8;
    
	public FlexiParser(String userInput) {
		
		try {
		    
			//userInput = userInput.trim();
			//inputArray = userInput.split(" ");
			inputArray = userInput.split("\\s*,\\s*");
			
			
			String[] outputArray = new String[LENGTH_COMMAND];
			outputArray[COMMAND_TYPE] = inputArray[COMMAND_TYPE];
			
			
			
			
			switch(inputArray[COMMAND_TYPE]) {
				case COMMAND_ADD:
					//WARNING: NO CHECKING VALIDITY
					outputArray[TASK_NAME] = inputArray[1];
					outputArray[TID] = TID_NOT_EXIST;
					addCommand(inputArray, outputArray);
					break;
				case COMMAND_EDIT:
					//WARNING: NO CHECKING VALIDITY
					outputArray[TID] = inputArray[TID];
					editCommand(inputArray, outputArray);
					break;
				case COMMAND_DELETE:
					//WARNING: NO CHECKING VALIDITY
					outputArray[TID] = inputArray[TID];
					break;
				case COMMAND_VIEW:
					break;
			}
			
			inputArray = outputArray;
//			
//			
//			outputArray[TASK_NAME] = inputArray[TASK_NAME];
//			outputArray[DATE_FROM] = inputArray[DATE_FROM];
//			outputArray[DATE_TO] = inputArray[DATE_TO];
//			outputArray[DEADLINE] = inputArray[DEADLINE];
//			outputArray[LOCATION] = inputArray[LOCATION];
//			outputArray[DETAILS] = inputArray[DETAILS];
			
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println("exception occurred");
				
		}
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
				outputArray[LOCATION] = inputArray[++i];
			}
		}
		outputArray[DATE_FROM] = dateF + " " + timeF;
		outputArray[DATE_TO] = dateT + " " + timeT;
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
				outputArray[LOCATION] = inputArray[++i];
			}
		}
		if(dateF != null && timeF != null)
			outputArray[DATE_FROM] = dateF + " " + timeF;
		if(dateT != null && timeT != null)
			outputArray[DATE_TO] = dateT + " " + timeT;
	}
	
    //getter
    public String[] getStringArray() {
    	
    	return inputArray;
    	
    }
    
    public static void main(String[] args) {
    	FlexiParser test1 = new FlexiParser("add,meeting,on,24/03/2015,from,14:00,to,16:00");
    	System.out.println("ORIGINAL: "+"add,meeting,on,24/03/2015,from,14:00,to,16:00");
    	String[] temp = test1.getStringArray();
    	for(int i = 0; i < temp.length; ++i) {
    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
    		System.out.print("| ");
    	}
    	System.out.println();
    	System.out.println();
    	
    	FlexiParser test2 = new FlexiParser("delete,1222");
    	temp = test2.getStringArray();
    	System.out.println("ORIGINAL: "+"delete,1222");
    	for(int i = 0; i < temp.length; ++i) {
    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
    		System.out.print("| ");
    	}
    	System.out.println();
    	System.out.println();

    	FlexiParser test3 = new FlexiParser("view");
    	System.out.println("ORIGINAL: "+"view");
    	temp = test3.getStringArray();
    	for(int i = 0; i < temp.length; ++i) {
    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
    		System.out.print("| ");
    	}
    	System.out.println();
    	System.out.println();
    	
    	FlexiParser test4 = new FlexiParser("edit,1005,to,15:00,on,20/03/2015");
    	System.out.println("ORIGINAL: "+"edit,1005,to,15:00,on,20/03/2015");
    	temp = test4.getStringArray();
    	for(int i = 0; i < temp.length; ++i) {
    		System.out.print((temp[i] == null)? "NULL":temp[i].toString());
    		System.out.print("| ");
    	}
    	System.out.println();
    	System.out.println();
    }
}