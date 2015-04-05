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

//@author A0108385B
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
		mySystem = SystemHandler.getSystemHandler("assert.txt");
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
		
//		List<DateGroup> groups = parser.parse("the day before next thursday");
//		for(DateGroup group:groups)  {
//			Date dates = group.getDates().get(0);   //Here to get the date (which we need mostly)
//			System.out.println(dates.toLocaleString());   
//		}
//
//		groups = parser.parse("\"do homework 03/04/2015 11:00 \" to 6pm from  4pm  on next wednesday before June");
//		for(DateGroup group:groups)  {
//			List<Date> dates = group.getDates();   //Here to get the date (which we need mostly)
//			for(int i = 0; i < dates.size(); ++i) {
//				System.out.println("count "+i+": "+dates.get(i).toLocaleString()); 
//			}
//			int line = group.getLine();			//Not sure what it does
//			int column = group.getPosition();	//Not sure what
//			String matchingValue = group.getText(); 	//thursday
//			String syntaxTree = group.getSyntaxTree().toStringTree(); //Not sure what
//			Map parseMap = group.getParseLocations();	//Not sure what
//			boolean isRecurreing = group.isRecurring();	//True
//			Date recursUntil = group.getRecursUntil();	//Mon Jun 01 21:25:06 SGT 2015
//			System.out.println(recursUntil);	
//			System.out.println(line);
//
//		}
		
//		groups = parser.parse("assignment from tuesday to wednesday");
//		for(DateGroup group:groups)  {
//			List<Date> dates = group.getDates();   //for range date
//			for(Date date:dates)
//				System.out.println(date);
//		}
	}
	
	
//	@Test
	public void testFullSystem() {
		// TC 1 - simple multiple add
		ArrayList<Task> expect1 = new ArrayList<Task>();
		//"ID","Title","From","To","On","At","Det","Pri"
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

//	@Test
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
		String[][] expected1 = Shortcut.defaultWordsSet;
		for(int i = 0; i < expected1.length; ++i) {
			Assert.assertArrayEquals(results[i], expected1[i]);
		}
		
		//TC2 - add a shortcut
		String[] cmd2 = {"addShortcut","addM","add"};
		String[][] results2 = myshortcut.processShortcutCommand(cmd2);
		String[][] expected2 = {{"addTask","add","addM"}};
		for(int i = 0; i < results2.length; ++i) {
			Assert.assertArrayEquals(results2[i], expected2[i]);
		}
		
		//TC3 - add another shortcut
		String[] cmd3 = {"addShortcut","eTemp","editTemplate"};
		String[][] results3 = myshortcut.processShortcutCommand(cmd3);
		String[][] expected3 = {{"editTemplate","editTemplate","eTemp"}};
		for(int i = 0; i < expected3.length; ++i) {
			Assert.assertArrayEquals(results3[i], expected3[i]);
		}
		
		//TC4 - use added shortcut to do something
		String[] cmd4 = {"addShortcut","addS","eTemp"};
		String[][] results4 = myshortcut.processShortcutCommand(cmd4);
		String[][] expected4 = {{"editTemplate","eTemp","addS"}};
		for(int i = 0; i < expected4.length; ++i) {
			Assert.assertArrayEquals(results4[i], expected4[i]);
		}
		
		//TC5 - view all changes
		String[] cmd5 = {"viewShortcut", null, null};
		String[][] results5 = myshortcut.processShortcutCommand(cmd5);
		String[][] expected5 = Shortcut.defaultWordsSet;
		String[] changes1 = {"add","addTask","addM"};
		expected5[0] = changes1;
		String[] changes2= {"editTemplate","editTemp","eTemp","addS"};
		expected5[14] = changes2;
		for(int i = 0; i < expected5.length; ++i) { 
			Assert.assertArrayEquals(results5[i], expected5[i]);
		}
		String[] revert1 = {"add","addTask"};
		expected5[0] = revert1;
		String[] revert2= {"editTemplate","editTemp"};
		expected5[14] = revert2;
		
		//TC6 - delete shortcut
		String[] cmd6 = {"deleteShortcut", "eTemp", null};
		String[][] results6 = myshortcut.processShortcutCommand(cmd6);
		String[][] expected6 = {{"editTemplate","eTemp"}};
		for(int i = 0; i < expected6.length; ++i) {
			Assert.assertArrayEquals(results6[i], expected6[i]);
		}
		
		//TC7 - reset
		String[] cmd7 = {"resetShortcut", null, null};
		String[][] results7 = myshortcut.processShortcutCommand(cmd7);
		String[][] expected7 = Shortcut.defaultWordsSet;
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

//		FlexiParser test1 = new FlexiParser();
//		String[] expect1  = {"add",null,"homework",
//								"20/04/2015 20:00","20/04/2015 22:00",null,
//								null,null,null};
//		String[] abc = test1.parseText("add,homework,on,20/04/2015,from,20:00,to,22:00");
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
//		Assert.assertArrayEquals(abc, expect1);
	}
 
	@Test
	public void testCustomizedManager() {
		Template template = new Template(true);
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
			System.out.println("HERE");
			template.processCustomizingCommand(cmd4);
			
		} catch(NoSuchElementException e) {
			Assert.assertEquals(e.getMessage(), "No such template saved in the system");
		} catch(IllegalArgumentException e) {
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		try {
			String[] cmd5 = {"resetTemplates", null, null, null, null, null, null, null, null};
			ArrayList<Task> result5 = template.processCustomizingCommand(cmd5);
			assertTaskArrayListEquals(result5,new ArrayList<Task>());
			
		} catch(NoSuchElementException e) {
			
		} catch(IllegalArgumentException e) {
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		//TC5 - try reset
		
	}

	public boolean assertTaskArrayListEquals(ArrayList<Task> test,
			ArrayList<Task> expect) {
		Assert.assertEquals(test.size(), expect.size());
		for (int i = 0; i < test.size(); ++i) {
//			if(!assertTaskEqual(test.get(i), expect.get(i))) {
//				showNotMatch(test.get(i), expect.get(i));
//				showTask(test.get(i));
//				showTask(expect.get(i));	
//			}
			Assert.assertTrue(assertTaskEqual(test.get(i), expect.get(i)));
		}

		return true;
	}

	public void showNotMatch(Task a, Task b) {
		if(!a.getTaskName().equals(b.getTaskName()))
			System.out.println("#NAME = " + a.getTaskName());
		if(a.getTID() != b.getTID())
			System.out.println("#TID  = " + a.getTID());
		if(!a.getDateFrom().equals(b.getDateFrom()))
			System.out.println("#DFro = " + a.getDateFrom());
		if(!a.getDateTo().equals(b.getDateTo()))
			System.out.println("#DTo  = " + a.getDateTo());
		if(!a.getDeadline().equals(b.getDeadline()))
			System.out.println("#dead = " + a.getDeadline());
		if(!a.getLocation().equals(b.getLocation()))
			System.out.println("#loca = " + a.getLocation());
		if(!a.getDetails().equals(b.getDetails()))
			System.out.println("#Deta = " + a.getDetails());
		if(a.getStatus() != b.getStatus())
			System.out.println("#State= " + a.getStatus());
		if(a.getPriority() != b.getPriority())
			System.out.println("#prio = " + a.getPriority());

	}
	
	public boolean assertTaskEqual(Task taskA, Task taskB) {
		return taskA.isEqual(taskB);
	}

	
	private void showTask(Task t) {
		System.out.println("NAME = " + t.getTaskName());
		System.out.println("TID  = " + t.getTID());
		System.out.println("DFro = " + t.getDateFrom());
		System.out.println("DTo  = " + t.getDateTo());
		System.out.println("dead = " + t.getDeadline());
		System.out.println("loca = " + t.getLocation());
		System.out.println("Deta = " + t.getDetails());
		System.out.println("State= " + t.getStatus());
		System.out.println("prio = " + t.getPriority());
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
