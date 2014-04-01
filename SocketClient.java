
import java.net.*;
import java.nio.file.Files;
/* The java.io package contains the basics needed for IO operations. */
import java.io.*;

import javax.swing.JOptionPane;
/** The SocketClient class is a simple example of a TCP/IP Socket Client.
 *
 */

public class SocketClient {
	private static final int RUNTIME_ERROR = 10;	
	private static final int COMPILE_ERROR = 20;
	private static final int SUCCESS_RETURN = 0;

	private static final int CORRECT_ANSWER = 30;
	private static final int WRONG_ANSWER = 40;
	private static final int CONNECTION_ESTABILISHED = 50;
	
	static Socket connection;
	static InetAddress address;

    int verdict = WRONG_ANSWER;
	
	/**
	 * @param args
	 */
	public static int main(String[] args) {
		// TODO Auto-generated method stub
		/** Define a host server */
	    String host = "localhost";
	    /** Define a port */
	    int port = 27016;

	    //StringBuffer instr = new StringBuffer();
	    //String TimeStamp;
	    System.out.println("SocketClient initialized");
	    
	    try {
	        /** Obtain an address object of the server */
	        address = InetAddress.getByName(host);
	        /** Establish a socket connetion */
	        connection = new Socket(address, port);
	        /** Instantiate a BufferedOutputStream object */
	        //BufferedOutputStream bos = new BufferedOutputStream(connection.
	        //        getOutputStream());

	            /** Instantiate an OutputStreamWriter object with the optional character
	             * encoding.
	             */
	         //   OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
	            
	           // TimeStamp = new java.util.Date().toString();
/*	            String process = "Calling the Socket Server on "+ host + " port " + port +
	                " at " + TimeStamp +  (char) 13;
*/
	            //System.out.println("In client : Sending source to server.");
	            /** Write across the socket connection and flush the buffer */
	            
	            //osw.write(args[0]);
	            //osw.flush();

	            //System.out.println("Client: Will wait for incoming data now.");
	            /** Instantiate a BufferedInputStream object for reading
	            /** Instantiate a BufferedInputStream object for reading
	             * incoming socket streams.
	             */

	            //BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
	            /**Instantiate an InputStreamReader with the optional
	             * character encoding.
	             */

	            //InputStreamReader isr = new InputStreamReader(bis, "UTF-8");

	            /**Read the socket's InputStream and append to a StringBuffer */
	            //int c = 0;
	            //c = isr.read();
	            
	            //if(c == CORRECT_ANSWER)
	            //	System.out.println("Verdict Returned : CORRECT ANSWER");
	            //else	
	            //	System.out.println("Verdict Returned : WRONG ANSWER");
	            //Add labels here as well.
	            
	            //verdict = c;
	            
	            
	            /** Close the socket connection. */
	            //connection.close();
	            
	            
	            
	            //System.exit(0);
	           }
	          catch (IOException f) {
	            System.out.println("IOException: " + f);
	          }
	          catch (Exception g) {
	            System.out.println("Exception: " + g);
	          }
		return CONNECTION_ESTABILISHED;
	    
	    
	        }
	
	
	public int sendSubmission(String submission, int currentProblemID) {
		
		System.out.println("In client: sendSubmision(): Sending source to server.");
        String sendToServer = ""; 
		
		verdict = WRONG_ANSWER;
		try {
			//Send source
			BufferedOutputStream bos = new BufferedOutputStream(connection.
			            getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
			
			sendToServer = "SUBMISSION" + (char)13 + currentProblemID + (char)13 + submission + (char)13;
			
			osw.write(sendToServer);
            osw.flush();
            
            System.out.println("Client: sendSubmission() : Wrote out to osw, submission sent. Waiting for verdict.");
			
			// Get verdict
			BufferedInputStream bis = new BufferedInputStream(connection.
		                getInputStream());
			InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
			
			
			verdict = isr.read();
			
			System.out.println("Client: sendSubmission() : Verdict received: " + verdict);
			
			
			if(verdict == CORRECT_ANSWER)
            	System.out.println("Client: sendSubmission() : Verdict Returned : CORRECT ANSWER");
            else	
            	System.out.println("Client: sendSubmission() : Verdict Returned : WRONG ANSWER");
            
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verdict;
		
	}
	
	
	public String getProblemStatement(int problemID) {
		
		System.out.println("In client: getProblemStatement(): Getting problem statement from server.");
        String statement = "";
        
		try {
			//Send request
			BufferedOutputStream bos = new BufferedOutputStream(connection.
			            getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
			
			String request = "GETSTATEMENT" + (char)13 + problemID + (char)13;
			
			osw.write(request);
            osw.flush();
            
            System.out.println("Client: getProblemStatement() : Wrote out to osw, request sent. Waiting for problem statement to arrive.");
			
			// Get statement
			BufferedInputStream bis = new BufferedInputStream(connection.
		                getInputStream());
			InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
			
			
			statement = "";
			int c;
			while( (c = isr.read()) != 13 ) {
				statement += (char)c;
			}
			
			System.out.println("Client: getProblemStatement() : statement received: " + statement);
			 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statement;
		
	}
	
	
	public int sendLoginDetails(String loginDetails) {
		
		int return_value = 0;
		
		try {
			//Send request
			BufferedOutputStream bos = new BufferedOutputStream(connection.
			            getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
			
			osw.write(loginDetails);
            osw.flush();
            
            System.out.println("Client: sendLoginDetails() : Wrote out to osw, authentication in process");
			
			// Get statement
			BufferedInputStream bis = new BufferedInputStream(connection.
		                getInputStream());
			InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
			
			
			int login_status = isr.read();
			
			if(login_status == 1) {
				System.out.println("Client: sendLoginDetails() : Logged in successfully: ");
				return_value = 1;
			} else {
				System.out.println("Client: sendLoginDetails() : Login failed. ");
				return_value = 0;
			}
			 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_value;
	}
	
	public int Logout(String loginDetails) {
		
		int return_value = 0;
		
		try {
			//Send request
			BufferedOutputStream bos = new BufferedOutputStream(connection.
			            getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
			
			osw.write(loginDetails);
            osw.flush();
            
            System.out.println("Client: sendLoginDetails() : Wrote out to osw, authentication in process");
			
			// Get statement
			BufferedInputStream bis = new BufferedInputStream(connection.
		                getInputStream());
			InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
			
			
			int login_status = isr.read();
			
			System.out.println("Client: sendLoginDetails() : Logged out successfully: ");
			return_value = 1;
			
			 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_value;
	}
	
}
