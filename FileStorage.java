import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileStorage {
	
	private static final int COMMAND_TYPE = 0;
	private static final int TASK_ID = 1;
	private static final int TASK_NAME = 2;
	private static final int DATE_FROM = 3;
	private static final int DATE_TO = 4;
	private static final int DEADLINE = 5;
	private static final int LOCATION = 6;
	private static final int DETAILS = 7;
	private static final int PRIORITY = 8;
	
	private static final String EMPTY_INPUT = "";
		
	private static File textFile;
    
	public FileStorage(String fileName) {
    	
		textFile = new File(fileName);
		try {
            
        	if(!textFile.exists()) {
            
            	textFile.createNewFile();
            
            }

         } 
         
		catch(Exception e) {
             
        	 System.out.println("exception occurred"); 
         
         }
		
	} 
	
    public FileStorage() {
    	
		textFile = new File("default.txt");
    	
	} 
	
    // takes the ||blah|| all of it at the end and store arraylist task
    public void writeToFile(ArrayList<Task> taskList) {
    	
    	try {
    	
    	textFile.delete();
		textFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
		String newLine="\n";
        for (int i = 0; i < taskList.size(); i++) {
            
        	Task tempTask = taskList.get(i);
        	String[] taskArray = taskToStrArray(tempTask);
        	for(int j = 0; j < taskArray.length; j++) {
        	
        	    bw.write(taskArray[j]);
        	
        	}
        	bw.write(newLine);
        }

        bw.close();
    	
    	}catch(Exception e) {
    		
    		System.out.println("Exception occurred");
    		
    	 }
        
	}
    
    
    //no priority yet
    private String[] taskToStrArray(Task tempTask) {
    	
    	String[] strArr = new String[8];
    	
    	strArr[0] = IntegerToString(tempTask.getTID());
    	strArr[1] =	tempTask.getTaskName();
    	strArr[2] =	dateToString(tempTask.getDateFrom());	
    	strArr[3] = dateToString(tempTask.getDateTo());
        strArr[4] = dateToString(tempTask.getDeadline());
    	strArr[5] = tempTask.getLocation();
    	strArr[6] = tempTask.getDetails();
    	strArr[7] = IntegerToString(tempTask.getPriority());
    	
    	return strArr;
    	
    }
    
    private String IntegerToString(int id ) {
    	
    	return Integer.toString(id);
    	
    }
    
    private String dateToString(Date date) {
    	
    	Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String dateString = formatter.format(date);
    	return dateString;
    }
    
    //return add,.. same as parse format;
    public void readFromFile(TaskManager tm) throws ParseException {
        
    	String[] inputs = new String[9];
        inputs[COMMAND_TYPE] = "add";
        
        if(textFile.exists()) {
           
        	try {
                
        		Scanner sc = new Scanner(textFile);
                
                while (sc.hasNextLine()) {
                    
                	inputs[TASK_ID] = sc.next();
                    inputs[TASK_NAME] = sc.next();
                    inputs[DATE_FROM] = sc.next();
                    if(!inputs[DATE_FROM].equals(EMPTY_INPUT)) {
                        inputs[DATE_FROM] += " " + sc.next();
                    
                    }
                    
                    inputs[DATE_TO] = sc.next();
                    if(!inputs[DATE_TO].equals(EMPTY_INPUT)) {
                        inputs[DATE_TO] += " " + sc.next();
                    }
                    
                    inputs[DEADLINE] = sc.next();
                    if(!inputs[DEADLINE].equals(EMPTY_INPUT)) {
                        inputs[DEADLINE] += " " + sc.next();
                    
                    }
                    
                    inputs[LOCATION] = sc.next();
                    inputs[DETAILS] = sc.next();
                    inputs[PRIORITY] = sc.next();
                    
                    tm.processAddForInitialization(inputs);       
                }
                
                sc.close();
            
        	}catch(FileNotFoundException e) {
                
            	e.printStackTrace();
            
            }

        }
    
    }
}