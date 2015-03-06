import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskManager {
    //what format of time do you provide and
    //I suggest this format 31/05/2011 14:00
    //                      DD/MM/YYYY HH:mm

    //can I assume all commands are valid, no invalid commands?
    //almost valid, 
    //
    
    //for edit task, are you gonna send me what is being edited in the String[]
    //yes

    //null indicate nonexistence
    private static final int COMMAND_TYPE = 0;
    private static final int TID = 1;
    private static final int TASK_NAME = 2;
    private static final int DATE_FROM = 3;
    private static final int DATE_TO = 4;
    private static final int DEADLINE = 5;
    private static final int LOCATION = 6;
    private static final int DETAILS = 7;
    private static final int PRIORITY = 8;
    private static final boolean TID_IS_NOT_FOUND = false;
    private static final boolean TID_IS_FOUND = true;
    private static final int INITIAL_TID = 1000;
    private static final String EMPTY_INPUT = "null";

    private enum COMMAND_TYPE_TASK_MANAGER {
        add, edit, view, delete, init, undo, redo, invalid
    }

    private ArrayList<Task> _tasks;
    //private ArrayList<String[]> _handledTasks = new ArrayList<String[]>();
    private int _IDCounter;

    public TaskManager() {
        this._tasks = new ArrayList<Task>();
    }

    @SuppressWarnings("incomplete-switch")
    public ArrayList<Task> processTM(String[] inputs) throws ParseException {
        COMMAND_TYPE_TASK_MANAGER commandObtained = obtainCommand(inputs[COMMAND_TYPE]);
        ArrayList<Task> returningTasks = new ArrayList<Task>();

        switch(commandObtained) {
        case add: 
            returningTasks = new ArrayList<Task>(addATask(inputs));
            break;
        case edit: 
            returningTasks = new ArrayList<Task>(editATask(getIntType(inputs[TID]), inputs));
            break;
        case view: 
            returningTasks = new ArrayList<Task>(viewTasks()); 
            break;
        case delete: 
            returningTasks = new ArrayList<Task>(deleteATask(getIntType(inputs[TID])));
            break;
        }
        
        return returningTasks;
    }
    
    private ArrayList<Task> addATask(String[] inputs) throws ParseException {
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        Task newTask = new Task(getNewTID(), inputs[TASK_NAME], 
                getDateObject(inputs[DATE_FROM]), getDateObject(inputs[DATE_TO]), 
                getDateObject(inputs[DEADLINE]), inputs[LOCATION], inputs[DETAILS], 
                getIntType(inputs[PRIORITY]));
        _tasks.add(newTask);
        returningTasks.add(newTask);
        return returningTasks;
    }
    
    private Date getDateObject(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = format.parse(dateString);
        return date;
    }
    
    private int getIntType(String intString) {
        int intType = Integer.parseInt(intString);
        return intType;
    }
    
    private ArrayList<Task> editATask(int TID, String[] inputs) throws ParseException {
        ArrayList<Task> returningTasks = new ArrayList<Task>();
        for(Task task : _tasks) {
            if(TID == task.getTID()) {
                for(int i = TASK_NAME; i < inputs.length; i++) {
                    if(!inputs[i].equals(EMPTY_INPUT)) {
                        switch(i) {
                        case TASK_NAME: task.setDetails(inputs[TASK_NAME]); break;
                        case DATE_FROM: 
                            Date newDateFrom = getDateObject(inputs[DATE_FROM]);
                            task.setDateFrom(newDateFrom);
                            break;
                        case DATE_TO:
                            Date newDateTo = getDateObject(inputs[DATE_TO]);
                            task.setDateFrom(newDateTo);
                            break;
                        case DEADLINE: 
                            Date newDeadline = getDateObject(inputs[DEADLINE]);
                            task.setDateFrom(newDeadline);
                            break;
                        case LOCATION: task.setLocation(inputs[LOCATION]); break;
                        case DETAILS: task.setDetails(inputs[DETAILS]); break;
                        case PRIORITY: 
                            int newPriority = getIntType(inputs[PRIORITY]);
                            task.setPriority(newPriority);
                            break;
                        }
                    }
                }
                returningTasks.add(task);
            }
        }
        return returningTasks;
    }
    
    /*private String getStringFromInt() {
        
    }
    
    private String getStringFromDate() {
        
    }*/



    private ArrayList<Task> deleteATask(int TID) {
        ArrayList<Task> returningTasks = new ArrayList<Task>(viewTasks());
        for(Task task : _tasks) {
            if(TID == task.getTID()) {
                
                returningTasks.add(task);
            }
        }
        return returningTasks;
    }



    private COMMAND_TYPE_TASK_MANAGER obtainCommand (String command) {
        COMMAND_TYPE_TASK_MANAGER commandObtained;
        try {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.valueOf(command);
        } catch (IllegalArgumentException ex) {
            commandObtained = COMMAND_TYPE_TASK_MANAGER.invalid;
        }
        return commandObtained;
    }

    private int getNewTID() {
        int newTID;
        if(_tasks.isEmpty()) {
            newTID = INITIAL_TID + 1;
        } else {
            newTID = _tasks.get(_tasks.size() - 1).getTID() + 1;
        }

        return newTID;
    }

    private ArrayList<Task> viewTasks() {
        return _tasks;
    }

    //+undoAddTask(): boolean
    //+undoDeleteTask(): boolean

}
