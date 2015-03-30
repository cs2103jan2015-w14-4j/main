
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;


public class UserInterface extends DefaultTableCellRenderer {

	public JFrame frame;
	public JPanel panel;
	private JTextField textField;
	private JTextArea outputArea;
	private JTextArea sysFeedbackArea;
	private JTable outputTable;
	private TaskTableModel model;
	private final static String newline = "\n";
	private JScrollPane scrollPaneMain;
	private ArrayList<Task> outputArray;

	private SystemHandler mainHandler;

	public static final String APP_NAME = "Flexi Tracker";
	public static final String MSG_WELCOME = "Welcome to Flexi Tracker!";
	public static final String MSG_HELP = "Type \"help\" into the  text field if you need help.";
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	public static final String MSG_ECHO_FILENAME = "File location: %1$s";
	public static final String MSG_REMINDERS = "The following task(s) are due today: " + newline + newline;
	public static final String MSG_SEPARATOR = "=========================================================";
	
    private static final double taskIndex = 5,
            taskName = 20,
            dateFrom = 10,
            dateTo = 10,
            deadline = 10,
            location = 10,
            details = 20,
            priority = 5,
            reminder = 10;

	private boolean hasFilename;
	private String prevInput;

	public void  displayTaskTable(ArrayList<Task> outputData, boolean success){
		viewTaskPane();
		model.refreshTable(outputData);

	}

	public void displayShortcuts(String[][] outputData, boolean success) {
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


	public void displayMsg(String outputData, int success){
        clearFeedbackArea();     
        sysFeedbackArea.append(outputData);
	}

	public void displayTemplate(ArrayList<Task> outputData, boolean success){

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

		panel = new JPanel();
		frame = new JFrame(APP_NAME);
		frame.setBounds(100, 100, 800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{536, 0};
		gridBagLayout.rowHeights = new int[]{278, 0, 0, 40, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		textField = new JTextField();
		textField.setBackground(new Color(240, 248, 255));
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
		.put(KeyStroke.getKeyStroke("ctrl D"), "viewTask");
		textField.getActionMap().put("viewTask", view );

		//until here this needs to refactored out


		outputArray = new ArrayList<Task>();

		initDisplay();

	}

	public void initDisplay() {
		outputArea = new JTextArea();	
		outputArea.setBackground(new Color(255, 250, 250));
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Arial", Font.PLAIN, 18));
		outputArea.setColumns(30);
		outputArea.setTabSize(10);
		outputArea.setRows(10);
		outputArea.setLineWrap(true);
		scrollPaneMain = new JScrollPane(); 
		scrollPaneMain.setViewportView(outputArea);

		GridBagConstraints gbc_outputArea = new GridBagConstraints();
		gbc_outputArea.insets = new Insets(0, 0, 5, 0);
		gbc_outputArea.fill = GridBagConstraints.BOTH;
		gbc_outputArea.gridx = 0;
		gbc_outputArea.gridy = 0;
		frame.getContentPane().add(scrollPaneMain, gbc_outputArea);

		sysFeedbackArea = new JTextArea();
		sysFeedbackArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		sysFeedbackArea.setEditable(false);
		sysFeedbackArea.setBackground(new Color(245, 255, 250));
		GridBagConstraints gbc_sysFeedbackArea = new GridBagConstraints();
		gbc_sysFeedbackArea.gridheight = 2;
		gbc_sysFeedbackArea.insets = new Insets(0, 0, 5, 0);
		gbc_sysFeedbackArea.fill = GridBagConstraints.BOTH;
		gbc_sysFeedbackArea.gridx = 0;
		gbc_sysFeedbackArea.gridy = 1;
		frame.getContentPane().add(sysFeedbackArea, gbc_sysFeedbackArea);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 3;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);

		outputArea.append(MSG_WELCOME + newline + newline + MSG_HELP +newline + newline + MSG_REMINDERS);

	}

	public JScrollPane createTaskTable(ArrayList<Task> outputArray) {
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("Index");
		columnNames.add("Task Name");
		columnNames.add("Date From");
		columnNames.add("Date To");
		columnNames.add("Deadline");
		columnNames.add("Location");
		columnNames.add("Details");
		columnNames.add("Priority");
		columnNames.add("Reminder");

		double[] preferredWidth = {taskIndex, taskName,dateFrom, dateTo, deadline , location, details, priority, reminder};
		
		model = new TaskTableModel(outputArray, columnNames, Task.class );
		outputTable = new JTable (model);
        setJTableColumnsWidth(outputTable, 800, preferredWidth ) ;
		scrollPaneMain.setViewportView(outputTable);
		System.out.println(model+ "in createTaskTable");
		return scrollPaneMain;
	}

	private void viewTextPane(){
		scrollPaneMain.setViewportView(outputArea);

	}

	private void viewTaskPane(){
		scrollPaneMain.setViewportView(outputTable);
	}

	private void kbShortcuts() {
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
				String input  = "undo";
				mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
		textField.getActionMap().put("undo", undo );

		Action redo = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "redo";
				 mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
		textField.getActionMap().put("redo", redo );

		Action view = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "view";
				mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl D"), "view");
		textField.getActionMap().put("view", view );

		//until here this needs to refactored out
	}

	private void addDummy() {
		
		for (int tid = 1000; tid<1020;  tid++){
			Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
					new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);
			
			outputArray.add(testTask);
		
		//	model.addRow(testTask);
			
		}

	}



	private class inputListener implements ActionListener {

		public void actionPerformed(ActionEvent e){
			String input = textField.getText().trim();
			prevInput = input;
			if (input.length() != 0){

				if (!hasFilename){		
					clearInput();
					//Init system handler with filename

					hasFilename = true;
					mainHandler = SystemHandler.getSystemHandler();
					createTaskTable(outputArray);
			
					mainHandler.rawUserInput(input);
					//addDummy();
					//displayTaskTable(outputArray, true);

				}else{
					clearInput();
					//displayTaskTable(outputArray, true);

					mainHandler.rawUserInput(input);
		
				//		addDummy();
					 
				}
			}
		}

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




	/**

	private void printOutput(String commandType, ArrayList<Task> returnedOutput){
		if(returnedOutput == null) return;
		for(int i = 0; i < returnedOutput.size(); i++){
			Task nextTask = returnedOutput.get(i);
			outputArea.append(commandType);
			outputArea.append(" the following:"+newline);
			outputArea.append("ID    : "+ nextTask.getTID() + newline);
			outputArea.append("Name  : "+ nextTask.getTaskName() + newline);
			if(nextTask.getLocation() != null) {
				outputArea.append("Location  : "+nextTask.getLocation() + newline);
			}
			if(nextTask.getDateFrom() != null) {
				outputArea.append("Date From  : "+nextTask.getDateFrom().toLocaleString() + newline);
			}
			if(nextTask.getDateTo() != null) {
				outputArea.append("Date To  : "+nextTask.getDateTo().toLocaleString() + newline);
			}

			if(nextTask.getDeadline() != null) {
				outputArea.append("Deadline  : "+nextTask.getDeadline() + newline);
			}
			if(nextTask.getDetails() != null) {
				outputArea.append("Detail  : "+nextTask.getDetails() + newline);
			}
			outputArea.append(newline);
		}

	}

	 */


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
    


}


