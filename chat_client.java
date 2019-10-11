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
			// Communicating to server from client
			while (true) 
			{ 
				//initial get server request
				do {
					received = dis.readUTF();
					System.out.println(received);
				} while (dis.available() > 0);
				boolean ans = false;
				while(ans) {
					if (dis.available() > 0)
					{
						dos.flush();
						received = dis.readUTF();
						System.out.println(received);
					} else {
						String tosend = scn.nextLine(); 
						dos.writeUTF(tosend); 
					}
				}
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