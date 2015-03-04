import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseNFile {
    	
	public static void main(String[] args) {
    	
		String dummyInput = "add dummy Line";
    	//given that I got an input
		Parser pa = new Parser(dummyInput);
		FileHandler fh = new FileHandler();
		System.out.println("I am going to " + pa.getCommand() + " wait for it... " + pa.getText());
		
    }
}

//enum?
class Parser {
	
	private static final int regexForSplit = 2;
	private String command = "";
	private String text = "";
	
    public Parser(String userInput) {
    	
		try {
		    
			userInput = standardizeStr(userInput);
			extractCommandText(userInput);
			
			
		}catch(IllegalArgumentException ex) { 
			
			System.out.println("Illegal Argument Exception");
				
		}
    	
    }
    
    //can i just shortform userInput?
    private static String standardizeStr(String input) {
    	
        input = input.toUpperCase();
        input = input.trim();
        
        return input;
          	
    }
    
    private void extractCommandText(String input) {
	    
        String[] strArray;
        strArray = input.split(" ", regexForSplit); 
        command = strArray[0];
        text = strArray[1];
        
    }
    
    //getter
    public String getCommand() {
    	
        return command;
    	
    }
    
    public String getText() {
    	
        return text;
    	
    }
       
}

class FileHandler {
	//given no file name
	private static File textFile;
	private ArrayList<String> textList = new ArrayList<String>();
	
	public FileHandler() {
    	
		textFile = new File("random.txt");
    	
		try {
	            
	       	if(!textFile.exists()) {
	            
	           	textFile.createNewFile();
	            
	           }
	       	//not confirmed
	       /*	else {
	            	
	       		readFromFile(fileName);
	       	}*/
		} 
		catch(Exception e) {
			
			System.out.println("Exception occurred"); 
	         
		}
    	
	} 	
	//take in arraylist from Ma Cong?
    private void writeToFile(ArrayList<String> strList) {
    	
    	try {
    	
    	textFile.delete();
		textFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
		String newLine="\n";
        for (int i = 0; i < strList.size(); i++) {
               
        	bw.write(strList.get(i) + newLine);
        	
        }

        bw.close();
    	
    	}catch(Exception e) {
    		
    		System.out.println("Exception occurred");
    		
    	 }
        
	}
    
    private void readFromFile(String fileName) {
    	
    	try {
		    
			Scanner fsc = new Scanner(System.in);
		    File file = new File(fileName);
		    String tempStore;
		    fsc = new Scanner(file);
	
		    while(fsc.hasNextLine()) {
	        
		    	tempStore = fsc.nextLine();
		    	textList.add(tempStore);
		
		    }
		
		    fsc.close();
		
		}catch(Exception e) {
		     
			System.out.println("Exception occurred");	
			
		 }
    	
    }
    
    //getter
    public ArrayList<String> getList() {
    	
    	return textList;
    	
    }
    	
}

	
