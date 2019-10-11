import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

// Client class 
public class chat_client 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			Scanner scn = new Scanner(System.in); 
			
			// This is the Ip the cleint is connecting to
			InetAddress ip = InetAddress.getByName("localhost"); 
	
			//Establish Socket
			Socket s = new Socket(ip, 5056); 
	
			// Input and Outputstream of the socket
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			String received;
			// Communicating to server from client
			while (true) 
			{ 
				//initial get server request
				do {
					received = dis.readUTF();
					System.out.println(received);
				} while (dis.available() > 0);
                String tosend = scn.nextLine(); 
                dos.writeUTF(tosend); 
                // If client sends exit, close connection 
				if(tosend.equals("Exit")) 
			    { 
			    	System.out.println("Closing this connection : " + s); 
					s.close();
					System.out.println("Connection closed"); 
				    break; 
			 	} 
			} 
			// Close everything when done
			scn.close(); 
			dis.close(); 
			dos.close(); 
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 
} 