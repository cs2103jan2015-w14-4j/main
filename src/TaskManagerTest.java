import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskManagerTest {
    public static final String[] ADD_TASK_7 = {"addTask", "7", "CS3103T Tutorial", 
        "18/03/2015 12:00", "18/03/2015 15:00", null, "SOC", null, "1"};
    public static final String[] ADD_TASK_10 = {"addTask", null, "CS2103T Tutorial", 
        "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "1"};
    public static final String[] ADD_TASK_11 = {"addTask", null, "LAG3203 MidTerm", 
        "20/03/2015 12:00", "20/03/2015 13:30", null, "LT27", null, "1"};
    public static final String[] ADD_TASK_12 = {"addTask", null, "CS2211 Reflection", null, 
        null, "21/03/2015 23:59", null, "name the file properly", "1"};
    public static final String[] ADD_TASK_13 = {"addTask", null, "Homework!", null, 
        null, "21/04/2015 23:59", null, null, "3"};
    public static final String[] ADD_TASK_14 = {"addTask", null, "CS2107 Lecture", 
        "18/04/2015 14:00", "18/04/2015 15:00", null, "SOC", null, "2"};
    public static final String[] ADD_TASK_17 = {"addTask", "17", "CS2331 Reflection", null, 
        null, "21/03/2015 23:59", null, "name the file properly", "1"};
    public static final String[] DELETE_TASK_10 = {"deleteTask", "10", null, null, null, 
        null, null, null, null};    
    public static final String[] DELETE_TASK_11 = {"deleteTask", "11", null, null, null, 
        null, null, null, null};
    public static final String[] DELETE_TASK_9999 = {"deleteTask", "9999", null, null, null, 
        null, null, null, null};
    public static final String[] UNDO_OPERATION = {"undoTask", null, null, null, null, null, 
        null, null, null};
    public static final String[] REDO_OPERATION = {"redoTask", null, null, null, null, null, 
        null, null, null};
    public static final String[] EDIT_TASK_10 = {"editTask", "10", null, null, 
        "18/03/2015 15:30", null, null, null, null};
    public static final String[] EDIT_TASK_11 = {"editTask", "11", null, null, 
        "20/03/2015 15:30", null, "LT108", null, null};
    public static final String[] EDIT_TASK_12 = {"editTask", "12", null, null,
        null, null, "IVLE", "", null};
    public static final String[] EDIT_TASK_9999 = {"editTask", "9999", null, null, 
        "20/03/2015 15:30", null, null, null, null};
    public static final String[] VIEW_TASK = {"viewTask", null, null, null, null, null, 
        null, null, null};

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
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    
    
    //--------------------testing initialization starts-------------------
    @Test
    public void testProcessInitialization() {
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_7);
        myTaskManager.processInitialization(ADD_TASK_10);
        myTaskManager.processInitialization(ADD_TASK_10);
        myTaskManager.processInitialization(ADD_TASK_17);
        myTaskManager.processInitialization(ADD_TASK_12);
        
        
        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS3103T Tutorial", convertToDateObject("18/03/2015 12:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask17 = new Task(17, "CS2331 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        Task expectTask18 = new Task(18, "CS2211 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
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

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);   
    }

    @Test
    public void testAddWithIDLessThanTen() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_7);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS3103T Tutorial", convertToDateObject("18/03/2015 12:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        expectTasks.add(expectTask10);

        Assert.assertEquals(myTaskManager.getTasks().get(0).getTID(), 10);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);

    }

    @Test
    public void testAddWithClashIDs() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_10);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);        
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        expectTasks.add(expectTask10);
        expectTasks.add(expectTask11);
        expectTasks.add(expectTask12);

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 
    }

    @Test
    public void testClasedTasks() {
        myTaskManager = new TaskManager();
        ArrayList<Task> expectTasks = new ArrayList<Task>();
        ArrayList<Task> clashTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);        
        Task expectTask11 = new Task(11, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask12 = new Task(12, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);

        expectTasks.add(expectTask10);
        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(ADD_TASK_10), 
                clashTasks);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 

        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask11);
        clashTasks.add(expectTask10);
        assertTaskArrayListEquals(myTaskManager.processTM(ADD_TASK_10), 
                clashTasks);
        expectTasks.add(expectTask11);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 


        clashTasks = new ArrayList<Task> ();
        clashTasks.add(expectTask12);
        clashTasks.add(expectTask10);
        clashTasks.add(expectTask11);
        assertTaskArrayListEquals(myTaskManager.processTM(ADD_TASK_10), 
                clashTasks);
        expectTasks.add(expectTask12);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }
    //--------------------testing add command ends-----------------------


    
    //--------------------testing edit command starts--------------------
    @Test
    public void testUnableToEdit() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_9999), null);
    }

    @Test
    public void testEditCommand() {
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
    public void testEditWithEmptyingContent() {
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

        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 
        expectTasks.get(TASK12).setLocation("IVLE");
        expectTasks.get(TASK12).setDetails(null);
        ArrayList<Task> expectEdit = new ArrayList<Task>();
        expectEdit.add(expectTasks.get(TASK12));

        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_12), expectEdit);
        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }

    @Test
    public void testEditWithTimeclash() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        
        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);
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
    @Test
    public void testViewCommandDefault() {
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
    public void testViewLocation() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);
        myTaskManager.processTM(ADD_TASK_13);
        myTaskManager.processTM(ADD_TASK_14);
        
        String[] VIEW_TASK_LOCATION = {"viewTask", null, "6", null, null, null, 
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
        
        assertTaskArrayListEquals(myTaskManager.processTM(VIEW_TASK_LOCATION), expectView);

    }
    //--------------------testing view command ends----------------------


    
    //--------------------testing delete command starts------------------
    @Test
    public void testDeleteCommand() {
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
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        assertTaskArrayListEquals(myTaskManager.processTM(DELETE_TASK_9999), null);
    }
    //--------------------testing delete command ends--------------------


    
    //--------------------testing search command starts------------------
    @Test
    public void testSearchTask() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
        myTaskManager.processTM(ADD_TASK_11);
        myTaskManager.processTM(ADD_TASK_12);

        Task expectTask10 = new Task(10, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        /*Task expectTask11 = new Task(11, "LAG3203 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT27", null, 1);*/
        Task expectTask12 = new Task(12, "CS2211 Reflection", null, null,
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);

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
    //--------------------testing search command ends--------------------

    

    //--------------------testing undo and redo command starts------------------
    @Test
    public void testUndoAndRedoForAdd() {
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_10);
        myTaskManager.processInitialization(ADD_TASK_11);
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
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_10);
        myTaskManager.processInitialization(ADD_TASK_11);
        myTaskManager.processInitialization(ADD_TASK_12);

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
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_10);
        myTaskManager.processInitialization(ADD_TASK_11);
        myTaskManager.processInitialization(ADD_TASK_12);

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
    public void testUndoWithoutChangesToCache() {
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_10);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION), null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testRedoWithoutUndo() {
        myTaskManager = new TaskManager();
        myTaskManager.processInitialization(ADD_TASK_10);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testUndoWithNoMoreUndoForAdd() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
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
        myTaskManager.processTM(UNDO_OPERATION);
        myTaskManager.processTM(UNDO_OPERATION);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION),null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        //test the undo redo cycle
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testUndoWithNoMoreUndoForDelete() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);
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
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoWithNoMoreUndoForEdit() {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_10);

        myTaskManager.processTM(EDIT_TASK_10);
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
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION), null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    //--------------------testing undo and redo command ends--------------------


    
    //--------------------testing others starts---------------------------------
    @Test
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
    }

    @Test
    public void testIsDeadlineAfterCurrentTime() {
        myTaskManager = new TaskManager();
        Date deadline1 = convertToDateObject("18/02/2010 14:00");
        Assert.assertFalse(myTaskManager.isDeadlineAfterCurrentTime(deadline1));

        Date deadline2 = convertToDateObject("18/02/2050 14:00");
        Assert.assertTrue(myTaskManager.isDeadlineAfterCurrentTime(deadline2));
    }
    
    @Test
    public void testClone() {
        myTaskManager = new TaskManager();
        ArrayList<Task> tks = myTaskManager.processTM(ADD_TASK_10);
        tks.get(0).setTID(5000);
        Assert.assertFalse(assertTaskEqual(tks.get(0), myTaskManager.getTasks().get(0)));

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
