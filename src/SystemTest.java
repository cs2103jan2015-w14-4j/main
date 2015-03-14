import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class SystemTest {
	public static SystemHandler mySystem;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mySystem = new SystemHandler("assert.txt");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File myfile = new File("assert.txt");
		myfile.deleteOnExit();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		if(!testFullSystem()) {
			System.out.println("Full System Test Not yet implemented");
		}
		if(!testTaskManager()) {
			System.out.println("Task Manager Test Not yet implemented");
		}
		if(!testShortcutManager()) {
			System.out.println("Shortcut Manager Test Not yet implemented");
		}
		if(!testFileStorage()) {
			System.out.println("File Storage Test Not yet implemented");
		}
		if(!testParser()) {
			System.out.println("Parser Test Not yet implemented");
		}
		if(!testCustomizedManager()) {
			System.out.println("Customized Manager Test Not yet implemented");
		}
	}
	
	public boolean testFullSystem() {
		ArrayList<Task> test1 = new ArrayList<Task>();
		ArrayList<Task> expect1 = new ArrayList<Task>();
		test1.add(new Task(123, "NEW", convertToDateObject("12/09/2015 22:00"), 
				convertToDateObject("15/09/2015 12:00"), convertToDateObject("15/09/2015 14:00"), 
				"ABC", "NO DETAIL", 1));
		expect1.add(new Task(123, "NEW", convertToDateObject("12/09/2015 22:00"), 
				convertToDateObject("15/09/2015 12:00"), convertToDateObject("15/09/2015 14:00"), 
				"ABC", "NO DETAIL", 1));
		assertArrayListEquals(test1,expect1);

		ArrayList<Task> test2 = new ArrayList<Task>();
		ArrayList<Task> expect2 = new ArrayList<Task>();
		ArrayList<Task> test3 = new ArrayList<Task>();
		ArrayList<Task> expect3 = new ArrayList<Task>();
		ArrayList<Task> test4 = new ArrayList<Task>();
		ArrayList<Task> expect4 = new ArrayList<Task>();
		ArrayList<Task> test5 = new ArrayList<Task>();
		ArrayList<Task> expect5 = new ArrayList<Task>();
		ArrayList<Task> test6 = new ArrayList<Task>();
		ArrayList<Task> expect6 = new ArrayList<Task>();
		return false;
	}

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String CLEAR_INFO_INDICATOR = "";
	 private Date convertToDateObject(String dateString){
		 try{
		        Date date = null;
		        if(dateString != null && !dateString.equals(CLEAR_INFO_INDICATOR)) {
		            DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		            date = format.parse(dateString);
		        }
		        return date;
		}
	 	catch(ParseException e) {
	 		System.out.println(e);
	 		return null;
	 	} 
	 }
	 
	public boolean testTaskManager() {
		return false;
	}
	
	public boolean testShortcutManager() {
		return false;
	}
	
	public boolean testFileStorage() {
		return false;
	}
	
	public boolean testParser() {
		return false;
	}
	
	public boolean testCustomizedManager() {
		return false;
	}
	
	public boolean assertArrayListEquals(ArrayList<Task> test,ArrayList<Task> expect) {
		int index = 0;
		Assert.assertEquals((Object)test.get(index),(Object)expect.get(index));
		return true;
	}

}
