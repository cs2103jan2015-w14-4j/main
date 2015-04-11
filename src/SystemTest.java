import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;







//Import for natty
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

//@author A0108385B
public class SystemTest {
	private static final String[] DEFAULT_HELP = {"help"};
	private static final String[] DEFAULT_RESET_TEMP = {"resetTemplate", "resetTemp"};
	private static final String[] DEFAULT_DELETE_TEMP = {"deleteTemplate","deleteTemp"};
	private static final String[] DEFAULT_USE_TEMP = {"useTemplate", "useTemp"};
	private static final String[] DEFAULT_VIEW_TEMP = {"viewTemplate","viewTemp"};
	private static final String[] DEFAULT_EDIT_TEMP = {"editTemplate","editTemp"};
	private static final String[] DEFAULT_ADD_TEMP = {"addTemplate","addTemp"};
	private static final String[] DEFAULT_RESET_SHORTCUT = {"resetShortcut","resetKeyword"};
	private static final String[] DEFAULT_DELETE_SHORTCUT = {"deleteShortcut","deleteKeyword"};
	private static final String[] DEFAULT_VIEW_SHORTCUT = {"viewShortcut","viewKeyword"};
	private static final String[] DEFAULT_ADD_SHORTCUT = {"addShortcut","addKeyword"};
	private static final String[] DEFAULT_MARK_TASK = {"mark","markTask"};
	private static final String[] DEFAULT_REDO_TASK = {"redo","redoTask"};
	private static final String[] DEFAULT_UNDO_TASK = {"undo","undoTask"};
	private static final String[] DEFAULT_SEARCH_TASK = {"search", "searchTask"};
	private static final String[] DEFAULT_CLEAR_ATTR = {"clear","clearAttr"};
	private static final String[] DEFAULT_DELETE_TASK = {"delete","deleteTask"};
	private static final String[] DEFAULT_VIEW_TASK = {"view","viewTask"};
	private static final String[] DEFAULT_EDIT_TASK = {"edit","editTask"};
	private static final String[] DEFAULT_ADD_TASK = {"add","addTask"};
	public static final String[] DEFAULT_SET_PATH = {"saveTo"};
	
	private static final String[][] DEFAULT_WORD_SET = {	DEFAULT_ADD_TASK, 		DEFAULT_EDIT_TASK,
														DEFAULT_VIEW_TASK, 		DEFAULT_DELETE_TASK,
														DEFAULT_CLEAR_ATTR,		DEFAULT_SEARCH_TASK, 
														DEFAULT_UNDO_TASK, 		DEFAULT_REDO_TASK,
														DEFAULT_MARK_TASK, 		DEFAULT_ADD_SHORTCUT, 
														DEFAULT_VIEW_SHORTCUT, 	DEFAULT_DELETE_SHORTCUT, 
														DEFAULT_RESET_SHORTCUT, DEFAULT_ADD_TEMP, 
														DEFAULT_EDIT_TEMP, 		DEFAULT_VIEW_TEMP, 
														DEFAULT_USE_TEMP, 		DEFAULT_DELETE_TEMP, 
														DEFAULT_RESET_TEMP, 	DEFAULT_HELP,
														DEFAULT_SET_PATH
													};
	
	
	public static SystemHandler mySystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		mySystem = SystemHandler.getSystemHandler();
	}

	@After
	public void tearDown() throws Exception {
		File myfile = new File("assert.txt");
		myfile.deleteOnExit();
	}

	
	
	//@Test
	// Full System is not testable at production phase due to switching of return statement to void
	// To test, changes on return statement needs to be done
	public void testFullSystem() {
		// TC 1 - simple multiple add
		ArrayList<Task> expect1 = new ArrayList<Task>();
		expect1.add(new Task(10, "NEW",
				convertToDateObject("10/04/2015 19:00"),
				convertToDateObject("10/05/2015 19:00"), null, "ABC", null, 0));
		String test1 = "addTask 'NEW' from 10th April evening to 10th May evening at ABC";
		
		//assertTaskArrayListEquals(mySystem.rawUserInput(test1), expect1);

		// TC2 - continue -- multiple add same inputs then view
		expect1.add(new Task(11, "NEW",
						convertToDateObject("10/04/2015 19:00"),
						convertToDateObject("10/05/2015 19:00"), null, "ABC", null, 0));
		expect1.add(new Task(12, "NEW",
				convertToDateObject("10/04/2015 19:00"),
				convertToDateObject("10/05/2015 19:00"), null, "ABC", null, 0));
		expect1.add(new Task(13, "NEW",
				convertToDateObject("10/04/2015 19:00"),
				convertToDateObject("10/05/2015 19:00"), null, "ABC", null, 0));
		mySystem.rawUserInput("addTask 'NEW' from 10th April evening to 10th May evening at ABC");
		mySystem.rawUserInput("addTask 'NEW' from 10th April evening to 10th May evening at ABC");
		mySystem.rawUserInput("addTask 'NEW' from 10th April evening to 10th May evening at ABC");

		//assertTaskArrayListEquals(mySystem.rawUserInput("view"), expect1);

		// TC3 - continue -- delete and get deleted task
		ArrayList<Task> expect2 = new ArrayList<Task>();
		expect2.add(new Task(14, "TO BE DELETED",
				convertToDateObject("16/04/2015 19:00"),
				convertToDateObject("17/05/2015 19:00"), null, "XYZ", null, 0));
		mySystem.rawUserInput("add 'TO BE DELETED' at XYZ from 16th April evening to 17th May evening");
		//assertTaskArrayListEquals(mySystem.rawUserInput("delete 14"), expect2);
		
		// TC4 - continue -- edit and get back result
		ArrayList<Task> expect3 = new ArrayList<Task>();
		expect3.add(new Task(15, "EDITED",
				convertToDateObject("24/08/2015 19:00"),
				convertToDateObject("24/08/2015 20:00"), null, "NUS", null, 0));
		mySystem.rawUserInput("add 'TO BE DELETED' at XYZ from 24th August evening to 24th August 8pm");
		//assertTaskArrayListEquals(mySystem.rawUserInput("edit 15 title EDITED at NUS"), expect3);
		
	}

	@Test
	public void testTaskManager() {
		// TC1 - test add normal result
		ArrayList<Task> test1 = new ArrayList<Task>();
		ArrayList<Task> expect1 = new ArrayList<Task>();
		test1.add(new Task(123, "NEW", convertToDateObject("12/09/2015 22:00"),
				convertToDateObject("15/09/2015 12:00"),
				convertToDateObject("15/09/2015 14:00"), "ABC", "NO DETAIL", 1));
		expect1.add(new Task(123, "NEW",
				convertToDateObject("12/09/2015 22:00"),
				convertToDateObject("15/09/2015 12:00"),
				convertToDateObject("15/09/2015 14:00"), "ABC", "NO DETAIL", 1));
		assertTaskArrayListEquals(test1, expect1);

		// TC2 - test empty array
		ArrayList<Task> test2 = new ArrayList<Task>();
		ArrayList<Task> expect2 = new ArrayList<Task>();
		assertTaskArrayListEquals(test2, expect2);

		// TC3 - test result with null
		ArrayList<Task> test3 = new ArrayList<Task>();
		ArrayList<Task> expect3 = new ArrayList<Task>();
		test3.add(new Task(123, "ABC", convertToDateObject("12/09/2015 22:00"),
				convertToDateObject("15/09/2015 12:00"), null, null, null, 1));
		expect3.add(new Task(123, "ABC",
				convertToDateObject("12/09/2015 22:00"),
				convertToDateObject("15/09/2015 12:00"), null, null, null, 1));
		assertTaskArrayListEquals(test3, expect3);

	}
	
	@Test
	public void testMatchingShortcut() {
		Shortcut myshortcut = new Shortcut();
		String[] cmd0 = {"resetShortcut",null,null};
		myshortcut.processShortcutCommand(cmd0);
		
		//TC0 - test keyword matching working
		String result = myshortcut.keywordMatching("add");
		Assert.assertEquals(result, "addTask");
		result = myshortcut.keywordMatching("deleteShortcut");
		Assert.assertEquals(result, "deleteShortcut");
		
		String[] cmd2 = {"addShortcut","addM","add"};
		myshortcut.processShortcutCommand(cmd2);
		
		result = myshortcut.keywordMatching("addM");
		Assert.assertEquals(result, "addTask");
		
	}

	@Test
	public void testShortcutManager() {
		Shortcut myshortcut = new Shortcut();
		String[] cmd0 = {"resetShortcut",null,null};
		myshortcut.processShortcutCommand(cmd0);
		
		
		//TC1 - test view and initialize shortcut list
		String[] cmd = {"viewShortcut",null,null};
		String[][] results = myshortcut.processShortcutCommand(cmd);
		String[][] expected1 = DEFAULT_WORD_SET;
		for(int i = 0; i < expected1.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results[i], 1, results[i].length), expected1[i]);
		}
		
		//TC2 - add a shortcut
		String[] cmd2 = {"addShortcut","addM","add"};
		String[][] results2 = myshortcut.processShortcutCommand(cmd2);
		String[][] expected2 = {{"add","addTask","addM"}};
		for(int i = 0; i < results2.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results2[i], 1, results2[i].length), expected2[i]);
		}
		
		//TC3 - add another shortcut
		String[] cmd3 = {"addShortcut","eTemp","editTemplate"};
		String[][] results3 = myshortcut.processShortcutCommand(cmd3);
		String[][] expected3 = {{"editTemplate","editTemp","eTemp"}};
		for(int i = 0; i < expected3.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results3[i], 1, results3[i].length), expected3[i]);
		}
		
		//TC4 - use added shortcut to do something
		String[] cmd4 = {"addShortcut","addS","eTemp"};
		String[][] results4 = myshortcut.processShortcutCommand(cmd4);
		String[][] expected4 = {{"editTemplate","editTemp","eTemp","addS"}};
		for(int i = 0; i < expected4.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results4[i], 1, results4[i].length), expected4[i]);
		}
		
		//TC5 - view all changes
		String[] cmd5 = {"viewShortcut", null, null};
		String[][] results5 = myshortcut.processShortcutCommand(cmd5);
		String[][] expected5 = DEFAULT_WORD_SET;
		String[] changes1 = {"add","addTask","addM"};
		expected5[0] = changes1;
		String[] changes2= {"editTemplate","editTemp","eTemp","addS"};
		expected5[14] = changes2;
		for(int i = 0; i < expected5.length; ++i) { 
			Assert.assertArrayEquals(Arrays.copyOfRange(results5[i], 1, results5[i].length), expected5[i]);
		}
		String[] revert1 = {"add","addTask"};
		expected5[0] = revert1;
		String[] revert2= {"editTemplate","editTemp"};
		expected5[14] = revert2;
		
		//TC6 - delete shortcut
		String[] cmd6 = {"deleteShortcut", "eTemp", null};
		String[][] results6 = myshortcut.processShortcutCommand(cmd6);
		String[][] expected6 = {{"eTemp"}};
		for(int i = 0; i < expected6.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results6[i], 1, results6[i].length), expected6[i]);
		}
		
		//TC7 - reset
		String[] cmd7 = {"resetShortcut", null, null};
		String[][] results7 = myshortcut.processShortcutCommand(cmd7);
		String[][] expected7 = DEFAULT_WORD_SET;
		for(int i = 0; i < expected7.length; ++i) {
			Assert.assertArrayEquals(Arrays.copyOfRange(results7[i], 1, results7[i].length), expected7[i]);
		}
	}

	@Test
	public void testFileStorage() {
		System.out.println("File Storage Test Not yet implemented");
	}
 
	@Test
	public void testCustomizedManager() {
		Template template = new Template();
		try {
			//TC1 - test adding
			Task temp = new Task(1000, "NEW",
					null,
					null, null, "ABC", null, 0);
			String[] cmd1 = {"addTemplate","1000","task1", null, null, null, null, null, null};
			ArrayList<Task> result1 = template.processCustomizingCommand(cmd1);
			ArrayList<Task> expected1 = new ArrayList<Task>();
			expected1.add(temp);
			assertTaskArrayListEquals(expected1, result1);
			
			//TC2 - test view
			String[] cmd2 = {"viewTemplates", null, null, null, null, null, null, null, null};
			ArrayList<Task> result2 = template.processCustomizingCommand(cmd2);
			ArrayList<Task> expected2 = new ArrayList<Task>();
			expected2.add(temp);
			assertTaskArrayListEquals(expected2, result2);
			
			//TC3 - test delete
			String[] cmd3 = {"deleteTemplate", "task1", null, null, null, null, null, null, null};
			ArrayList<Task> result3 = template.processCustomizingCommand(cmd3);
			ArrayList<Task> expected3 = new ArrayList<Task>();
			expected3.add(temp);
			assertTaskArrayListEquals(expected3, result3);
		} catch (Exception e) {
			
		}
		
		
		//TC4 - try delete invalid template
		String[] cmd4 = {"deleteTemplate", "task0", null, null, null, null, null, null, null};
		try {
			template.processCustomizingCommand(cmd4);
			
		} catch(NoSuchElementException e) {
			Assert.assertEquals(e.getMessage(), "No such template exists.");
		} catch(IllegalArgumentException e) {
			
		} catch (Exception e) {
			
		}

		//TC5 - try reset
		try {
			String[] cmd5 = {"resetTemplates", null, null, null, null, null, null, null, null};
			ArrayList<Task> result5 = template.processCustomizingCommand(cmd5);
			assertTaskArrayListEquals(result5,new ArrayList<Task>());
			
		} catch(NoSuchElementException e) {
			
		} catch(IllegalArgumentException e) {
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

	public boolean assertTaskArrayListEquals(ArrayList<Task> test,
			ArrayList<Task> expect) {
		Assert.assertEquals(test.size(), expect.size());
		for (int i = 0; i < test.size(); ++i) {
			Assert.assertTrue(assertTaskEqual(test.get(i), expect.get(i)));
		}

		return true;
	}

	
	public boolean assertTaskEqual(Task taskA, Task taskB) {
		return taskA.isEqual(taskB);
	}

	private Date convertToDateObject(String dateString) {
		try {
			Date date = null;
			if (dateString != null && !dateString.equals("")) {
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				date = format.parse(dateString);
			}
			return date;
		} catch (ParseException e) {
			System.out.println(e);
			return null;
		}
	}
}
