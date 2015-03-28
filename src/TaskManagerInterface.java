import java.util.ArrayList;


public interface TaskManagerInterface {
    
    /**
     * This method adds tasks from file once the program starts before user entering commands
     * If the task does not have an ID, call getNewTID()
     * @param inputs  a parsed command received from FileStorage, with command type
     *                being "addTask"
     */
    public void processInitialization(String[] inputs);
    
    
    /**
     * @param inputs  a parsed command received from SystemHandler
     * @return        affected tasks in an ArrayList
     */
    public ArrayList<Task> processTM(String[] inputs);
    
    
    /**
     * @param TID  an integer Task ID
     * @return     if ID is found, a Task with that ID; else null
     */
    public Task getTaskFromTID(int TID);
}
