
/* A Java program for a Server */
import java.net.*; 
import java.io.*; 
  
public class Server 
{ 
/* initialize socket and input output stream */
private Socket socket = null; 
private BufferedReader input = null;
private ServerSocket server = null;
private DataInputStream in = null; 
private DataOutputStream out = null;

/* constructor with port */
public Server(int port) 
{ 
	/* starts server and waits for a connection */
	try { 
		server = new ServerSocket(port); 
	} catch(Exception i) {
		System.out.println("Client: Error in port");
		System.exit(0);
	}
	System.out.println("Server: Server started"); 

	System.out.println("Server: Waiting for a client ..."); 
	try {
		socket = server.accept(); 
		System.out.println("Server: Client accepted");

		/* takes input from terminal */
		input = new BufferedReader(new InputStreamReader(System.in)); 
		
		/* takes input from the client socket */
		in = new DataInputStream( 
		    new BufferedInputStream(socket.getInputStream())); 
		
		/* sends output to the socket */
		out = new DataOutputStream(socket.getOutputStream());
		
		String line = ""; 
		
		/* reads message from client until "Over" is sent */
		while (!line.equals("Over")) 
		{ 
		        line = in.readUTF(); 
		        System.out.println("Client: " + line); 

			line = input.readLine();
			out.writeUTF(line);
		} 
		System.out.println("Server: Closing connection"); 
		
		/* close connection */
		socket.close(); 
		in.close(); 

		out.close();
		input.close();

	} catch(EOFException i) { 
	    System.out.println(i); 
	} 
	catch(Exception i) { 
	    System.out.println(i); 
	} 
}

public static void main(String args[]) 
{ 
	if (args.length < 1) {
		System.out.println("Server usage: java Server #port_number");
	}
	else {
		try {
			Server server = new Server(Integer.parseInt(args[0])); 
		} catch(Exception i) {
			System.out.println("Error in port");	
		}
	}
} 

} 
