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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;



@SuppressWarnings("serial")
public class UserInterface extends DefaultTableCellRenderer {

	public JFrame frame;
	public JPanel panel;
	private JTextField textField;
	private JTextArea outputArea;
	private JTextPane sysFeedbackArea;
	private JTable taskTable;
	private JTable shortcutTable;
	private TaskTableModel model;
	private final static String newline = "\n";
	private JScrollPane scrollPaneMain;
	private JScrollPane scrollPaneFB;
	private JScrollPane scrollPaneInput;
	private ArrayList<Task> outputArray;
	private ArrayList<String[]> outputArrayString;
	private ArrayList<String[]> shortcutArrayString;

	private SystemHandler mainHandler;

	public static final String APP_NAME = "Flexi Tracker";
	public static final String MSG_WELCOME = "Welcome to Flexi Tracker!";
	public static final String MSG_HELP = "Type \"help\" into the  text field if you need help.";
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	public static final String MSG_ECHO_FILENAME = "File location: %1$s";
	public static final String MSG_REMINDERS = "The following task(s) are due today: " + newline + newline;
	public static final String MSG_SEPARATOR = "=========================================================";
	public static final String MSG_EMPTY_TASKLIST = "There are no tasks to display.";
	
	//setting values for column widths in percentage
	// column widths for task table
    private static final double taskID = 5, 
					            taskName = 30,
					            dateFrom = 10,
					            dateTo = 10,
					            location = 15,
					            details = 20,
					            status = 10;
    private static final double[] PREFERRED_WIDTH_TASKTABLE = {taskID, taskName,dateFrom, dateTo, location, details, status};
    private static final double systemKW = 15, userDefinedKW = 85;       
    private static final double[] PREFERRED_WIDTH_SHORTCUTS = {systemKW, userDefinedKW};
   
    private static final int WINDOW_WIDTH = 1200;
    private static final String[] SYS_KEYWORDS = {"Create a new task", "Edit an existing task", "View list of tasks", "Delete a task", 
    											 "Clear a field", "Search","Undo", "Redo", "Update task status",
    											 "Add a new keyword", "View available keywords", "Delete a keyword", 
	   											 "Reset keywords", "Create a template", "Edit existing template", 
	   											 "View list of templates", "Use a template", "Delete a template",
	   											 "Clear all templates", "Help"};
    
    private static final String[][] HELP = { {MSG_WELCOME +newline+newline },
    										 {"Getting Started:"+newline},
    										 {"  Start adding new tasks by uisng the keyword \"add [task title]\" followed by the task details."},	
    										 {"      eg. \"add Meeting at room 303 on 27/2/2015 from 2pm to 4pm\"   OR"},
    										 {"      eg. \"add Meeting from 1400 to 1600 on 27th July at room 303\""},
    										 {"  Both inputs are accepted and will create a task with the same details." +newline},
    										 {"  Feel free to leave any fields you don't need blank, only the task title is needed to create a new task."},
    										 {"  Many variations of inputs are accepted, experiment and find the best fit for you!" + newline},
    										 {"  Edit tasks by using \"edit [taskID]\" followed by the information you want to change."},
    										 {"  Delete tasks by typing \"delete [taskID]\""},
    										 {newline},
    										 {"Changing default keywords:"},
    										 {"  You can change any of the default keywords such as \"add\" and \"edit\" to anything you prefer by following the following format:"},
    										 {"  addShortcut [new keyword] onto [existing keyword]"},
    										 {"      eg. \"addKeyword create onto add\""},
    										 {newline},
    										 {"For the full list of available keywords and functions, type \"help\" or \"viewShortcut\""}
    										};
   
//    private static final String[] SYS_KEYWORDS = {	"addTask", "editTask","viewTask","deleteTask", 
//  		"clearAttr", "searchTask", "undoTask", "redoTask", "markTask",
//  		"addShortcut", "viewShortcut", "deleteShortcut",
//  		"resetShortcut", "addTemplate", "editTemplate", 
//  		"viewTemplates", "useTemplate", "deleteTemplate", 
//  		"resetTemplates", "help"};
	
    private static final Color TASK_FONT= new Color(46,67,113); 
	private static final Color TASK_BG= new Color(160, 212, 237); 
	private static final Color TASK_BG2 = new Color(255,255,255); 
	private static final Color TASK_GRID= new Color(73, 159, 201);
	private static final Color TASK_EDITED= new Color(255, 220, 128);
	private static final Color TASK_CLASH= new Color(255, 196, 128);
	private static final Color TASK_OVERDUE= new Color(255, 180, 94);
	
	private static final Color SHORTCUT_FONT = new  Color(46,67,113); 
	private static final Color SHORTCUT_BG = new Color(160, 212, 237); 
	private static final Color SHORTCUT_BG2 = new Color(255,255,255);
	private static final Color SHORTCUT_GRID = new Color(115, 184, 218);
	
	private static final Color FEEDBACK_FONT = new Color(44,72,111); 
	private static final Color FEEDBACK =new Color(255, 208, 85);
	
	private static final Color INPUT_FONT = new Color(44,72,111); 
	private static final Color INPUT = new Color(255, 255, 255);
	
	private static final Color OUTPUT_FONT = new Color(46,67,113);  
	private static final Color OUTPUT_BG = new Color(255,255,255); 
	
	private String prevInput;
	
	
	ArrayList<Task> dummy2;
	
	public void  displayTaskTable(ArrayList<Task> affectedTask, ArrayList<Task> fullListTask, int status){
		viewTaskTable();
		ArrayList<String[]> outputDataString = new ArrayList<String[]>();
		
		if(fullListTask == null){
			displayMsg(MSG_EMPTY_TASKLIST, 1);
		}else if(affectedTask == null){
			for (int i = 0 ; i < fullListTask.size(); i++){
					outputDataString.add(fullListTask.get(i).toStringArray());
			}
		}else{
			for (int j = 0 ; j < affectedTask.size(); j++){
					outputDataString.add(affectedTask.get(j).toStringArray());
					
			}
			for (int k = 0 ; k < fullListTask.size(); k++){
				String[] strArray = fullListTask.get(k).toStringArray();
				if (!isAlreadyAdded(strArray, outputDataString)){
					outputDataString.add(strArray);
				}
			}
		}
		model.refreshTable(outputDataString);
	}

	private boolean isAlreadyAdded(String[] task, ArrayList<String[]> taskOutput) {
		for(String[] t: taskOutput){
			if (Arrays.equals(task, t))
				return true;
		}
		return false;
	}

	//displays a table of shortcuts with 2 columns
	public void displayShortcuts(String[][] outputData, boolean success) {
		viewShortcutTable();
		String[] keywords;
		
		ArrayList<String[]> outputDataString = new ArrayList<String[]>();
		for(int i = 0; i < outputData.length; i++){
			keywords= new String[2];
			
			String[] strArray = outputData[i];
			keywords[0] = SYS_KEYWORDS[Integer.parseInt(strArray[0])];
			String nextLine = "";
			for(int j = 1; j < strArray.length; j++) {
				nextLine += "'" + strArray[j] + "'" + " || " ;
			}
			
			keywords[1] = nextLine.substring(0, nextLine.length() - 3);
			outputDataString.add(keywords);
		}
		model.refreshTable(outputDataString);				
	}
	
	public void displayText(String[][] outputData, boolean success) {
		clearTextPane();
		viewTextPane();
		for(int i = 0; i < outputData.length; i++){
			String[] strArray = outputData[i];
			String nextLine = "";
			for(int j = 0; j < strArray.length; ++j) {
				nextLine += " " + strArray[j];
			}
			outputArea.append(nextLine + newline);
		}

	}
	public void displayMsg(String[] outputData, int success){
        clearFeedbackArea();     
        String display = "";
        for(int i = 0; i < outputData.length; ++i) {
        	display += outputData[i]+"\n";
        }
        sysFeedbackArea.setText(display);
	}

	public void displayMsg(String outputData, int success){
        clearFeedbackArea();     
        sysFeedbackArea.setText(outputData);
	}

	public void displayTemplate(ArrayList<Task> outputData, ArrayList<String> templateName, boolean success){
		viewTaskTable();
		ArrayList<String[]> outputDataString = new ArrayList<String[]>();
		for (int i = 0 ; i < outputData.size(); i++){
			outputDataString.add(outputData.get(i).toStringArray());
			outputDataString.get(i)[0] = templateName.get(i);
		}
		model.refreshTable(outputDataString);
	}

	public String getRawUserInput(){
		return prevInput;
	}


	/**
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
	}
	/**
	 * Launch the application.
	 */
	
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		initFrame();
		initDisplay();
		initTextArea();
		
		outputArrayString = new ArrayList<String[]>();
		outputArray  =  new ArrayList<Task>();
		shortcutArrayString = new ArrayList<String[]>();
		mainHandler = SystemHandler.getSystemHandler();
		
		createShortcutTable(shortcutArrayString);
		createTaskTable(outputArrayString);

	}

	private void initFrame() {
		panel = new JPanel();
		frame = new JFrame(APP_NAME);
		frame.getContentPane().setBackground(new Color(25, 25, 112));
		frame.setBounds(100, 100, 1200, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1200};
		gridBagLayout.rowHeights = new int[]{300, 40, 40, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		panel.setBorder(null);
	}

	
	private void initDisplay() {
		initOutputArea();
		initSysFBArea();
		initInputArea();

		displayText(HELP, true);

	}

	private void initInputArea() {
		textField = new JTextField();
		textField.setBackground(INPUT);
		textField.setForeground(INPUT_FONT);
		textField.setFont(new Font("Verdana", Font.PLAIN, 18));
		textField.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 200, 0), new Color(255, 200, 0), Color.ORANGE, Color.ORANGE));
		scrollPaneInput = new JScrollPane(textField);
		scrollPaneInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneInput.setViewportBorder(null);
		
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(scrollPaneInput, gbc_textField);
	}

	private void initSysFBArea() {
		sysFeedbackArea = new JTextPane();
		sysFeedbackArea.setEditable(false);
		sysFeedbackArea.setFont(new Font("Candara", Font.PLAIN, 26));
		sysFeedbackArea.setForeground(FEEDBACK_FONT);
		sysFeedbackArea.setBackground(FEEDBACK);
		sysFeedbackArea.setBorder(null);
		
	/*	StyledDocument doc = sysFeedbackArea.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	*/
		GridBagConstraints gbc_sysFeedbackArea = new GridBagConstraints();
		gbc_sysFeedbackArea.fill = GridBagConstraints.BOTH;
		gbc_sysFeedbackArea.gridx = 0;
		gbc_sysFeedbackArea.gridy = 1;
		frame.getContentPane().add(sysFeedbackArea, gbc_sysFeedbackArea);
	}

	private void initOutputArea() {
		outputArea = new JTextArea();	
		outputArea.setBackground(Color.WHITE);
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Verdana", Font.PLAIN, 18));
		outputArea.setColumns(30);
		outputArea.setTabSize(10);
		outputArea.setRows(10);
		outputArea.setLineWrap(true);
		outputArea.setBackground(OUTPUT_BG);
		outputArea.setForeground(OUTPUT_FONT);
		outputArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 200, 0), Color.ORANGE));
		DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		scrollPaneMain = new JScrollPane(); 
		scrollPaneMain.setBackground(Color.WHITE);
		scrollPaneMain.setViewportBorder(null);
		scrollPaneMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneMain.setViewportView(outputArea);
		

		GridBagConstraints gbc_outputArea = new GridBagConstraints();
		gbc_outputArea.fill = GridBagConstraints.BOTH;
		gbc_outputArea.gridx = 0;
		gbc_outputArea.gridy = 0;
		frame.getContentPane().add(scrollPaneMain, gbc_outputArea);
	}
	
	private void initTextArea() {
		inputListener listener = new inputListener();
		textField.addActionListener(listener);

		//	kbShortcuts();

		//keyboard shortcuts needs to be refactored out from here

		//pressing up restores previous input in textField
		Action lastInput = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				textField.setText(prevInput);			
			}		
		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("UP"), "lastInput");
		textField.getActionMap().put("lastInput", lastInput );

		Action undo = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "undoTask";
				mainHandler.rawUserInput(input);

			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Z"), "undoTask");
		textField.getActionMap().put("undoTask", undo );

		Action redo = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "redoTask";
				mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Y"), "redoTask");
		textField.getActionMap().put("redoTask", redo );

		Action view = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "viewTask";
				mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("alt D"), "viewTask");
		textField.getActionMap().put("viewTask", view );
		
		//until here this needs to refactored out
	}

	

	private JScrollPane createTaskTable(ArrayList<String[]> outputArrayString) {
		ArrayList<String> columnNamesTaskTable = new ArrayList<String>();
		columnNamesTaskTable.add("ID");
		columnNamesTaskTable.add("Task Name");
		columnNamesTaskTable.add("Date From");
		columnNamesTaskTable.add("Date To");
		columnNamesTaskTable.add("Location");
		columnNamesTaskTable.add("Remarks");
		columnNamesTaskTable.add("Status");
		
		model = new TaskTableModel(outputArrayString, columnNamesTaskTable, String[].class );
		taskTable = new JTable (model)
		{
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)){
					c.setBackground(row % 2 == 0 ? getBackground() : TASK_BG);
				}
				
				if ((model.getValueAt(row,getColumnCount()-1)).equals("Overdue")){
					c.setBackground(TASK_OVERDUE);
				}
				return c;
			}
			



			public boolean isAffected(int row, int affected) {
				if (row < affected)
					return true;
				return false;
			}
		};
		
		//taskTable.getModel().addTableModelListener((TableModelListener) this);
		
        setJTableColumnsWidth(taskTable, WINDOW_WIDTH, PREFERRED_WIDTH_TASKTABLE ) ;
    	taskTable.setRowHeight(25);
    	taskTable.setFont(new Font("Arial", Font.BOLD, 15));
    	taskTable.setGridColor(TASK_GRID);
    	taskTable.setForeground(TASK_FONT);
    	taskTable.setBackground(TASK_BG2);
    	taskTable.setBorder(null);
    	taskTable.setRowSelectionAllowed(true);
    	taskTable.setColumnSelectionAllowed(false);
        for (int c = 0; c < taskTable.getColumnCount(); c++)
		{
		    Class<?> col_class = taskTable.getColumnClass(c);
		    taskTable.setDefaultEditor(col_class, null);        // remove editor
		}
		
		return scrollPaneMain;
	}
	
	private JScrollPane createShortcutTable(ArrayList<String[]> shortcutArrayString){
		ArrayList<String>columnNamesST = new ArrayList<String>();
		columnNamesST.add("System Default Keyword");
		columnNamesST.add("User-Defined Keywords");
		
		model = new TaskTableModel(shortcutArrayString, columnNamesST, String[].class );
		shortcutTable = new JTable (model){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)){
					c.setBackground(row % 2 == 0 ? getBackground() : SHORTCUT_BG2);
				}
				return c;
			}
		};
        setJTableColumnsWidth(shortcutTable, WINDOW_WIDTH, PREFERRED_WIDTH_SHORTCUTS ) ;
    	shortcutTable.setRowHeight(25);
    	shortcutTable.setFont(new Font("Arial", Font.BOLD, 15));
    	shortcutTable.setGridColor(SHORTCUT_GRID);
    	shortcutTable.setForeground(SHORTCUT_FONT);
    	shortcutTable.setBackground(SHORTCUT_BG);
        for (int c = 0; c < shortcutTable.getColumnCount(); c++)
		{
		    Class<?> col_class = shortcutTable.getColumnClass(c);
		    shortcutTable.setDefaultEditor(col_class, null);        // remove editor
		}
		
		return scrollPaneMain;
	}

	private void viewTextPane(){
		scrollPaneMain.setViewportView(outputArea);
	}

	private void viewTaskTable(){
		scrollPaneMain.setViewportView(taskTable);
	}
	
	private void viewShortcutTable(){
		scrollPaneMain.setViewportView(shortcutTable);
	}

	private class inputListener implements ActionListener {

		public void actionPerformed(ActionEvent e){
			
			clearFeedbackArea();
			String input = textField.getText().trim();
			prevInput = input;
			System.out.println("input = " + input);
			clearInput();	
		
			if (input.length() == 0){
						mainHandler.rawUserInput("viewTask");
					}else{
						mainHandler.rawUserInput(input);
					}
				
		/*		
			
			String[] dummyMsg = {"dummymsg", "asdkhaskjdhaksdakhdkajhdkjashdkjashdiheudhaksdi2345678945679345678i123oihdi23y", "adjq981uodu09237e3asdadfadsfr0239idq2dqj"};
				 	if( input.equals("1")){
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


	private void clearInput() {
		textField.selectAll();
		textField.setText("");
	}

	private void clearTextPane() {
		outputArea.setText("");	
	}
	
    private void clearFeedbackArea(){
        sysFeedbackArea.setText("");
    }
    
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
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
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		 Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
	       return c;

	}
	
	 public void tableChanged(TableModelEvent e) {
	        int row = e.getFirstRow();
	        int column = e.getColumn();
	        TaskTableModel model = (TaskTableModel)e.getSource();
	        String columnName = model.getColumnName(column);
	        Object data = model.getValueAt(row, column);
	        
	        System.out.println("table change= " + data);
	        // Do something with the data...
	    }

    

	
private void addDummyTask() {
		
		for (int tid = 1000; tid<1020;  tid++){
			Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
					new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME",null, 6);
			
			outputArray.add(testTask);
			
		}

	}
	
	
	private void addDummy2(){
		dummy2 = new ArrayList<Task>();
		for (int tid = 1005; tid<1010;  tid++){
			Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
					new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);
			
			dummy2.add(testTask);
		}
	}
	
	private void addDummyShortcut() {
		
		
			String[][] keywordArray = new String[SYS_KEYWORDS.length][5];
			
			for ( int i = 0 ; i < SYS_KEYWORDS.length ; i++){
				for ( int j = 0; j<5 ; j ++){
					keywordArray[i][j] = i + "shortcut" + j ;
				}
			}				
			displayShortcuts(keywordArray, true);
	}


}


