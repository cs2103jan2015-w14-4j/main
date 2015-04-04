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
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;



@SuppressWarnings("serial")
public class UserInterface {

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
					            details = 25,
					            status = 5;
    private static final double[] PREFERRED_WIDTH_TASKTABLE = {taskID, taskName,dateFrom, dateTo, location, details, status};
    private static final double systemKW = 15, userDefinedKW = 85;       
    private static final double[] PREFERRED_WIDTH_SHORTCUTS = {systemKW, userDefinedKW};
   
    private static final int WINDOW_WIDTH = 1200;
    private static final String[] SYS_KEYWORDS = {"Create a new task", "Edit an existing task", "View list of tasks", "Delete a task", 
    											 "undo", "redo", "Add a new keyword", "View available keywords", "Delete a keyword", 
    											 "Reset keywords", "Create a template", "Edit existing template", "View list of templates",
    											 "Delete a template", "Clear all templates", "Help"};
      
	private static final Color TASK_FONT= new Color(255,240,175); 
	private static final Color TASK_BG= new Color(18,41,77); 
	private static final Color TASK_BG2 = new Color(44,71,122); 
	private static final Color TASK_GRID= new Color(82,108,148);
	
	private static final Color SHORTCUT_FONT = new Color(187,197,255); 
	private static final Color SHORTCUT_BG = new Color( 18,41,77); 
	private static final Color SHORTCUT_BG2 = new Color(44,71,112);
	private static final Color SHORTCUT_GRID = new Color(18,41,77);
	
	private static final Color INPUT = new Color(18,42,77); 
	private static final Color INPUT_FONT = new Color(136,157,191); 
	private static final Color FEEDBACK = new Color(4,18,62); 
	private static final Color FEEDBACK_FONT = new Color(255,240,175); 
	private static final Color OUTPUT_FONT = new Color(187,197,255); 
	private static final Color OUTPUT_BG = new Color( 18,41,77); 
	
	private boolean hasFilename;
	private String prevInput;
	private TableCellRenderer renderer = new MyTableCellRenderer();
	
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
		if(affectedTask.size() != 0){
			for ( int l = 0; l< affectedTask.size(); l++){
				taskTable.prepareRenderer(renderer, l, 0);
			}
		}
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
			keywords[0] = SYS_KEYWORDS[i];
		
			String[] strArray = outputData[i];
			String nextLine = "";
			for(int j = 0; j < strArray.length; j++) {
				nextLine += "'" + strArray[j] + "'" + " || " ;
			}
			keywords[1] = nextLine;
			outputDataString.add(keywords);
		}
		model.refreshTable(outputDataString);	
				
	}
	
	public void displayTextPane(String[][] outputData, boolean success) {
		clearTextPane();
		viewTextPane();
		for(int i = 0; i < outputData.length; i++){
			String[] strArray = outputData[i];
			String nextLine = "";
			for(int j = 0; j < strArray.length; ++j) {
				nextLine += " | " + strArray[j];
			}
			outputArea.append(nextLine + newline);
		}

	}


	public void displayMsg(String outputData, int success){
		//fix the area, maybe change to string array in future
		//write messages to display
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

		outputArea.append(MSG_WELCOME + newline + newline + MSG_HELP +newline + newline + MSG_REMINDERS);

	}

	private void initInputArea() {
		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
		textField.setBackground(INPUT);
		textField.setForeground(INPUT_FONT);
		textField.setFont(new Font("Verdana", Font.PLAIN, 18));
		textField.setBorder(null);
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
		sysFeedbackArea.setBorder(new EmptyBorder(1, 0, 1, 0));
		
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
		outputArea.setBackground(new Color(255, 250, 250));
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Verdana", Font.PLAIN, 18));
		outputArea.setColumns(30);
		outputArea.setTabSize(10);
		outputArea.setRows(10);
		outputArea.setLineWrap(true);
		outputArea.setBackground(OUTPUT_BG);
		outputArea.setForeground(OUTPUT_FONT);
		outputArea.setBorder(null);
		scrollPaneMain = new JScrollPane(); 
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
		.put(KeyStroke.getKeyStroke("TAB"), "viewTask");
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
		columnNamesTaskTable.add("Details");
		columnNamesTaskTable.add("Status");
		
		model = new TaskTableModel(outputArrayString, columnNamesTaskTable, String[].class );
		taskTable = new JTable (model)
		{
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row))
					c.setBackground(row % 2 == 0 ? getBackground() : TASK_BG);

				return c;
			}
		};
        setJTableColumnsWidth(taskTable, WINDOW_WIDTH, PREFERRED_WIDTH_TASKTABLE ) ;
    	taskTable.setRowHeight(25);
    	taskTable.setFont(new Font("Arial", Font.BOLD, 15));
    	taskTable.setGridColor(TASK_GRID);
    	taskTable.setForeground(TASK_FONT);
    	taskTable.setBackground(TASK_BG2);
    	taskTable.setBorder(null);
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
				 	if( input.equals("1")){
						displayMsg("Display List of Shortcuts ",1);
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
		
private void addDummyTask() {
		
		for (int tid = 1000; tid<1020;  tid++){
			Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
					new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);
			
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
	
	   public class MyTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	      //      setBackground(TASK_BG);
	            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	      //      setText(String.valueOf(value));
	            boolean interestingRow = row % 5 == 2;
	            boolean secondColumn = column == 1;
	            if (interestingRow && secondColumn) {
	                setBackground(Color.ORANGE);
	            } else if (interestingRow) {
	                setBackground(Color.YELLOW);
	            } else if (secondColumn) {
	                setBackground(Color.RED);
	            }
	            return this;
	        }

	    }


}


