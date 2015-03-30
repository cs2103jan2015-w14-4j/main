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
	private HashMap<String,Task> templates; 
	private boolean isTest;
	private SystemHandler system;
	
	private static Template template;
	
	
	private Template() {
		templates = new HashMap<String,Task>();
		isTest = false;
	}
	
	
	private Template(boolean test) {
		templates = new HashMap<String,Task>();
		isTest = test;
	}
	
	public static Template getTemplate() {
		if(template == null) {
			template = new Template();
		}
		return template;
	}
	
	public static Template getTemplate(boolean test) {
		if(template == null) {
			template = new Template(test);
		}
		return template;
	}
	
	public ArrayList<Task> processCustomizingCommand(String[] command) 
			throws NoSuchElementException, NumberFormatException {
		
		
		COMMAND_TYPE_TEMPLATE commandType = getCommandType(command[0]);
		ArrayList<Task> result;
		
		assertValidity(command, commandType);
		
		switch(commandType) {
			case addTemplate:
				if(system == null) {
					system = SystemHandler.getSystemHandler();
				}
				
				Task taskToBeAdded;
				if(isTest) {
					taskToBeAdded = new Task(1000, "NEW",
							convertToDateObject("12/09/2015 10:00"),
							convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0);
				}
				else {
					taskToBeAdded = system.requestTask(Integer.parseInt(command[1]));	
				}
				writeOutToFile();
				return addTemplate(command[2], taskToBeAdded);
				
			case viewTemplates:
				return viewTemplates();
				
			case deleteTemplate:
				result = removeTemplate(command[1]);
				writeOutToFile();
				return result;
				
			case editTemplate:
				Task temp = editTemplate(command);
				result = new ArrayList<Task>(); 
				result.add(temp);
				writeOutToFile();
				return result;
				
			case resetTemplates:
				resetTemplates();
				writeOutToFile();
				return new ArrayList<Task>();
			
			case addTemplateInit:
				if(system == null) {
					system = SystemHandler.getSystemHandler();
				}
				Task taskToBeAddedInit = new Task(Integer.parseInt(command[1]),command[2], 
						convertToDateObject(command[3]), convertToDateObject(command[4]), 
						convertToDateObject(command[5]), command[5], command[6],
						Integer.parseInt(command[7]));
				addTemplate(command[2], taskToBeAddedInit);
		}
		return null;
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
		system.writeTemplateToFile(templatesList, match);
		
	}


	private Task editTemplate(String[] command) {
		Task task = templates.get(command[1]);
		if(task == null) {
			throw new NoSuchElementException(NO_SUCH_TEMPLATE);
		}
		if(command[3] != null) {
			task.setTaskName(command[3]);
		}
		if(command[4] != null) {
			Date dateFrom = getDate(command[4]);
			task.setDateFrom(dateFrom);
		}
		if(command[5] != null) {
			Date dateTo = getDate(command[5]);
			task.setDateFrom(dateTo);
		}
		if(command[6] != null) {
			Date deadline = getDate(command[5]);
			task.setDateFrom(deadline);
		}
		if(command[7] != null) {
			task.setLocation(command[7]);
		}
		if(command[8] != null) {
			task.setDetails(command[8]);
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
