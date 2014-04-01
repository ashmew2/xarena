import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import java.awt.Color;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;


public class signupscreen {

	private JFrame frmXarena;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JButton btnRegister;
	private JButton btnCancel;
	private JLabel lblSignUp;
	

	static String username = "sudipto";
	static signupscreen window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new signupscreen();
					window.frmXarena.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public signupscreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmXarena = new JFrame();
		frmXarena.setExtendedState(frmXarena.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frmXarena.setTitle("X.ARENA");
		frmXarena.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 14));
		frmXarena.getContentPane().setBackground(Color.BLACK);
		frmXarena.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(69dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(53dlu;default):grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(17dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(28dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(25dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(22dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(21dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblSignUp = new JLabel("WELCOME TO X.ARENA!");
		lblSignUp.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSignUp.setForeground(Color.RED);
		frmXarena.getContentPane().add(lblSignUp, "4, 2");
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.GREEN);
		frmXarena.getContentPane().add(lblUsername, "2, 4, right, default");
		
		textField = new JTextField();
		frmXarena.getContentPane().add(textField, "4, 4");
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.GREEN);
		frmXarena.getContentPane().add(lblPassword, "2, 6, right, default");
		
		passwordField = new JPasswordField();
		frmXarena.getContentPane().add(passwordField, "4, 6");
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setForeground(Color.GREEN);
		frmXarena.getContentPane().add(lblConfirmPassword, "2, 8, right, default");
		
		passwordField_1 = new JPasswordField();
		frmXarena.getContentPane().add(passwordField_1, "4, 8, fill, default");
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Connection conn = null;

				Statement stmt = null;
				try {
					Class.forName("org.sqlite.JDBC");
					conn = DriverManager.getConnection("jdbc:sqlite:/home/" + username + "/xarena.db");
					conn.setAutoCommit(false);
					System.out.println("Opened database successfully");

					stmt = conn.createStatement();
					
					String user;
					char [] pass, confirmpass;
					
					user = textField.getText();
					pass = passwordField.getPassword();
					confirmpass = passwordField_1.getPassword();
					
					if(! new String(pass).equals( new String(confirmpass))) {
						JOptionPane.showMessageDialog(frmXarena, "Passwords don't match.");
						return;
					} else if(user.length() < 4 || user.length() > 10) {
						JOptionPane.showMessageDialog(frmXarena, "Please enter username of length in interval [3,10]");
						return;					
					} else if( new String(pass).length() < 5 || new String(pass).length() > 10) {
						JOptionPane.showMessageDialog(frmXarena, "Please enter password of length in interval [5,10]");
						return;
					}
					
					System.out.println("Exec queery 1");
					stmt = conn.createStatement();
					conn.setAutoCommit(false);
					ResultSet rs = stmt.executeQuery( "SELECT * FROM PARTICIPANT WHERE USERNAME = '" + user.toString() + "';" );

						System.out.println("Exec queery");
					int exists = 0;
					while ( rs.next() ) {
						String username = rs.getString("username");
						String  password = rs.getString("password");
						
						if((username.equals(user.toString())))
						{
							exists = 1;
							//Send to client "Welcome to the X Arena";
							break;
						}
					}
					

					System.out.println("Exec queery after lopp , " + exists);
					
					if(exists == 1 ) {
						JOptionPane.showMessageDialog(frmXarena, "The username already exists");
						return;
					} else {
						
						System.out.println( "String is $" + "INSERT INTO PARTICIPANT VALUES ( \"" + user.toString() + "\" , \"" + new String(pass) + "\" );" + "$");
						//rs = stmt.executeQuery( "INSERT INTO PARTICIPANT VALUES ( \"" + user.toString() + "\" , \"" + new String(pass) + "\" );");
						
						
						stmt = conn.createStatement();
						conn.setAutoCommit(false);
						String qs = "INSERT INTO PARTICIPANT VALUES ('" + user.toString() + "' , '" + new String(pass) + "');";
						stmt.executeUpdate(qs);
						
						conn.commit();
						stmt.close();
					
						
						JOptionPane.showMessageDialog(frmXarena, "Registered successfully");
						
						window.frmXarena.setVisible(false);
						
						conn.close();
						
						loginscreen ls = new loginscreen();
						
						ls.main(null);
						
					}
				} catch(Exception e) {
					try {
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		frmXarena.getContentPane().add(btnRegister, "4, 10, center, default");
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.frmXarena.setVisible(false);
				loginscreen ls = new loginscreen();
				
				ls.main(null);
			}
		});
		frmXarena.getContentPane().add(btnCancel, "4, 12, center, default");
		//frmXarena.setBounds(100, 100, 450, 300);
		frmXarena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
