import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		System.out.println("Shortcut Manager Test Not yet implemented");
	}

	@Test
	public void testFileStorage() {
		System.out.println("File Storage Test Not yet implemented");
	}

	@Test
	public void testParser() {

		FlexiParser test1 = new FlexiParser("add,homework,on,20/04/2015,from,20:00,to,22:00");
		String[] expect1  = {"add",null,"homework",
								"20/04/2015 20:00","20/04/2015 22:00",null,
								null,null,null};
		String[] abc = test1.getStringArray();
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
		Assert.assertArrayEquals(test1.getStringArray(), expect1);
	}
 
	@Test
	public void testCustomizedManager() {
		System.out.println("Customized Manager Test Not yet implemented");
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
