import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JTextArea;

import java.awt.Insets;

public class UserInterface {

	private JFrame frame;
	private JTextField textField;
	private JTextArea outputArea;
    private final static String newline = "\n";
    private JScrollPane scrollPane;
    private ArrayList<Task> outputArray = new arrayList<Task>;
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
				outputArray = rawUserInput(input);
				printOutput(outputArray);
				textField.selectAll();
				//outputArea.setCaretPosition(outputArea.getDocument().getLength());		
			
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
	}
	
	private void printOutput(ArrayList<Task> returnedOutput){
		
		for(String nextLine: returnedOutput){
			
			outputArea.append(nextLine + newline);
			
			}
		
	}
	
}

