import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileStorage {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    
    private static final int COMMAND_TYPE_INDEX = 0;
    private static final int INDEX_TASK_ID_READ_TASK_FROM_FILE = 1;
    //The indexes for how a task object is stored in an ArrayList<Task>
    private static final int TASK_ID_INDEX = 0;
    private static final int TEMPLATE_NAME_INDEX = 0;
    private static final int TASK_NAME_INDEX = 1;
    private static final int TASK_DATE_FROM_INDEX = 2;
    private static final int TASK_DATE_TO_INDEX = 3;
    private static final int TASK_DEADLINE_INDEX = 4;
    private static final int TASK_LOCATION_INDEX = 5;
    private static final int TASK_DETAILS_INDEX = 6;
    private static final int TASK_STATUS_INDEX = 7;
    
    
    private static final String ADD_TASK_COMMAND = "addTask";
    private static final String ADD_TEMPLATE_INIT_COMMAND = "addTemplateInit";
    
    //for file location
    private static final char SLASH = '/';
    private static final char BACKSLASH = '\\';
    
    //Error message
    private static final String MSG_ERR_MOVE_FILE = "File is not moved successfully, "
            + "please try again with another location or a different file name.";
        
    private static final int SHORTCUT_NAME_INDEX = 1;
    private static final int SHORTCUT_ID_INDEX = 2;
    private static final String DEFAULT_DELIMITER = "||";
    private static final int DEFAULT_STRING_SIZE = 9;
    private static final int TEMP_STRING_SIZE = 8;
    
    private static final String NULL_INPUT = "null";
    private static final String DEFAULT_TASK_FILE_NAME = "default.txt";
    private static final String DEFAULT_TEMPLATE_FILE_NAME = "template";
    private static final String DEFAULT_SHORTCUT_FILE_NAME = "shortcut";
    private static final String DEFAULT_LOCATION_FILE_NAME = "location";


    
    private File taskFile;
    private File templateFile;
    private File shortcutFile;
    private File location = new File(DEFAULT_LOCATION_FILE_NAME);
    private String taskFileLocation;
    private String path;

    
    //--------------------constructor method starts-----------------
    /**
     * This constructor reads the location of task file from a file named location; 
     * then it creates File object for tasks, templates and shortcuts.
     * If location file does not exist, it will be created with default contents.
     */
    //@author A0118892U
    public FileStorage() {
        if(!location.exists()){
            initializeFileStorageDefault();
        } else {
            initializeFileStorageFromALocation();
        }
    }

    /**
     * This method initializes File object for tasks, templates and shortcuts 
     * from their default locations.
     */
    //@author A0118892U
    private void initializeFileStorageDefault() {
        location = new File(DEFAULT_LOCATION_FILE_NAME);
        try {
            writeNewFileLocationToFile(DEFAULT_TASK_FILE_NAME);

            createTaskFile(DEFAULT_TASK_FILE_NAME);
            createTemplateFile(DEFAULT_TEMPLATE_FILE_NAME);
            createShortcutFile(DEFAULT_SHORTCUT_FILE_NAME);      
        } catch (IOException e) {
            assert(true);
        }
    }

    /**
     * This method creates a File object for tasks with the given file name.
     * If the file with that name is not found, it creates a file.
     * @param taskFileName  the file name for the task file
     * @throws IOException
     */
    //@author A0118892U
    private void createTaskFile(String taskFileName) throws IOException {
        taskFile = new File(taskFileName);
        if(!taskFile.exists()) {
            taskFile.createNewFile();           
        }
    }

    /**
     * This method creates a File object for templates with the given file name.
     * If the file with that name is not found, it creates a file.
     * @param templateFileName  the file name for the template file
     * @throws IOException
     */
    //@author A0118892U
    private void createTemplateFile(String templateFileName) throws IOException {
        templateFile = new File(templateFileName);
        if(!templateFile.exists()) {
            templateFile.createNewFile();
        }
    }

    /**
     * This method creates a File object for shortcuts with the given file name.
     * If the file with that name is not found, it creates a file.
     * @param shortcutFileName  the file name for the shortcut file
     * @throws IOException
     */
    //@author A0118892U
    private void createShortcutFile(String shortcutFileName) throws IOException {
        shortcutFile = new File(shortcutFileName);
        if(!shortcutFile.exists()) {
            shortcutFile.createNewFile();
        }
    }

    /**
     * This method reads the location where task file is saved from the location file.
     */
    //@author A0118892U
    private void getTaskFileLocation() {
        try {
            Scanner sc = new Scanner(location);
            taskFileLocation = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            assert(true);
        } 
    }

    /**
     * This method initializes File object for tasks, templates and shortcuts 
     * from location stores in the location file.
     */
    //@author A0118892U
    private void initializeFileStorageFromALocation() {
        getTaskFileLocation();
        path = getPath(taskFileLocation);

        try {
            createTaskFile(taskFileLocation);
            createTemplateFile(path + DEFAULT_TEMPLATE_FILE_NAME);
            createShortcutFile(path + DEFAULT_SHORTCUT_FILE_NAME);
            taskFile = new File(taskFileLocation);
        } catch (IOException e) {
            assert(true);
        }
    }
    //--------------------constructor method ends-----------------


    //--------------------other methods-----------------
    /**
     * This method moves tasks, templates and shortcuts file to the new path
     * specified in the new file name for tasks
     * @param newFileName  The new file name where user wants to save it
     * @throw IOException  Unsuccessful move of files
     */
    //@author A0118892U
    public void saveToAnotherLocation(String newFileName) throws IOException {
        boolean isSaveSuccessful = taskFile.renameTo(new File(newFileName));

        if(isSaveSuccessful) {
            writeNewFileLocationToFile(newFileName);
            taskFile = new File(newFileName);
            path = getPath(newFileName);

            templateFile.renameTo(new File(path + DEFAULT_TEMPLATE_FILE_NAME));
            templateFile = new File(path + DEFAULT_TEMPLATE_FILE_NAME);

            shortcutFile.renameTo(new File(path + DEFAULT_SHORTCUT_FILE_NAME));
            shortcutFile = new File(path + DEFAULT_SHORTCUT_FILE_NAME);
        } else {
            throw new IOException(MSG_ERR_MOVE_FILE);
        }
    }

    /**
     * This method extracts the path form the location where tasks file is saved
     * @param fileName  the path and name the task file is saved
     * @return          the path where task file is saved
     */
    //@author A0118892U
    private String getPath(String fileName) {
        String path = "";
        if(isSlashInFileName(fileName)) {
            int indexAfterLastSlash = fileName.lastIndexOf(SLASH) + 1;
            path = fileName.substring(0, indexAfterLastSlash);
        } else if (isBackslashInFileName(fileName)) {
            int indexAfterLastBackSlash = fileName.lastIndexOf(BACKSLASH) + 1;
            path = fileName.substring(0, indexAfterLastBackSlash);
        }
        return path;
    }

    /**
     * This method checks whether fileName contains slash("/")
     * @param fileName  file name and its path
     * @return          true if input contains slash("/")
     */
    private boolean isSlashInFileName(String fileName) {
        return fileName.indexOf(SLASH) != -1;
    }

    /**
     * This method checks whether fileName contains backslash("\")
     * @param fileName  file name and its path
     * @return          true if input contains backslash("\")
     */
    private boolean isBackslashInFileName(String fileName) {
        return fileName.indexOf(BACKSLASH) != -1;
    }

    /**
     * This methods writes the new location and file name where tasks file is store 
     * into the location file
     * @param fileName  the file name where tasks file is stored
     */
    //@author A0118892U
    private void writeNewFileLocationToFile(String fileName) {
        try {
            location.delete();
            location.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(location));
            bw.write(fileName);
            bw.close();
        } catch (IOException e) {
            assert(true);
        }
    }


    //----------tasks read and write methods starts----------
    /**
     * This method extracts each line from specified file as String array
     * and passes it as parameter to processInitialization method in TaskManager.
     * @param tm  TaskManager object to call its processInitialization method
     */    
    //@author A0118892U
    public void readTaskFromFile(TaskManager tm) {
        if(taskFile.exists()) {
            try {
                Scanner sc = new Scanner(taskFile);

                while (sc.hasNextLine()) {
                    String[] inputs = new String[DEFAULT_STRING_SIZE];
                    inputs[COMMAND_TYPE_INDEX] = ADD_TASK_COMMAND;
                    int index = INDEX_TASK_ID_READ_TASK_FROM_FILE;
                    StringTokenizer st = new StringTokenizer(sc.nextLine(), DEFAULT_DELIMITER);

                    while (st.hasMoreElements()) {
                        String nextStr = st.nextElement().toString();
                        if(isEmptyInput(nextStr)) {
                            inputs[index] = null;
                        } else {
                            inputs[index] = nextStr;
                        }
                        ++index;
                    }

                    tm.processInitialization(inputs);
                }

                sc.close();      
            } catch (FileNotFoundException e) {
                assert(true);
            }
        }   
    }

    /**
     * If the string read is "null", this means the data should be set to null
     * @param str  string from task file or template file
     * @return     true if string is null, else false
     */
    //@author A0118892U
    private boolean isEmptyInput(String str) {
        if(str.equals(NULL_INPUT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method writes tasks back to the specified file with specific format
     * @param taskList  ArrayList of tasks to be written to file
     */
    //@author A0116514N
    public void writeTaskToFile(ArrayList<Task> taskList) {
        try {
            taskFile.delete();
            taskFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(taskFile));

            for (int i = 0; i < taskList.size(); i++) {
                Task tempTask = taskList.get(i);
                String[] taskArray = convertTaskToStringArray(tempTask, null);
                for(int j = 0; j < taskArray.length; j++) {
                    if ( j != taskArray.length - 1) {
                        taskArray[j] += (DEFAULT_DELIMITER);
                        bw.write(taskArray[j]);
                    } else {
                        bw.write(taskArray[j]);
                    }
                }

                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            assert(true);
        }
    }
    //----------task read and write methods ends----------


    //----------shortcut read and write methods starts----------
    /**
     * This method extracts each line from specified file as 2D array
     * and passes it as parameter to processShortcutCommand method.
     * If there is nothing in the shortcut file, all shortcut will be reset by SystemHandler.
     * @param shortcut  Shortcut object to call its processShortcutCommand method
     */
    //@author A0116514N
    public void readKeywordFromFile(KeywordManager shortcut) {
        if(shortcutFile.exists()) {
            if(shortcutFile.length() == 0) {
                SystemHandler system = SystemHandler.getSystemHandler();
                system.resetKeywordToDefault();
            } else {
                try {
                    Scanner sc = new Scanner(shortcutFile);
                    int i = 0;

                    while (sc.hasNextLine()) {
                        String[] inputs = new String[3];
                        String[] tempStringArray = new String[8];
                        inputs[COMMAND_TYPE_INDEX] = "addKeywordInit";
                        tempStringArray = sc.nextLine().split("\\s*,\\s*");
                        for(int j = 0; j < tempStringArray.length; ++j) {
                            inputs[SHORTCUT_NAME_INDEX] = tempStringArray[j];
                            inputs[SHORTCUT_ID_INDEX] = Integer.toString(i);
                            shortcut.processKeywordCommand(inputs);    
                        }

                        ++i;
                    }

                    sc.close();
                }catch(FileNotFoundException e) {
                    assert(true);
                }
            }
        }
    }

    /**
     * This method writes shortcuts back to the specified file with specific format
     * @param shortcuts  String[][] of size 9 each string[i] keeps a variable length represent certain keyword 
     *                   store in order, one row is one id during retrieve
     */
    //@author A0116514N
    public void writeKeywordToFile(String[][] shortcuts) {
        try {
            shortcutFile.delete();
            shortcutFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(shortcutFile));
            //shortcut first column all the different shortcut each row is the mapped shortcuts
            for(int i = 0; i<shortcuts.length; i++) {
                for(int j = 0; j<shortcuts[i].length; j++) {
                    if ( j != shortcuts[i].length - 1) {
                        shortcuts[i][j] += (",");
                        bw.write(shortcuts[i][j]);
                    } else {
                        bw.write(shortcuts[i][j]);
                    }
                }

                bw.newLine();
            }

            bw.close();
        } catch(Exception e) {
            assert(true);
        }
    }
    //----------shortcut read and write methods ends----------


    //----------template read and write methods starts----------
    /**
     * This method updates template file
     * @param templateList  given an ArrayList of task, update the the template file
     * @param matchingName  the name of each template
     */
    //@author A0116514N
    public void writeTemplateToFile(ArrayList<Task> templateList, 
            ArrayList<String> matchingName) {
        try {
            //write template like normal tasks must check what is different is everything null
            templateFile.delete();
            templateFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(templateFile));
            for (int i = 0; i < templateList.size(); i++) {

                Task tempTask = templateList.get(i);
                String templateName = matchingName.get(i);
                String[] taskArray = convertTaskToStringArray(tempTask, templateName);
                for(int j = 0; j < taskArray.length; j++) {

                    if (j != taskArray.length - 1) {
                        taskArray[j] += (DEFAULT_DELIMITER);
                        bw.write(taskArray[j]);
                    } else {
                        bw.write(taskArray[j]);
                    }
                }
                bw.newLine();
            }
            bw.close();
        } catch(Exception e) {
            assert(true);
        }
    }

    /**
     * This method converts Task object to a string array of size of size 8.
     * If the templateName is not null, puts the task ID in the string array;
     * else puts the templateName in the string array.
     * @param tempTask      the task to be converted to String array
     * @param templateName  the template name to be converted to String array
     * @return
     */
    //@author A0116514N
    private String[] convertTaskToStringArray(Task tempTask, String templateName) {
        String[] strArr = new String[TEMP_STRING_SIZE];

        if(templateName == null) {
            strArr[TASK_ID_INDEX] = Integer.toString(tempTask.getTID());
        } else {
            strArr[TEMPLATE_NAME_INDEX] = templateName;
        }

        strArr[TASK_NAME_INDEX] =	tempTask.getTaskName();

        Date tempDateFrom = tempTask.getDateFrom();

        if(tempDateFrom != null) {
            strArr[TASK_DATE_FROM_INDEX] =	convertToStringFromDate(tempDateFrom);	
        } else {
            strArr[TASK_DATE_FROM_INDEX] = null;
        }

        Date tempDateTo = tempTask.getDateTo();

        if(tempDateTo != null) {
            strArr[TASK_DATE_TO_INDEX] = convertToStringFromDate(tempDateTo);
        } else {
            strArr[TASK_DATE_TO_INDEX] = null;
        }

        Date tempDeadline = tempTask.getDeadline();
        if(tempDeadline != null) {
            strArr[TASK_DEADLINE_INDEX] = convertToStringFromDate(tempDeadline);
        } else {
            strArr[TASK_DEADLINE_INDEX] = null;
        }

        if(tempTask.getLocation() != null) {
            strArr[TASK_LOCATION_INDEX] = tempTask.getLocation();
        } else {
            strArr[TASK_LOCATION_INDEX] = null;
        }

        if(tempTask.getDetails() != null) {
            strArr[TASK_DETAILS_INDEX] = tempTask.getDetails();
        } else {
            strArr[TASK_DETAILS_INDEX] = null;
        }

        int tempStatus = tempTask.getStatus();

        if(convertToStringFromInt(tempStatus) != null) {
            strArr[TASK_STATUS_INDEX] = convertToStringFromInt(tempStatus);
        } else {
            strArr[TASK_STATUS_INDEX] = null;
        }

        return strArr;
    }

    /**
     * This method converts an int type data to a String type
     * @param integer  int type data to be converted
     * @return         String type data of the input
     */
    //@author A0118892U
    private String convertToStringFromInt(int integer) {
        return Integer.toString(integer);
    }

    /**
     * This method converts an Date type data to a String type with this default format 
     * ("dd/MM/yyyy HH:mm")
     * @param date  date type data to be converted
     * @return      String type data of the input with this format "dd/MM/yyyy HH:mm"
     */
    //@author A0118892U
    private String convertToStringFromDate(Date date) {
        Format formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * This method extracts each line from specified file as String array
     * and passes it as parameter to processCustomizingCommand method in Template.     
     * @param template  Template to call its processCustomizingCommand method
     */
    //@author A0116514N
    public void readTemplateFromFile(TemplateManager template) {
        if(templateFile.exists()) {

            try {
                Scanner sc = new Scanner(templateFile);
                while (sc.hasNextLine()) {
                    String[] inputs = new String[DEFAULT_STRING_SIZE];

                    inputs[COMMAND_TYPE_INDEX] = ADD_TEMPLATE_INIT_COMMAND;
                    int index = 1;
                    StringTokenizer st = new StringTokenizer(sc.nextLine(), DEFAULT_DELIMITER);

                    while (st.hasMoreElements()) {
                        String nextStr = st.nextElement().toString();
                        if(isEmptyInput(nextStr)) {
                            inputs[index] = null;
                        } else {
                            inputs[index] = nextStr;
                        }
                        ++index;
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
    //----------template read and write methods ends----------
}