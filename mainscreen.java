import java.awt.EventQueue;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;

import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JComboBox;

import org.omg.CORBA.FREE_MEM;


public class mainscreen {
	
	private static final int RUNTIME_ERROR = 10;	
	private static final int COMPILE_ERROR = 20;
	private static final int SUCCESS_RETURN = 0;

	private static final int CORRECT_ANSWER = 30;
	private static final int WRONG_ANSWER = 40;

	private static final int CONNECTION_ESTABILISHED = 50;
	
	int currentProblemID = 0;
	
	SocketClient client;
	private JFrame frame;
	public String selectedFilePath = "", selectedFileContents = "";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainscreen window = new mainscreen();
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
	public mainscreen() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.WHITE);
		frame.getContentPane().setBackground(UIManager.getColor("Button.background"));
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{21, 136, 129, 84, 194, 24, 0};
		gridBagLayout.rowHeights = new int[]{30, 242, 0, 0, 25, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblProblemStatement = new JLabel("Problem Statement");
		GridBagConstraints gbc_lblProblemStatement = new GridBagConstraints();
		gbc_lblProblemStatement.anchor = GridBagConstraints.WEST;
		gbc_lblProblemStatement.fill = GridBagConstraints.VERTICAL;
		gbc_lblProblemStatement.insets = new Insets(0, 0, 5, 5);
		gbc_lblProblemStatement.gridx = 1;
		gbc_lblProblemStatement.gridy = 0;
		frame.getContentPane().add(lblProblemStatement, gbc_lblProblemStatement);
		
		JLabel lblProblem = new JLabel("Problem:");
		GridBagConstraints gbc_lblProblem = new GridBagConstraints();
		gbc_lblProblem.anchor = GridBagConstraints.EAST;
		gbc_lblProblem.insets = new Insets(0, 0, 5, 5);
		gbc_lblProblem.gridx = 3;
		gbc_lblProblem.gridy = 0;
		frame.getContentPane().add(lblProblem, gbc_lblProblem);
		
		
		String[] problemNamesList = { "choose a problem" , "Problem 1" , "Problem 2"};
		JComboBox comboBoxProblemsList = new JComboBox(problemNamesList);
		GridBagConstraints gbc_comboBoxProblemsList = new GridBagConstraints();
		gbc_comboBoxProblemsList.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProblemsList.anchor = GridBagConstraints.SOUTH;
		gbc_comboBoxProblemsList.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxProblemsList.gridx = 4;
		gbc_comboBoxProblemsList.gridy = 0;
		frame.getContentPane().add(comboBoxProblemsList, gbc_comboBoxProblemsList);
		
		JScrollPane scrollPane = new JScrollPane();
		
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("FreeMono", Font.BOLD, 16));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JLabel lblChooseYourFile = new JLabel("Choose your file:");
		GridBagConstraints gbc_lblChooseYourFile = new GridBagConstraints();
		gbc_lblChooseYourFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblChooseYourFile.gridx = 1;
		gbc_lblChooseYourFile.gridy = 3;
		frame.getContentPane().add(lblChooseYourFile, gbc_lblChooseYourFile);
		
		JButton btnBrowse = new JButton("Browse...");
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.anchor = GridBagConstraints.WEST;
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.gridx = 2;
		gbc_btnBrowse.gridy = 3;
		frame.getContentPane().add(btnBrowse, gbc_btnBrowse);
		
		JButton btnSubmit = new JButton("Submit");
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 5);
		gbc_btnSubmit.gridx = 3;
		gbc_btnSubmit.gridy = 3;
		frame.getContentPane().add(btnSubmit, gbc_btnSubmit);
		
		JButton btnQuit = new JButton("Close");
		GridBagConstraints gbc_btnQuit = new GridBagConstraints();
		gbc_btnQuit.anchor = GridBagConstraints.EAST;
		gbc_btnQuit.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuit.gridx = 4;
		gbc_btnQuit.gridy = 3;
		frame.getContentPane().add(btnQuit, gbc_btnQuit);
		
		btnQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				System.exit(0);
			}
		});
		
		//String problemStatementString = loadProblem(1);
		
		textArea.setText("Select a problem.");
		
		textArea.setCaretPosition(0);
		
		final JLabel lblChosenFile = new JLabel("");
		GridBagConstraints gbc_lblChosenFile = new GridBagConstraints();
		gbc_lblChosenFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblChosenFile.gridx = 1;
		gbc_lblChosenFile.gridy = 4;
		frame.getContentPane().add(lblChosenFile, gbc_lblChosenFile);
		
		final JFileChooser fc = new JFileChooser();
		
		
		btnBrowse.setActionCommand("browse");
		btnBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String command = e.getActionCommand();
				
				if(command.equals("browse")) {
					int returnValue = fc.showOpenDialog(null);
					if(returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fc.getSelectedFile();
						lblChosenFile.setText(selectedFile.getName());
						
						try {
							//textArea.setText(selectedFile.getCanonicalPath());
							selectedFilePath = selectedFile.getCanonicalPath();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		btnSubmit.setActionCommand("submit");
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e2) {
				// TODO Auto-generated method stub
				
				String command = e2.getActionCommand();
				
				if(command.equals("submit")) {
					
					if(selectedFilePath == "") {
						JOptionPane.showMessageDialog(frame, "Please select a file to submit first");
					} else {
					
						Charset charset = Charset.forName("UTF-8");
	//					selectedFilePath = "/home/sudipto/tester.cpp";
						Path file = Paths.get(selectedFilePath);
						
						try (BufferedReader reader = Files.newBufferedReader(file , charset)) {
						    String line = null;
						    selectedFileContents = "";
						    while ((line = reader.readLine()) != null) {
						        //System.out.println(line);
						        selectedFileContents += (line + "\n");					  
						    }
						    sendDataToServer(selectedFileContents);
						} catch (IOException x) {
						    System.err.format("IOException: %s%n", x);
						}
					}
				}
			}

		} );
		
		
		comboBoxProblemsList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) arg0.getSource();
				String problemSelected = (String) cb.getSelectedItem();
				int problemSelectedIndex = (int) cb.getSelectedIndex();
				
				if(problemSelectedIndex > 0) { 
					JOptionPane.showMessageDialog(frame, "You selected problem ID: " + problemSelectedIndex);
					
					String statement = loadProblem(problemSelectedIndex);
					textArea.setText(statement);
					textArea.setCaretPosition(0);
					currentProblemID = problemSelectedIndex;
					
					// Fetch problem from server
				}
			}
		});
		
				
	}
	
	private String loadProblem(int problemID) {
		// TODO Auto-generated method stub
		/*
		Charset charset = Charset.forName("UTF-8");
		String problemPath = "/home/sudipto/xprog/prob1_statement.txt";
		String statement = "";
		
		Path file = Paths.get(problemPath);
		try (BufferedReader reader = Files.newBufferedReader(file , charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        //System.out.println(line);
		        statement += (line + "\n");					  
		    }		    
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}*/
		
		String statement = "";
		client = new SocketClient();
		int connectionStatus = client.main(null);
		if(connectionStatus == CONNECTION_ESTABILISHED) {
			JOptionPane.showMessageDialog(frame, "Connection to server estabilished.");
		}
		statement = client.getProblemStatement(problemID);
		
		
		return statement;
	}

	private void sendDataToServer(String selectedFileContents) {
		// TODO Auto-generated method stub
		String submission = "";
		String username = "X", problem_code = "A123";
		
		/*
		submission += "<?xml?>";
		submission += "<username>" + username + "</username>\n";
		submission += "<problem>" + problem_code + "</problem>\n";
		submission += "<code>" + selectedFileContents + "</code>\n";
		*/
		
		submission += selectedFileContents;
		submission += (char)13;
		
		System.out.println(submission + "\nWill send data to server now");
		
		client = new SocketClient();
		int connectionStatus = client.main(null);
		if(connectionStatus == CONNECTION_ESTABILISHED) {
			JOptionPane.showMessageDialog(frame, "Connection to server estabilished.");
		}
		
		int verdict = client.sendSubmission(submission, currentProblemID);
		
		System.out.println("verdict at mainscreen : "+ verdict);
		if(verdict == CORRECT_ANSWER) {
			JOptionPane.showMessageDialog(frame, "Your submission was judged as : CORRECT ANSWER");
		} else if(verdict == RUNTIME_ERROR) {
			JOptionPane.showMessageDialog(frame, "Your submission was judged as : RUNTIME ERROR");
		} else if(verdict == COMPILE_ERROR) {
			JOptionPane.showMessageDialog(frame, "Your submission was judged as : COMPILE ERROR");
		} else if(verdict == WRONG_ANSWER) {
			JOptionPane.showMessageDialog(frame, "Your submission was judged as : WRONG ANSWER");
		}
	
	}
	
}
