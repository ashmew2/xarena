import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

	
public class SingleSocketServer {

	/**
	 * @param args
	 */
	 static String username = "ashish";
	 static ServerSocket socket1;
	 protected final static int port = 27016;
	 private static final int RUNTIME_ERROR = 10;	
	 private static final int COMPILE_ERROR = 20;
 	 private static final int SUCCESS_RETURN = 0;
     private static final int CORRECT_ANSWER = 30;
	 private static final int WRONG_ANSWER = 40;
	 static Socket connection;

	 static boolean first;
	 static StringBuffer input_string, input_type, submission_string;
	 static String TimeStamp;
	
	 public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
		      socket1 = new ServerSocket(port);
		      System.out.println("SingleSocketServer Initialized");
		      int character;
		      
		      while (true) {
		          connection = socket1.accept();
		      	  System.out.println("Server: Accepted client.");
				
		          BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
		          InputStreamReader isr = new InputStreamReader(is);
		          System.out.println("Server: Made Streams.");
		          
		          input_type = new StringBuffer();
		          input_string = new StringBuffer(); //We get the program in input_string
		          

		          while((character = isr.read()) != 13) {
		            input_type.append((char)character);
		            System.out.println("Server: trying to read char, read : "+ (char)character);
		          }
		          
		          while((character = isr.read()) != 13) {
		            input_string.append((char)character);
		            System.out.println("Server: trying to read char, read : "+ (char)character);
		          }
		          

		          System.out.println("Server: input_type : $" + input_type + "$");
		          System.out.println("Server: input_string : $" + input_string + "$");
		          
		          if(input_type.toString().equals("SUBMISSION")) {
		        	//System.out.println(input_string);
		        	  int problemID = Integer.parseInt(input_string.toString());
		        	  
		        	  submission_string = new StringBuffer();
		        	  
		        	  while((character = isr.read()) != 13) {
				            submission_string.append((char)character);
				            System.out.println("Server: trying to read char, read : "+ (char)character);
				          }
		        	  
			          System.out.println("Server: submission_string : " + submission_string);		          
			          
			          PrintWriter writer = new PrintWriter("/home/" + username + "/xprog/srccode.cpp", "UTF-8");
					      		          
			          writer.print(submission_string.toString());
			          
			          writer.flush();
			          writer.close();					    					
			          
			          BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			          OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			          System.out.println("Opening file for judging now.");
			          int verdict = JudgeSubmission(problemID);
			          System.out.println("verdict : " + verdict);
			          
			          //osw.write(JudgeSubmission());
			          osw.write(verdict);
			          osw.flush(); //Response sent to client This should be the verdict.
		          } else if(input_type.toString().equals("GETSTATEMENT")) {
		        	  System.out.println("here in GETSTATEMENT");
		        	  int problemID = Integer.parseInt(input_string.toString());
		        	  Charset charset = Charset.forName("UTF-8");
		      		String problemPath = "/home/" + username + "/xprog/" + problemID + "/statement.txt";
		      		
		      		System.out.println("GETSTATEMENT: getting problem from path " + problemPath);
		      		
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
		      		}
		      		
		      		System.out.println("Server: sending statement now as " + statement);
		      		statement += (char)13; // NEVER FORGET THIS! or client/server would keep waiting for it
		      		
		      		
		      		BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			          
			        osw.write(statement);
			        osw.flush();
		      		
		          }
		          
		          System.out.println("Reached here");
		          
		          //System.exit(0);
		       }
		      }
		      catch (IOException e) {}
		        try {
		          if(connection !=  null)
		        	  connection.close();
		        }
		        catch (IOException e) 
		        {
		        	
		        }
		        System.exit(0);
	 }
	
	private static int JudgeSubmission(int problemID) {		
		Runtime r = Runtime.getRuntime();		
		int return_status = -1;
		ProcessBuilder x = new ProcessBuilder();
		
		String tcFilePath = "/home/" + username + "/xprog/" + problemID + "/" + "input.txt";
		String opFilePath = "/home/" + username + "/xprog/" + problemID + "/" + "testop.txt";
		
		File tcfile = new File(tcFilePath); 
		File opfile = new File(opFilePath);
		x.redirectInput(tcfile);
		x.redirectOutput(opfile);
		
		x.command("/home/" + username + "/xprog/temp_bin");
		
		try {
			Process p = r.exec("g++ -c /home/" + username + "/xprog/srccode.cpp");
			System.out.println("Here 1.1.1.1.1");
			
			try {
				return_status = p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		System.out.println("Here 1, return_status is : " + return_status);
        
		if(return_status == 0)
		{
			return_status = -1;
			System.out.println("Here 1.1");	
			try {
				Process p = r.exec("g++ -lm /home/" + username + "/xprog/srccode.cpp -o /home/" + username + "/xprog/temp_bin");
				System.out.println("Here 1.2");
				try {
					return_status = p.waitFor();
					System.out.println("Here 1.1.1");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*49941 - password*/
				try {
				//Process p = r.exec("/home/" + username + "/temp_bin < /home/" + username + "/tester_case > /home/" + username + "/tester_op.txt");
				Process p = x.start();
				try {
					return_status = p.waitFor();
					System.out.println("After running program, return : " + return_status);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
				
				try {
					return_status = p.waitFor();
					System.out.println("Here 4.2.2");
					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("Here 2");
				
		if(return_status != 0)			
			{
				return RUNTIME_ERROR;
			}
		}
		else
			return COMPILE_ERROR;
		
		// If it reached here, that means the code ran successfullt. Time to diff.
		
		/*ProcessBuilder b = new ProcessBuilder();
		File f = new File("/home/" + username + "/differ.diff");
		b.redirectOutput(f);
		//b.command("diff -u /home/" + username + "/tester_op.txt /home/" + username + "/expected_op.txt");
		b.command("/usr/bin/diff -u");
		return_status = -1;
		
		try {
			Process p = b.start();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Charset charset = Charset.forName("UTF-8");
		
		String selectedFilePath = "/home/" + username + "/xprog/" + problemID + "/testop.txt";
		Path file = Paths.get(selectedFilePath);
		String selectedFileContents = "";
				
		try (BufferedReader reader = Files.newBufferedReader(file , charset)) {
			String line;
			
		    while ((line = reader.readLine()) != null) {
		        System.out.println("line: " + line);
		        selectedFileContents += (line);		        
		    }		    		    
		    
		} catch (IOException x2) {
		    System.err.format("IOException: %s%n", x2);
		}
		
		selectedFilePath = "/home/" + username + "/xprog/" + problemID + "/expected.txt";
		file = Paths.get(selectedFilePath);
		String selectedFileContents2 = "";
				
		try (BufferedReader reader = Files.newBufferedReader(file , charset)) {
			String line;
			
		    while ((line = reader.readLine()) != null) {
		        System.out.println("line: " + line);
		        selectedFileContents2 += (line);		        
		    }		    
		    
		    if(selectedFileContents.equals(selectedFileContents2))
		    	return CORRECT_ANSWER;
		    else
		    	return WRONG_ANSWER;
		    
		} catch (IOException x2) {
		    System.err.format("IOException: %s%n", x2);
		}
				
		return SUCCESS_RETURN;
	}
}
