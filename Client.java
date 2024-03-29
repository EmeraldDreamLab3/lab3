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
			// Make sure arguments are passed
			if (args.length < 2) {
				System.out.println("Client usage: java Client #IP_address #port_number");
			}

			Scanner scn = new Scanner(System.in); 
			
			// This is the Ip the cleint is connecting to
			InetAddress ip = InetAddress.getByName(args[0]); 
			int port = Integer.parseInt(args[1]);
			//Establish Socket
			Socket s = new Socket(ip, port); 
	
			// Input and Outputstream of the socket
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			String received;
			// System.out.println("[TRY]");
			// Communicating to server from client
			while (true) 
			{ 
				//initial get server request
				received = dis.readUTF();
				System.out.println(received);
				// v This line should constantly read the server's output
				while(dis.available() > 0) {
					received = dis.readUTF();
					System.out.println(received);
				}
				String tosend = scn.nextLine(); 
				dos.writeUTF(tosend); 
				
				// System.out.println("[WHILE]");
				// If client sends exit, close connection 
				if(tosend.equals("Exit")) 
				{ 
					System.out.println("Closing this connection : " + s); 
					s.close(); 
					System.out.println("Connection closed"); 
					// System.out.println("[EXIT]");
					break; 
				} 
				
				/* NOTICE ME ALEXXXXXXXXXXXXXXXXXXXXXXX */
				/* Needs a line that lets user type again to server!!!!!! */
			} 
			// System.out.println("[OUT TRUE]");

			
			// Close everything when done
			scn.close(); 
			dis.close(); 
			dos.close(); 
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 
} 
