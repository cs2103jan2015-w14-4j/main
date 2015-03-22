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
    public static final String[] ADD_TASK_765 = {"addTask", "765", "CS2103T Tutorial", 
        "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "1"};
    public static final String[] ADD_TASK_1000 = {"addTask", null, "CS2103T Tutorial", 
        "18/03/2015 14:00", "18/03/2015 15:00", null, "SOC", null, "1"};
    public static final String[] ADD_TASK_1001 = {"addTask", null, "CS2107 MidTerm", 
        "20/03/2015 12:00", "20/03/2015 13:30", null, "LT18", null, "1"};
    public static final String[] ADD_TASK_1002 = {"addTask", null, "CS2101 Reflection", null, 
        null, "21/03/2015 23:59", null, "name the file properly", "1"};
    public static final String[] DELETE_TASK_1000 = {"deleteTask", "1000", null, null, null, 
        null, null, null, null};    
    public static final String[] DELETE_TASK_1001 = {"deleteTask", "1001", null, null, null, 
            null, null, null, null};
    public static final String[] DELETE_TASK_9999 = {"deleteTask", "9999", null, null, null, 
        null, null, null, null};
    public static final String[] UNDO_OPERATION = {"undoTask", null, null, null, null, null, 
        null, null, null};
    public static final String[] REDO_OPERATION = {"redoTask", null, null, null, null, null, 
        null, null, null};
    public static final String[] EDIT_TASK_1000 = {"editTask", "1000", null, null, 
        "20/03/2015 15:30", null, null, null, null};
    public static final String[] EDIT_TASK_1001 = {"editTask", "1001", null, null, 
        "20/03/2015 15:30", null, "LT108", null, null};
    public static final String[] EDIT_TASK_1002 = {"editTask", "1002", null, null,
        null, null, "IVLE", "", null};
    public static final String[] EDIT_TASK_9999 = {"editTask", "9999", null, null, 
            "20/03/2015 15:30", null, null, null, null};
    public static final String[] VIEW_TASK = {"viewTask", null, null, null, null, null, 
        null, null, null};
    public static final String[] ADD_TASK_1000_SEARCH = {"addTask", null, "CS2103T Tutorial", 
        "18/02/2015 14:00", "18/03/2016 15:00", null, "SOC", null, "1"};
    public static final String[] ADD_TASK_1001_SEARCH = {"addTask", null, "CS4107 MidTerm", 
        "20/01/2015 12:00", "20/03/2017 13:30", null, "LT18", null, "1"};
    public static final String[] ADD_TASK_1002_SEARCH = {"addTask", null, "CS2101 Reflection", 
        null, null, "21/05/2015 23:59", null, "name the file properly", "1"};

    public static final int TASK1000 = 0;
    public static final int TASK1001 = 1;
    public static final int TASK1002 = 2;
    public static final int COMMAND_TYPE = 0;
    public static final String COMMAND_ADD = "addTask";
    public static final String COMMAND_DELETE = "deleteTask";
    public static final String COMMAND_EDIT = "editTask";
    


    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String CLEAR_INFO_INDICATOR = "";
    public static TaskManager myTaskManager;
    public static FileStorage myFileStorage;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        myFileStorage = new FileStorage("testTaskManager.txt");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testAddCommand() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test the ArrayList
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);   
    }


    @Test
    public void testDeleteCommand() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test the ArrayList before delete
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 

        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1002);

        ArrayList<Task> expectDelete = new ArrayList<Task>();
        expectDelete.add(expectTask1001);

        //test the return of processTM for delete
        assertTaskArrayListEquals(myTaskManager.processTM(DELETE_TASK_1001, myFileStorage),
                expectDelete);
        //test the ArrayList after delete
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }
    
    @Test
    public void testUnableToDelete() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        assertTaskArrayListEquals(myTaskManager.processTM(DELETE_TASK_9999, myFileStorage), 
                null);
    }

    @Test
    public void testEditCommand() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);


        ArrayList<Task> expectEdit = new ArrayList<Task>();
        Date newDateTo = convertToDateObject("20/03/2015 15:30");
        expectTasks.get(TASK1001).setDateTo(newDateTo);
        expectTasks.get(TASK1001).setLocation("LT108");;
        expectEdit.add(expectTasks.get(TASK1001));

        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_1001, myFileStorage),
                expectEdit);

        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }
    
    @Test
    public void testUnableToEdit() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_9999, myFileStorage), 
                null);
    }

    @Test
    public void testViewCommand() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        ArrayList<Task> expectView = new ArrayList<Task>(expectTasks);

        //test the ArrayList before view
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        //test the return of processTM for view
        assertTaskArrayListEquals(myTaskManager.processTM(VIEW_TASK, myFileStorage), 
                expectView);        
    }

    @Test
    public void testUndoAndRedoForAdd() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test before doing any undo and redo
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);

        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTask1002);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);

        //test the return of processTM for undo (undo for add is delete)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION, myFileStorage),
                expectUndo);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        //test undo one more time


        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectRedo.add(expectTask1002);
        expectTasks.add(expectTask1002);
        //test the return of processTM for redo (redo for delete is add)
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                expectRedo);
        //test the ArrayList after redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoRedoForDelete() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test before doing any undo and redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTasks.get(1));
        myTaskManager.processTM(DELETE_TASK_1001, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 4);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        
        //test the return of processTM for undo (undo for delete is add)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION, myFileStorage),
                expectUndo);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1002);
        expectTasks.add(expectTask1001);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);

        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectRedo.add(expectTask1001);
        //test the return of processTM for redo (redo for add is delete)
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                expectRedo);
        expectTasks = new ArrayList<Task>();
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1002);
        //test the ArrayList after redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 4);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }

    @Test
    public void testUndoRedoForEdit() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);

        //test before doing any undo and redo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        myTaskManager.processTM(EDIT_TASK_1001, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 4);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        
        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectUndo.add(expectTask1001);
        //test the return of processTM for undo (undo for edit is edit)
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION, myFileStorage),
                expectUndo);
        //test the ArrayList after undo
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 3);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);


        ArrayList<Task> expectRedo = new ArrayList<Task>();
        Date newDateTo = convertToDateObject("20/03/2015 15:30");
        expectTasks.get(TASK1001).setDateTo(newDateTo);
        expectTasks.get(TASK1001).setLocation("LT108");;
        expectRedo.add(expectTasks.get(TASK1001));        

        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                expectRedo);
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 4);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testRedoWithoutUndo() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        expectTasks.add(expectTask1000);
        
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testUndoWithNoMoreUndoForAdd() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        expectTasks.add(expectTask1000);
        
        myTaskManager.processTM(UNDO_OPERATION, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        myTaskManager.processTM(UNDO_OPERATION, myFileStorage);
        myTaskManager.processTM(UNDO_OPERATION, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION, myFileStorage),
                null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 0);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        
        //test the undo redo cycle
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                expectTasks);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testUndoWithNoMoreUndoForDelete() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        
        myTaskManager.processTM(DELETE_TASK_1000, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        
        myTaskManager.processTM(UNDO_OPERATION, myFileStorage);
        Assert.assertNotEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getRedoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        
        //test the undo redo cycle
        myTaskManager.processTM(REDO_OPERATION, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_DELETE);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);

    }
    
    @Test
    public void testUndoWithNoMoreUndoForEdit() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        
        myTaskManager.processTM(EDIT_TASK_1000, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        
        myTaskManager.processTM(UNDO_OPERATION, myFileStorage);
        Assert.assertNotEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getRedoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_ADD);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 1);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 1);
        
        //test the undo redo cycle
        myTaskManager.processTM(REDO_OPERATION, myFileStorage);
        Assert.assertEquals(myTaskManager.getUndoStack().peek()[COMMAND_TYPE], 
                COMMAND_EDIT);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                null);
        Assert.assertEquals(myTaskManager.getUndoStack().size(), 2);
        Assert.assertEquals(myTaskManager.getRedoStack().size(), 0);
    }
    
    @Test
    public void testEditWithEmptyingContent() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002, myFileStorage);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        Task expectTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/03/2015 14:00"), 
                convertToDateObject("18/03/2015 15:00"), null, "SOC", null, 1);
        Task expectTask1001 = new Task(1001, "CS2107 MidTerm", convertToDateObject("20/03/2015 12:00"), 
                convertToDateObject("20/03/2015 13:30"), null, "LT18", null, 1);
        Task expectTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/03/2015 23:59"), null, "name the file properly", 1);
        expectTasks.add(expectTask1000);
        expectTasks.add(expectTask1001);
        expectTasks.add(expectTask1002);
        
        //test the ArrayList before edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks); 
        expectTasks.get(TASK1002).setLocation("IVLE");
        expectTasks.get(TASK1002).setDetails(null);
        ArrayList<Task> expectEdit = new ArrayList<Task>();
        expectEdit.add(expectTasks.get(TASK1002));
        
        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(EDIT_TASK_1002, myFileStorage),
                expectEdit);
        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        
        ArrayList<Task> expectUndo = new ArrayList<Task>();
        expectTasks.get(TASK1002).setLocation(null);
        expectTasks.get(TASK1002).setDetails("name the file properly");
        expectUndo.add(expectTasks.get(TASK1002));
        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(UNDO_OPERATION, myFileStorage),
                expectUndo);
        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
        
        
        ArrayList<Task> expectRedo = new ArrayList<Task>();
        expectTasks.get(TASK1002).setLocation("IVLE");
        expectTasks.get(TASK1002).setDetails(null);
        expectRedo.add(expectTasks.get(TASK1002));
        //test the return of processTM for edit
        assertTaskArrayListEquals(myTaskManager.processTM(REDO_OPERATION, myFileStorage),
                expectUndo);
        //test the ArrayList after edit
        assertTaskArrayListEquals(myTaskManager.getTasks(), expectTasks);
    }
    
    @Test
    public void testIsDateValid() {
        myTaskManager = new TaskManager();
        String date1 = "22/03/2015 23:59";
        String date2 = "29/02/2000 23:59";
        String date3 = "29/02/2012 23:59";
        String invalidDate1 = "29/02/2015 23:59";
        String invalidDate2 = "29/02/1900 23:59";
        String invalidDate3 = "35/01/2013 23:59";
        String invalidDate4 = "25/13/2013 23:59";
        String invalidDate5 = "25/01/2013 24:59";
        String invalidDate6 = "25/01/2013 23:61";
        String invalidDate7 = "00/01/2013 23:41";
        
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
    }
    
    @Test
    public void testSearchTask() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_1000_SEARCH, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1001_SEARCH, myFileStorage);
        myTaskManager.processTM(ADD_TASK_1002_SEARCH, myFileStorage);
        
        Task searchTask1000 = new Task(1000, "CS2103T Tutorial", convertToDateObject("18/02/2015 14:00"), 
                convertToDateObject("18/03/2016 15:00"), null, "SOC", null, 1);
        //Task searchTask1001 = new Task(1001, "CS4107 MidTerm", convertToDateObject("20/01/2015 12:00"), 
        //        convertToDateObject("20/03/2017 13:30"), null, "LT18", null, 1);
        Task searchTask1002 = new Task(1002, "CS2101 Reflection", null, null, 
                convertToDateObject("21/05/2015 23:59"), null, "name the file properly", 1);

        ArrayList<Task> expectTasks = new ArrayList<Task>();
        String[] search1 = {"searchTask", "CS2", null, null, null, null, 
                null, null, null};
        expectTasks.add(searchTask1000);
        expectTasks.add(searchTask1002);
        assertTaskArrayListEquals(myTaskManager.processTM(search1, myFileStorage), 
                expectTasks);
        
        expectTasks = new ArrayList<Task>();
        String[] search2 = {"searchTask", "05/2015", null, null, null, null, 
                null, null, null};
        expectTasks.add(searchTask1002);
        assertTaskArrayListEquals(myTaskManager.processTM(search2, myFileStorage), 
                expectTasks);
        
        String[] searchNotFound = {"searchTask", "cannot found", null, null, null, null, 
                null, null, null};
        expectTasks = null;
        assertTaskArrayListEquals(myTaskManager.processTM(searchNotFound, myFileStorage), 
                expectTasks);
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
    public void testAddWithIDLessThanOneThousand() throws ParseException {
        myTaskManager = new TaskManager();
        myTaskManager.processTM(ADD_TASK_765, myFileStorage);
        
        Assert.assertEquals(myTaskManager.getTasks().get(0).getTID(), 1000);
    }
    
    @Test
    public void testClone() throws ParseException {
        myTaskManager = new TaskManager();
        ArrayList<Task> tks = myTaskManager.processTM(ADD_TASK_1000, myFileStorage);
        tks.get(0).setTID(5000);
        Assert.assertFalse(assertTaskEqual(tks.get(0), myTaskManager.getTasks().get(0)));
        
    }

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
