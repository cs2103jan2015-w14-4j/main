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
	
	private static final int SHORTCUT_NAME_INDEX = 1;
	private static final int SHORTCUT_ID_INDEX = 2;
	
	private static final String TYPE_TASK = "task";
	private static final String TYPE_SHORTCUT = "shortcut";
	private static final String TYPE_TEMPLATE = "template";
	
	private static final String ERROR_EXCEPTION = "Exception caught";
	
	private static final String DEFAULT_FILENAME = "default";
	private static final String EMPTY_INPUT = "null";
		
	private static File textFile;
	//include the two secret files;
    private File templateFile = new File("template");
    private File shortcutFile = new File("shortcut");
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
        	if(!templateFile.exists()) {
                
            	templateFile.createNewFile();
            
            }
        	if(!shortcutFile.exists()) {
                
            	shortcutFile.createNewFile();
            
            }

         }catch(Exception e) {
             
        	 System.out.println(ERROR_EXCEPTION); 
         
         }
		
	} 
	
    public FileStorage() {
    	
		textFile = new File(DEFAULT_FILENAME);
    	
	} 
    
    //stub
    public FileStorage(String fileName, TaskManager myTaskList, Template myTemplate, Shortcut myShortcut) {
    	//stub
//    	textFile = new File(fileName);
		try {
            new FileStorage(fileName);
//        	if(!textFile.exists()) {
//            
//            	textFile.createNewFile();
//            
//            }

         }catch(Exception e) {
             
        	 System.out.println(ERROR_EXCEPTION); 
         
         }
    	//TODO read from file and add respective info to the 3 parts
		//to call TM, same as before
		//to call template, use processCustomizingCommand(String[] cmd) 
		//	cmd = {"addTemplate", , <template name>,<datefrom>......} //same as task manager 
		//to call shortcut, use processShortcutCommand(String[] command)
		// cmd = {"addShortcutINIT", shortcutname, id} // id the the row, eg addTask = row 0, deleteTask = row 3
														// refer to shortcut keywords for the row
    }
    //TODO update template file
    //@param given an arraylist of task, update the the template file
    public void writeTemplateToFile(ArrayList<Task> templateList, ArrayList<String> matchingName) {
    	try {
        	//write template like normal tasks must check what is different is everything null
        	templateFile.delete();
    		templateFile.createNewFile();
    		BufferedWriter bw = new BufferedWriter(new FileWriter(templateFile));
    		for (int i = 0; i < templateList.size(); i++) {
                
            	Task tempTask = templateList.get(i);
            	String templateName = matchingName.get(i);
            	String[] taskArray = taskToStringArray(tempTask, templateName);
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
    
    
    
	//TODO update shortcut file
    //@param shortcuts String[][] of size 9 each string[i] keeps a variable length represent certain keyword 
    //store in order , one row is one id during retrieve
    public void writeShortcutToFile(String[][] shortcuts) {
    	try {
        	
        	shortcutFile.delete();
        	shortcutFile.createNewFile();
    		BufferedWriter bw = new BufferedWriter(new FileWriter(shortcutFile));
    		//shortcut first column all the different shortcut each row is the mapped shortcuts
    		for(int i=0;i<shortcuts.length;i++) {
    			for(int j=0;j<shortcuts[i].length;j++) {
    				if ( j != shortcuts[i].length-1) {
            			
            			shortcuts[i][j] += (",");
            			bw.write(shortcuts[i][j]);
            			
            		}
            		
            		else {
            			
            			bw.write(shortcuts[i][j]);
            			
            		}
    				
    				
    			}
    			
    			bw.newLine();
    			
    		}
            bw.close();
        	
        	}catch(Exception e) {
        		
        		System.out.println(ERROR_EXCEPTION);
        		
        	 }
    }
    
    //stores each task as a string delimited by ,
    public void writeToFile(ArrayList<Task> taskList) {
    	
    	try {
    	
    	textFile.delete();
		textFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
		for (int i = 0; i < taskList.size(); i++) {
            
        	Task tempTask = taskList.get(i);
        	String[] taskArray = taskToStringArray(tempTask, null);
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
    private String[] taskToStringArray(Task tempTask, String tempName) {
    	
    	String[] strArr = new String[8];

    	//NO ERROR CATCHING FOR NULL ITEM
    	if(tempName == null) {
    		strArr[0] = Integer.toString(tempTask.getTID());
    	} else {
    		strArr[0] = tempName;
    	}
    	
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
    
    public void readTemplateFromFile(Template template) throws ParseException {
    	//needs a different one because format may be diff slightly
    	//not just textFile
        if(templateFile.exists()) {
           
        	try {
                
        		Scanner sc = new Scanner(templateFile);
                while (sc.hasNextLine()) {
                	
                	
                	String[] inputs = new String[9];
                	String[] tempStringArray = new String[8];
                	String test = sc.nextLine();
                	System.out.println(test);
                	if(test.length() == 0) return;
//                	tempStringArray = sc.nextLine().split("\\s*,\\s*");
                	tempStringArray = test.split("\\s*,\\s*");          	
                	inputs[COMMAND_TYPE_INDEX] = "addTemplateInit";
                	inputs[TASK_ID_INDEX] = tempStringArray[0];
 
                	
                	inputs[TASK_NAME_INDEX] = tempStringArray[1];
                 
                    if(isEmptyInput(tempStringArray[2])) {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = tempStringArray[2];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = null;
                    	
                    }
                                       
                    if(isEmptyInput(tempStringArray[3])) {
                        
                    	inputs[TASK_DATE_TO_INDEX] = tempStringArray[3];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_TO_INDEX] = null;
                    	
                    }
                    
                    
                    if(isEmptyInput(tempStringArray[4])) {
                        
                    	inputs[TASK_DEADLINE_INDEX] = tempStringArray[4];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DEADLINE_INDEX] = null;
                    	
                    }
                    
                    if(isEmptyInput(tempStringArray[5])) {
                        
                    	inputs[TASK_LOCATION_INDEX] = tempStringArray[5];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_LOCATION_INDEX] = null;
                    	
                    }

                    if(isEmptyInput(tempStringArray[6])) {
                        
                    	inputs[TASK_DETAILS_INDEX] = tempStringArray[6];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DETAILS_INDEX] = null;
                    	
                    }
                    
                    
                    if(isEmptyInput(tempStringArray[7])) {
                        
                    	inputs[TASK_PRIORITY_INDEX] = tempStringArray[7];
                    	
                    }
                    
                    else {
                    	
                    	inputs[TASK_PRIORITY_INDEX] = null;
                    	
                    }
                    for(int i =0; i < inputs.length; ++i) {
                    	System.out.print(inputs[i] + ":");
                    }
                    try {
                    	template.processCustomizingCommand(inputs);     
                    } catch(Exception e) {
                    	
                    }
                      
               
                }
                
                sc.close();
            
        	}catch(FileNotFoundException e) {
                
            	e.printStackTrace();
            
            }

        }
    
    	
    }
    
    public void readShortcutFromFile(Shortcut shortcut) {
    	if(shortcutFile.exists()) {
            
        	try {
                
        		Scanner sc = new Scanner(shortcutFile);
                int i = 0;
                while (sc.hasNextLine()) {
                	
                	
                	String[] inputs = new String[3];
                	String[] tempStringArray = new String[8];
                	inputs[COMMAND_TYPE_INDEX] = "addShortcutInit";
                	tempStringArray = sc.nextLine().split("\\s*,\\s*");
                	for(int j = 0; j < tempStringArray.length; ++j) {
                		System.out.println(tempStringArray[j]);
	                	inputs[SHORTCUT_NAME_INDEX] = tempStringArray[j];
	 
	                	
	                	inputs[SHORTCUT_ID_INDEX] = Integer.toString(i);
	                 
	                    shortcut.processShortcutCommand(inputs);    
                	}
                	++i;
               
                }
                
                sc.close();
            
        	}catch(FileNotFoundException e) {
                
            	e.printStackTrace();
            
            }

        }
    	
    }
    
    /**
     * extracts each line from specified file as String array
     * and pass it as parameter to logic via processAddForInitialization
     * method. This is for normal and template.
     */    
    public void readFromFile(TaskManager tm) throws ParseException {
        //not just textFile
        if(textFile.exists()) {
           
        	try {
                
        		Scanner sc = new Scanner(textFile);
                
                while (sc.hasNextLine()) {
                	
                	
                	String[] inputs = new String[9];
                	String[] tempStringArray = new String[8];
                	
                	tempStringArray = sc.nextLine().split("\\s*,\\s*");
//                	System.out.println(tempStringArray.length);
//                	
//                	for(int i=0; i<tempStringArray.length;i++) {
//                		
//                		System.out.println(tempStringArray[i]);
//                		
//                	}
                	             	
                	inputs[COMMAND_TYPE_INDEX] = "addTask";
                	inputs[TASK_ID_INDEX] = tempStringArray[0];
 
                	
                	inputs[TASK_NAME_INDEX] = tempStringArray[1];
                 
                    if(isEmptyInput(tempStringArray[2])) {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = tempStringArray[2];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_FROM_INDEX] = null;
                    	
                    }
                                       
                    if(isEmptyInput(tempStringArray[3])) {
                        
                    	inputs[TASK_DATE_TO_INDEX] = tempStringArray[3];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DATE_TO_INDEX] = null;
                    	
                    }
                    
                    
                    if(isEmptyInput(tempStringArray[4])) {
                        
                    	inputs[TASK_DEADLINE_INDEX] = tempStringArray[4];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DEADLINE_INDEX] = null;
                    	
                    }
                    
                    if(isEmptyInput(tempStringArray[5])) {
                        
                    	inputs[TASK_LOCATION_INDEX] = tempStringArray[5];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_LOCATION_INDEX] = null;
                    	
                    }

                    if(isEmptyInput(tempStringArray[6])) {
                        
                    	inputs[TASK_DETAILS_INDEX] = tempStringArray[6];
                    
                    }
                    
                    else {
                    	
                    	inputs[TASK_DETAILS_INDEX] = null;
                    	
                    }
                    
                    
                    if(isEmptyInput(tempStringArray[7])) {
                        
                    	inputs[TASK_PRIORITY_INDEX] = tempStringArray[7];
                    	
                    }
                    
                    else {
                    	
                    	inputs[TASK_PRIORITY_INDEX] = null;
                    	
                    }
                  
                    tm.processInitialization(inputs);       
               
                }
                
                sc.close();
            
        	}catch(FileNotFoundException e) {
                
            	e.printStackTrace();
            
            }

        }
    
    }
    
    private boolean isEmptyInput(String str) {
    	
    	if(!str.equals(EMPTY_INPUT)) {
    		
    		return true;
    		
    	}
    	else {
    		
    		return false;
    		
    	}
    	
    }
    
}