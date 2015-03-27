
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.EventQueue;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;


public class UserInterface  {

	public JFrame frame;
	public JPanel panel;
	private JTextField textField;
	private JTextArea outputArea;
	private JTable outputTable;
	private TaskTableModel model;
	private final static String newline = "\n";
	private JScrollPane scrollPane;
	private ArrayList<Task> outputArray;

	private SystemHandler mainHandler;

	public static final String APP_NAME = "Flexi Tracker";
	public static final String MSG_WELCOME = "Welcome to Flexi Tracker!";
	public static final String MSG_HELP = "Type \"help\" as a command into the  text field for help";
	public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	public static final String MSG_ECHO_FILENAME = "File location: %1$s";
	public static final String MSG_SEPARATOR = "=========================================================";

	private boolean hasFilename;
	private String prevInput;

	/**
	public void update(Observable obs, Object obj ){

			//printOutputTask(mainHandler.getTaskResult());

	}

	public void update(Observable obserable, ArrayList<Task> outputArray ){

//		ArrayList<Task> result = new ArrayList<Task>();
//		Task testTask = new Task(1, input + " (The rest are dummies)", new Date(115,3,8,14,0) , 
//				new Date(115,3,8,17,0), null, "HOME", null, 0);
//		result.add(testTask);
	//	printOutputTask(outputArray);

		outputArea.append(MSG_SEPARATOR + newline);
		outputArea.append(MSG_ASK_INPUT + newline);

	}

	 */

	public void  displayTaskTable(ArrayList<Task> outputData, boolean success){
		viewTaskPane();
		model.refreshTable(outputData);

	}

	public void displayShortcuts(String[][] outputData, boolean success) {
		// TODO Auto-generated method stub
		viewTextPane();

	}

	public void displayMsg(ArrayList<String> outputData, boolean success){
		
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		panel = new JPanel();
		frame = new JFrame(APP_NAME);
		frame.setBounds(100, 100, 552, 357);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{536, 0};
		gridBagLayout.rowHeights = new int[]{278, 40, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		textField = new JTextField();
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
				ArrayList<Task> result = mainHandler.rawUserInput(input);

			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Z"), "undoTask");
		textField.getActionMap().put("undoTask", undo );

		Action redo = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "redoTask";
				ArrayList<Task> result = mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Y"), "redoTask");
		textField.getActionMap().put("redoTask", redo );

		Action view = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "viewTask";
				ArrayList<Task> result = mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl D"), "viewTask");
		textField.getActionMap().put("viewTask", view );

		//until here this needs to refactored out


		outputArray = new ArrayList<Task>();

		initOutputArea();





	}

	public void initOutputArea() {
		outputArea = new JTextArea();	
		scrollPane = new JScrollPane(); 
		scrollPane.setViewportView(outputArea);
		outputArea.setColumns(30);
		outputArea.setTabSize(10);
		outputArea.setRows(10);
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);

		GridBagConstraints gbc_outputArea = new GridBagConstraints();
		gbc_outputArea.insets = new Insets(0, 0, 5, 0);
		gbc_outputArea.fill = GridBagConstraints.BOTH;
		gbc_outputArea.gridx = 0;
		gbc_outputArea.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_outputArea);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		outputArea.append(MSG_WELCOME + newline + newline + MSG_HELP +newline + MSG_ASK_FILENAME);

	}

	public JScrollPane createTaskTable(ArrayList<Task> outputArray) {
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("Task ID");
		columnNames.add("Task Name");
		columnNames.add("Date From");
		columnNames.add("Date To");
		columnNames.add("Deadline");
		columnNames.add("Location");
		columnNames.add("Details");
		columnNames.add("Priority");
		model = new TaskTableModel(outputArray, columnNames, Task.class );
		outputTable = new JTable (model);
		scrollPane.setViewportView(outputTable);
		System.out.println(model+ "in createTaskTable");
		return scrollPane;
	}

	private void viewTextPane(){
		scrollPane.setViewportView(outputArea);

	}

	private void viewTaskPane(){
		scrollPane.setViewportView(outputTable);
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
				ArrayList<Task> result = mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
		textField.getActionMap().put("undo", undo );

		Action redo = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "redo";
				ArrayList<Task> result = mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
		textField.getActionMap().put("redo", redo );

		Action view = new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input  = "view";
				ArrayList<Task> result = mainHandler.rawUserInput(input);
			}

		};

		textField.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke("ctrl D"), "view");
		textField.getActionMap().put("view", view );

		//until here this needs to refactored out
	}

	private void addDummy() {
		int tid =1001;
		Task testTask = new Task( tid  , " (The rest are dummies)", new Date(115,3,8,14,0) , 
				new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);

		System.out.println(model);
		model.addRow(testTask);
	}



	private class inputListener implements ActionListener {

		public void actionPerformed(ActionEvent e){
			String input = textField.getText();
			prevInput = input;

			if (!hasFilename){		
				clearInput();
				//Init system handler with filename

				hasFilename = true;
				mainHandler = SystemHandler.getSystemHandler();
				mainHandler.initialize(input);
				createTaskTable(outputArray);


			}
			else{
				clearInput();
				
				mainHandler.rawUserInput(input);
				//dummytest
				/*
				for (int i = 0 ; i<9 ;  i++){
					Task testTask = new Task( i , " (The rest are dummies)", new Date(115,3,8,14,0) , 
							new Date(115,3,8,17,0), new Date(113,2,8,17,0), "HOME", null, 0);

					outputArray.add(testTask);
				}

*/

			}
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

}


