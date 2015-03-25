import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class SystemTest {

	public static SystemHandler mySystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		mySystem = new SystemHandler("assert.txt");
	}

	@After
	public void tearDown() throws Exception {
		File myfile = new File("assert.txt");
		myfile.deleteOnExit();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNatty() {

		Parser parser = new Parser();
		
		List<DateGroup> groups = parser.parse("the day before next thursday");
		for(DateGroup group:groups)  {
			Date dates = group.getDates().get(0);   //Here to get the date (which we need mostly)
			System.out.println(dates.toLocaleString());   
		}

		groups = parser.parse("every thursday until June");
		for(DateGroup group:groups)  {
			Date dates = group.getDates().get(0);   //Here to get the date (which we need mostly)
			System.out.println(dates.toLocaleString());        
			int line = group.getLine();			//Not sure what it does
			int column = group.getPosition();	//Not sure what
			String matchingValue = group.getText(); 	//thursday
			String syntaxTree = group.getSyntaxTree().toStringTree(); //Not sure what
			Map parseMap = group.getParseLocations();	//Not sure what
			boolean isRecurreing = group.isRecurring();	//True
			Date recursUntil = group.getRecursUntil();	//Mon Jun 01 21:25:06 SGT 2015
			System.out.println(recursUntil);	
			System.out.println(line);

		}
		
		groups = parser.parse("assignment from tuesday to wednesday");
		for(DateGroup group:groups)  {
			List<Date> dates = group.getDates();   //for range date
			for(Date date:dates)
				System.out.println(date);
		}
	}
	
	
	@Test
	public void testFullSystem() {
		// TC 1 - simple multiple add
		String test1 = "add,NEW,at,ABC,on,12/09/2015,from,10:00,to,12:00";
		ArrayList<Task> expect1 = new ArrayList<Task>();

		expect1.add(new Task(1000, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		assertTaskArrayListEquals(mySystem.rawUserInput(test1), expect1);

		// TC2 - continue -- multiple add same inputs then view

		expect1.add(new Task(1001, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		expect1.add(new Task(1002, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		expect1.add(new Task(1003, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		expect1.add(new Task(1004, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		mySystem.rawUserInput("add,NEW,at,ABC,on,12/09/2015,from,10:00,to,12:00");
		mySystem.rawUserInput("add,NEW,at,ABC,on,12/09/2015,from,10:00,to,12:00");
		mySystem.rawUserInput("add,NEW,at,ABC,on,12/09/2015,from,10:00,to,12:00");
		mySystem.rawUserInput("add,NEW,at,ABC,on,12/09/2015,from,10:00,to,12:00");

		assertTaskArrayListEquals(mySystem.rawUserInput("view"), expect1);

		// TC3 - continue -- delete and get deleted task
		ArrayList<Task> expect2 = new ArrayList<Task>();
		expect2.add(new Task(1005, "TO BE DELETED",
				convertToDateObject("16/10/2015 10:00"),
				convertToDateObject("16/10/2015 12:00"), null, "XYZ", null, 0));
		mySystem.rawUserInput("add,TO BE DELETED,at,XYZ,on,16/10/2015,from,10:00,to,12:00");
		assertTaskArrayListEquals(mySystem.rawUserInput("delete,1005"), expect2);
		
		// TC4 - continue -- edit and get back result
		ArrayList<Task> expect3 = new ArrayList<Task>();
		expect3.add(new Task(1004, "EDITED",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "NUS", null, 0));
		assertTaskArrayListEquals(mySystem.rawUserInput("edit,1004,name,EDITED,at,NUS"), expect3);
		
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

		// TC4 dummy
		ArrayList<Task> test4 = new ArrayList<Task>();
		ArrayList<Task> expect4 = new ArrayList<Task>();
		assertTaskArrayListEquals(test4, expect4);

		// TC5 dummy
		ArrayList<Task> test5 = new ArrayList<Task>();
		ArrayList<Task> expect5 = new ArrayList<Task>();
		assertTaskArrayListEquals(test5, expect5);

		// TC6 dummy
		ArrayList<Task> test6 = new ArrayList<Task>();
		ArrayList<Task> expect6 = new ArrayList<Task>();
		assertTaskArrayListEquals(test6, expect6);
		

		
	}

	@Test
	public void testShortcutManager() {
		Shortcut myshortcut = Shortcut.getShortcut();
		String[] cmd0 = {"resetShortcut",null,null};
		myshortcut.processShortcutCommand(cmd0);
		
		//TC0 - test keyword matching working
		String result = myshortcut.keywordMatching("add");
		Assert.assertEquals(result, "addTask");
		result = myshortcut.keywordMatching("deleteShortcut");
		Assert.assertEquals(result, "deleteShortcut");
		
		
		//TC1 - test view and initialize shortcut list
		String[] cmd = {"viewShortcuts",null,null};
		String[][] results = myshortcut.processShortcutCommand(cmd);
		String[][] expected1 = {
								{"add"}, {"edit"}, {"view"}, {"delete"}, {"undo"}, {"redo"},
								{"addShortcut"}, {"viewShortcuts"}, {"deleteShortcut"},
								{"resetShortcuts"}, {"addTemplate"}, {"editTemplate"},
								{"viewTemplates"}, {"deleteTemplate"}, {"resetTemplates"}, 
								{"help"},
							};
		for(int i = 0; i < expected1.length; ++i) {
			Assert.assertArrayEquals(results[i], expected1[i]);
		}
		
		//TC2 - add a shortcut
		String[] cmd2 = {"addShortcut","add","+"};
		String[][] results2 = myshortcut.processShortcutCommand(cmd2);
		String[][] expected2 = {{"addTask","add","+"}};
		for(int i = 0; i < expected2.length; ++i) {
			Assert.assertArrayEquals(results2[i], expected2[i]);
		}
		
		//TC3 - add another shortcut
		String[] cmd3 = {"addShortcut","editTemplate","eT"};
		String[][] results3 = myshortcut.processShortcutCommand(cmd3);
		String[][] expected3 = {{"editTemplate","editTemplate","eT"}};
		for(int i = 0; i < expected3.length; ++i) {
			Assert.assertArrayEquals(results3[i], expected3[i]);
		}
		
		//TC4 - use added shortcut to do something
		String[] cmd4 = {"addShortcut","eT","addS"};
		String[][] results4 = myshortcut.processShortcutCommand(cmd4);
		String[][] expected4 = {{"editTemplate","eT","addS"}};
		for(int i = 0; i < expected4.length; ++i) {
			Assert.assertArrayEquals(results4[i], expected4[i]);
		}
		
		//TC5 - view all changes
		String[] cmd5 = {"viewShortcuts", null, null};
		String[][] results5 = myshortcut.processShortcutCommand(cmd5);
		String[][] expected5 = {
								{"add","+"}, {"edit"}, {"view"}, {"delete"}, {"undo"}, {"redo"},
								{"addShortcut"}, {"viewShortcuts"}, {"deleteShortcut"},
								{"resetShortcuts"}, {"addTemplate"}, {"editTemplate","eT","addS"},
								{"viewTemplates"}, {"deleteTemplate"}, {"resetTemplates"}, 
								{"help"},
							};
		for(int i = 0; i < expected5.length; ++i) {
			Assert.assertArrayEquals(results5[i], expected5[i]);
		}
		
		//TC6 - delete shortcut
		String[] cmd6 = {"deleteShortcut", "eT", null};
		String[][] results6 = myshortcut.processShortcutCommand(cmd6);
		String[][] expected6 = {{"editTemplate","eT"}};
		for(int i = 0; i < expected6.length; ++i) {
			Assert.assertArrayEquals(results6[i], expected6[i]);
		}
		
		//TC7 - reset
		String[] cmd7 = {"resetShortcut", null, null};
		String[][] results7 = myshortcut.processShortcutCommand(cmd7);
		String[][] expected7 = {
				{"add"}, {"edit"}, {"view"}, {"delete"}, {"undo"}, {"redo"},
				{"addShortcut"}, {"viewShortcuts"}, {"deleteShortcut"},
				{"resetShortcuts"}, {"addTemplate"}, {"editTemplate"},
				{"viewTemplates"}, {"deleteTemplate"}, {"resetTemplates"}, 
				{"help"},
			};
		for(int i = 0; i < expected7.length; ++i) {
			Assert.assertArrayEquals(results7[i], expected7[i]);
		}
	}

	@Test
	public void testFileStorage() {
		System.out.println("File Storage Test Not yet implemented");
	}

	@Test
	public void testParser() {

		FlexiParser test1 = new FlexiParser();
		String[] expect1  = {"add",null,"homework",
								"20/04/2015 20:00","20/04/2015 22:00",null,
								null,null,null};
		String[] abc = test1.parseText("add,homework,on,20/04/2015,from,20:00,to,22:00");
//		for(int i=0;i<expect1.length;++i) {
//			
//			if(abc[i] == expect1[i] ||
//				(abc[i] != null && abc[i].equals(expect1[i]))) {
//				continue;
//			}
//			else {
//				System.out.println(i+" "+abc[i]+" "+expect1[i]);	
//			}
//			
//		}
		Assert.assertArrayEquals(abc, expect1);
	}
 
	@Test
	public void testCustomizedManager() {
		//TC1 - test adding
		Template template = new Template(true);
		String[] cmd1 = {"addTemplate","1000","task1", null, null, null, null, null, null};
		ArrayList<Task> result1 = template.processCustomizingCommand(cmd1);
		ArrayList<Task> expected1 = new ArrayList<Task>();
		expected1.add(new Task(1000, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		assertTaskArrayListEquals(expected1, result1);
		
		//TC2 - test view
		String[] cmd2 = {"viewTemplates", null, null, null, null, null, null, null, null};
		ArrayList<Task> result2 = template.processCustomizingCommand(cmd2);
		ArrayList<Task> expected2 = new ArrayList<Task>();
		expected2.add(new Task(1000, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		assertTaskArrayListEquals(expected2, result2);
		
		//TC3 - test delete
		String[] cmd3 = {"deleteTemplate", "task1", null, null, null, null, null, null, null};
		ArrayList<Task> result3 = template.processCustomizingCommand(cmd3);
		ArrayList<Task> expected3 = new ArrayList<Task>();
		expected3.add(new Task(1000, "NEW",
				convertToDateObject("12/09/2015 10:00"),
				convertToDateObject("12/09/2015 12:00"), null, "ABC", null, 0));
		assertTaskArrayListEquals(expected3, result3);
		
		//TC4 - try delete invalid template
		String[] cmd4 = {"deleteTemplate", "task0", null, null, null, null, null, null, null};
		try {
			template.processCustomizingCommand(cmd4);
			
		} catch(NoSuchElementException e) {
			Assert.assertEquals(e.getMessage(), "No such template saved in the system");
		}
		//TC5 - try reset
		String[] cmd5 = {"resetTemplates", null, null, null, null, null, null, null, null};
		ArrayList<Task> result5 = template.processCustomizingCommand(cmd5);
		assertTaskArrayListEquals(result5,new ArrayList<Task>());
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

	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	private static final String CLEAR_INFO_INDICATOR = "";

	private Date convertToDateObject(String dateString) {
		try {
			Date date = null;
			if (dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
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
