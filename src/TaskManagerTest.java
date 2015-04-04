import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskManagerTest {
    private static final String[] DELETE_TASK_10 = {"deleteTask", "10", null, null, null, 
        null, null, null, null};    
    private static final String[] DELETE_TASK_11 = {"deleteTask", "11", null, null, null, 
        null, null, null, null};
    private static final String[] DELETE_TASK_9999 = {"deleteTask", "9999", null, null, null, 
        null, null, null, null};
    private static final String[] UNDO_OPERATION = {"undoTask", null, null, null, null, null, 
        null, null, null};
    private static final String[] REDO_OPERATION = {"redoTask", null, null, null, null, null, 
        null, null, null};
    private static final String[] EDIT_TASK_11 = {"editTask", "11", null, null, 
        "20/03/2015 15:30", null, "LT108", null, null};
    private static final String[] EDIT_TASK_9999 = {"editTask", "9999", null, null, 
        "20/03/2015 15:30", null, null, null, null};

    private static final int TASK10 = 0;
    private static final int TASK11 = 1;
    private static final int TASK12 = 2;
    private static final int COMMAND_TYPE = 0;
    private static final String COMMAND_ADD = "addTask";
    private static final String COMMAND_DELETE = "deleteTask";
    private static final String COMMAND_EDIT = "editTask";


    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String CLEAR_INFO_INDICATOR = "";
    private static TaskManager myTaskManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //SystemHandler handler = SystemHandler.getSystemHandler("testTaskManager.txt");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }



    //--------------------testing initialization starts-------------------
    @Test
    public void testProcessInitialization() {
        String[] addTask7 = {"addTask", "7", "CS3103T Tutorial", 
                "18/03/2015 12:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        String[] addTask17 = {"addTask", "17", "CS2331 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "1"};
        String[] addTask18 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "3"};

        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask7);
        myTaskManager.processInitialization(addTask10);
        myTaskManager.processInitialization(addTask10);
        myTaskManager.processInitialization(addTask17);
        myTaskManager.processInitialization(addTask18);


        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS3103T Tutorial", convertToDateObject("18/03/2015 12:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask17 = new Task(17, "CS2331 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask18 = new Task(18, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);
        expectTasks.add(expectTask17);
        expectTasks.add(expectTask18);

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }
    //--------------------testing initialization ends---------------------



    //--------------------testing add command starts----------------------
    @Test
    public void testAddCommand() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};

        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);   
    }

    @Test
    public void testAddWithIDLessThanTen() {
        String[] addTask7 = {"addTask", "7", "CS3103T Tutorial", 
                "18/03/2015 12:00", "18/03/2015 15:00", null, "SOC", null, "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask7);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS3103T Tutorial", convertToDateObject("18/03/2015 12:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        expectTasks.add(expectTask10);

        Assert.assertEquals(myTaskManager.getTasks().get(0).getTID(), 10);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testAddWithClashIDs() {
        String[] addTaskClash10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "normal"};
        String[] addTaskClash11 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "Normal"};
        String[] addTaskClash12 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "NORMAL"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTaskClash10);
        myTaskManager.processTM(addTaskClash11);
        myTaskManager.processTM(addTaskClash12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);        
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 
    }

    @Test
    public void testClashedTasks() {
        String[] addTaskClash10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "normal"};
        String[] addTaskClash11 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "Normal"};
        String[] addTaskClash12 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "NORMAL"};

        myTaskManager = new TaskManager();
        ArrayList<Task> expectTasks = new ArrayList<Task>();
        ArrayList<Task> clashTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);        
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);

        expectTasks.add(expectTask10);
        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(addTaskClash10), 
                clashTasks);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 

        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask11);
        clashTasks.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(addTaskClash11), 
                clashTasks);
        expectTasks.add(expectTask11);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 


        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask12);
        clashTasks.add(expectTask10);
        clashTasks.add(expectTask11);
        assertTaskArrayListEquals(myTaskManager.processTM(addTaskClash12), 
                clashTasks);
        expectTasks.add(expectTask12);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testEmptyTaskNameEmptyInput() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "        ", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        try{
            myTaskManager.processTM(addTask11);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Task name cannot be empty");
        }
    }

    @Test
    public void testEmptyTaskNameNull() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, null, 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        try{
            myTaskManager.processTM(addTask11);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Task name cannot be empty");
        }
    }

    @Test
    public void testTaskNameLengthMoreThan30() {
        String[] addTask10 = {"addTask", null, "this task name is definitely gonna"
                + "be more than thirtykadgakhga", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        try{
            myTaskManager.processTM(addTask10);
        } catch (StringIndexOutOfBoundsException e) {
            Assert.assertEquals(e.getMessage(), "task title has maximum length of 30");
        }
    }

    @Test
    public void testTaskLocationMoreThan30() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "null", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27 in Science Faculty"
                        + "National University of Singapore, Singapore, Earth", null,
        "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        try{
            myTaskManager.processTM(addTask11);
        } catch (StringIndexOutOfBoundsException e) {
            Assert.assertEquals(e.getMessage(), "location has maximum length of 30");
        }
    }

    @Test
    public void testTaskDateDuration() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/02/2015 15:00", null, "SOC", null, null};

        myTaskManager = new TaskManager();
        try{
            myTaskManager.processTM(addTask10);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Start must be before end");
        }
    }

    @Test
    public void testInvalidStatus() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "important"};
        myTaskManager = new TaskManager();
        try{
            myTaskManager.processTM(addTask10);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "System does not recognize this status");
        }
    }
    //--------------------testing add command ends-----------------------



    //--------------------testing edit command starts--------------------
    @Test
    public void testUnableToEdit() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/05/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        try {
            myTaskManager.processTM(EDIT_TASK_9999);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "ID does not exist");
        }
    }

    @Test
    public void testEditCommand() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);


        ArrayList<Task> expectEdit = new ArrayList<Task>();
        Date newDateTo = convertToDateObject("20/03/2015 15:30");
        expectTasks.get(TASK11).setDateTo(newDateTo);
        expectTasks.get(TASK11).setLocation("LT108");;
        expectEdit.add(expectTasks.get(TASK11));

        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_11), expectEdit);

        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testEditWithEmptyTaskName() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] editTask10 = {"editTask", "10", "        ", null, 
                null, null, null, null, null};

        try {
            myTaskManager.processTM(editTask10);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Task name cannot be empty");
        }
    }

    @Test
    public void testEditWithTaskNameMoreThan30() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] editTask10 = {"editTask", "10", "I want to meet my best friend on"
                + "next Monday but I don't know what to do how", null, 
                null, null, null, null, null};

        try {
            myTaskManager.processTM(editTask10);
        } catch (StringIndexOutOfBoundsException e) {
            Assert.assertEquals(e.getMessage(), "task title has maximum length of 30");
        }
    }

    @Test
    public void testEditWithDateDurationWrong() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] editTask10 = {"editTask", "10", null, null, 
                "18/02/2015 15:00", null, null, null, null};

        try {
            myTaskManager.processTM(editTask10);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Start must be before end");
        }
    }

    @Test
    public void testEditWithDateNumberWrong() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] editTask10 = {"editTask", "10", null, null, 
                "18/04/2015 15:00", "18/04/2015 15:00", null, null, null};

        try {
            myTaskManager.processTM(editTask10);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Wrong dates entered");
        }
    }

    @Test
    public void testEditWithEmptyingContent() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 
        expectTasks.get(TASK12).setLocation(null);
        expectTasks.get(TASK12).setDetails(null);
        ArrayList<Task> expectEdit = new ArrayList<Task>();
        expectEdit.add(expectTasks.get(TASK12));

        String[] clearAttrTask12 = {"clearAttr", "12", null, null,
                null, null, "", "", null};

        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(clearAttrTask12), expectEdit);
        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testEditWithTimeclash() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);

        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 

        Date newDateTo = convertToDateObject("27/03/2015 15:30");
        expectTasks.get(TASK10).setDateTo(newDateTo);
        ArrayList<Task> expectEdit = new ArrayList<Task>();
        expectEdit.add(expectTasks.get(TASK10));
        expectEdit.add(expectTask11);

        String[] EDIT_TASK10_CLASH = {"editTask", "10", null, null, 
                "27/03/2015 15:30", null, null, null, null};

        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK10_CLASH), expectEdit); 
    }
    //--------------------testing edit command ends----------------------



    //--------------------testing view command starts--------------------
    /*@Test
    public void testViewCommandDefaultByID() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        ArrayList<Task> expectView = new ArrayList<Task>(expectTasks);

        //test the ArrayList before view
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        //test the return of processTM for view
        assertTaskArrayListEquals(myTaskManager.processTM(VIEW_TASK), 
                expectView);        
    }

    @Test
    public void testViewByDateFrom() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);

        String[] viewTaskByDateFrom = {"viewTask", null, "Date From", null, null, null, 
                null, null, null};

        ArrayList<Task> expectView = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask13 = new Task(13, "Homework!", null, null, 
                convertToDateObject("21/04/2015 23:59"), null, null, 3);
        Task expectTask14 = new Task(14, "CS2107 Lecture", convertToDateObject("18/04/2015 14:00"), 
                convertToDateObject("18/04/2015 15:00"), null, "SOC", null, 2);
        expectView.add(expectTask10);
        expectView.add(expectTask11);
        expectView.add(expectTask14);
        expectView.add(expectTask12);
        expectView.add(expectTask13);

        assertTaskArrayListEquals(myTaskManager.processTM(viewTaskByDateFrom), expectView);
    }

    @Test
    public void testViewByDeadline() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);

        String[] viewTaskByDeadline = {"viewTask", null, "Deadline", null, null, null, 
                null, null, null};

        ArrayList<Task> expectView = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask13 = new Task(13, "Homework!", null, null, 
                convertToDateObject("21/04/2015 23:59"), null, null, 3);
        Task expectTask14 = new Task(14, "CS2107 Lecture", convertToDateObject("18/04/2015 14:00"), 
                convertToDateObject("18/04/2015 15:00"), null, "SOC", null, 2);
        expectView.add(expectTask12);
        expectView.add(expectTask13);
        expectView.add(expectTask10);
        expectView.add(expectTask11);
        expectView.add(expectTask14);

        assertTaskArrayListEquals(myTaskManager.processTM(viewTaskByDeadline), expectView);
    }

    @Test
    public void testViewByPriority() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);

        String[] viewTaskByPriority = {"viewTask", null, "Priority", null, null, null, 
                null, null, null};

        ArrayList<Task> expectView = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask13 = new Task(13, "Homework!", null, null, 
                convertToDateObject("21/04/2015 23:59"), null, null, 3);
        Task expectTask14 = new Task(14, "CS2107 Lecture", convertToDateObject("18/04/2015 14:00"), 
                convertToDateObject("18/04/2015 15:00"), null, "SOC", null, 2);
        expectView.add(expectTask10);
        expectView.add(expectTask11);
        expectView.add(expectTask12);
        expectView.add(expectTask14);
        expectView.add(expectTask13);

        assertTaskArrayListEquals(myTaskManager.processTM(viewTaskByPriority), expectView);
    }

    /*@Test
    public void testViewByTaskName() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);

        String[] viewTaskByTaskName = {"viewTask", null, "Task Name", null, null, null, 
                null, null, null};

        ArrayList<Task> expectView = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask13 = new Task(13, "Homework!", null, null, 
                convertToDateObject("21/04/2015 23:59"), null, null, 3);
        Task expectTask14 = new Task(14, "CS2107 Lecture", convertToDateObject("18/04/2015 14:00"), 
                convertToDateObject("18/04/2015 15:00"), null, "SOC", null, 2);
        expectView.add(expectTask10);
        expectView.add(expectTask14);
        expectView.add(expectTask12);
        expectView.add(expectTask13);
        expectView.add(expectTask11);

        assertTaskArrayListEquals(myTaskManager.processTM(viewTaskByTaskName), expectView);
    }

    @Test
    public void testViewByLocation() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);

        String[] viewTaskByLocation = {"viewTask", null, "location", null, null, null, 
                null, null, null};

        ArrayList<Task> expectView = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask13 = new Task(13, "Homework!", null, null, 
                convertToDateObject("21/04/2015 23:59"), null, null, 3);
        Task expectTask14 = new Task(14, "CS2107 Lecture", convertToDateObject("18/04/2015 14:00"), 
                convertToDateObject("18/04/2015 15:00"), null, "SOC", null, 2);
        expectView.add(expectTask11);
        expectView.add(expectTask10);
        expectView.add(expectTask14);
        expectView.add(expectTask12);
        expectView.add(expectTask13);

        assertTaskArrayListEquals(myTaskManager.processTM(viewTaskByLocation), expectView);
    }*/
    //--------------------testing view command ends----------------------



    //--------------------testing delete command starts------------------
    @Test
    public void testDeleteCommand() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList before delete
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 

        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask12);

        ArrayList<Task> expectDelete = new ArrayList<Task>();
        expectDelete.add(expectTask11);

        //test the return of processTM for delete
        assertTaskArrayListEquals(myTaskManager.processTM(DELETE_TASK_11), expectDelete);
        //test the ArrayList after delete
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testUnableToDelete() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        try {
            myTaskManager.processTM(DELETE_TASK_9999);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "ID does not exist");
        }
    }
    //--------------------testing delete command ends--------------------



    //--------------------testing search command starts------------------
    @Test
    public void testSearchEmptyString() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] searchEmptyString = {"searchTask", null, "   ", null, null, null, 
                null, null, null};

        try{
            myTaskManager.processTM(searchEmptyString);
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Search cannot be empty");
        }

    }

    @Test
    public void testSearchTaskNonDateObject() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", null};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        /*Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);*/
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null,
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);

        ArrayList<Task> expectSearch = new ArrayList<Task>();
        String[] search_CS2 = {"searchTask", null, "CS2", null, null, null, 
                null, null, null};
        expectSearch.add(expectTask10);
        expectSearch.add(expectTask12);
        assertTaskArrayListEquals(myTaskManager.processTM(search_CS2), expectSearch);

        expectSearch = new ArrayList<Task>();
        String[] search_tutorial = {"searchTask", null, "tutorial", null, null, null, 
                null, null, null};
        expectSearch.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(search_tutorial), expectSearch);

        String[] searchNotFound = {"searchTask", null, "cannot find", null, null, null, 
                null, null, null};
        assertTaskArrayListEquals(myTaskManager.processTM(searchNotFound), null);
    }

    @Test
    public void testSearchTaskDateObject() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "normal"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};

        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);

        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        /*Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);*/
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null,
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);

        ArrayList<Task> expectSearch = new ArrayList<Task>();
        String[] search_18032015 = {"searchTask", null, "18/03/2015 15:00", null, null, null, 
                null, null, null};
        expectSearch.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(search_18032015), expectSearch);


        expectSearch = new ArrayList<Task>();
        String[] search_21032015 = {"searchTask", null, "21/03/2015 15:00", null, null, null, 
                null, null, null};
        expectSearch.add(expectTask12);
        assertTaskArrayListEquals(myTaskManager.processTM(search_21032015), expectSearch);



        String[] search_27032015 = {"searchTask", null, "27/03/2015 15:00", null, null, null, 
                null, null, null};
        assertTaskArrayListEquals(myTaskManager.processTM(search_27032015), null);


        String[] addTaskForSearch = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "20/03/2015 15:00", null, "SOC", null, "normal"};
        myTaskManager.processTM(addTaskForSearch);
        Task expectTask13 = new Task(13, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("20/03/2015 15:00"), null, "SOC", null, 3);
        expectSearch = new ArrayList<Task>();
        expectSearch.add(expectTask10);
        expectSearch.add(expectTask13);
        assertTaskArrayListEquals(myTaskManager.processTM(search_18032015), expectSearch);
    }
    
    @Test
    public void testSearchTaskDateObjectAgain() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, null, null, null};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "18/03/2015 12:00", null, null, null, null, "normal"};
        String[] addTask12 = {"addTask", null, "LAG3203 MidTerm", 
                null, "18/03/2015 12:00", null, null, null, "normal"};
        String[] addTask13 = {"addTask", null, "CS2211 Reflection", null, 
                null, "18/03/2015 23:59", null, null, "normal"};
        String[] addTask14 = {"addTask", null, "CS2211 Reflection", null, 
                null, null, null, null, "normal"};

        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        myTaskManager.processTM(addTask11);
        myTaskManager.processTM(addTask12);
        myTaskManager.processTM(addTask13);
        myTaskManager.processTM(addTask14);
       
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, null, null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("18/03/2015 12:00"), 
                null, null, null, null, 3);
        Task expectTask12 = new Task(12, "LAG3203 MidTerm", null, 
                convertToDateObject("18/03/2015 12:00"), null, null, null, 3);
        Task expectTask13 = new Task(13, "CS2211 Reflection", null, null, 
                convertToDateObject("18/03/2015 23:59"), null, null, 3);
        ArrayList<Task> expectSearch = new ArrayList<Task>();

        expectSearch.add(expectTask11);
        expectSearch.add(expectTask12);
        expectSearch.add(expectTask10);
        expectSearch.add(expectTask13);    
        
        String[] search_18032015 = {"searchTask", null, "18/03/2015 15:00", null, null, null, 
                null, null, null};
        assertTaskArrayListEquals(myTaskManager.processTM(search_18032015), expectSearch);
    }
    //--------------------testing search command ends--------------------



    //--------------------testing undo and redo command starts------------------
    @Test
    public void testUndoAndRedoForAdd() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        String[] addTask11 = {"addTask", "11", "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "3"};
        String[] addTask12 = {"addTask", "12", "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "normal"};

        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);
        myTaskManager.processInitialization(addTask11);
        myTaskManager.processTM(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test before doing any undo and redo
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);

        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTask12);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);

        //test the return of processTM for undo (undo for add is delete)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION), expectUndo);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        //test undo one more time


        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectRedo.add(expectTask12);
        expectTasks.add(expectTask12);
        //test the return of processTM for redo (redo for delete is add)
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectRedo);
        //test the ArrayList after redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoRedoForDelete() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        String[] addTask11 = {"addTask", "11", "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "3"};
        String[] addTask12 = {"addTask", "12", "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "3"};
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);
        myTaskManager.processInitialization(addTask11);
        myTaskManager.processInitialization(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test before doing any undo and redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTasks.get(1));
        myTaskManager.processTM(DELETE_TASK_11);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        //test the return of processTM for undo (undo for delete is add)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION), expectUndo);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectRedo.add(expectTask11);
        //test the return of processTM for redo (redo for add is delete)
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectRedo);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask12);
        //test the ArrayList after redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }


    @Test
    public void testUndoRedoForEdit() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        String[] addTask11 = {"addTask", null, "LAG3203 MidTerm", 
                "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "3"};
        String[] addTask12 = {"addTask", null, "CS2211 Reflection", null, 
                null, "21/03/2015 23:59", null, "name the file properly", "3"};
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);
        myTaskManager.processInitialization(addTask11);
        myTaskManager.processInitialization(addTask12);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 3);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 3);
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 3);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test before doing any undo and redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        myTaskManager.processTM(EDIT_TASK_11);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTask11);
        //test the return of processTM for undo (undo for edit is edit)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION), expectUndo);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);


        ArrayList<Task> expectRedo = new ArrayList<Task>();
        Date newDateTo = convertToDateObject("20/03/2015 15:30");
        expectTasks.get(TASK11).setDateTo(newDateTo);
        expectTasks.get(TASK11).setLocation("LT108");;
        expectRedo.add(expectTasks.get(TASK11));        

        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectRedo);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoRedoForEdit_V2() {
        String[] addTask10 = {"addTask", "10", "CS2331 Reflection", null, 
                null, null, "SOC", null, "1"};
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2331 Reflection", null, 
                null, null, "SOC", null, 1);
        expectTasks.add(expectTask10);

        //test before doing any undo and redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);

        String[] clear = {"clearAttr", "10", null, "", 
                "", "", "", "", null};
        expectTasks.get(TASK10).setLocation(null);
        ArrayList<Task> expectEdit = new ArrayList<Task>();
        expectEdit.add(expectTasks.get(0));    
        assertTaskArrayListEquals(myTaskManager.processTM(clear), expectEdit);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        expectTasks.get(TASK10).setLocation("SOC");
        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTasks.get(0));
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION), expectUndo);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().peek()[COMMAND_TYPE], COMMAND_EDIT);


        expectTasks.get(TASK10).setLocation(null);
        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectRedo.add(expectTasks.get(0));
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectRedo);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoWithoutChangesToCache() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        try{
            myTaskManager.processTM(UNDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to undo");
        }
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testRedoWithoutUndo() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "3"};
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(addTask10);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        try{
            myTaskManager.processTM(REDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to redo");
        }
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoWithNoMoreUndoForAdd() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "urgent"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        expectTasks.add(expectTask10);

        myTaskManager.processTM(UNDO_OPERATION);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        try{
            myTaskManager.processTM(UNDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to undo");
        }
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);


        //test the undo redo cycle
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        try{
            myTaskManager.processTM(REDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to redo");
        }        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoWithNoMoreUndoForDelete() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "urgent"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        myTaskManager.processTM(DELETE_TASK_10);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        myTaskManager.processTM(UNDO_OPERATION);
        Assert.assertNotEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getRedoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        //test the undo redo cycle
        myTaskManager.processTM(REDO_OPERATION);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        try{
            myTaskManager.processTM(REDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to redo");
        }
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoWithNoMoreUndoForEdit() {
        String[] addTask10 = {"addTask", "10", "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "urgent"};
        myTaskManager = new TaskManager();
        myTaskManager.processTM(addTask10);

        String[] editTask10 = {"editTask", "10", null, 
                null, null, null, "COM11", null, "urgent"};

        myTaskManager.processTM(editTask10);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        myTaskManager.processTM(UNDO_OPERATION);
        Assert.assertNotEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getRedoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        //test the undo redo cycle
        myTaskManager.processTM(REDO_OPERATION);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        try{
            myTaskManager.processTM(REDO_OPERATION);
        } catch (NoSuchElementException e) {
            Assert.assertEquals(e.getMessage(), "No operation to redo");
        }
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    //--------------------testing undo and redo command ends--------------------


    //--------------------testing others starts---------------------------------
    /*@Test
    public void testIsDateValid() {
        myTaskManager = new TaskManager();
        String date1 = "22/03/2015 23:59";
        String date2 = "29/02/2000 23:59";
        String date3 = "29/02/2012 23:59";
        //boundary case for date in February that is not in leap year
        String invalidDate1 = "29/02/2015 23:59";
        //boundary case for date in February that is not in leap year
        String invalidDate2 = "29/02/1900 23:59";
        //boundary case for date
        String invalidDate3 = "32/01/2013 23:59";
        //boundary case for month
        String invalidDate4 = "25/13/2013 23:59";
        //boundary case for hour
        String invalidDate5 = "25/01/2013 24:59";
        //boundary case for minute
        String invalidDate6 = "25/01/2013 23:60";
        //boundary case for date
        String invalidDate7 = "00/01/2013 23:41";
        //boundary case for month
        String invalidDate8 = "01/00/2013 23:41";

        Assert.assertEquals(myTaskManager.isDateValid(date1), true);
        Assert.assertEquals(myTaskManager.isDateValid(date2), true);
        Assert.assertEquals(myTaskManager.isDateValid(date3), true);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate1), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate2), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate3), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate4), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate5), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate6), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate7), false);
        Assert.assertEquals(myTaskManager.isDateValid(invalidDate8), false);
    }

    /*
    @Test
    public void testIsDateFromSmallerThanDateTo() {
        myTaskManager = new TaskManager();

        Date dateFrom1 = convertToDateObject("18/02/2015 14:00");
        Date dateTo1 = convertToDateObject("18/03/2016 15:00");
        Assert.assertTrue(myTaskManager.isDateFromSmallerThanDateTo(dateFrom1, dateTo1));

        Date dateFrom2 = convertToDateObject("18/02/2015 14:00");
        Date dateTo2 = convertToDateObject("18/02/2015 14:00");
        Assert.assertFalse(myTaskManager.isDateFromSmallerThanDateTo(dateFrom2, dateTo2));

        Date dateFrom3 = convertToDateObject("18/02/2015 14:00");
        Date dateTo3 = convertToDateObject("18/02/2010 14:00");
        Assert.assertFalse(myTaskManager.isDateFromSmallerThanDateTo(dateFrom3, dateTo3));
    }*/

    /*@Test
    public void testIsDeadlineAfterCurrentTime() {
        myTaskManager = new TaskManager();
        Date deadline1 = convertToDateObject("18/02/2010 14:00");
        Assert.assertFalse(myTaskManager.isDeadlineAfterCurrentTime(deadline1));

        Date deadline2 = convertToDateObject("18/02/2050 14:00");
        Assert.assertTrue(myTaskManager.isDeadlineAfterCurrentTime(deadline2));
    }*/

    @Test
    public void testClone() {
        String[] addTask10 = {"addTask", null, "CS2103T Tutorial", 
                "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "normal"};
        myTaskManager = new TaskManager();
        ArrayList<Task> tks = myTaskManager.processTM(addTask10);
        tks.get(0).setTID(5000);
        Assert.assertFalse(assertTaskEqual(tks.get(0), myTaskManager.getTasks().get(0)));

    }

    @Test
    public void testTaskToStringArray() {
        Task durationalTask = new Task(10, "durationalTask", convertToDateObject("18/03/2015 12:00"), 
                convertToDateObject("18/03/2015 00:00"), null, null, null, 0);
        Task deadlineTaskDateTo = new Task(11, "deadlineTaskDateTo", null, 
                convertToDateObject("18/03/2015 15:00"), null, null, null, 0);
        Task deadlineTaskDeadline = new Task(12, "deadlineTaskDeadline", null, null, 
                convertToDateObject("18/03/2015 00:00"), null, null, 0);
        Task foreverTask = new Task(17, "foreverTask", null, 
                convertToDateObject("21/03/2015 23:59"), null, null, null, 0);
        Task floatingTask = new Task(18, "floatingTask", null, null, null, null, null, 0);

        Assert.assertEquals("[10, durationalTask, 18/03/2015 12:00, 18/03/2015, null, "
                + "null, Normal]", Arrays.toString(durationalTask.toStringArray()));
        Assert.assertEquals("[11, deadlineTaskDateTo, null, 18/03/2015 15:00, null, "
                + "null, Normal]", Arrays.toString(deadlineTaskDateTo.toStringArray()));
        Assert.assertEquals("[12, deadlineTaskDeadline, null, 18/03/2015, null, "
                + "null, Normal]", Arrays.toString(deadlineTaskDeadline.toStringArray()));
        Assert.assertEquals("[17, foreverTask, null, 21/03/2015 23:59, null, "
                + "null, Normal]", Arrays.toString(foreverTask.toStringArray()));
        Assert.assertEquals("[18, floatingTask, null, null, null, "
                + "null, Normal]", Arrays.toString(floatingTask.toStringArray()));
    }
    //--------------------testing others ends-----------------------------------



    public boolean assertTaskArrayListEquals(ArrayList<Task> test, 
            ArrayList<Task> expected) {
        if(test != null) {
            Assert.assertEquals(test.size(), expected.size());
            for(int i = 0; i < test.size(); ++i) {
                Assert.assertTrue(assertTaskEqual(test.get(i), expected.get(i)));
            }
        } else {
            Assert.assertTrue(test == null);
            Assert.assertTrue(expected == null);
        }
        return true;
    }

    public boolean assertTaskEqual(Task taskA, Task taskB) {
        return taskA.isEqual(taskB);
    }

    public Date convertToDateObject(String dateString){
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
}
