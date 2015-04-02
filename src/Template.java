import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Template {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final int COMMAND_LENGTH = 9;
	public static final String NO_SUCH_TEMPLATE = "No such template saved in the system";
	private static final String COMMAND_ADD_TASK = "addTask";
	private static final String MSG_INVALID_GET_FIELD = "No such field value to get from";
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
			throws Exception {
		
		
		COMMAND_TYPE_TEMPLATE commandType = getCommandType(command[0]);
		ArrayList<Task> result = null;
		
		assertValidity(command, commandType);
		try {
			switch(commandType) {
				case addTemplate:
					Task taskToBeAdded;
					if(isTest) {
						taskToBeAdded = new Task(1000, "NEW",
								convertToDateObject("12/09/2015 10:00"),
								convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0);
					}
					else {
						taskToBeAdded = system.requestTask(Integer.parseInt(command[1]));	
					}
					result =  addTemplate(command[2], taskToBeAdded);
					writeOutToFile();
					break;
					
				case addTemplateInit:
					if(system == null) {
						system = SystemHandler.getSystemHandler();
					}
					Task taskToBeAddedInit = new Task(Integer.parseInt(command[1]),command[2], 
							convertToDateObject(command[3]), convertToDateObject(command[4]), 
							convertToDateObject(command[5]), command[5], command[6],
							Integer.parseInt(command[7]));
					addTemplate(command[2], taskToBeAddedInit);
					break;
				case viewTemplates:
					result = viewTemplates();
					break;
				case deleteTemplate:
					result = removeTemplate(command[1]);
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
					Task fetchedTask = fetchTemplate(command[1]);
					String[] convertedTask = convertTasktoTaskManagerInput(fetchedTask, command);
					system.addTaskFromTemplate(convertedTask);
					break;
			}
		} catch (IllegalArgumentException e) {
			System.out.println("ERROR :"+ e);
		}
			
		return result;
	}
	
	private void writeOutToFile() {
		if(system == null) {
			system = SystemHandler.getSystemHandler();
		}
		Iterator<String> listing = templates.keySet().iterator();
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<Task> templatesList = new ArrayList<Task>();
		while(listing.hasNext()) {
			String next = listing.next();
			match.add(next);
			templatesList.add(templates.get(next).clone());
			
		}
		//system.writeTemplateToFile(templatesList, match);
		
	}


	private Task editTemplate(String[] command) {
		Task task = templates.get(command[1]);
		if(task == null) {
			throw new NoSuchElementException(NO_SUCH_TEMPLATE);
		}
		for(int i = 0; i < COMMAND_LENGTH; ++i) {
			if(command[i] != null) {
				switch(i) {
				case 2:
					task.setTaskName(command[2]);
					break;
				case 3:
					Date dateFrom = getDate(command[4]);
					task.setDateFrom(dateFrom);
					break;
				case 4:
					Date dateTo = getDate(command[5]);
					task.setDateFrom(dateTo);
					break;
				case 5:
					Date deadline = getDate(command[5]);
					task.setDateFrom(deadline);
					break;
				case 6:
					task.setLocation(command[6]);
					break;
				case 7:
					task.setDetails(command[8]);
					break;
				case 8:
					task.setPriority(Integer.parseInt(command[8]));
					break;
				}
			}
		}
		return task.clone();
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
		assert(command[0] != null);
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
	
	private COMMAND_TYPE_TEMPLATE getCommandType(String command) throws NoSuchElementException {
		switch(command) {
			case "addTemplate":
				return COMMAND_TYPE_TEMPLATE.addTemplate;
			case "viewTemplates":
				return COMMAND_TYPE_TEMPLATE.viewTemplates;
			case "deleteTemplate":
				return COMMAND_TYPE_TEMPLATE.deleteTemplate;
			case "editTemplate":
				return COMMAND_TYPE_TEMPLATE.editTemplate;
			case "resetTemplates":
				return COMMAND_TYPE_TEMPLATE.resetTemplates;
			case "addTemplateInit":
				return COMMAND_TYPE_TEMPLATE.addTemplateInit;
			default:
				throw new NoSuchElementException("Wrong command received at Template Manager.");
		}
	}
	
	private ArrayList<Task> removeTemplate(String name) throws NoSuchElementException {
		Task deletedTask = removeFromMap(name);
		
		if(deletedTask == null) {
			throw new NoSuchElementException(NO_SUCH_TEMPLATE);
		}
		else {
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
			template.setDateFrom(null);
			template.setDateTo(null);
			template.setDeadline(null);
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
			throw new NoSuchElementException(NO_SUCH_TEMPLATE);
		}
		
	}
	
	private String getFieldValue(Task task, int index, String change) throws IllegalArgumentException {
		if(change == null) {
			switch(index) {
				case 2: 
					return task.getTaskName();
				case 3: 
					return task.getDateFromString();
				case 4: 
					return task.getDateToString();
				case 5: 
					return task.getDeadlineString();
				case 6: 
					return task.getLocation();
				case 7: 
					return task.getDetails();
				case 8: 
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
		
		
		for(int i = 2; i < COMMAND_LENGTH; ++i) {
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
