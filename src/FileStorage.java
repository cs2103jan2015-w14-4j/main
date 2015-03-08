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
	
	private static final String EMPTY_INPUT = "null";
		
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
		for (int i = 0; i < taskList.size(); i++) {
            
        	Task tempTask = taskList.get(i);
        	String[] taskArray = taskToStrArray(tempTask);
        	for(int j = 0; j < taskArray.length; j++) {
        		
        		
        		
        		if ( j != taskArray.length-1) {
        			
        			taskArray[j] += (",");
        			bw.write(taskArray[j]);
        			
        		}
        		
        		else {
        			
        			bw.write(taskArray[j]);
        			
        		}
        		
        	}
        	bw.newLine();
        }

        bw.close();
    	
    	}catch(Exception e) {
    		
    		System.out.println("Exception occurred");
    		
    	 }
        
	}
    
    
    //no priority yet
    private String[] taskToStrArray(Task tempTask) {
    	
    	String[] strArr = new String[8];
    	//NO ERROR CATCHING FOR NULL ITEM
    	
    	
    	strArr[0] = IntegerToString(tempTask.getTID());
    	
    	strArr[1] =	tempTask.getTaskName();
    	
    	Date temp = tempTask.getDateFrom();
    	
    	if(temp != null) {
        	
    		strArr[2] =	dateToString(temp);	
    	
    	}
    	
    	else {
    		
    		strArr[2] = null;
    		
    	}
    	
    	temp = tempTask.getDateTo();
    	
    	if(temp != null) {
    		
    		strArr[3] = dateToString(temp);
    	
    	}
    	
    	else {
    		
    		strArr[3] = null;
    		
    	}
    	
    	temp = tempTask.getDeadline();
    	
    	if(temp != null) {
    		
    		strArr[4] = dateToString(temp);
    	
    	}
    	
    	else {
    		
    	    strArr[4] = null;
    		
    	}
    	
    	if(tempTask.getLocation() != null) {
    	
    		strArr[5] = tempTask.getLocation();
    	
    	}
    	
    	else {
    		
    		strArr[5] = null;
    		
    	}
    	
    	if(tempTask.getDetails() != null) {
    	
    		strArr[6] = tempTask.getDetails();
    	
    	}
    	
    	else {
    		
    		strArr[6] = null;
    		
    	}
    	
    	int tempInt = tempTask.getPriority();
    	
    	if(IntegerToString(tempInt) != null) {
    	
    		strArr[7] = IntegerToString(tempInt);
    	
    	}
    	
    	else {
    		
    		strArr[7] = null;
    		
    	}
    	
    	return strArr;
    	
    }
    
    private String IntegerToString(int id ) {
    	
    	return Integer.toString(id);
    	
    }
    
    private String dateToString(Date date) {
    	
    	Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	String dateString = formatter.format(date);
    	return dateString;
    }
    //return add,.. same as parse format;
    public void readFromFile(TaskManager tm) throws ParseException {
        
        if(textFile.exists()) {
           
        	try {
                
        		Scanner sc = new Scanner(textFile);
                
                while (sc.hasNextLine()) {
                	String[] inputs = new String[9];
                	String[] tempStringArray = new String[8];
                	tempStringArray = sc.nextLine().split("\\s*,\\s*");
                	System.out.println(tempStringArray.length);
                	for(int i=0; i<tempStringArray.length;i++) {
                		
                		System.out.println(tempStringArray[i]);
                		
                	}
                	             	
                	inputs[COMMAND_TYPE] = "add";
                	inputs[TASK_ID] = tempStringArray[0];
 
                	
                	inputs[TASK_NAME] = tempStringArray[1];
                 
                    if(!tempStringArray[2].equals(EMPTY_INPUT)) {
                    	
                    	inputs[DATE_FROM] = tempStringArray[2];
                    
                    }
                    
                    else {
                    	
                    	inputs[DATE_FROM] = null;
                    	
                    }
                                       
                    if(!tempStringArray[3].equals(EMPTY_INPUT)) {
                        
                    	inputs[DATE_TO] = tempStringArray[3];
                    
                    }
                    
                    else {
                    	
                    	inputs[DATE_TO] = null;
                    	
                    }
                    
                    
                    if(!tempStringArray[4].equals(EMPTY_INPUT)) {
                        
                    	inputs[DEADLINE] = tempStringArray[4];
                    
                    }
                    
                    else {
                    	
                    	inputs[DEADLINE] = null;
                    	
                    }
                    
                    if(!tempStringArray[5].equals(EMPTY_INPUT)) {
                        
                    	inputs[LOCATION] = tempStringArray[5];
                    
                    }
                    
                    else {
                    	
                    	inputs[LOCATION] = null;
                    	
                    }

                    if(!tempStringArray[6].equals(EMPTY_INPUT)) {
                        
                    	inputs[DETAILS] = tempStringArray[6];
                    
                    }
                    
                    else {
                    	
                    	inputs[DETAILS] = null;
                    	
                    }
                    
                    
                    if(!tempStringArray[7].equals(EMPTY_INPUT)) {
                        
                    	inputs[PRIORITY] = tempStringArray[7];
                    	
                    }
                    
                    else {
                    	
                    	inputs[PRIORITY] = null;
                    	
                    }
                  
                    tm.processAddForInitialization(inputs);       
                }
                
                sc.close();
            
        	}catch(FileNotFoundException e) {
                
            	e.printStackTrace();
            
            }

        }
    
    }
}