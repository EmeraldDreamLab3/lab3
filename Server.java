import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server 
{ 
	static ArrayList<ClientHandler> listOfUsers;
	public static void main(String[] args) throws IOException 
	{ 
		// Port the server is listening to 
        ServerSocket ss = new ServerSocket(5056); 
        // List of users
        listOfUsers = new ArrayList<ClientHandler>();
        
		// Infinite loop bc clients are connecting
		while (true) 
		{ 
			Socket s = null; 
			//System.out.println("Waiting for a client ...");
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept(); 
				System.out.println("Client accepted"); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				
				//System.out.println("Assigning new thread for this client"); 

				// create a new thread object 
				ClientHandler t = new ClientHandler(s, dis, dos); 
                // add client to list of clients
				listOfUsers.add(t);
				// Invoking the start() method 
				t.start();
			} 
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
	}
	public ArrayList<ClientHandler> getList() {
		return listOfUsers;
	} 

	public void printList() {
		System.out.println("List of clients and states");
		for (ClientHandler e : listOfUsers) {
			System.out.println(e.getUserName() +"\t"+e.getState());
		}
	}
	public String stringList() {
		String s = "List of clients & states\n";
		for (ClientHandler e : listOfUsers) {
			s = s + e.getUserName() +"\t"+e.getState()+"\n";
		}
		return s;
	}
} 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	Server myServer = new Server();
	final DataInputStream dis; 
	final DataOutputStream dos; 
    final Socket s; 
    // bool to check if user name was set
	boolean setUserName;
	// bool to check if target user to chat to has been set to
	boolean setTargetUser;
    String userName;
    
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
        this.dos = dos; 
		setUserName = false;
		setTargetUser = false;
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
					dos.writeUTF("Enter client name: ");

					// receive the answer from client 
					received = dis.readUTF(); 
					userName = received;
					myServer.printList();
					//dos.writeUTF("You set your username to:" + userName);
					setUserName = true;
					//send list to users once a connection is established
					dos.writeUTF(myServer.stringList());
				}
				
				//  !!If user has not designated who they want to talk to, set their target user
				if (setTargetUser == false) {
					dos.writeUTF("Select an Option: [Speak to User | Exit]..\n"+ 
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
			try
			{ 
				// closing resources 
				this.dis.close(); 
				this.dos.close(); 
				
			}catch(IOException e){ 
				e.printStackTrace(); 
			}
        }  
    } 
    
    public String getUserName() {
        return userName;
    }
} 
