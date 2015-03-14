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
	
	//The indexes for how a task object is stored in a ArrayList<Task>
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
	
	private static final String DEFAULT_FILENAME = "default.txt";
	private static final String EMPTY_INPUT = "null";
		
	private static File textFile;
    
	/**
	 * Constructor for FileStorage object, this will store the text file name
	 * in a global variable textFile for reference.
	 * If the file does not exist it will be created.
	 */
	public FileStorage(String fileName) {
    	
		textFile = new File(fileName);
		try {
            
        	if(!textFile.exists()) {
            
            	textFile.createNewFile();
            
            }

         }catch(Exception e) {
             
        	 System.out.println(ERROR_EXCEPTION); 
         
         }
		
	} 
	
    public FileStorage() {
    	
		textFile = new File(DEFAULT_FILENAME);
    	
	} 
	
    //stores each task as a string delimited by ,
    public void writeToFile(ArrayList<Task> taskList) {
    	
    	try {
    	
    	textFile.delete();
		textFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
		for (int i = 0; i < taskList.size(); i++) {
            
        	Task tempTask = taskList.get(i);
        	String[] taskArray = taskToStringArray(tempTask);
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
    		
    		System.out.println(ERROR_EXCEPTION);
    		
    	 }
        
	}
    
    
    //String array is size 8 as priority is not included yet
    private String[] taskToStringArray(Task tempTask) {
    	
    	String[] strArr = new String[8];

    	//NO ERROR CATCHING FOR NULL ITEM
    	strArr[0] = IntegerToString(tempTask.getTID());
    	strArr[1] =	tempTask.getTaskName();
    	
    	Date tempDate = tempTask.getDateFrom();
    	
    	if(tempDate != null) {
        	
    		strArr[2] =	dateToString(tempDate);	
    	
    	}
    	
    	else {
    		
    		strArr[2] = null;
    		
    	}
    	
    	tempDate = tempTask.getDateTo();
    	
    	if(tempDate != null) {
    		
    		strArr[3] = dateToString(tempDate);
    	
    	}
    	
    	else {
    		
    		strArr[3] = null;
    		
    	}
    	
    	tempDate = tempTask.getDeadline();
    	
    	if(tempDate != null) {
    		
    		strArr[4] = dateToString(tempDate);
    	
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
    
    private String IntegerToString(int id) {
    	
    	return Integer.toString(id);
    	
    }
    
    private String dateToString(Date date) {
    	
    	Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	String dateString = formatter.format(date);
    	
    	return dateString;
    
    }
    
    /**
     * extracts each line from specified file as String array
     * and pass it as parameter to logic via processAddForInitialization
     * method
     */    
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
                	             	
                	inputs[COMMAND_TYPE_INDEX] = "add";
                	inputs[TASK_ID_INDEX] = tempStringArray[0];
 
                	
                	inputs[TASK_NAME_INDEX] = tempStringArray[1];
                 
                    if(!tempStringArray[2].equals(EMPTY_INPUT)) {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = tempStringArray[2];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = null;
                    	
                    }
                                       
                    if(!tempStringArray[3].equals(EMPTY_INPUT)) {
                        
                    	inputs[TASK_DATE_TO_INDEX] = tempStringArray[3];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_TO_INDEX] = null;
                    	
                    }
                    
                    
                    if(!tempStringArray[4].equals(EMPTY_INPUT)) {
                        
                    	inputs[TASK_DEADLINE_INDEX] = tempStringArray[4];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DEADLINE_INDEX] = null;
                    	
                    }
                    
                    if(!tempStringArray[5].equals(EMPTY_INPUT)) {
                        
                    	inputs[TASK_LOCATION_INDEX] = tempStringArray[5];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_LOCATION_INDEX] = null;
                    	
                    }

                    if(!tempStringArray[6].equals(EMPTY_INPUT)) {
                        
                    	inputs[TASK_DETAILS_INDEX] = tempStringArray[6];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DETAILS_INDEX] = null;
                    	
                    }
                    
                    
                    if(!tempStringArray[7].equals(EMPTY_INPUT)) {
                        
                    	inputs[TASK_PRIORITY_INDEX] = tempStringArray[7];
                    	
                    }
                    
                    else {
                    	
                    	inputs[TASK_PRIORITY_INDEX] = null;
                    	
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