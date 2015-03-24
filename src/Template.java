import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Template {
	
	public static final String NO_SUCH_TEMPLATE = "No such template saved in the system";
	private HashMap<String,Task> templates; 
	private SystemHandler system;
	
	public Template() {
		templates = new HashMap<String,Task>();
		
	}
	
	public ArrayList<Task> processCustomizingCommand(String[] command) 
			throws NoSuchElementException, NumberFormatException {
		String commandType = command[0];
		assert(command.length == 3);
		switch(getCommandType(commandType)) {
			case addTemplate:
				if(system == null) {
					system = SystemHandler.getSystemHandler();
				}
				Task taskToBeAdded = system.requestTask(Integer.parseInt(command[2]));
				return addTemplate(command[1], taskToBeAdded);
				
			case viewTemplates:
				assert(command[1] == null);
				assert(command[2] == null);
				return viewTemplates();
				
			case deleteTemplate:
				assert(command[2] == null);
				return removeTemplate(command[1]);
				
			case editTemplate:
				return null;
			case resetTemplates:
				resetTemplates();
				return new ArrayList<Task>();
		}
		return null;
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
			templateList.add(templates.next());
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
	
}
