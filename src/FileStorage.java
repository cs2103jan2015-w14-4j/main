import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

//@author A0118892U
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
    private static final String ADD_TASK_COMMAND = "addTask";
    private static final String ADD_TEMPLATE_INIT_COMMAND = "addTemplateInit";
    private static final char SLASH = '/';
    private static final char BACKSLASH = '\\';
    private static final String MSG_ERR_MOVE_FILE = "File is not moved successfully, "
            + "please try again with another location or a different file name.";


    private static final int SHORTCUT_NAME_INDEX = 1;
    private static final int SHORTCUT_ID_INDEX = 2;

    private static final String TYPE_TASK = "task";
    private static final String TYPE_SHORTCUT = "shortcut";
    private static final String TYPE_TEMPLATE = "template";
    private static final String DEFAULT_DELIMITER = "||";

    private static final String ERROR_EXCEPTION = "Exception caught";

    private static final boolean INPUT_IS_EMPTY = true;
    private static final boolean INPUT_IS_NOT_EMPTY = false;

    private static final int DEFAULT_STRING_SIZE = 9;
    private static final int TEMP_STRING_SIZE = 8;
    private static final String DEFAULT_FILENAME = "default";
    private static final String EMPTY_INPUT = "null";

    private static File taskFile;
    //include the two secret files;
    private final String DEFAULT_TASK_FILE_NAME = "default.txt";
    private final String DEFAULT_TEMPLATE_FILE_NAME = "template";
    private final String DEFAULT_SHORTCUT_FILE_NAME = "shortcut";
    private final String DEFAULT_FILE_LOCATION_FILE_NAME = "fileLocation";
    private File templateFile;// = new File(DEFAULT_TEMPLATE_FILE_NAME);
    private File shortcutFile;// = new File(DEFAULT_SHORTCUT_FILE_NAME);
    private File fileLocation = new File(DEFAULT_FILE_LOCATION_FILE_NAME);
    private String taskFileLocation;
    private String path;

    //--------------------constructor-----------------
    /**
     * Constructor for FileStorage object, this will store the text file name
     * in a global variable textFile for reference.
     * If the file does not exist it will be created.
     */
    public FileStorage() {
        //taskFile = new File(fileName);

        //create a fileLocation file with default.txt inside
        if(!fileLocation.exists()){
            initializeFileStorage();
        } else {
            try {
                Scanner sc = new Scanner(fileLocation);
                taskFileLocation = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException e) {
                //this exception will never be used
            }
            path = getPath(taskFileLocation);
            taskFile = new File(taskFileLocation);
            templateFile = new File(path + DEFAULT_TEMPLATE_FILE_NAME);
            shortcutFile = new File(path + DEFAULT_SHORTCUT_FILE_NAME);
        }
    }


    private void initializeFileStorage() {
        fileLocation = new File(DEFAULT_FILE_LOCATION_FILE_NAME);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileLocation));
            bw.write(DEFAULT_TASK_FILE_NAME);
            bw.close();

            taskFile = new File(DEFAULT_TASK_FILE_NAME);
            if(!taskFile.exists()) {
                taskFile.createNewFile();           
            }
            templateFile = new File(DEFAULT_TEMPLATE_FILE_NAME);
            if(!templateFile.exists()) {
                templateFile.createNewFile();
            }
            shortcutFile = new File(DEFAULT_SHORTCUT_FILE_NAME);
            if(!shortcutFile.exists()) {
                shortcutFile.createNewFile();
            }
        } catch (IOException e) {
        }
    }


    public void saveToAnotherLocation(String newFileName) throws IOException {
        boolean isRenameSuccessful = taskFile.renameTo(new File(newFileName));

        if(isRenameSuccessful) {
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

    private void writeNewFileLocationToFile(String fileName) {
        try {
            fileLocation.delete();
            fileLocation.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileLocation));
            bw.write(fileName);
            bw.close();
        } catch (IOException e) {
        }
    }

    //create a fileLocation file with tasks.txt inside


    /*public FileStorage() {
        taskFile = new File(DEFAULT_FILENAME);
    } */



    /*public void moveFileTo(String newLocation) {
        String directory = getDirectory(newLocation);


        String newTemplateFile = directory + DEFAULT_TEMPLATE_FILE_NAME;
        templateFile.renameTo(new File(newTemplateFile));
        String newShortcutFile = directory + DEFAULT_SHORTCUT_FILE_NAME;
        shortcutFile.renameTo(new File(newShortcutFile));
    }*/



    /*private String getPath() {
        String location = "";
        if(fileLocation.exists()) {
            try {
                Scanner sc = new Scanner(fileLocation);
                location = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException e) {
            }
        }
        return location;
    }*/

    private String getPath(String fileName) {
        String directory = "";
        if(fileName.indexOf(SLASH) != -1) {
            int indexAfterLastSlash = fileName.lastIndexOf(SLASH) + 1;
            directory = fileName.substring(0, indexAfterLastSlash);
        } else if (fileName.indexOf(BACKSLASH) != -1) {
            int indexAfterLastBackSlash = fileName.lastIndexOf(BACKSLASH) + 1;
            directory = fileName.substring(0, indexAfterLastBackSlash);
        }
        return directory;
    }

    //----------task read and write starts----------
    /**
     * extracts each line from specified file as String array
     * and pass it as parameter to logic via processInitialization method
     * This is for normal tasks and template.
     */    
    public void readTaskFromFile(TaskManager tm) {
        if(taskFile.exists()) {
            try {
                Scanner sc = new Scanner(taskFile);

                while (sc.hasNextLine()) {

                    String[] inputs = new String[DEFAULT_STRING_SIZE];
                    inputs[COMMAND_TYPE_INDEX] = ADD_TASK_COMMAND;
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
                    tm.processInitialization(inputs);
                }
                sc.close();      

            } catch (FileNotFoundException e) {
            }
        }   
    }


    public void writeTaskToFile(ArrayList<Task> taskList) {
        try {
            taskFile.delete();
            taskFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(taskFile));
            for (int i = 0; i < taskList.size(); i++) {
                Task tempTask = taskList.get(i);
                String[] taskArray = taskToStringArray(tempTask, null);
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
            System.out.println(ERROR_EXCEPTION);
        }
    }
    //----------task read and write ends----------


    //----------shortcut read and write starts----------
    public void readShortcutFromFile(Shortcut shortcut) {
        if(shortcutFile.exists()) {
            if(shortcutFile.length() == 0) {
                SystemHandler system = SystemHandler.getSystemHandler();
                system.resetShortcutToDefault();
            } else {
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
    }


    public void writeShortcutToFile(String[][] shortcuts) {
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
            System.out.println(ERROR_EXCEPTION);
        }
    }
    //----------shortcut read and write ends----------



    // update template file
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
            System.out.println(ERROR_EXCEPTION);
        }
    }


    // update shortcut file
    //@param shortcuts String[][] of size 9 each string[i] keeps a variable length represent certain keyword 
    //store in order , one row is one id during retrieve


    //stores each task as a string delimited by ,



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
        } else {
            strArr[2] = null;
        }

        tempDate = tempTask.getDateTo();

        if(tempDate != null) {
            strArr[3] = dateToString(tempDate);
        } else {
            strArr[3] = null;
        }

        tempDate = tempTask.getDeadline();
        if(tempDate != null) {
            strArr[4] = dateToString(tempDate);
        } else {
            strArr[4] = null;
        }

        if(tempTask.getLocation() != null) {
            strArr[5] = tempTask.getLocation();
        } else {
            strArr[5] = null;
        }

        if(tempTask.getDetails() != null) {
            strArr[6] = tempTask.getDetails();
        } else {
            strArr[6] = null;
        }

        int tempInt = tempTask.getPriority();

        if(IntegerToString(tempInt) != null) {
            strArr[7] = IntegerToString(tempInt);
        } else {
            strArr[7] = null;
        }

        return strArr;
    }

    private String IntegerToString(int integer) {
        return Integer.toString(integer);
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




    private boolean isEmptyInput(String str) {
        if(str.equals(EMPTY_INPUT)) {
            return INPUT_IS_EMPTY;
        } else {
            return INPUT_IS_NOT_EMPTY;
        }
    }
}