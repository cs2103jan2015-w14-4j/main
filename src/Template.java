import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

//@author A0108385B
public class Template {

	private static final String MSG_ERR_DUPLICATE_NAME = "Template name:\"%s\" has been used by another template.";
	private static final String MSG_ERR_TASK_NUMBER_NOT_EXIST = "Task number: %s does not exist.";
	private static final String MSG_INVALID_GET_FIELD = "No such field value to get from";
	private static final String MSG_ERR_NO_SUCH_COMMAND = "No such command in Template Manager: %1$s";
	public static final String MSG_ERR_NO_SUCH_TEMPLATE = "No such template saved in the system";


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
	
	/**
	 * It set the system path so that it can call the system Handler that governs it to 
	 * fetch data from other components when required
	 * @param system	The system handler that governs this template manager
	 */
	public void setSystemPath(SystemHandler system) {
		this.system = system;
	}
	
	
	
	/**
	 * @param command	The string array of command to be executed
	 * @return			ArrayList of Tasks that are demanded by command
	 * @throws IllegalArgumentException		Invalid instruction has been demanded or command violates 
	 * 										the constraints set by template manager
	 * @throws NoSuchElementException		Demanded template from command does not exist
	 */
	public ArrayList<Task> processCustomizingCommand(String[] command) 
			throws IllegalArgumentException, NoSuchElementException {
		
		
		COMMAND_TYPE_TEMPLATE commandType = getCommandType(command[0]);
		ArrayList<Task> result = null;
		
		assertValidity(command, commandType);
		switch(commandType) {
			case addTemplate:
				result = addTemplate(command);
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
	 * @param command	The string array of command to be executed
	 */
	private void initTemplate(String[] command) {

		Task taskToBeAddedInit = createNewTemplate(command);
		addTemplateToArray(command[INDEX_TEMPLATE_NAME], taskToBeAddedInit);
	}

	/**
	 * @param command	The string array of command to be executed
	 * @param result	
	 * @return
	 */
	private ArrayList<Task> addTemplate(String[] command) {
		Task taskToBeAdded = system.requestTaskInformationfromTM(Integer.parseInt(command[1]));	
		ArrayList<Task> result = new ArrayList<Task>();
		if(taskToBeAdded == null) {
			throw new NoSuchElementException(String.format(MSG_ERR_TASK_NUMBER_NOT_EXIST,command[1]));
		} else {
			result =  addTemplateToArray(command[INDEX_NEW_TEMPLATE_NAME], taskToBeAdded);
			
		}
		return result;
	}
	
	
	/**
	 * @param name							Name of the template
	 * @param template						Template to be created
	 * @return								ArrayList of Task with a newly created template Task
	 * @throws IllegalArgumentException		There exists a template with the same name 
	 */
	private ArrayList<Task> addTemplateToArray(String name, Task template) throws IllegalArgumentException {
		boolean sameName = hasSameName(name);
		if(!sameName) {
			
			clearTaskDateField(template);
			insertTemplateIntoArray(name, template);
			
			ArrayList<Task> result = new ArrayList<Task>();
			result.add(template.clone());
			return result;
			
		} else {
			throw new IllegalArgumentException(String.format(MSG_ERR_DUPLICATE_NAME,name));
		
		}
	}
	
	/**
	 * This method construct an ArrayList of names that match the templates given
	 * @param templates		ArrayList of template
	 * @return				ArrayList of names correspond to the templates
	 * 
	 */
	public ArrayList<String> getTemplateNames(ArrayList<Task> templates) {
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < templates.size(); ++i) {
			result.add(getMatchingName(templates.get(i)));
		}
		
		return result;
	}

	/**
	 * This method returns the name of the template
	 * @param task							A Template object
	 * @return								String of name that matches the template object
	 * @throws NoSuchElementException		There is no such template in templates list
	 */
	private String getMatchingName(Task task) throws NoSuchElementException {
		for(int i = 0; i < templates.size(); ++i) {
			
			if(task.isEqual(templates.get(i))) {
				return tempNames.get(i);
			}
			
		}
		
		throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
	}

	/**
	 * @param command	The string array of command to be executed
	 * @return			Task object created by following the command parameter.
	 */
	private Task createNewTemplate(String[] command) {
		return new Task(DUMMY_TID,command[INDEX_TASK_NAME], 
				convertToDateObject(command[INDEX_DATE_FROM]), 
				convertToDateObject(command[INDEX_DATE_TO]), 
				convertToDateObject(command[INDEX_DEADLINE]), 
				command[INDEX_LOCATION], command[INDEX_DETAILS],
				Integer.parseInt(command[INDEX_PRIORITY]));
	}
	
	
	/**
	 * 	This method calls system handler to initiate write out to storage. 
	 *  It is called when there are changes made to templates' data
	 */
	private void writeOutToFile() {
	
		system.writeTemplateToFile(templates, tempNames);
		
	}

	/**
	 * @param name		Template name 
	 * @return			Index of the template stored in ArrayList, -1 if not found.
	 */
	private int getTemplateIndex(String name) {
		for(int i = 0; i < tempNames.size(); ++i) {
			
			if(tempNames.get(i).equals(name)) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}
	
	/**
	 * @param name	Template name 
	 * @return		Template that matches the name, null if not found.
	 */
	private Task getTemplate(String name) {
		int index = getTemplateIndex(name);
		
		if(index == INDEX_NOT_FOUND) {
			return null;
		} else {
			return templates.get(index);
		}
	}

	
	/**
	 * @param command 					The string array of command to be executed
	 * @return							ArrayList of template with the edited template in the list. 
	 * @throws NoSuchElementException	The template to be edited does not exist in template manager
	 */
	private ArrayList<Task> editTemplate(String[] command) throws NoSuchElementException {
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
	 * @param command		The string array of command to be executed
	 * @param task			Template to be edited
	 * @param index			Index of field to be edited in the template
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
	 * This method checks if the command demands the field to be edited.
	 * @param command		The string array of command to be executed
	 * @param index			Index of field to be checked
	 * @return				True if there exist an instruction in the field
	 */
	private boolean isFieldToBeEdited(String[] command, int index) {
		return command[index] != null;
	}
		

	/**
	 * MA CONG
	 */
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
	 * This method assert the correct length of command to be executed.
	 * @param command		The string array of command to be executed
	 * @param cmdType		Command Type to be executed by Template.
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
	 * @param command	The string array of command to be executed
	 * @return			The command type to be executed
	 * @throws IllegalArgumentException The command type is not recognized by Template Manager
	 */
	public static COMMAND_TYPE_TEMPLATE getCommandType(String command) throws IllegalArgumentException {
		try{
        	return COMMAND_TYPE_TEMPLATE.valueOf(command);
        } catch (IllegalArgumentException e) {
        	throw new IllegalArgumentException(String.format(MSG_ERR_NO_SUCH_COMMAND, 
        			e.getMessage().substring(STRING_POSITION_INVALID_COMMAND)));
        }
	}
	
	/**
	 * @param tempName					Name of the template to be deleted
	 * @return							The deleted template in an ArrayList
	 * @throws NoSuchElementException	The template to be removed is not found in templates list
	 */
	private ArrayList<Task> removeTemplate(String tempName) throws NoSuchElementException {
		Task deletedTask = removeFromArray(tempName);
		
		if(deletedTask == null) {
			throw new NoSuchElementException(MSG_ERR_NO_SUCH_TEMPLATE);
		} else {
			ArrayList<Task> result = new ArrayList<Task>();
			result.add(deletedTask);
			return result;
		}
	}

	/**
	 * @param tempName		Name of the template to be deleted
	 * @return				Deleted template, null if template does not exist
	 */
	private Task removeFromArray(String tempName) {
		int index = getTemplateIndex(tempName);
		if(index == INDEX_NOT_FOUND) {
			return null;
		} else {
			tempNames.remove(index);
			return templates.remove(index);
		}
	}
	
	/**
	 * @return		Return the templates list in ArrayList
	 */
	private ArrayList<Task> viewTemplates() {
		return templates;
	}
	
	
	
	
	/**
	 * This method clears all the date related fields in template before saving the template.
	 * @param template	Template to be added into templates list
	 */
	private void clearTaskDateField(Task template) {
		template.setDateFrom(null);
		template.setDateTo(null);
		template.setDeadline(null);
	}
	
	/**
	 * 	This method clear all the templates.
	 */
	private void resetTemplates() {
		templates.clear();
		tempNames.clear();
	}
	
	/**
	 * This method adds the template as well as the name corresponds to the template into the list.
	 * @param name		Name of the template
	 * @param template	Template to be added
	 */
	private void insertTemplateIntoArray(String name, Task template) {
		templates.add(template);
		tempNames.add(name);
	}
	
	/**
	 * @param name	Name of the template
	 * @return	True if there is a template with same name
	 */
	private boolean hasSameName(String name) {
		return getTemplateIndex(name) != INDEX_NOT_FOUND;
	}
	
	
	/**
	 * @param name						Name of the template to be fetched	
	 * @return							The template that corresponds to the name
	 * @throws NoSuchElementException	There is no templates called by the name given
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
	 * @param task							Template with the information to be extracted
	 * @param index							Index of information to be extracted
	 * @param change						Changes demanded by user to overwrite the template
	 * @return								Final updates on the field of task to be created from template
	 * @throws IllegalArgumentException		Illegal field index
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
	 * @param task							Task to be added into Tasks List
	 * @param changes						Changes demanded by user to overwrite the template
	 * @return								String array that follows task manager command format
	 * @throws IllegalArgumentException		Invalid changes is demanded by user
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
	 * MA CONG
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
