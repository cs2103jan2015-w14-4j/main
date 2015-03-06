import java.util.ArrayList;

public class TaskManager {
    private static final int COMMAND_TYPE = 0;
    private static final int ID = 1;
    private static final int NAME = 2;
    private static final int DATEFROM = 3;
    private static final int DATETO = 4;
    private static final int DEADLINE = 5;
    private static final int LOCATION = 6;
    private static final int DEATAIL = 7;
    
    
    //cmd,id,name,datefrom,dateto,deadline,location,detail
    
	private ArrayList<Task> _tasks;
    
	public TaskManager() {
		this._tasks = new ArrayList<Task>();
	}
	
	public ArrayList<Task> processTM(String[] input) {
		
	}
}
