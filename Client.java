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
			
			// This is the Ip the cleint is connecting to
			InetAddress ip = InetAddress.getByName("localhost"); 
	
			//Establish Socket
			Socket s = new Socket(ip, 5056); 
	
			// Input and Outputstream of the socket
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			System.out.println("[TRY]");
			// Communicating to server from client
			while (true) 
			{ 
                // v This line should constantly read the server's output
				System.out.println(dis.readUTF()); 
				String tosend = scn.nextLine(); 
				dos.writeUTF(tosend); 
				
				System.out.println("[WHILE]");
				// If client sends exit, close connection 
				if(tosend.equals("Exit")) 
				{ 
					System.out.println("Closing this connection : " + s); 
					s.close(); 
					System.out.println("Connection closed"); 
					System.out.println("[EXIT]");

					break; 
				} 
				
				// v So the line is updated, so reread 
				String received = dis.readUTF(); 
				System.out.println(received); 
				System.out.println("[END WHILE]");

			} 
			System.out.println("[OUT TRUE]");

			
			// Close everything when done
			scn.close(); 
			dis.close(); 
			dos.close(); 
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 
} 
