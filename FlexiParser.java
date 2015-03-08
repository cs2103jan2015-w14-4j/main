
public class FlexiParser {
	private static String[] inputArray;
	//private static final int regexForSplit = 2; 
	
	public FlexiParser(String userInput) {
		
		try {
		    
			//userInput = userInput.trim();
			//inputArray = userInput.split(" ");
			inputArray = userInput.split("\\s*,\\s*");
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println("exception occurred");
				
		}
		
		
	}

	
    //getter
    public String[] getStringArray() {
    	
    	return inputArray;
    	
    }
}
