import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Template {
	
	public static final String NO_SUCH_TEMPLATE = "No such template saved in the system";
	private HashMap<String,Task> templates; 
	
	public Template() {
		templates = new HashMap<String,Task>();
	}
	
	public String[] processCustomizingCommand(String[] command) throws NoSuchElementException {
		return null; //stub
	}
	
	public Task removeTemplate(String name) throws NoSuchElementException {
		Task deletedTask = removeFromMap(name);
		
		if(deletedTask == null) {
			throw new NoSuchElementException(NO_SUCH_TEMPLATE);
		}
		else {
			return deletedTask;
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
	
	private Task addTemplate(String name, Task template) {
		boolean sameName = hasSameName(name);
		if(!sameName) {
			
			Task returnTemplate = insertTemplateIntoMap(name, template);
			assert(returnTemplate != null);
			
			return returnTemplate;
		}
		else {
			return null;
		}
	}
	
	
	private Task insertTemplateIntoMap(String name, Task template) {
		return templates.put(name, template);
	}
	
	private boolean hasSameName(String name) {
		return templates.containsKey(name);
	}
	
}
