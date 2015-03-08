import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JTextArea;

import java.awt.Insets;

public class UserInterface {

	public JFrame frame;
	private JTextField textField;
	private JTextArea outputArea;
    private final static String newline = "\n";
    private JScrollPane scrollPane;
    private ArrayList<Task> outputArray;
    
    private SystemHandler mainHandler;
    
    public static final String MSG_ASK_FILENAME = "Please enter the name of your file";
	public static final String MSG_ASK_INPUT = "Please enter your command";
	public static final String MSG_ECHO_FILENAME = "File location: %1$s";
	
	
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
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		outputArray = new ArrayList<Task>();
		
		
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 552, 357);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{536, 0};
		gridBagLayout.rowHeights = new int[]{278, 40, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		textField = new JTextField();
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = textField.getText();
				textField.selectAll();
				//Actual
//				ArrayList<Task> result = mainHandler.rawUserInput(input);
//				printOutput(result);
				//outputArea.setCaretPosition(outputArea.getDocument().getLength());		

				//Dummy
				ArrayList<Task> result = new ArrayList<Task>();
				Task testTask = new Task(1, input + " (The rest are dummies)", new Date(115,3,8,14,0) , 
						new Date(115,3,8,17,0), null, "HOME", null, 0);
				result.add(testTask);
				printOutput(result);
				
				outputArea.append(MSG_ASK_INPUT + newline);
			}
		});
		
		scrollPane = new JScrollPane(); 
		outputArea = new JTextArea();
		outputArea.setColumns(30);
		outputArea.setTabSize(10);
		outputArea.setRows(10);
		outputArea.setLineWrap(true);
	//	textArea_1.setEditable(false);
		GridBagConstraints gbc_outputArea = new GridBagConstraints();
		gbc_outputArea.insets = new Insets(0, 0, 5, 0);
		gbc_outputArea.fill = GridBagConstraints.BOTH;
		gbc_outputArea.gridx = 0;
		gbc_outputArea.gridy = 0;
		frame.getContentPane().add(outputArea, gbc_outputArea);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		
		//Init system handler with filename
		outputArea.append(MSG_ASK_FILENAME + newline);
		String fileName = "default.txt";
		outputArea.append(String.format(MSG_ECHO_FILENAME, fileName) + newline);
		outputArea.append(MSG_ASK_INPUT + newline);
		
		
		mainHandler = new SystemHandler(fileName);
		
		
	}
	
	private void printOutput(ArrayList<Task> returnedOutput){
		for(int i = 0; i < returnedOutput.size(); i++){
			Task nextTask = returnedOutput.get(i);
			outputArea.append("Added(dummy) the following:"+newline);
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
				outputArea.append("ID  : "+nextTask.getDeadline() + newline);
			}
			if(nextTask.getDetails() != null) {
				outputArea.append("Detail  : "+nextTask.getDetails() + newline);
			}
			outputArea.append(newline);
		}
		
	}
	
}

