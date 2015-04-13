import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;

//@author A0101735R
/**
 * This class creates the User Interface which handles all user interactions.
 *
 */

@SuppressWarnings("serial")
public class UserInterface {

    public JFrame frame_;
    private JTextField textField_;
    private JTextArea outputArea_;
    private JTextPane sysFeedbackArea_;
    private JTable taskTable_;
    private JTable shortcutTable_;
    private JTableHeader taskHeader_;
    private JTableHeader shortcutHeader_;
    private TaskTableModel model_;
    private JScrollPane scrollPaneMain_;
    private JScrollPane scrollPaneInput_;
    private ArrayList<String[]> outputArrayString_;
    private ArrayList<String[]> shortcutArrayString_;
    private String prevInput_;
    private SystemHandler mainHandler;

    //setting values for column widths in percentage
    // column widths for task table
    private static final double COLUMN_taskID = 8, 
            COLUMN_taskName = 30,
            COLUMN_dateFrom = 10,
            COLUMN_dateTo = 10,
            COLUMN_location = 15,
            COLUMN_details = 17,
            COLUMN_status = 10;

    private static final double COLUMN_systemKW = 15,
            COLUMN_userDefinedKW = 85;   

    private static final double[] TASK_PREFERRED_COLUMN_WIDTHS = {COLUMN_taskID,
        COLUMN_taskName,
        COLUMN_dateFrom,
        COLUMN_dateTo,
        COLUMN_location, 
        COLUMN_details,
        COLUMN_status}; 
    private static final double[] SHORTCUT_PREFERRED_COLUMN_WIDTHS = {COLUMN_systemKW,
        COLUMN_userDefinedKW};

    private static final int WINDOW_WIDTH = 1200;

    private static final String HEADER_SC_1 = "System Action";
    private static final String HEADER_SC_2 = "User-Defined Keywords";

    private static final String HEADER_TASK_1 = "Task";
    private static final String HEADER_TASK_2 = "Task Title";
    private static final String HEADER_TASK_3 = "Start Date";
    private static final String HEADER_TASK_4 = "End Date";
    private static final String HEADER_TASK_5 = "Location";
    private static final String HEADER_TASK_6 = "Remarks";
    private static final String HEADER_TASK_7 = "Status";

    private final static String newline = "\n";	
    private static final String APP_NAME = "Flexi Tracker";
    private static final String MSG_WELCOME = "Welcome to Flexi Tracker!";
    private static final String MSG_HELP = "If you need any help in adding a new task, type \"help\", or consult the user manual.";
    private static final String MSG_EMPTY_TASKLIST = "There are no tasks to display.";
    private static final String EMPTY = " ";
    private static final String OVERDUE  = "Overdue";
    private static final String[] EMPTYROW = {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};
    private static final String[][] NO_TASKS = {{MSG_EMPTY_TASKLIST},
        {newline},
        {newline},
        {MSG_HELP}};

    private static final String[] SYS_KEYWORDS = {"Create a new task", "Edit an existing task", 
        "View list of tasks", "Delete a task", 
        "Clear a field", "Search","Undo", "Redo", "Update task status",
        "Add a new keyword", "View available keywords", "Delete a keyword", 
        "Reset keywords", "Create a template", "Edit existing template", 
        "View list of templates", "Use a template", "Delete a template",
        "Clear all templates", "Help","Change saved file location"};

    public static final String[][] HELP = { 
        {"GETTING STARTED:"+newline},
        {"  Start adding new tasks by using the keyword \"add [task title]\" followed by the task details."},	

        {"      eg. \"add Meeting at room 303 from 27/2/2015 2pm to 27/2/2015 4pm\"   OR"},
        {"      eg. \"add Meeting from 27th July 1400 to 27th July 1600 at room 302\""},
        {"  Both inputs are accepted and will create a task with the same details."},
        {newline},
        {"Accepted field markers are  (non-case-sensitive):"},
        {"     \"On, Before\" for deadlines,"},
        {"     \"From and To \" for durations,"},
        {"     \"At\" for locations, and"},
        {"     \"Remarks\" to add any additional remarks or details."},
        {newline},
        {"  Feel free to leave any fields you don't need blank, only the task title is needed to create a new task."},
        {"  Many variations of inputs are accepted, experiment and find the best fit for you!" + newline},
        {newline},
        {"VIEWING THE TASK LIST"},
        {"  If you enter an empty line, the default view mode  will be displayed, where all ongoing tasks will be sorted by date."},
        {"  To view a specific range of tasks, type \"view [status] by \"[sort order]\"\"."},
        {"  If the sort order does not matter, you can just type \"view [status]\", or "},
        {"  if you want to sort the list without filtering, type \"view by \"[sort order]\"\"."}, 
        {newline},
        {"UPDATING TASKS"},
        {"  New Tasks are set to the \"Normal\" status by default."},
        {"  Mark or change the status of a task by typing \"mark [task ID] as [status]\""},
        {"  Accepted statuses are:"},
        {"        Urgent , Major, Normal, Minor, Casual, Complete."},
        {"  Tasks will be automatically set to \"Overdue\" and highlighted if an incomplete task is past its due date."}, 
        {newline},
        {"  Edit other task information by using \"edit [task ID]\" followed by the information you want to change, such as:"},
        {"      eg. \"edit 12 at room 303\""},
        {"  The above example will change the location field of task 12 to \"room 303\"."},
        {newline},
        {"DELETING TASKS"},
        {"  Delete tasks by typing \"delete [task ID]\"."},
        {"  This will permanently delete the task."},
        {"  If you just want to remove it from the task list while keeping the task, use \"mark [task ID] as complete\" instead."},
        {newline},
        {"CHANGING DEFAULT KEYWORDS"},
        {"  You can change any of the default keywords such as \"add\" and \"edit\" to anything you prefer by using" + 
        " the following format:"},
        {"  addShortcut [new keyword] onto [existing keyword]"},
        {"      eg. \"addKeyword create onto add\""},
        {newline},
        {"ADDING TASK TEMPLATES"},
        {"  You can save existing tasks as templates by typing \"addTemplate [task ID] as [template name]\"."},
        {"      eg. \"addTemplate 12 as meeting with boss\"."},
        {"  This will save the task with all the details intact."},
        {"  When you use a template to create a new task, all you need to do is input " + 
        "\"useTemplate [template name] [date]\" to duplicate the task."},
        {newline},
        {"For the full list of available keywords and functions, type \"viewKeyword\"."},
        { "Please consult the User Guide for additional help and functionality."}
    };

    private static final Color TASK_FONT_COLOR= new Color(46,67,113); 
    private static final Color TASK_BG= new Color(160, 212, 237); 
    private static final Color TASK_BG2 = new Color(255,255,255); 
    private static final Color TASK_GRID= new Color(73, 159, 201);
    private static final Color TASK_OVERDUE= new Color(255, 180, 94);
    private static final Color TASK_TEMPLATE= new Color(217, 221, 251);

    private static final Color SHORTCUT_FONT_COLOR = new  Color(46,67,113); 
    private static final Color SHORTCUT_BG = new Color(160, 212, 237); 
    private static final Color SHORTCUT_BG2 = new Color(255,255,255);
    private static final Color SHORTCUT_GRID = new Color(115, 184, 218);

    private static final Color TABLE_HEADER_BG = new Color(255, 208, 85);
    private static final Color TABLE_HEADER_FONT = new Color(44,72,111); 

    private static final Color FEEDBACK_FONT_COLOR = new Color(44,72,111); 
    private static final Color FEEDBACK = new Color(255, 208, 85);

    private static final Color INPUT_FONT_COLOR = new Color(44,72,111); 
    private static final Color INPUT = new Color(255, 255, 255);

    private static final Color OUTPUT_FONT_COLOR = new Color(46,67,113);  
    private static final Color OUTPUT_BG = new Color(255,255,255); 

    private static final Font TABLE_FONT = new Font("Arial", Font.BOLD, 15);
    private static final Font TEXTBOX_FONT =  new Font("Verdana", Font.PLAIN, 18);
    private static final Font FEEDBACK_FONT = new Font("Candara", Font.BOLD, 20);

    private static final String FN_DELETE = "delete %1$s";
    private static final String FN_MARK = "mark %1$s as complete";
    private static final String FN_EDIT = "edit %1$s ";
    private static final String FN_UNDO = "undo";
    private static final String FN_REDO = "redo";
    private static final String FN_VIEW = "viewTask";



    /**
     * Create the application.
     */
    public UserInterface() {
        initialize();
    }

    /**
     * Gets user input as a string.
     * 
     * @return
     */
    public String getRawUserInput() {
        return prevInput_;
    }

    /**
     * Converts Tasks to String[] and moves the affected tasks to the top of the ArrayList to be displayed.
     *Passes formatted ArrayList to TaskTableModel to be displayed.
     *
     * @param affectedTask.
     * @param fullListTask.
     * @param status.
     */
    public void  displayTaskTable(ArrayList<Task> affectedTask, ArrayList<Task> fullListTask, int status) {
        viewTaskTable();
        ArrayList<String[]> outputDataString = new ArrayList<String[]>();

        if (fullListTask.size() == 0) {
            //if there are no tasks to be displayed 
            displayText(NO_TASKS, true);
        } else if (affectedTask == null) {
            //displays task list
            convertTaskToString(fullListTask, outputDataString);
        } else {
            //if there are edited/affected tasks, display them first
            convertTaskToString(affectedTask, outputDataString);
            outputDataString.add(EMPTYROW);	
            for (int i = 0 ; i < fullListTask.size(); i++) {
                String[] strArray = fullListTask.get(i).toStringArray();
                if (!isAlreadyAdded(strArray, outputDataString)) {
                    outputDataString.add(strArray);
                }
            }
        }

        model_.refreshTable(outputDataString);
    }

    /**
     * Converts ArrayList<Task> to ArrayList<String>.
     * 
     */
    private void convertTaskToString(ArrayList<Task> fullListTask,
            ArrayList<String[]> outputDataString) {
        for (int i = 0 ; i < fullListTask.size(); i++) {
            outputDataString.add(fullListTask.get(i).toStringArray());
        }
    }

    /**
     * Checks if array already contains the task.
     * 
     * @param task.
     * @param taskOutput.
     * @return True if tasks is present.
     */
    private boolean isAlreadyAdded(String[] task, ArrayList<String[]> taskOutput) {
        for (String[] t: taskOutput) {
            if (Arrays.equals(task, t)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Displays a table of shortcuts with 2 columns.
     * 
     * @param outputData.
     * @param success.
     */
    public void displayKeywords(String[][] outputData, boolean success) {
        viewShortcutTable();
        String[] keywords;
        ArrayList<String[]> outputDataString = new ArrayList<String[]>();

        for (int i = 0; i < outputData.length; i++) {
            keywords= new String[2];

            String[] strArray = outputData[i];
            keywords[0] = SYS_KEYWORDS[Integer.parseInt(strArray[0])];
            String nextLine = "";

            for (int j = 1; j < strArray.length; j++) {
                nextLine += "'" + strArray[j] + "'" + " || " ;
            }

            keywords[1] = nextLine.substring(0, nextLine.length() - 3);
            outputDataString.add(keywords);
        }
        model_.refreshTable(outputDataString);				
    }

    /**
     * Displays a paragraph of text in the Text Pane.
     * 
     * @param outputData.
     * @param success.
     */
    public void displayText(String[][] outputData, boolean success) {
        clearTextPane();
        viewTextPane();
        DefaultCaret caret = (DefaultCaret)outputArea_.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        for (int i = 0; i < outputData.length; i++ ) {
            String[] strArray = outputData[i];
            String nextLine = "";
            for (int j = 0; j < strArray.length; j++ ) {
                nextLine += " " + strArray[j];
            }
            outputArea_.append(nextLine + newline);
        }
    }

    /**
     * Displays multiple lines of feedback in feedback pane.
     * 
     * @param outputData.
     * @param success.
     */
    public void displayMsg(String[] outputData, int success) {
        clearFeedbackArea();     
        String display = "";

        for (int i = 0; i < outputData.length; i++ ) {
            display += outputData[i]+"\n";
        }
        sysFeedbackArea_.setText(display);
    }

    /**
     * Displays a single line of feedback in the feedback pane.
     * 
     * @param outputData.
     * @param success.
     */
    public void displayMsg(String outputData, int success) {
        clearFeedbackArea();     
        sysFeedbackArea_.setText(outputData);
    }

    /**
     * Replaces Task ID with template names and displays it in the table model.
     * 
     * @param outputData.
     * @param templateName.
     * @param success.
     */
    public void displayTemplate(ArrayList<Task> outputData, ArrayList<String> templateName, boolean success) {
        viewTaskTable();
        ArrayList<String[]> outputDataString = new ArrayList<String[]>();

        for (int i = 0 ; i < outputData.size(); i++) {
            //converts outputData to String and replaces TaskID with templateName
            outputDataString.add(outputData.get(i).toStringArray());
            outputDataString.get(i)[0] = templateName.get(i);
        }
        model_.refreshTable(outputDataString);
    }

    /**
     * Launch the application.
     * *Only used when testing UI*
     */
    /*
     * //@author A0101735R-unused
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
     */

    /**
     * Calls the functions to initialize the contents of the frame,
     * initialize variables and creates tables.
     */
    private void initialize() {
        outputArrayString_ = new ArrayList<String[]>();
        shortcutArrayString_ = new ArrayList<String[]>();
        mainHandler = SystemHandler.getSystemHandler();

        createShortcutTable(shortcutArrayString_);
        createTaskTable(outputArrayString_);
        initFrame();
        initDisplay();
        initTextAreaListeners();

        displayText(HELP,true);
        textField_.requestFocus();
    }

    /**
     * Initialize the frame
     */
    private void initFrame() {
        frame_ = new JFrame(APP_NAME);
        frame_.getContentPane().setBackground(new Color(25, 25, 112));
        frame_.setBounds(100, 100, 1200, 400);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {1200};
        gridBagLayout.rowHeights = new int[]{300, 40, 40, 0};
        gridBagLayout.columnWeights = new double[]{1.0};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
        frame_.getContentPane().setLayout(gridBagLayout);	
    }

    /**
     * Calls the functions to initialize containers and frame content
     */
    private void initDisplay() {
        initOutputArea();
        initSysFBArea();
        initInputArea();
        String[] welcome  = {MSG_WELCOME};
        displayMsg(welcome, 0);
    }

    /**
     * Initialize text input box
     */
    private void initInputArea() {
        textField_ = new JTextField();
        textField_.setBackground(INPUT);
        textField_.setForeground(INPUT_FONT_COLOR);
        textField_.setFont(TEXTBOX_FONT);
        textField_.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 200, 0),
                new Color(255, 200, 0), Color.ORANGE, Color.ORANGE));
        scrollPaneInput_ = new JScrollPane(textField_);
        scrollPaneInput_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneInput_.setViewportBorder(null);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 2;
        frame_.getContentPane().add(scrollPaneInput_, gbc_textField);

    }

    /**
     * Initialize message display text pane
     */
    private void initSysFBArea() {
        sysFeedbackArea_ = new JTextPane();
        sysFeedbackArea_.setEditable(false);	
        sysFeedbackArea_.setFont(FEEDBACK_FONT);
        sysFeedbackArea_.setForeground(FEEDBACK_FONT_COLOR);
        sysFeedbackArea_.setBackground(FEEDBACK);
        sysFeedbackArea_.setBorder(null);

        GridBagConstraints gbc_sysFeedbackArea = new GridBagConstraints();
        gbc_sysFeedbackArea.fill = GridBagConstraints.BOTH;
        gbc_sysFeedbackArea.gridx = 0;
        gbc_sysFeedbackArea.gridy = 1;
        frame_.getContentPane().add(sysFeedbackArea_, gbc_sysFeedbackArea);
    }

    /**
     * Initialize text area for displaying large paragraphs of text, eg. help page.
     */
    private void initOutputArea() {
        outputArea_ = new JTextArea();	
        outputArea_.setBackground(Color.WHITE);
        outputArea_.setEditable(false);
        outputArea_.setFont(TEXTBOX_FONT);
        outputArea_.setColumns(30);
        outputArea_.setTabSize(10);
        outputArea_.setRows(10);
        outputArea_.setLineWrap(true);
        outputArea_.setBackground(OUTPUT_BG);
        outputArea_.setForeground(OUTPUT_FONT_COLOR);
        outputArea_.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 200, 0), Color.ORANGE));
        DefaultCaret caret = (DefaultCaret)outputArea_.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        scrollPaneMain_ = new JScrollPane(); 
        scrollPaneMain_.setBackground(Color.WHITE);
        scrollPaneMain_.setViewportBorder(null);
        scrollPaneMain_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneMain_.setViewportView(outputArea_);

        GridBagConstraints gbc_outputArea = new GridBagConstraints();
        gbc_outputArea.fill = GridBagConstraints.BOTH;
        gbc_outputArea.gridx = 0;
        gbc_outputArea.gridy = 0;
        frame_.getContentPane().add(scrollPaneMain_, gbc_outputArea);
    }


    /**
     * Initialize action listeners for input area
     */
    private void initTextAreaListeners() {
        inputListener listener = new inputListener();
        textField_.addActionListener(listener);

        //pressing up restores previous input in textField
        Action lastInput = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textField_.setText(prevInput_);			
            }		
        };

        textField_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("UP"), "lastInput");
        textField_.getActionMap().put("lastInput", lastInput );

        Action undo = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainHandler.rawUserInput(FN_UNDO);
            }

        };

        textField_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ctrl Z"), "undoTask");
        textField_.getActionMap().put("undoTask", undo );

        Action redo = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainHandler.rawUserInput(FN_REDO);
            }

        };

        textField_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ctrl Y"), "redoTask");
        textField_.getActionMap().put("redoTask", redo );

        Action view = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainHandler.rawUserInput(FN_VIEW);
            }

        };

        textField_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("alt D"), "viewTask");
        textField_.getActionMap().put("viewTask", view );
    }

    /**
     * Gets input from text input box and passes it to mainHandler to be processed
     * Sets default command to display ongoing tasks if no input is detected
     *
     */
    private class inputListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            clearFeedbackArea();
            String input = textField_.getText().trim();
            prevInput_ = input;
            System.out.println("input = " + input);
            clearInput();	

            if (input.length() == 0) {
                mainHandler.rawUserInput("viewTask");
            } else {
                mainHandler.rawUserInput(input);
            }

            //@author A0101735R-unused
            //Tests code for making sure UI displays correctly
            /*		
			String[] dummyMsg = {"dummymsg",
			 					"test line one test line one test line one test line one test line one test line one ",
			 					"testlinetwotestlinetwotestlinetwotestlinetwotestlinetwotestlinetwotestlinetwotestlinetwo"};
				 	if ( input.equals("1")) {
						displayMsg(dummyMsg,1);
						addDummyShortcut();

					}else{
						addDummyTask();
						addDummy2();
						displayTaskTable( dummy2, outputArray, 1);
						displayMsg("adding dummies", 1);
					}
             */	
        }
    }

    /**
     * Creates a table that displays an ArrayList of String[].
     * Renders rows in alternating colors.
     * 
     * @param outputArrayString
     * @return
     */
    private JScrollPane createTaskTable(ArrayList<String[]> outputArrayString) {
        ArrayList<String> columnNamesTaskTable = new ArrayList<String>();

        columnNamesTaskTable.add(HEADER_TASK_1);
        columnNamesTaskTable.add(HEADER_TASK_2);
        columnNamesTaskTable.add(HEADER_TASK_3);
        columnNamesTaskTable.add(HEADER_TASK_4);
        columnNamesTaskTable.add(HEADER_TASK_5);
        columnNamesTaskTable.add(HEADER_TASK_6);
        columnNamesTaskTable.add(HEADER_TASK_7);

        model_ = new TaskTableModel(outputArrayString, columnNamesTaskTable, String[].class );
        taskTable_ = new JTable (model_) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isNumeric((String) model_.getValueAt(row, 0))) {
                    c.setBackground(row % 2 == 0 ? getBackground() : TASK_TEMPLATE);
                } else if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? getBackground() : TASK_BG);
                }

                if (model_.getRowCount() > 0 && (model_.getValueAt(row,getColumnCount()-1)).equals(OVERDUE)
                        && !isRowSelected(row)) {
                    c.setBackground(TASK_OVERDUE);
                }

                if ((model_.getValueAt(row, 0).equals(EMPTY))) {				
                    c.setBackground(TASK_FONT_COLOR);
                }	
                if (isRowSelected(row)) {

                    String remarks = (String) model_.getValueAt(row,5);
                    if (remarks == null)
                        remarks  = "";
                    displayMsg(remarks, 0);			
                }	
                return c;
            }		
        };

        setJTableColumnsWidth(taskTable_, WINDOW_WIDTH, TASK_PREFERRED_COLUMN_WIDTHS );
        taskTable_.setRowHeight(25);
        taskTable_.setFont(TABLE_FONT);
        taskTable_.setGridColor(TASK_GRID);
        taskTable_.setForeground(TASK_FONT_COLOR);
        taskTable_.setBackground(TASK_BG2);
        taskTable_.setBorder(null);
        taskTable_.setRowSelectionAllowed(true);
        taskTable_.setColumnSelectionAllowed(false);

        taskHeader_ = taskTable_.getTableHeader();
        taskHeader_.setBackground(TABLE_HEADER_BG);
        taskHeader_.setForeground(TABLE_HEADER_FONT);
        taskHeader_.setFont(TABLE_FONT);

        for (int i = 0; i < taskTable_.getColumnCount(); i++) {
            // remove cell editor
            Class<?> col_class = taskTable_.getColumnClass(i);
            taskTable_.setDefaultEditor(col_class, null);        
        }

        //binds ctrl D to delete action
        Action delete = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int row  = taskTable_.getSelectedRow();
                String input  = String.format(FN_DELETE, model_.getValueAt(row, 0));
                mainHandler.rawUserInput(input);
            }
        };

        taskTable_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ctrl D"), "delete");
        taskTable_.getActionMap().put("delete", delete );

        //binds D to mark as complete
        Action done = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int row  = taskTable_.getSelectedRow();
                String input  =String.format(FN_MARK, model_.getValueAt(row, 0));
                mainHandler.rawUserInput(input);
            }
        };

        taskTable_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(" D"), "done");
        taskTable_.getActionMap().put("done", done );

        //binds ctrl E to edit action 	
        Action edit = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int row  = taskTable_.getSelectedRow();
                String input  = String.format(FN_EDIT, model_.getValueAt(row, 0));
                textField_.setText(input);
                textField_.setCaretPosition(input.length());
                textField_.requestFocus();
            }
        };

        taskTable_.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ctrl E"), "edit");
        taskTable_.getActionMap().put("edit", edit );

        return scrollPaneMain_;
    }

    /**
     * Creates a table to display user defined keywords.
     * 
     * @param shortcutArrayString
     * @return
     */
    private JScrollPane createShortcutTable(ArrayList<String[]> shortcutArrayString) {
        ArrayList<String>columnNamesST = new ArrayList<String>();
        columnNamesST.add(HEADER_SC_1);
        columnNamesST.add(HEADER_SC_2);

        model_ = new TaskTableModel(shortcutArrayString, columnNamesST, String[].class );
        shortcutTable_ = new JTable (model_) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? getBackground() : SHORTCUT_BG2);
                }
                return c;
            }
        };

        setJTableColumnsWidth(shortcutTable_, WINDOW_WIDTH, SHORTCUT_PREFERRED_COLUMN_WIDTHS);
        shortcutTable_.setRowHeight(25);
        shortcutTable_.setFont(TABLE_FONT);
        shortcutTable_.setGridColor(SHORTCUT_GRID);
        shortcutTable_.setForeground(SHORTCUT_FONT_COLOR);
        shortcutTable_.setBackground(SHORTCUT_BG);
        shortcutHeader_ = shortcutTable_.getTableHeader();
        shortcutHeader_.setBackground(TABLE_HEADER_BG);
        shortcutHeader_.setForeground(TABLE_HEADER_FONT);
        shortcutHeader_.setFont(TABLE_FONT);

        for (int i = 0; i < shortcutTable_.getColumnCount(); i++) {	
            // remove cell editor
            Class<?> col_class = shortcutTable_.getColumnClass(i);
            shortcutTable_.setDefaultEditor(col_class, null);        
        }
        return scrollPaneMain_;
    }

    private void viewTextPane() {
        scrollPaneMain_.setViewportView(outputArea_);
    }

    private void viewTaskTable() {
        scrollPaneMain_.setViewportView(taskTable_);
    }

    private void viewShortcutTable() {
        scrollPaneMain_.setViewportView(shortcutTable_);
    }

    private void clearInput() {
        textField_.selectAll();
        textField_.setText("");
    }

    private void clearTextPane() {
        outputArea_.setText("");	
    }

    private void clearFeedbackArea() {
        sysFeedbackArea_.setText("");
    }

    private static boolean isNumeric(String s) {
        try{ 
            Integer.parseInt(s);
            return true; 
        } catch (NumberFormatException er) { 
            return false; 
        }
    }

    //@author A0101735R - reused
    /**
     * Sets table column widths according to percentage.
     */
    private static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
            double[] percentages) {
        double total = 0;

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)
                    (tablePreferredWidth * (percentages[i] / total)));
        }
    }


    /**
     * *For testing purposes*
     * 
     * Creates dummy tasks and shortcuts to test display.
     */
    private ArrayList<Task> dummyArray;
    private ArrayList<Task> dummy2;

    //@author A0101735R-unused
    private void addDummyTask() {
        dummyArray  =  new ArrayList<Task>();

        for (int tid = 1000; tid<1020;  tid++) {
            Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
                    new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME",null, 6);
            dummyArray.add(testTask);
        }
    }

    //@author A0101735R-unused
    private void addDummy2() {
        dummy2 = new ArrayList<Task>();

        for (int tid = 1005; tid<1010;  tid++) {
            Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
                    new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);
            dummy2.add(testTask);
        }
    }

    //@author A0101735R-unused
    private void addDummyShortcut() {
        String[][] keywordArray = new String[SYS_KEYWORDS.length][5];

        for ( int i = 0 ; i < SYS_KEYWORDS.length ; i++) {
            for ( int j = 0; j<5 ; j ++) {
                keywordArray[i][j] = i + "shortcut" + j ;
            }
        }				
        displayKeywords(keywordArray, true);
    }
}


