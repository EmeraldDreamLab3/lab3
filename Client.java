import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

// Client class 
public class Client 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			Scanner scn = new Scanner(System.in); 
			
			// This is the Ip the client is connecting to
			InetAddress ip = InetAddress.getByName("localhost"); 
	
			//Establish Socket
			Socket s = new Socket(ip, 5000); 
	
			// Input and Outputstream of the socket
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			String received;
			// Communicating to server from client
			while (true) 
			{ 
				// v This line should constantly read the server's output
				received = dis.readUTF();
				System.out.println(received); 
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
				
				// v So the line is updated, so reread 
				received = dis.readUTF(); 
				System.out.println(received); 

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
