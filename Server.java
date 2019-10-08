import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		// Port the server is listening to 
        ServerSocket ss = new ServerSocket(5056); 
        // List of users
        ArrayList<ClientHandler> listOfUsers = new ArrayList<ClientHandler>();
        
		// Infinite loop bc clients are connecting
		while (true) 
		{ 
			Socket s = null; 
			
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept(); 
				System.out.println("A new client is connected : " + s); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				
				System.out.println("Assigning new thread for this client"); 

				// create a new thread object 
				ClientHandler t = new ClientHandler(s, dis, dos); 
                // add client to list of clients
                listOfUsers.add(t);
                for (ClientHandler e : listOfUsers) {
                    System.out.print("Online:" + e.getUserName() +", ");
                }
				// Invoking the start() method 
                t.start();
			} 
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
	} 
} 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	final DataInputStream dis; 
	final DataOutputStream dos; 
    final Socket s; 
    // bool to check if user name was set
	boolean setUserName;
	// bool to check if target user to chat to has been set to
    String userName;
    
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
        this.dos = dos; 
        setUserName = false;
	} 

	@Override
	public void run() 
	{ 
		String received; 
		String toreturn; 
		while (true) 
		{ 
            try { 
				//  !!If user has not set their user name yet, set it
				if (setUserName == false) {
					dos.writeUTF("Set your user name:");

					// receive the answer from client 
					received = dis.readUTF(); 

					userName = received;
					dos.writeUTF("You set your username to:" + userName);
					setUserName = true;

				}
				
				//  !!If user has not designated who they want to talk to, set their target user
				if (setTargetUser == false) {
					dos.writeUTF("\nSelect an Option: [Speak to User | Exit]..\n"+ 
								"Type to chat."); 
					

					// receive the answer from client 
					received = dis.readUTF(); 

					switch (received) {     
						case "Speak to User" :
							dos.writeUTF("Please list who you wish to speak to:");
							// dis.readUTF() <-- Make sure to have client write to unblock this
							setTargetUser = true;
							break;
						
						case "Exit":
							System.out.println("Client " + this.s + " sends exit..."); 
							System.out.println("Closing this connection."); 
							this.s.close(); 
							System.out.println("Connection closed"); 
							break; 
						default: 
							dos.writeUTF("Invalid Option"); 
							break; 
					}
				}

            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 

		try
		{ 
			// closing resources 
			this.dis.close(); 
			this.dos.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
    } 
    
    public String getUserName() {
        return userName;
    }
} 
