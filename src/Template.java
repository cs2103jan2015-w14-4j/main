import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

//@author A0108385B
public class Template {

	private static final String MSG_ERR_DUPLICATE_NAME = "Template name:\"%s\" has been used by another template.";
	private static final String MSG_ERR_TASK_NUMBER_NOT_EXIST = "Task number: %s does not exist.";
	private static final String MSG_INVALID_GET_FIELD = "No such field value to get from.";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command in Template Manager: %1$s";
	public static final String MSG_ERR_NO_SUCH_TEMPLATE = "No such template exists.";


	private static final int INDEX_NOT_FOUND = -1;
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
    
	private static final int STARTING_INDEX_CHANGEABLE_FIELD = 2;
	private static final int STRING_POSITION_INVALID_COMMAND = 39;

	private static final int DUMMY_TID = 0;

	private static final String COMMAND_ADD_TASK = "addTask";
	
    
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final int LENGTH_COMMAND_TEMPLATE = 9;
	
	private ArrayList<Task> templates;
	private ArrayList<String> tempNames;
	private SystemHandler system;
	
	public Template() {
		templates = new ArrayList<Task>();
		tempNames = new ArrayList<String>();
	}
	
	
	public Template(boolean test) {
		templates = new ArrayList<Task>();
		tempNames = new ArrayList<String>();
	}
	

	/**
	 * @param system
	 */
	public void setSystemPath(SystemHandler system) {
		this.system = system;
	}
	
	
	/**
	 * @param command
	 * @return
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Task> processCustomizingCommand(String[] command) 
			throws IllegalArgumentException {
		
		
		COMMAND_TYPE_TEMPLATE commandType = getCommandType(command[0]);
		ArrayList<Task> result = null;
		
		assertValidity(command, commandType);
		switch(commandType) {
			case addTemplate:
				result = addTemplate(command, result);
				writeOutToFile();
				break;
				
			case addTemplateInit:
				initTemplate(command);
				break;
				
			case viewTemplate:
				result = viewTemplates();
				break;
				
			case deleteTemplate:
				result = removeTemplate(command[INDEX_TEMPLATE_NAME]);
				writeOutToFile();
				break;
				
			case editTemplate:
				result = editTemplate(command);
				writeOutToFile();
				break;
				
			case resetTemplate:
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
	 */
	private void initTemplate(String[] command) {

		Task taskToBeAddedInit = createNewTemplate(command);
		addTemplateToArray(command[INDEX_TEMPLATE_NAME], taskToBeAddedInit);
	}

	/**
	 * @param command
	 * @param result
	 * @return
	 */
	private ArrayList<Task> addTemplate(String[] command, ArrayList<Task> result) {
		Task taskToBeAdded = system.requestTaskInformationfromTM(Integer.parseInt(command[1]));	
		
		if(taskToBeAdded == null) {
			throw new NoSuchElementException(String.format(MSG_ERR_TASK_NUMBER_NOT_EXIST,command[1]));
		} else {
			result =  addTemplateToArray(command[INDEX_NEW_TEMPLATE_NAME], taskToBeAdded);
			
		}
		return result;
	}
	
	/**
	 * @param name
	 * @param template
	 * @return
	 */
	private ArrayList<Task> addTemplateToArray(String name, Task template) {
		boolean sameName = hasSameName(name);
		if(!sameName) {
			
			clearTaskDateField(template);
			insertTemplateIntoArray(name, template);
			
			ArrayList<Task> result = new ArrayList<Task>();
			result.add(template.clone());
			return result;
			
		} else {
			//Wrong type 
			throw new NoSuchElementException(String.format(MSG_ERR_DUPLICATE_NAME,name));
		
		}
	}
	
	/**
	 * @param templates
	 * @return
	 */
	public ArrayList<String> getTemplateNames(ArrayList<Task> templates) {
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < templates.size(); ++i) {
			result.add(getMatchingName(templates.get(i)));
		}
		
		return result;
	}

	/**
	 * @param task
	 * @return
	 */
	private String getMatchingName(Task task) {
		for(int i = 0; i < templates.size(); ++i) {
			
			if(task.isEqual(templates.get(i))) {
				return tempNames.get(i);
			}
			
		}
		
		return null;
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
	
		system.writeTemplateToFile(templates, tempNames);
		
	}

	private int getTemplateIndex(String name) {
		for(int i = 0; i < tempNames.size(); ++i) {
			
			if(tempNames.get(i).equals(name)) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}
	
	private Task getTemplate(String name) {
		int index = getTemplateIndex(name);
		
		if(index == INDEX_NOT_FOUND) {
			return null;
		} else {
			return templates.get(index);
		}
	}

	private ArrayList<Task> editTemplate(String[] command) {
		Task task = getTemplate(command[1]);
		ArrayList<Task> result = new ArrayList<Task>();
		
		if(task == null) {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		}
		
		for(int index = 0; index < LENGTH_COMMAND_TEMPLATE; ++index) {
			if(isFieldToBeEdited(command, index)) {
				editRequiredField(command[index], task, index);
			}
		}
		
		result.add(task.clone());
		return result;
	}

	/**
	 * @param command
	 * @param task
	 * @param index
	 */
	private void editRequiredField(String command, Task task, int index) {
		switch(index) {
			case INDEX_TASK_NAME:
				task.setTaskName(command);
				break;
				
			case INDEX_DATE_FROM:
				Date dateFrom = getDate(command);
				task.setDateFrom(dateFrom);
				break;
				
			case INDEX_DATE_TO:
				Date dateTo = getDate(command);
				task.setDateFrom(dateTo);
				break;
				
			case INDEX_DEADLINE:
				Date deadline = getDate(command);
				task.setDateFrom(deadline);
				break;
			case INDEX_LOCATION:
				task.setLocation(command);
				break;
				
			case INDEX_DETAILS:
				task.setDetails(command);
				break;
				
			case INDEX_PRIORITY:
				task.setPriority(Integer.parseInt(command));
				break;
				
		}
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

	/**
	 * @param command
	 * @param cmdType
	 */
	private void assertValidity(String[] command, COMMAND_TYPE_TEMPLATE cmdType) {
		assert(command.length == LENGTH_COMMAND_TEMPLATE);
		assert(command[INDEX_COMMAND] != null);
		switch(cmdType) {
			case viewTemplate:
			case resetTemplate:
				assert(command[1] == null);
				
			case deleteTemplate:
				assert(command[2] == null);
				
			case addTemplate:
				for(int i = 3; i < LENGTH_COMMAND_TEMPLATE; ++i) {
					assert(command[i] == null);
				}
				
			case useTemplate:
			case addTemplateInit:
			case editTemplate:
		}
	}
	
	/**
	 * @param command
	 * @return
	 * @throws IllegalArgumentException
	 */
	private COMMAND_TYPE_TEMPLATE getCommandType(String command) throws IllegalArgumentException {
		try{
        	return COMMAND_TYPE_TEMPLATE.valueOf(command);
        } catch (IllegalArgumentException e) {
        	throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
        			e.getMessage().substring(STRING_POSITION_INVALID_COMMAND)));
        }
	}
	
	/**
	 * @param name
	 * @return
	 * @throws NoSuchElementException
	 */
	private ArrayList<Task> removeTemplate(String name) throws NoSuchElementException {
		Task deletedTask = removeFromArray(name);
		
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
	private Task removeFromArray(String name) {
		int index = getTemplateIndex(name);
		if(index == INDEX_NOT_FOUND) {
			return null;
		} else {
			tempNames.remove(index);
			return templates.remove(index);
		}
	}
	
	/**
	 * @return
	 */
	private ArrayList<Task> viewTemplates() {
		return templates;
	}
	
	
	
	
	/**
	 * @param template
	 */
	private void clearTaskDateField(Task template) {
		template.setDateFrom(null);
		template.setDateTo(null);
		template.setDeadline(null);
	}
	
	/**
	 * 
	 */
	private void resetTemplates() {
		templates.clear();
		tempNames.clear();
	}
	
	/**
	 * @param name
	 * @param template
	 */
	private void insertTemplateIntoArray(String name, Task template) {
		templates.add(template);
		tempNames.add(name);
	}
	
	/**
	 * @param name
	 * @return
	 */
	private boolean hasSameName(String name) {
		return getTemplateIndex(name) != INDEX_NOT_FOUND;
	}
	
	
	/**
	 * @param name
	 * @return
	 * @throws NoSuchElementException
	 */
	private Task fetchTemplate(String name) throws NoSuchElementException {
		int index = getTemplateIndex(name);
		
		if(index != INDEX_NOT_FOUND) {
			Task task = templates.get(index);
			return task.clone();
		}
		else {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		}
		
	}
	
	/**
	 * @param task
	 * @param index
	 * @param change
	 * @return
	 * @throws IllegalArgumentException
	 */
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
					return task.getStatusString();
				default:
					throw new IllegalArgumentException(MSG_INVALID_GET_FIELD);
			}
		} else {
			return change;
		}
	}
	
	/**
	 * @param task
	 * @param changes
	 * @return
	 * @throws IllegalArgumentException
	 */
	private String[] convertTasktoTaskManagerInput(Task task, String[] changes) 
			throws IllegalArgumentException {
		String[] converted = new String[LENGTH_COMMAND_TEMPLATE];
		converted[TaskManager.COMMAND_TYPE] = COMMAND_ADD_TASK;
		converted[TaskManager.TID] = null;
		
		
		for(int i = STARTING_INDEX_CHANGEABLE_FIELD; i < LENGTH_COMMAND_TEMPLATE; ++i) {
			converted[i] = getFieldValue(task, i, changes[i]);
		}
		
		return converted;
	}
	
	/**
	 * @param dateString
	 * @return
	 */
	private Date convertToDateObject(String dateString) {
		try {
			Date date = null;
			if (dateString != null && !dateString.equals("")) {
				DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
				date = format.parse(dateString);
			}
			return date;
		} catch (ParseException e) {
			return null;
		}
	}
	
}
