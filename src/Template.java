import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Template {

	private static final int STARTING_INDEX_CHANGEABLE_FIELD = 2;


	private static final int STRING_POSITION_INVALID_COMMAND = 39;


	private static final int DUMMY_TID = 0;
	
	
	private static final int INDEX_COMMAND = 0;
	private static final int INDEX_TEMPLATE_NAME = 1;
	private static final int INDEX_NEW_TEMPLATE_NAME = 2;
	private static final int INDEX_TASK_NAME = 2;
	private static final int INDEX_DATE_FROM = 3;
	private static final int INDEX_DATE_TO = 4;
	private static final int INDEX_DEADLINE = 5;
	private static final int INDEX_LOCATION = 6;
	private static final int INDEX_DETAILS = 7;
    private static final int INDEX_PRIORITY = 8;
    
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final int COMMAND_LENGTH = 9;
	public static final String MSG_ERR_NO_SUCH_TEMPLATE = "No such template saved in the system";
	private static final String COMMAND_ADD_TASK = "addTask";
	private static final String MSG_INVALID_GET_FIELD = "No such field value to get from";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command in Template Manager: %1$s";
	private HashMap<String,Task> templates; 
	private SystemHandler system;
	

	private boolean isTest;
	
	public void setSystemPath(SystemHandler system) {
		this.system = system;
	}
	
	public Template() {
		templates = new HashMap<String,Task>();
		isTest = false;
	}
	
	
	public Template(boolean test) {
		templates = new HashMap<String,Task>();
		isTest = test;
	}
	
	
	
	public ArrayList<Task> processCustomizingCommand(String[] command) 
			throws IllegalArgumentException {
		
		
		COMMAND_TYPE_TEMPLATE commandType = getCommandType(command[0]);
		ArrayList<Task> result = null;
		
		assertValidity(command, commandType);
		switch(commandType) {
			case addTemplate:
				Task taskToBeAdded;
				if(isTest) {
					taskToBeAdded = new Task(1000, "NEW",
							null, null, null, "ABC", null, 0);
				}
				else {
					taskToBeAdded = system.requestTask(Integer.parseInt(command[1]));	
				}
				result =  addTemplate(command[INDEX_NEW_TEMPLATE_NAME], taskToBeAdded);
				writeOutToFile();
				break;
				
			case addTemplateInit:
				Task taskToBeAddedInit = createNewTemplate(command);
				addTemplate(command[INDEX_NEW_TEMPLATE_NAME], taskToBeAddedInit);
				break;
				
			case viewTemplates:
				result = viewTemplates();
				break;
				
			case deleteTemplate:
				result = removeTemplate(command[INDEX_TEMPLATE_NAME]);
				writeOutToFile();
				break;
				
			case editTemplate:
				Task temp = editTemplate(command);
				result = new ArrayList<Task>(); 
				result.add(temp);
				writeOutToFile();
				break;
				
			case resetTemplates:
				resetTemplates();
				writeOutToFile();
				result = viewTemplates();
				break;
				
			case useTemplate:
				Task fetchedTask = fetchTemplate(command[INDEX_TEMPLATE_NAME]);
				String[] convertedTask = convertTasktoTaskManagerInput(fetchedTask, command);
				system.addTaskFromTemplate(convertedTask);
				break;
		}
		
		return result;
	}

	/**
	 * @param command
	 * @return
	 */
	private Task createNewTemplate(String[] command) {
		return new Task(DUMMY_TID,command[INDEX_TASK_NAME], 
				convertToDateObject(command[INDEX_DATE_FROM]), 
				convertToDateObject(command[INDEX_DATE_TO]), 
				convertToDateObject(command[INDEX_DEADLINE]), 
				command[INDEX_LOCATION], command[INDEX_DETAILS],
				Integer.parseInt(command[INDEX_PRIORITY]));
	}
	
	private void writeOutToFile() {
		
		ArrayList<String> key = new ArrayList<String>();
		ArrayList<Task> templatesList = new ArrayList<Task>();
		extractKeyAndTemplate(key, templatesList);
		
		//system.writeTemplateToFile(templatesList, match);
		
	}

	/**
	 * @param listing
	 * @param match
	 * @param templatesList
	 */
	private void extractKeyAndTemplate(ArrayList<String> match, 
			ArrayList<Task> templatesList) {
		
		Iterator<String> listing = templates.keySet().iterator();
		
		while(listing.hasNext()) {
			String next = listing.next();
			match.add(next);
			templatesList.add(templates.get(next).clone());
			
		}
	}


	private Task editTemplate(String[] command) {
		Task task = templates.get(command[1]);
		if(task == null) {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		}
		for(int index = 0; index < COMMAND_LENGTH; ++index) {
			if(isFieldToBeEdited(command, index)) {
				switch(index) {
					case INDEX_TASK_NAME:
						task.setTaskName(command[INDEX_TASK_NAME]);
						break;
						
					case INDEX_DATE_FROM:
						Date dateFrom = getDate(command[INDEX_DATE_FROM]);
						task.setDateFrom(dateFrom);
						break;
						
					case INDEX_DATE_TO:
						Date dateTo = getDate(command[INDEX_DATE_TO]);
						task.setDateFrom(dateTo);
						break;
						
					case INDEX_DEADLINE:
						Date deadline = getDate(command[INDEX_DEADLINE]);
						task.setDateFrom(deadline);
						break;
					case INDEX_LOCATION:
						task.setLocation(command[INDEX_LOCATION]);
						break;
						
					case INDEX_DETAILS:
						task.setDetails(command[INDEX_DETAILS]);
						break;
						
					case INDEX_PRIORITY:
						task.setPriority(Integer.parseInt(command[INDEX_PRIORITY]));
						break;
						
				}
			}
		}
		return task.clone();
	}

	/**
	 * @param command
	 * @param index
	 * @return
	 */
	private boolean isFieldToBeEdited(String[] command, int index) {
		return command[index] != null;
	}
		

	private Date getDate(String date) {
		try {
			DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			return format.parse(date);
			
		} catch (ParseException e) {
			assert(true);
		}
		return null;
	}

	private void assertValidity(String[] command, COMMAND_TYPE_TEMPLATE cmdType) {
		assert(command.length == COMMAND_LENGTH);
		assert(command[INDEX_COMMAND] != null);
		switch(cmdType) {
			case viewTemplates:
			case resetTemplates:
				assert(command[1] == null);
				
			case deleteTemplate:
				assert(command[2] == null);
				
			case addTemplate:
				for(int i = 3; i < COMMAND_LENGTH; ++i) {
					assert(command[i] == null);
				}
				
			case useTemplate:
			case addTemplateInit:
			case editTemplate:
		}
	}
	
	private COMMAND_TYPE_TEMPLATE getCommandType(String command) throws IllegalArgumentException {
		try{
        	return COMMAND_TYPE_TEMPLATE.valueOf(command);
        } catch (IllegalArgumentException e) {
        	throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
        			e.getMessage().substring(STRING_POSITION_INVALID_COMMAND)));
        }
	}
	
	private ArrayList<Task> removeTemplate(String name) throws NoSuchElementException {
		Task deletedTask = removeFromMap(name);
		
		if(deletedTask == null) {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		} else {
			ArrayList<Task> result = new ArrayList<Task>();
			result.add(deletedTask);
			return result;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	private Task removeFromMap(String name) {
		return templates.remove(name);
	}
	
	private ArrayList<Task> viewTemplates() {
		Iterator<Task> temp = extractTemplatesFromMapping();
		return copyOverToArrayList(temp);
	}
	
	private Iterator<Task> extractTemplatesFromMapping() {
		return templates.values().iterator();
	}
	
	private ArrayList<Task> copyOverToArrayList(Iterator<Task> templates) {
		ArrayList<Task> templateList = new ArrayList<Task>();
		while(templates.hasNext()) {
			templateList.add(templates.next().clone());
		}
		return templateList;
		
	}

	
	
	private ArrayList<Task> addTemplate(String name, Task template) {
		boolean sameName = hasSameName(name);
		if(!sameName) {
			
			clearTaskDateField(template);
			Task returnTemplate = insertTemplateIntoMap(name, template);
			assert(returnTemplate == null);
			
			ArrayList<Task> result = new ArrayList<Task>();
			result.add(template.clone());
			return result;
		}
		else {
			return null;
		}
	}
	
	private void clearTaskDateField(Task template) {
		template.setDateFrom(null);
		template.setDateTo(null);
		template.setDeadline(null);
	}
	
	private void resetTemplates() {
		templates.clear();
	}
	
	private Task insertTemplateIntoMap(String name, Task template) {
		return templates.put(name, template.clone());
	}
	
	private boolean hasSameName(String name) {
		return templates.containsKey(name);
	}
	
	
	private Task fetchTemplate(String key) throws NoSuchElementException {
		Task task = templates.get(key);
		if(task != null) {
			return task;
		}
		else {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		}
		
	}
	
	private String getFieldValue(Task task, int index, String change) throws IllegalArgumentException {
		if(change == null) {
			switch(index) {
				case INDEX_TASK_NAME: 
					return task.getTaskName();
				case INDEX_DATE_FROM: 
					return task.getDateFromString();
				case INDEX_DATE_TO: 
					return task.getDateToString();
				case INDEX_DEADLINE: 
					return task.getDeadlineString();
				case INDEX_LOCATION: 
					return task.getLocation();
				case INDEX_DETAILS: 
					return task.getDetails();
				case INDEX_PRIORITY: 
					return Integer.toString(task.getPriority());
				default:
					throw new IllegalArgumentException(MSG_INVALID_GET_FIELD);
			}
		} else {
			return change;
		}
	}
	
	private String[] convertTasktoTaskManagerInput(Task task, String[] changes) 
			throws IllegalArgumentException {
		String[] converted = new String[COMMAND_LENGTH];
		converted[TaskManager.COMMAND_TYPE] = COMMAND_ADD_TASK;
		converted[TaskManager.TID] = null;
		
		
		for(int i = STARTING_INDEX_CHANGEABLE_FIELD; i < COMMAND_LENGTH; ++i) {
			converted[i] = getFieldValue(task, i, changes[i]);
		}
		
		return converted;
	}
	
	private Date convertToDateObject(String dateString) {
		try {
			Date date = null;
			if (dateString != null && !dateString.equals("")) {
				DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
				date = format.parse(dateString);
			}
			return date;
		} catch (ParseException e) {
			System.out.println(e);
			return null;
		}
	}
	
}
