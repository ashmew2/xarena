import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.Font;


public class loginscreen {

	private JFrame frmXarena;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;


	private static final int CONNECTION_ESTABILISHED = 50;
	public static loginscreen window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new loginscreen();
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
	public loginscreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmXarena = new JFrame();
		frmXarena.setTitle("X.ARENA");
		frmXarena.getContentPane().setBackground(Color.BLACK);
		frmXarena.setBounds(100, 100, 450, 300);
		frmXarena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmXarena.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("60px"),
				ColumnSpec.decode("72px"),
				ColumnSpec.decode("25px"),
				ColumnSpec.decode("166px"),},
			new RowSpec[] {
				RowSpec.decode("30px"),
				RowSpec.decode("15px"),
				RowSpec.decode("35px"),
				RowSpec.decode("19px"),
				RowSpec.decode("35px"),
				RowSpec.decode("19px"),
				RowSpec.decode("35px"),
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblXarena = new JLabel("X.ARENA");
		lblXarena.setFont(new Font("Dialog", Font.BOLD, 17));
		lblXarena.setForeground(Color.RED);
		frmXarena.getContentPane().add(lblXarena, "4, 2, center, center");
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.GREEN);
		frmXarena.getContentPane().add(lblUsername, "2, 4, center, center");
		
		usernameTextField = new JTextField();
		frmXarena.getContentPane().add(usernameTextField, "4, 4, fill, center");
		usernameTextField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.GREEN);
		frmXarena.getContentPane().add(lblPassword, "2, 6, center, center");
		
		passwordTextField = new JPasswordField();
		frmXarena.getContentPane().add(passwordTextField, "4, 6, fill, center");
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SocketClient client = new SocketClient();
				if(client.main(null) == CONNECTION_ESTABILISHED) {
					String username, loginDetails;
					char [] password;
					
					username = usernameTextField.getText();
					password = passwordTextField.getPassword();
					
					loginDetails = "LOGIN" + (char)13 + username + (char)13 + (new String(password)) + (char)13; 
					
					if( client.sendLoginDetails(loginDetails) == 1) {
						JOptionPane.showMessageDialog(frmXarena, "Login successfull!");
						
						mainscreen screen = new mainscreen();
						window.frmXarena.setVisible(false);
						
						screen.main(null);
						System.out.println("here after calling main");
						
					} else {
						JOptionPane.showMessageDialog(frmXarena, "Login failed");
					}
					
				}
				
				
			}
		});
		frmXarena.getContentPane().add(btnLogin, "4, 8, center, center");
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				signupscreen sus = new signupscreen();
				
				sus.main(null);
				
				window.frmXarena.setVisible(false);
			}
		});
		frmXarena.getContentPane().add(btnSignUp, "4, 10, center, center");
	}

}
